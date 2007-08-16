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

import java.net.URL;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.core.builder.JavaBuilder;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IObjectActionDelegate;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.console.ConsolePlugin;
import org.osgi.framework.Bundle;

public class ToggleNatureAction implements IObjectActionDelegate {

	private ISelection selection;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		IProject project = getProject(action);
		if (project != null) {
			toggleNature(project, action);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged(IAction action, ISelection selection) {
		this.selection = selection;
		IProject project = getProject(action);
		try {
			if (project != null) {
				if (project.hasNature(JavaCore.NATURE_ID)) {
					action.setEnabled(true);
					if (project.hasNature(FloggyNature.NATURE_ID)) {
						// setting the new label
						action.setText("Remove Floggy Nature");
					} else {
						action.setText("Add Floggy Nature");
					}
				} else {
					action.setEnabled(false);
				}
			}
		} catch (CoreException e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1,
					e.getMessage(), e);
			ConsolePlugin.log(status);
		}
	}

	/*
	 * 
	 * 
	 * @see org.eclipse.ui.IObjectActionDelegate#setActivePart(org.eclipse.jface.action.IAction,
	 *      org.eclipse.ui.IWorkbenchPart)
	 */
	public void setActivePart(IAction action, IWorkbenchPart targetPart) {
	}

	private IProject getProject(IAction action) {
		IProject project = null;
		if (selection instanceof IStructuredSelection) {
			for (Iterator it = ((IStructuredSelection) selection).iterator(); it
					.hasNext();) {
				Object element = it.next();
				if (element instanceof IProject) {
					project = (IProject) element;
				} else if (element instanceof IAdaptable) {
					project = (IProject) ((IAdaptable) element)
							.getAdapter(IProject.class);
				}
				if (project != null) {
					break;
				}
			}
		}
		return project;
	}

	/**
	 * Toggles sample nature on a project
	 * 
	 * @param project
	 *            to have sample nature added or removed
	 * @param action
	 */
	private void toggleNature(IProject project, IAction action) {
		try {
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();

			for (int i = 0; i < natures.length; ++i) {
				if (FloggyNature.NATURE_ID.equals(natures[i])) {
					// Remove the nature
					String[] newNatures = new String[natures.length - 1];
					System.arraycopy(natures, 0, newNatures, 0, i);
					System.arraycopy(natures, i + 1, newNatures, i,
							natures.length - i - 1);

					// verifing the synchronization
					if (!project.isSynchronized(IResource.DEPTH_INFINITE)) {
						project.refreshLocal(IResource.DEPTH_INFINITE, null);
					}

					description.setNatureIds(newNatures);
					project.setDescription(description, null);
					// setting the new label
					action.setText("Add Floggy Nature");
					return;
				}
			}

			// Add the nature
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			newNatures[natures.length] = FloggyNature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);

			
			
			// setting the new label
			action.setText("Remove Floggy Nature");
			
			reorderCommands(project);
			
			updateClasspath(project);

		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1,
					e.getMessage(), e);
			ConsolePlugin.log(status);
		}
	}
	private void reorderCommands(IProject project) throws CoreException {
		IProjectDescription description= project.getDescription();
		ICommand[] commands= description.getBuildSpec();
		ICommand[] newOrder= new ICommand[commands.length];
		int floggyIndex= -1;
		for (int i = 0; i < commands.length; i++) {
			if (commands[i].getBuilderName().equals(JavaCore.BUILDER_ID)) {
				newOrder[i]= commands[i];
				floggyIndex= i+1;
			} else if (commands[i].getBuilderName().equals(FloggyBuilder.BUILDER_ID)){
				newOrder[floggyIndex]= commands[i];
			} else {
				if (floggyIndex != -1) {
					newOrder[i+1]= commands[i];
				} else {
					newOrder[i]= commands[i];
				}
			}
		}
		description.setBuildSpec(newOrder);
		project.setDescription(description, null);
	}
	
	private void updateClasspath(IProject project) throws Exception{
		Bundle bundle = Activator.getDefault().getBundle();
		IJavaProject javaProject = JavaCore.create(project);
		List rawClasspath = new LinkedList(Arrays.asList(javaProject
				.getRawClasspath()));
		
		boolean contains = false;
		for (int i = 0; i < rawClasspath.size(); i++) {
			IClasspathEntry classpath = (IClasspathEntry) rawClasspath
					.get(i);
			if (classpath.getPath().toOSString().contains(
					"floggy-persistence-framework")) {
				contains = true;
				break;
			}
		}
		if (!contains) {
			Enumeration e = bundle.findEntries("/", "*.jar", true);
			while (e.hasMoreElements()) {
				URL url = (URL) e.nextElement();
				String path = FileLocator.toFileURL(url).getPath();
				if (path.contains("floggy-persistence-framework")) {
					IClasspathEntry varEntry = JavaCore.newLibraryEntry(
							new Path(path), null, null, true);
					rawClasspath.add(varEntry);
					javaProject.setRawClasspath(
							(IClasspathEntry[]) rawClasspath
									.toArray(new IClasspathEntry[0]), null);
				}
			}
		}
	}

}
