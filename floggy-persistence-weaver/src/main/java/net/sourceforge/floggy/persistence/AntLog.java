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
package net.sourceforge.floggy.persistence;

import org.apache.commons.logging.impl.NoOpLog;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

public class AntLog extends NoOpLog{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3923443304806683984L;
	
	private static Task task;
	
	public static void setTask(Task task) {
		AntLog.task= task;
	}

	public AntLog() {
		super();
	}

	public AntLog(String name) {
		super(name);
	}

	public void debug(Object message) {
		log(Project.MSG_INFO, message);
	}

	public void debug(Object message, Throwable t) {
		log(Project.MSG_INFO, message, t);
	}

	public void warn(Object message) {
		log(Project.MSG_WARN, message);
	}

	public void warn(Object message, Throwable t) {
		log(Project.MSG_WARN, message, t);
	}

	public void error(Object message) {
		log(Project.MSG_ERR, message);
	}
	
	public void error(Object message, Throwable t) {
		log(Project.MSG_ERR, message, t);
	}
	
	public void info(Object message, Throwable t) {
		log(Project.MSG_INFO, message, t);
	}
	
	public void info(Object message) {
		log(Project.MSG_INFO, message);
	}
	
	protected void log(int severity, Object message) {
		log(severity, message, null);
	}
	
	protected void log(int severity, Object message, Throwable throwable) {
		if (task != null) {
			if (message == null) {
				message= "null";
			}
			task.log(message.toString(), severity);
		}
	}
	

}
