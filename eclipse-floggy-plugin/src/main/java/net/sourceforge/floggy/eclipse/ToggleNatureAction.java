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
package net.sourceforge.floggy.eclipse;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import java.net.URL;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;

import org.eclipse.jdt.core.IAccessRule;
import org.eclipse.jdt.core.IClasspathAttribute;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

import org.eclipse.jface.action.IAction;

import org.eclipse.ui.console.ConsolePlugin;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class ToggleNatureAction extends AbstractFloggyAction {
	/**
	* Toggles sample nature on a project
	*
	* @param project to have sample nature added or removed
	* @param action
	*/
	public void run(IProject project, IAction action) {
		try {
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();

			for (int i = 0; i < natures.length; ++i) {
				if (FloggyNature.NATURE_ID.equals(natures[i])) {
					String[] newNatures = new String[natures.length - 1];
					System.arraycopy(natures, 0, newNatures, 0, i);
					System.arraycopy(natures, i + 1, newNatures, i, natures.length - i
						- 1);

					description.setNatureIds(newNatures);
					project.setDescription(description, null);

					return;
				}
			}

			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = FloggyNature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);

			reorderCommands(project);

			updateClasspath(project);

			setDefaultPersistentProperties(project);
		} catch (Exception e) {
			String message = e.getMessage();

			if (message == null) {
				message = e.getClass().getName();
			}

			IStatus status =
				new Status(IStatus.ERROR, Activator.PLUGIN_ID, IStatus.ERROR, message, e);
			ConsolePlugin.log(status);
		}
	}

	private String getVersion(String path) {
		String version = null;
		int endIndex = path.lastIndexOf(".jar");
		int startIndex = path.indexOf("work-") + 5;

		if ((startIndex != 4) && (endIndex != -1) && (startIndex < endIndex)) {
			version = path.substring(startIndex, endIndex);
		}

		return version;
	}

	private void reorderCommands(IProject project) throws CoreException {
		IProjectDescription description = project.getDescription();
		ICommand[] commands = description.getBuildSpec();
		ICommand[] newOrder = new ICommand[commands.length];
		int floggyIndex = -1;

		for (int i = 0; i < commands.length; i++) {
			if (commands[i].getBuilderName().equals(JavaCore.BUILDER_ID)) {
				newOrder[i] = commands[i];
				floggyIndex = i + 1;
			} else if (commands[i].getBuilderName().equals(FloggyBuilder.BUILDER_ID)) {
				newOrder[floggyIndex] = commands[i];
			} else {
				if (floggyIndex != -1) {
					newOrder[i + 1] = commands[i];
				} else {
					newOrder[i] = commands[i];
				}
			}
		}

		description.setBuildSpec(newOrder);
		project.setDescription(description, null);
	}

	private void setDefaultPersistentProperties(IProject project)
		throws CoreException {
		project.setPersistentProperty(SetGenerateSourceAction.PROPERTY_NAME,
			String.valueOf(false));
		project.setPersistentProperty(SetAddDefaultConstructorAction.PROPERTY_NAME,
			String.valueOf(true));
		project.setPersistentProperty(ConfigurationFileAction.PROPERTY_NAME,
			"floggy.xml");
	}

	private void updateClasspath(IProject project) throws Exception {
		IJavaProject javaProject = JavaCore.create(project);
		List rawClasspath =
			new LinkedList(Arrays.asList(javaProject.getRawClasspath()));

		boolean contains = false;

		for (int i = 0; i < rawClasspath.size(); i++) {
			IClasspathEntry classpath = (IClasspathEntry) rawClasspath.get(i);

			if (classpath.getPath().toOSString()
				 .indexOf("floggy-persistence-framework") != -1) {
				contains = true;

				break;
			}
		}

		if (!contains) {
			Enumeration e = Activator.findEntries("/", "*.jar", true);

			while (e.hasMoreElements()) {
				URL url = (URL) e.nextElement();
				String path = FileLocator.toFileURL(url).getPath();

				if (path.indexOf("floggy-persistence-framework") != -1) {
					String javadocURL =
						"http://floggy.sourceforge.net/modules/floggy-persistence-framework/apidocs/";

					String version = getVersion(path);

					if (version != null) {
						javadocURL = "http://floggy.sourceforge.net/modules/floggy-persistence-framework/"
							+ version + "/floggy-persistence-framework/apidocs/";
					}

					IClasspathAttribute attribute =
						JavaCore.newClasspathAttribute("javadoc_location", javadocURL);
					IClasspathEntry varEntry =
						JavaCore.newLibraryEntry(new Path(path), null, null, null,
							new IClasspathAttribute[] { attribute }, true);
					rawClasspath.add(varEntry);
					javaProject.setRawClasspath((IClasspathEntry[]) rawClasspath.toArray(
							new IClasspathEntry[rawClasspath.size()]), null);

					break;
				}
			}

			if (Activator.isMTJAvailble()) {
				IFile implJar =
					project.getFile("floggy-persistence-framework-impl.jar");

				if (!implJar.exists()) {
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					JarOutputStream out = new JarOutputStream(baos, new Manifest());

					out.close();

					implJar.create(new ByteArrayInputStream(baos.toByteArray()),
						IResource.DERIVED, null);
				}

				IPath filePatternImpl =
					new Path("net/sourceforge/floggy/persistence/impl/*");
				IPath filePatternImplMigration =
					new Path("net/sourceforge/floggy/persistence/impl/migration/*");

				IAccessRule[] accessRules = new IAccessRule[2];

				accessRules[0] = JavaCore.newAccessRule(filePatternImpl,
						IAccessRule.K_NON_ACCESSIBLE);
				accessRules[1] = JavaCore.newAccessRule(filePatternImplMigration,
						IAccessRule.K_NON_ACCESSIBLE);

				IClasspathEntry entry =
					JavaCore.newLibraryEntry(implJar.getLocation(), null, null,
						accessRules, null, true);

				rawClasspath.add(entry);
				javaProject.setRawClasspath((IClasspathEntry[]) rawClasspath.toArray(
						new IClasspathEntry[rawClasspath.size()]), null);
			}
		}
	}
}
