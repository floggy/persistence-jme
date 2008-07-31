/**
 *  Copyright (c) 2005-2008 Floggy Open Source Group. All rights reserved.
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

import org.apache.commons.logging.impl.NoOpLog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.console.ConsolePlugin;

public class EclipseLog extends NoOpLog {

	private static final long serialVersionUID = -1621992334936365145L;

	public EclipseLog() {
		super();
	}

	public EclipseLog(String name) {
		super(name);
	}

	public void debug(Object message) {
		log(IStatus.INFO, message);
	}

	public void debug(Object message, Throwable t) {
		log(IStatus.INFO, message, t);
	}

	public void error(Object message) {
		log(IStatus.ERROR, message);
	}

	public void error(Object message, Throwable t) {
		log(IStatus.ERROR, message, t);
	}

	public void info(Object message) {
		log(IStatus.INFO, message);
	}

	public void info(Object message, Throwable t) {
		log(IStatus.INFO, message, t);
	}

	protected void log(int severity, Object message) {
		log(severity, message, null);
	}

	protected void log(int severity, Object message, Throwable throwable) {
		IStatus status = new Status(severity, Activator.PLUGIN_ID, -1,
				(String) message, throwable);
		ConsolePlugin.log(status);
	}

	public void warn(Object message) {
		log(IStatus.WARNING, message);
	}

	public void warn(Object message, Throwable t) {
		log(IStatus.WARNING, message, t);
	}

}
