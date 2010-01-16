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

import javassist.ClassPool;
import javassist.NotFoundException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import org.eclipse.jface.dialogs.MessageDialog;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import net.sourceforge.floggy.eclipse.SetAddDefaultConstructorAction;
import net.sourceforge.floggy.eclipse.SetGenerateSourceAction;
import net.sourceforge.floggy.persistence.Configuration;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public abstract class AbstractBuilder {
	public static final String PERSISTABLE_CLASS_NAME =
		"net.sourceforge.floggy.persistence.Persistable";
	public static final String RECORDSTORE_CLASS_NAME =
		"javax.microedition.rms.RecordStore";

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
	public abstract IProject[] build(IProject project, IProgressMonitor monitor)
		throws Exception;

	/**
	 * DOCUMENT ME!
	*
	* @param folder DOCUMENT ME!
	* @param monitor DOCUMENT ME!
	*
	* @throws CoreException DOCUMENT ME!
	*/
	protected void cleanFolder(IFolder folder, IProgressMonitor monitor)
		throws CoreException {
		IResource[] members = folder.members();

		for (int i = 0; i < members.length; i++) {
			IResource member = members[i];

			if (member.getType() == IResource.FOLDER) {
				cleanFolder((IFolder) member, monitor);
			} else if (member.getType() == IResource.FILE) {
				((IFile) member).delete(true, monitor);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param source DOCUMENT ME!
	* @param destination DOCUMENT ME!
	* @param monitor DOCUMENT ME!
	*
	* @throws CoreException DOCUMENT ME!
	*/
	protected void copyFiles(IFolder source, IFolder destination,
		IProgressMonitor monitor) throws CoreException {
		IFolder newDestination;
		IFile targetFile;
		IFile sourceFile;
		source.refreshLocal(IResource.DEPTH_ONE, monitor);

		IResource[] resources = source.members();

		for (int i = 0; i < resources.length; i++) {
			IResource resource = resources[i];

			if (resource.getType() == IResource.FOLDER) {
				String folderName = resource.getFullPath().lastSegment();
				newDestination = destination.getFolder(folderName);

				if (!newDestination.exists()) {
					newDestination.create(IResource.DERIVED, true, monitor);
				}

				copyFiles((IFolder) resource, newDestination, monitor);
			} else if (resource.getType() == IResource.FILE) {
				sourceFile = (IFile) resource;
				targetFile = destination.getFile(sourceFile.getName());

				if (targetFile.exists()) {
					targetFile.setContents(sourceFile.getContents(), IResource.DERIVED,
						monitor);
				} else {
					targetFile.create(sourceFile.getContents(), IResource.DERIVED, monitor);
				}
			}
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param project DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws CoreException DOCUMENT ME!
	*/
	protected Configuration createWeaverConfiguration(IProject project)
		throws CoreException {
		String addDefaultConstructor =
			project.getPersistentProperty(SetAddDefaultConstructorAction.PROPERTY_NAME);
		String generateSource =
			project.getPersistentProperty(SetGenerateSourceAction.PROPERTY_NAME);

		Configuration configuration = new Configuration();

		configuration.setAddDefaultConstructor(Boolean.valueOf(
				addDefaultConstructor).booleanValue());
		configuration.setGenerateSource(Boolean.valueOf(generateSource)
			 .booleanValue());

		return configuration;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param classPool DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected boolean validateClasspath(ClassPool classPool) {
		boolean valid = true;
		final StringBuffer messages = new StringBuffer();

		try {
			classPool.get(PERSISTABLE_CLASS_NAME);
		} catch (NotFoundException e) {
			valid = false;
			messages.append(
				"You must to add the Floggy framework library to the build path.");
		}

		try {
			classPool.get(RECORDSTORE_CLASS_NAME);
		} catch (NotFoundException e) {
			valid = false;

			if (messages.length() != 0) {
				messages.append('\n');
			}

			messages.append("You must to add the MIDP library to the build path.");
		}

		if (!valid) {
			Display.getDefault().asyncExec(new Runnable() {
					public void run() {
						Shell shell = Display.getDefault().getActiveShell();
						MessageDialog.openError(shell, "Floggy", messages.toString());
					}
				});
		}

		return valid;
	}
}
