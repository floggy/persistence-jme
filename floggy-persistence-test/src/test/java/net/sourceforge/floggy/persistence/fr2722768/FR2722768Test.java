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
package net.sourceforge.floggy.persistence.fr2722768;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FR2722768Test extends AbstractTest {
	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getValueForSetMethod() {
		return "IDable";
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Persistable newInstance() {
		return new FR2722768SuperClass();
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testExtendedOfAbstractSuperClass() throws Exception {
		FR2722768AbstractSuperClass fr2722768 =
			new FR2722768ExtendedOfAbstractSuperClass();

		try {
			int id = manager.save(fr2722768);
			assertEquals(id, fr2722768.getId());
		} finally {
			manager.delete(fr2722768);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testExtendedOfSuperClass() throws Exception {
		FR2722768SuperClass fr2722768 = new FR2722768ExtendedOfSuperClass();

		try {
			int id = manager.save(fr2722768);
			assertEquals(id, fr2722768.getId());
		} finally {
			manager.delete(fr2722768);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testSuperClass() throws Exception {
		FR2722768SuperClass fr2722768 = new FR2722768SuperClass();

		try {
			int id = manager.save(fr2722768);
			assertEquals(id, fr2722768.getId());
		} finally {
			manager.delete(fr2722768);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected Class getParameterType() {
		return String.class;
	}
}
