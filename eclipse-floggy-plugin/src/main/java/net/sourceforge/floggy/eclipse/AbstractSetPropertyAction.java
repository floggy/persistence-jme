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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IAction;
import org.eclipse.ui.console.ConsolePlugin;

public abstract class AbstractSetPropertyAction extends AbstractFloggyAction {

	protected final QualifiedName propertyName;
	
	protected AbstractSetPropertyAction(QualifiedName propertyName) {
		this.propertyName = propertyName;
	}

	protected void changeProperty(IProject project) throws CoreException {
		project.setPersistentProperty(propertyName, String
				.valueOf(!isEnabled(project)));
	}

	private boolean isEnabled(IProject project) throws CoreException {
		String value = project.getPersistentProperty(propertyName);
		return Boolean.valueOf(value).booleanValue();
	}

	public void run(IProject project, IAction action) {
		try {
			changeProperty(project);
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1,
					e.getMessage(), e);
			ConsolePlugin.log(status);
		}
	}

}
