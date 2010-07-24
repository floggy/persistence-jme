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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

public class ConfigurationFileResourceListener implements
		IResourceChangeListener {

	public void resourceChanged(IResourceChangeEvent event) {

		IResourceDelta delta = event.getDelta();

		try {
			IPath movedFrom = getMovedFromPath(delta);

			if (movedFrom != null) {
				IPath movedTo = getMovedToPath(delta);

				String projectName = movedTo.segment(0);

				IWorkspace workspace = ResourcesPlugin.getWorkspace();

				IProject project = workspace.getRoot().getProject(projectName);

				String configurationFile =  movedTo.removeFirstSegments(1).toString();

				project.setPersistentProperty(
						ConfigurationFileAction.PROPERTY_NAME,configurationFile);
			}
		} catch (Exception e) {
		}
	}

	protected IPath getMovedFromPath(IResourceDelta delta) throws CoreException {
		if (IResourceDelta.MOVED_FROM == delta.getFlags()) {
			IPath configurationFilePath = delta.getMovedFromPath();

			String projectName = configurationFilePath.segment(0);

			IWorkspace workspace = ResourcesPlugin.getWorkspace();

			IProject project = workspace.getRoot().getProject(projectName);

			String configurationFile = project.getPersistentProperty(ConfigurationFileAction.PROPERTY_NAME);
			
			IFile configurationFileFile = project.getFile(configurationFile);

			if (configurationFileFile != null) {
				return configurationFilePath;
			}
		} else {
			IResourceDelta[] children = delta.getAffectedChildren();
			for (int i = 0; i < children.length; i++) {
				IPath path = getMovedFromPath(children[i]);
				if (path != null) {
					return path;
				}
			}
		}
		return null;
	}

	protected IPath getMovedToPath(IResourceDelta delta) {
		if (IResourceDelta.MOVED_TO == delta.getFlags()) {
			return delta.getMovedToPath();
		}
		else {
			IResourceDelta[] children = delta.getAffectedChildren();
			for (int i = 0; i < children.length; i++) {
				IPath path = getMovedToPath(children[i]);
				if (path != null) {
					return path;
				}
			}
		}
		return null;
	}
}
