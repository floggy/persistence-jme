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

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class WeaverException extends Exception {
	private static final long serialVersionUID = -5031495264446460275L;

	/**
	 * Creates a new WeaverException object.
	 *
	 * @param message DOCUMENT ME!
	 */
	public WeaverException(String message) {
		super(message);
	}

	/**
	 * Creates a new WeaverException object.
	 *
	 * @param message DOCUMENT ME!
	 * @param throwable DOCUMENT ME!
	 */
	public WeaverException(String message, Throwable throwable) {
		super(message, throwable);
	}

	/**
	 * Creates a new WeaverException object.
	 *
	 * @param throwable DOCUMENT ME!
	 */
	public WeaverException(Throwable throwable) {
		super(throwable);
	}
}
