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
package net.sourceforge.floggy.persistence.beans;

import java.lang.reflect.Field;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FloggyStringFinal implements Persistable {
	/**
	 * DOCUMENT ME!
	 */
	protected final String x = null;

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getX() {
		return x;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param s DOCUMENT ME!
	*
	* @throws SecurityException DOCUMENT ME!
	* @throws NoSuchFieldException DOCUMENT ME!
	* @throws IllegalArgumentException DOCUMENT ME!
	* @throws IllegalAccessException DOCUMENT ME!
	*/
	public void setX(String s)
		throws SecurityException, NoSuchFieldException, IllegalArgumentException,
			IllegalAccessException {
		Field f = this.getClass().getDeclaredField("x");
		f.set(this, s);
	}
}
