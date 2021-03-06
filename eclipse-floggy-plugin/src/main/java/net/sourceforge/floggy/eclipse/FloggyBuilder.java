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

import java.util.Map;

import net.sourceforge.floggy.eclipse.builder.AbstractBuilder;
import net.sourceforge.floggy.eclipse.builder.DefaultBuilder;
import net.sourceforge.floggy.eclipse.builder.MTJBuilder;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

/**
 * 
 DOCUMENT ME!
 * 
 * @author Thiago Moreira
 * @author Dan Murphy
 */
public class FloggyBuilder extends IncrementalProjectBuilder {
	public static final String BUILDER_ID = "net.sourceforge.floggy.floggyBuilder";

	private BuildRequestAssessor assesor;

	public FloggyBuilder() {
		super();
		assesor = new BuildRequestAssessor();
	}

	/**
	 * Performs a build if needed, delegates the decision to build to the
	 * {@link BuildRequestAssessor}. Assuming it is decided a build is required,
	 * the appropriate {@link FloggyBuilder} is used to actually do the build
	 * 
	 * @param kind
	 *            what type of build this is as defined by the constants in
	 *            {@link IncrementalProjectBuilder}
	 * @param args
	 *            a map of arguments for the builder
	 * @param monitor
	 *            progress monitor shown the user
	 * 
	 * @return an array of projects which are of interest if another build
	 *         happens
	 * 
	 * @throws CoreException
	 *             if build cannot be performed and needs user intervention to
	 *             fix the issue
	 */
	public IProject[] build(int kind, Map args, IProgressMonitor monitor)
			throws CoreException {
		try {
			IProject project = getProject();
			IResourceDelta projectDelta = getDelta(project);
			if (assesor.isBuildRequired(project, kind, projectDelta)) {
				AbstractBuilder builder = createFloggyBuilder();
				return builder.build(project, monitor);
			}
		} catch (CoreException ce) {
			throw ce;
		} catch (Exception e) {
			IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, -1,
					e.getMessage(), e);
			throw new CoreException(status);
		}
		return null;
	}

	private AbstractBuilder createFloggyBuilder() {
		if (Activator.isMTJAvailble()) {
			return new MTJBuilder();
		} else {
			return new DefaultBuilder();
		}
	}
}
