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

import java.util.Hashtable;

import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyTransient;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.MigrationManager;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class TransientTest extends AbstractTest {
	static final Object object = new Object();

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getNewValueForSetMethod() {
		return new Object();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getValueForSetMethod() {
		return object;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Persistable newInstance() {
		return new FloggyTransient();
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFR2422928Read() throws Exception {
		Persistable object = newInstance();
		setX(object, getValueForSetMethod());
		manager.save(object);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(object.getClass(), null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertTrue(data.isEmpty());
			}
		} finally {
			manager.delete(object);
			um.finish(object.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFR2422928Update() throws Exception {
		Persistable oldObject = newInstance();
		setX(oldObject, getValueForSetMethod());
		manager.save(oldObject);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(oldObject.getClass(), null);

		try {
			while (enumeration.hasMoreElements()) {
				Persistable newObject = newInstance();
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertTrue(data.isEmpty());

				int oldId = manager.getId(oldObject);
				int newId = enumeration.update(newObject);
				assertEquals(oldId, newId);

				manager.load(newObject, newId);
				assertNull(getX(newObject));
			}
		} finally {
			manager.delete(oldObject);
			um.finish(oldObject.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFindWithFilter() throws Exception {
		Persistable object = newInstance();
		setX(object, getValueForSetMethod());
		manager.save(object);

		try {
			ObjectSet set = manager.find(object.getClass(), getFilter(), null);
			assertEquals(0, set.size());
		} finally {
			manager.delete(object);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testNotNullAttribute() throws Exception {
		super.testNullAttribute();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected Class getParameterType() {
		return Object.class;
	}
}
