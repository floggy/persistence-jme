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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public abstract class AbstractFloggyAction implements IObjectActionDelegate {
	/**
	 * DOCUMENT ME!
	 */
	protected ISelection selection;

	/**
	 * DOCUMENT ME!
	*
	* @param action DOCUMENT ME!
	*/
	public void run(IAction action) {
		IProject project = getProject(action);

		if (project != null) {
			run(project, action);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param project DOCUMENT ME!
	* @param action DOCUMENT ME!
	*/
	public abstract void run(IProject project, IAction action);

	/**
	 * DOCUMENT ME!
	*
	* @param action DOCUMENT ME!
	* @param selection DOCUMENT ME!
	*/
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param action DOCUMENT ME!
	* @param targetPart DOCUMENT ME!
	*/
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	/**
	 * DOCUMENT ME!
	*
	* @param action DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected IProject getProject(IAction action) {
		IProject project = null;

		if (selection instanceof IStructuredSelection) {
			for (Iterator it = ((IStructuredSelection) selection).iterator();
				 it.hasNext();) {
				Object element = it.next();

				if (element instanceof IProject) {
					project = (IProject) element;
				} else if (element instanceof IAdaptable) {
					project = (IProject) ((IAdaptable) element).getAdapter(IProject.class);
				}

				if (project != null) {
					break;
				}
			}
		}

		return project;
	}
}
