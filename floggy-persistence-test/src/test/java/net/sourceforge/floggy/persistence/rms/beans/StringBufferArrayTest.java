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

import junit.framework.Assert;

import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyStringBufferArray;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class StringBufferArrayTest extends AbstractTest {
	/**
	 * DOCUMENT ME!
	 */
	protected StringBuffer[] buffers =
		new StringBuffer[] {
			StringBufferTest.stringBuffer, null, new StringBuffer("string")
		};

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Filter getFilter() {
		return new Filter() {
				public boolean matches(Persistable arg0) {
					StringBuffer[] temp = ((FloggyStringBufferArray) arg0).getX();
					StringBuffer[] s1 = temp;
					StringBuffer[] s2 = buffers;
					boolean equal = true;

					for (int i = 0; i < s1.length; i++) {
						if ((s1[i] == null) && (s2[i] == null)) {
						} else if ((s1[i] != null) && (s2[i] != null)) {
							if (!s1[i].toString().equals(s2[i].toString())) {
								equal = false;

								break;
							}
						} else {
							equal = false;

							break;
						}
					}

					return equal;
				}
			};
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getNewValueForSetMethod() {
		return new StringBuffer[0];
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getValueForSetMethod() {
		return buffers;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Persistable newInstance() {
		return new FloggyStringBufferArray();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param o1 DOCUMENT ME!
	* @param o2 DOCUMENT ME!
	*/
	protected void equals(Object o1, Object o2) {
		StringBuffer[] s1 = (StringBuffer[]) o1;
		StringBuffer[] s2 = (StringBuffer[]) o2;

		for (int i = 0; i < s1.length; i++) {
			if ((s1[i] == null) && (s2[i] == null)) {
				super.equals(s1[i], s2[i]);
			} else if ((s1[i] != null) && (s2[i] != null)) {
				super.equals(s1[i].toString(), s2[i].toString());
			} else {
				Assert.fail("This is different: " + s1[i] + " != " + s2[i]);
			}
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected Class getParameterType() {
		return StringBuffer[].class;
	}
}
