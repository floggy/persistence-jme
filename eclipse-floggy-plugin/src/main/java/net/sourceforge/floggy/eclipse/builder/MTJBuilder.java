/**
 * Copyright (c) 2006-2011 Floggy Open Source Group. All rights reserved.
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
package net.sourceforge.floggy.eclipse.builder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javassist.ClassPool;
import javassist.NotFoundException;
import net.sourceforge.floggy.eclipse.ConfigurationFileAction;
import net.sourceforge.floggy.persistence.Weaver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;

/**
 * For performing a build in the case where either the Mobile Tools for Java,
 * or Pulsar is being used. One of the main differences here is that in these
 * environments it's necessary to package some floggy impl classes in a jar
 * so that MTJ/Pulsar can do the bytecode verification needed.
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @author <a href="mailto:dan.murphy@floggy.org">Dan Murphy</a>
 * @version $Revision$
  */
public class MTJBuilder extends AbstractBuilder {
	private static final Log LOG = LogFactory.getLog(MTJBuilder.class);
	/**
	 * Entry point into builder
	 *
	 * @param project the project to be build
	 * @param monitor the current progress monitor displayed to the user
	 * @return List of any projects which the one being built says it depends on
	 * @throws Exception if an irrecoverable error occurs
	*/
	public IProject[] build(IProject project, IProgressMonitor monitor)
		throws Exception {
		LOG.debug(project.getName() + " is going to be build using the MTJBuilder");

		List classPoolList = new ArrayList();
		ClassPool classPool = new ClassPool();
		IJavaProject javaProject = JavaCore.create(project);
		
		configureClassPool(classPool, classPoolList, javaProject, true);
		
		if (validateClasspath(project, classPool)) {
			Weaver weaver = new Weaver(classPool);
			IPath root = project.getLocation();
			File input =
				root.removeLastSegments(1).append(javaProject.getOutputLocation())
				 .toFile();

			IFile implJar = project.getFile("floggy-persistence-framework-impl.jar");
			IFolder floggyTemp = project.getFolder(".floggy.tmp");

			if (!floggyTemp.exists()) {
				floggyTemp.create(IResource.DERIVED, true, monitor);
			}

			IFile configurationFile =
				project.getFile(project.getPersistentProperty(
						ConfigurationFileAction.PROPERTY_NAME));

			weaver.setEmbeddedClassesOutputPool(implJar.getLocation().toFile());
			weaver.setOutputFile(floggyTemp.getLocation().toFile());
			weaver.setInputFile(input);
			weaver.setClasspath((String[]) classPoolList.toArray(
					new String[classPoolList.size()]));

			if (!configurationFile.exists()) {
				weaver.setConfiguration(createWeaverConfiguration(project));
			} else {
				weaver.setConfigurationFile(configurationFile.getLocation().toFile());
			}
			// Let the weaving commence
			weaver.execute();

			IPath path = javaProject.getOutputLocation();

			if (path.segmentCount() > 1) {
				path = path.removeFirstSegments(1);
			}

			IFolder outputLocation = project.getFolder(path);
			floggyTemp.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			copyFiles(floggyTemp, outputLocation, monitor);
			outputLocation.refreshLocal(IResource.DEPTH_INFINITE, monitor);
			cleanFolder(floggyTemp, monitor);
		}
		return project.getReferencedProjects();
	}

	
	/**
	 * Entry point into the configureClassPool, recursively called to deal with dependencies
	 * @param classPool
	 * @param classPoolList
	 * @param javaProject
	 * @param isRootProject	true when this is the project that the builder was requested to build, false for dependencies
	 * @throws NotFoundException
	 * @throws CoreException
	 */
	private void configureClassPool(ClassPool classPool, List classPoolList,
			IJavaProject javaProject, boolean isRootProject) throws NotFoundException, CoreException {
		LOG.debug("Configuring ClassPool for " + javaProject.getProject().getName() + (isRootProject?" (root project)":" (project dependency"));
		IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);		
		configureClassPool(classPool, classPoolList, entries, javaProject.getProject(), isRootProject);
		// now do dependencies (recursively)
		IProject[] dependencies = javaProject.getProject().getReferencedProjects();
		for (int i=0; i < dependencies.length; i++){
			configureClassPool(classPool, classPoolList, JavaCore.create(dependencies[i]), false);
		}
	}

	/**
	 * This is where the classpool is actually set. The typical case only requires adding the required jar files
	 * to the classpool (classPool and classPoolList), however if the project is dependent on other projects then
	 * not only do we need the jar files from these dependencies but we also need the output folders.
	 * @TODO EXPERIMENTAL - Dependencies support should be considered experimental at this time because it isn't fully tested ! 
	 */
	private void configureClassPool(ClassPool classPool, List classPoolList,
			IClasspathEntry[] entries, IProject project, boolean isRootProject) throws NotFoundException, JavaModelException {
		
		IClasspathEntry classpathEntry;
		String pathName;
		
		for (int i = 0; i < entries.length; i++) {
			classpathEntry = JavaCore.getResolvedClasspathEntry(entries[i]);
			IPath classIPath = classpathEntry.getPath();

			if ((isRootProject || classpathEntry.isExported()) && classpathEntry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				pathName = getAccessablePathName(classIPath, project);
			} else if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE){
				classIPath = classpathEntry.getOutputLocation();
				if (classIPath == null){
					classIPath = JavaCore.create(project).getOutputLocation();
				}
				pathName = getAccessablePathName(classIPath, project);
			} else {
				// Currently we only add : All source folders, All libs in the root project & Exported libs in other projects
				continue;
			}

			if (pathName.contains("floggy-persistence-framework-impl.jar")) {
				continue;
			}
			if (pathName!=null && !classPoolList.contains(pathName)){
				LOG.debug(pathName + " added to classPool");
				classPoolList.add(pathName);
				classPool.appendClassPath(pathName);
			} else {
				LOG.debug(pathName + " alreaded added to classPool");
			}
		}
	}
	
	/**
	 * Make sure that the files or directories are available to the weaver, which doesn't understand eclipse project paths
	 * @param path
	 * @param project
	 * @return
	 */
	private String getAccessablePathName(IPath path, IProject project){
		String pathName = null;
		File pathFile = path.toFile();
		if (pathFile != null && pathFile.exists()){
			pathName = pathFile.toString();
		} else {
			IFile pathIFile = project.getWorkspace().getRoot().getFile(path);
			if (pathIFile.getLocationURI() != null){
				pathName = pathIFile.getLocationURI().getPath();
			}
		}
		return pathName;
	}
}
