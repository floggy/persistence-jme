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
package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyStringBuffer;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class StringBufferTest extends AbstractTest {
	public static final StringBuffer stringBuffer =
		new StringBuffer("floggy test");

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Filter getFilter() {
		return new Filter() {
				public boolean matches(Persistable arg0) {
					String temp = ((FloggyStringBuffer) arg0).getX().toString();

					return stringBuffer.toString().equals(temp);
				}
			};
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getNewValueForSetMethod() {
		return new StringBuffer();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getValueForSetMethod() {
		return stringBuffer;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Persistable newInstance() {
		return new FloggyStringBuffer();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param o1 DOCUMENT ME!
	* @param o2 DOCUMENT ME!
	*/
	protected void equals(Object o1, Object o2) {
		super.equals(o1.toString(), o2.toString());
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected Class getParameterType() {
		return StringBuffer.class;
	}
}
