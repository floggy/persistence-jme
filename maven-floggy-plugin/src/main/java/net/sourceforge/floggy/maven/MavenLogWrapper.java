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

package net.sourceforge.floggy.maven;

import org.apache.commons.logging.impl.NoOpLog;
import org.apache.maven.plugin.logging.Log;

public class MavenLogWrapper extends NoOpLog {

	private static Log log;

	public static void setLog(Log log) {
		MavenLogWrapper.log = log;
	}

	public MavenLogWrapper(String name) {
	}

	public void debug(Object message) {
		if (message instanceof Throwable) {
			MavenLogWrapper.log.debug((Throwable) message);
		} else {
			MavenLogWrapper.log.debug(String.valueOf(message));
		}
	}

	public void debug(Object message, Throwable t) {
		MavenLogWrapper.log.debug(String.valueOf(message), t);
	}

	public void error(Object message) {
		if (message instanceof Throwable) {
			MavenLogWrapper.log.error((Throwable) message);
		} else {
			MavenLogWrapper.log.error(String.valueOf(message));
		}
	}

	public void error(Object message, Throwable t) {
		MavenLogWrapper.log.error(String.valueOf(message), t);
	}

	public void info(Object message) {
		if (message instanceof Throwable) {
			MavenLogWrapper.log.info((Throwable) message);
		} else {
			MavenLogWrapper.log.info(String.valueOf(message));
		}
	}

	public void info(Object message, Throwable t) {
		MavenLogWrapper.log.info(String.valueOf(message), t);
	}

	public void warn(Object message) {
		if (message instanceof Throwable) {
			MavenLogWrapper.log.warn((Throwable) message);
		} else {
			MavenLogWrapper.log.warn(String.valueOf(message));
		}
	}

	public void warn(Object message, Throwable t) {
		MavenLogWrapper.log.warn(String.valueOf(message), t);
	}

}
