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
package net.sourceforge.floggy.persistence.pool;

import java.io.IOException;

import java.net.URL;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public interface InputPool {
	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getFileCount();

	/**
	 * DOCUMENT ME!
	*
	* @param index DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getFileName(int index);

	/**
	 * DOCUMENT ME!
	*
	* @param index DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public URL getFileURL(int index) throws IOException;
}
