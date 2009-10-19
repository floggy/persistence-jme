/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.floggy.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.NotFoundException;
import net.sourceforge.floggy.persistence.Configuration;
import net.sourceforge.floggy.persistence.Weaver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 * @author Thiago Moreira
 * @contributor Dan Murphy
 *
 */
public class FloggyBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "net.sourceforge.floggy.floggyBuilder";

	public static final String PERSISTABLE_CLASS_NAME = "net.sourceforge.floggy.persistence.Persistable";

	public static final String RECORDSTORE_CLASS_NAME = "javax.microedition.rms.RecordStore";
	
	private Log log;

	private Log getLog() {
		if (log == null) {
			LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", EclipseLog.class.getName());
			log = LogFactory.getLog(FloggyBuilder.class);
		}
		return log;
	}

	protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
		try {
			IProject project = getProject();
			boolean buildNeeded = false;
			IResourceDelta delta = getDelta(project);
			ArrayList sourceFolders = new ArrayList(1);

			// Only do a build if the change is to a java source folder. It's
			// highly unlikely that the delta will ever be null,
			// if it is then assume that a build is required even though we
			// don't know what changed.
			if (delta != null && project.hasNature(JavaCore.NATURE_ID) && project.hasNature(FloggyNature.NATURE_ID)) {
				IClasspathEntry[] entries = JavaCore.create(project).getResolvedClasspath(true);
				for (int i = 0; i < entries.length; i++) {
					IClasspathEntry entry = entries[i];
					if (entry.getEntryKind() == IClasspathEntry.CPE_SOURCE) {
						sourceFolders.add(entry.getPath());
					}
				}
				IResourceDelta[] changes = delta.getAffectedChildren();
				for (int i = 0; i < changes.length; i++) {
					IResourceDelta change = changes[i];
					if (sourceFolders.contains(change.getFullPath())) {
						buildNeeded = true;
						getLog().info("Floggy build needed due to changed source folder: " + change.getFullPath());
						break;
					}
				}
			} else if (delta == null) {
				buildNeeded = true;
			}

			if (buildNeeded) {
				IJavaProject javaProject = JavaCore.create(project);

				IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);

				boolean generateSource = Boolean.valueOf(project.getPersistentProperty(SetGenerateSourceAction.PROPERTY_NAME))
						.booleanValue();
				boolean addDefaultConstructor = Boolean.valueOf(
						project.getPersistentProperty(SetAddDefaultConstructorAction.PROPERTY_NAME)).booleanValue();

				// creating the classpath
				List classpathList = new ArrayList();
				ClassPool classPool = new ClassPool();
				IClasspathEntry classpathEntry;
				String pathName;
				for (int i = 0; i < entries.length; i++) {
					classpathEntry = JavaCore.getResolvedClasspathEntry(entries[i]);
					pathName = classpathEntry.getPath().toFile().toString();
					if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
						IPath cpePath = classpathEntry.getPath(); 
						if (!cpePath.toFile().exists()) {
							IFile pathIFile = project.getWorkspace().getRoot().getFile(cpePath);
							pathName = pathIFile.getLocationURI().getPath();
						}
					}
					classpathList.add(pathName);
					classPool.appendClassPath(pathName);
				}

				final StringBuffer messages = new StringBuffer();
				try {
					classPool.get(PERSISTABLE_CLASS_NAME);
				} catch (NotFoundException e) {
					messages.append("You must to add the Floggy framework library to the build path.");
				}
				try {
					classPool.get(RECORDSTORE_CLASS_NAME);
				} catch (NotFoundException e) {
					if (messages.length() != 0) {
						messages.append('\n');
					}
					messages.append("You must to add the MIDP library to the build path.");
				}
				if (messages.length() != 0) {
					Display.getDefault().asyncExec(new Runnable() {
						public void run() {
							Shell shell = Display.getDefault().getActiveShell();
							MessageDialog.openError(shell, "Floggy", messages.toString());
						}
					});

				} else {
					Weaver weaver = new Weaver(classPool);
					IPath root = project.getLocation();
					File input = root.removeLastSegments(1).append(javaProject.getOutputLocation()).toFile();

					IFolder floggyTemp = project.getFolder(".floggy.tmp");
					if (!floggyTemp.exists()) {
						floggyTemp.create(IResource.DERIVED, true, monitor);
					}
					weaver.setOutputFile(floggyTemp.getLocation().toFile());
					weaver.setInputFile(input);
					weaver.setClasspath((String[]) classpathList.toArray(new String[0]));
					Configuration configuration = new Configuration();
					configuration.setAddDefaultConstructor(addDefaultConstructor);
					configuration.setGenerateSource(generateSource);
					weaver.setConfiguration(configuration);
					weaver.execute();

					IPath path = javaProject.getOutputLocation();

					if (path.segmentCount() > 1) {
						path = path.removeFirstSegments(1);
					}

					IFolder outputLocation = project.getFolder(path);
					floggyTemp.refreshLocal(IResource.DEPTH_INFINITE, monitor);
					copyFiles(floggyTemp, outputLocation, monitor);
					exportWeaverClasses(floggyTemp, javaProject, monitor);
					project.refreshLocal(IResource.DEPTH_INFINITE, monitor);
					cleanFolder(floggyTemp, monitor);
				}
			}
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1, e.getMessage(), e);
			throw new CoreException(status);
		}
		return null;
	}

	private void cleanFolder(IFolder folder, IProgressMonitor monitor) throws CoreException {
		IResource[] members = folder.members();
		for (int i = 0; i < members.length; i++) {
			IResource member = members[i];
			if (member.getType()==IResource.FOLDER){
				cleanFolder((IFolder) member, monitor);
			} else if (member.getType()==IResource.FILE) {
				((IFile)member).delete(true, monitor);
			}
		}
	}

	private void copyFiles(IFolder source, IFolder destination, IProgressMonitor monitor) throws CoreException {
		IFolder newDestination;
		IFile targetFile;
		IFile sourceFile;
		source.refreshLocal(IResource.DEPTH_ONE, monitor);
		IResource[] resources = source.members();
		for (int i = 0; i < resources.length; i++) {
			IResource resource = resources[i];
			if (resource.getType() == IResource.FOLDER){
				String folderName = resource.getFullPath().lastSegment();
				newDestination = destination.getFolder(folderName);
				if (!newDestination.exists()){
					newDestination.create(IResource.DERIVED, true, monitor);
				}
				copyFiles((IFolder) resource, newDestination, monitor);
			} else if (resource.getType()==IResource.FILE) {
				sourceFile = (IFile) resource;
				targetFile = destination.getFile(sourceFile.getName());
				if (targetFile.exists()){
					targetFile.setContents(sourceFile.getContents(), IResource.DERIVED, monitor);
				} else {
					targetFile.create(sourceFile.getContents(), IResource.DERIVED, monitor);
				}
			}
		}
	}
	
	private void exportWeaverClasses(IFolder floggyTemp, IJavaProject javaProject, IProgressMonitor monitor) throws CoreException {
		RuntimeCollector collector = CollectorFactory.createCollector();
		collector.setEclipseProperties(javaProject, (EclipseLog) log, floggyTemp);
		collector.setSource(floggyTemp);
		collector.run(monitor);
		return;
	}
}
