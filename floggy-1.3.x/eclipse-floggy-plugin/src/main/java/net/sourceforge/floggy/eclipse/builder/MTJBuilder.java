/**
 * Copyright (c) 2006-2010 Floggy Open Source Group. All rights reserved.
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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import net.sourceforge.floggy.persistence.Weaver;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class MTJBuilder extends AbstractBuilder {
	/**
	 * DOCUMENT ME!
	*
	* @param project DOCUMENT ME!
	* @param monitor DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public IProject[] build(IProject project, IProgressMonitor monitor)
		throws Exception {
		IJavaProject javaProject = JavaCore.create(project);

		IClasspathEntry[] entries = javaProject.getResolvedClasspath(true);

		List classpathList = new ArrayList();
		ClassPool classPool = new ClassPool();
		IClasspathEntry classpathEntry;
		String pathName;

		for (int i = 0; i < entries.length; i++) {
			classpathEntry = JavaCore.getResolvedClasspathEntry(entries[i]);
			pathName = classpathEntry.getPath().toFile().toString();

			if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_LIBRARY) {
				if (!classpathEntry.getPath().toFile().exists()) {
					IFile pathIFile =
						project.getWorkspace().getRoot().getFile(classpathEntry.getPath());

					if (pathIFile.getLocationURI() == null) {
						continue;
					}

					pathName = pathIFile.getLocationURI().getPath();
				}
			}

			if (pathName.contains("floggy-persistence-framework-impl.jar")) {
				continue;
			}

			classpathList.add(pathName);
			classPool.appendClassPath(pathName);
		}

		if (validateClasspath(classPool)) {
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

			weaver.setEmbeddedClassesOutputPool(implJar.getLocation().toFile());
			weaver.setOutputFile(floggyTemp.getLocation().toFile());
			weaver.setInputFile(input);
			weaver.setClasspath((String[]) classpathList.toArray(
					new String[classpathList.size()]));
			weaver.setConfiguration(createWeaverConfiguration(project));
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

		return new IProject[0];
	}
}
