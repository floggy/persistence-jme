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

package net.sourceforge.floggy.eclipse;

import java.util.Iterator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.jface.viewers.IStructuredSelection;

public class ConfigurationFileAction extends AbstractSetPropertyAction {

	public static final QualifiedName PROPERTY_NAME = new QualifiedName(
			Activator.PLUGIN_ID, "configurationFile");

	public ConfigurationFileAction() {
		super(PROPERTY_NAME);
	}

	protected void changeProperty(IProject project) throws CoreException {

		if (selection instanceof IStructuredSelection) {
			for (Iterator it = ((IStructuredSelection) selection).iterator(); it.hasNext();) {
				Object element = it.next();

				if (element instanceof IFile) {
					IFile file = (IFile) element;

					project.setPersistentProperty(propertyName, file.getProjectRelativePath().toString());
				}
			}
		}

	}

}
