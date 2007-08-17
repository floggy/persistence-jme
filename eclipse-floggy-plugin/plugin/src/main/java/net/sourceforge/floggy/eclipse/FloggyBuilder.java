/**
 *  Copyright 2006 Floggy Open Source Group
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sourceforge.floggy.eclipse;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;
import javassist.NotFoundException;
import net.sourceforge.floggy.persistence.Weaver;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
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

public class FloggyBuilder extends IncrementalProjectBuilder {

	public static final String BUILDER_ID = "net.sourceforge.floggy.floggyBuilder";

	public static final String RECORDSTORE_CLASS_NAME = "javax.microedition.rms.RecordStore";

	public static final String PERSISTABLE_CLASS_NAME = "net.sourceforge.floggy.persistence.Persistable";

	/**
	 *
	 *
	 * @see org.eclipse.core.internal.events.InternalBuilder#build(int,
	 *      java.util.Map, org.eclipse.core.runtime.IProgressMonitor)
	 */
	protected IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		boolean generateSource = false;
		try {
			LogFactory.getFactory().setAttribute("org.apache.commons.logging.Log", EclipseLog.class.getName());
			IProject project= getProject();
			IJavaProject javaProject = JavaCore.create(project);
			IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);

			//creating the classpath
			List classpathList = new ArrayList();
			ClassPool classPool= new ClassPool();
			for (int i = 0; i < entries.length; i++) {
				IClasspathEntry classpathEntry= entries[i];
				String path= classpathEntry.getPath().toOSString();
				classpathList.add(path);
				classPool.appendClassPath(path);
			}

			final StringBuilder messages= new StringBuilder();
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
				IPath root= project.getLocation();
				File input= root.removeLastSegments(1).append(javaProject.getOutputLocation()).toFile();

				File temp = new File(root.toFile(), String
						.valueOf(System.currentTimeMillis()));
				FileUtils.forceMkdir(temp);
				weaver.setOutputFile(temp);
				weaver.setInputFile(input);
				weaver.setClasspath((String[])classpathList.toArray(new String[0]));
				weaver.setGenerateSource(generateSource);
				weaver.execute();
				FileUtils.copyDirectory(temp, input);
				FileUtils.forceDelete(temp);

				// verifing the synchronization
				if (!project.isSynchronized(IResource.DEPTH_INFINITE)) {
					project.refreshLocal(IResource.DEPTH_INFINITE, null);
				}

			}
		} catch (Exception e) {
			IStatus status= new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1, e.getMessage(), e);
			throw new CoreException(status);
		}
		return null;
	}
}
