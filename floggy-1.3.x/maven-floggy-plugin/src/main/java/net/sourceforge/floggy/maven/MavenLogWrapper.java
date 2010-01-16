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

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class MavenLogWrapper extends NoOpLog {
	private static Log log;

	/**
	 * Creates a new MavenLogWrapper object.
	 *
	 * @param name DOCUMENT ME!
	 */
	public MavenLogWrapper(String name) {
	}

	/**
	 * DOCUMENT ME!
	*
	* @param log DOCUMENT ME!
	*/
	public static void setLog(Log log) {
		MavenLogWrapper.log = log;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param message DOCUMENT ME!
	*/
	public void debug(Object message) {
		if (message instanceof Throwable) {
			MavenLogWrapper.log.debug((Throwable) message);
		} else {
			MavenLogWrapper.log.debug(String.valueOf(message));
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param message DOCUMENT ME!
	* @param t DOCUMENT ME!
	*/
	public void debug(Object message, Throwable t) {
		if (MavenLogWrapper.log.isDebugEnabled())
			MavenLogWrapper.log.debug(String.valueOf(message), t);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param message DOCUMENT ME!
	*/
	public void error(Object message) {
		if (message instanceof Throwable) {
			MavenLogWrapper.log.error((Throwable) message);
		} else {
			MavenLogWrapper.log.error(String.valueOf(message));
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param message DOCUMENT ME!
	* @param t DOCUMENT ME!
	*/
	public void error(Object message, Throwable t) {
		MavenLogWrapper.log.error(String.valueOf(message), t);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param message DOCUMENT ME!
	*/
	public void info(Object message) {
		if (message instanceof Throwable) {
			MavenLogWrapper.log.info((Throwable) message);
		} else {
			MavenLogWrapper.log.info(String.valueOf(message));
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param message DOCUMENT ME!
	* @param t DOCUMENT ME!
	*/
	public void info(Object message, Throwable t) {
		MavenLogWrapper.log.info(String.valueOf(message), t);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param message DOCUMENT ME!
	*/
	public void warn(Object message) {
		if (message instanceof Throwable) {
			MavenLogWrapper.log.warn((Throwable) message);
		} else {
			MavenLogWrapper.log.warn(String.valueOf(message));
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param message DOCUMENT ME!
	* @param t DOCUMENT ME!
	*/
	public void warn(Object message, Throwable t) {
		MavenLogWrapper.log.warn(String.valueOf(message), t);
	}
}
