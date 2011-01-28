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
import net.sourceforge.floggy.persistence.beans.FloggyNoneFields;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.MigrationManager;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class NoneFieldTest extends AbstractTest {
	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getNewValueForSetMethod() {
		return null;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getValueForSetMethod() {
		return null;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Persistable newInstance() {
		return new FloggyNoneFields();
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFR2422928Read() throws Exception {
		Persistable object = newInstance();
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
		manager.save(oldObject);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(oldObject.getClass(), null);

		try {
			while (enumeration.hasMoreElements()) {
				Persistable newObject = newInstance();
				enumeration.nextElement();

				int oldId = manager.getId(oldObject);
				int newId = enumeration.update(newObject);
				assertEquals(oldId, newId);
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
	public void testFind() throws Exception {
		Persistable object = newInstance();
		manager.save(object);

		try {
			ObjectSet set = manager.find(object.getClass(), null, null);
			assertTrue(1 <= set.size());
		} finally {
			manager.delete(object);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFindWithFilter() throws Exception {
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testNotNullAttribute() throws Exception {
		Persistable object1 = newInstance();
		int id = manager.save(object1);

		try {
			assertTrue("Deveria ser diferente de -1!", id != -1);

			Persistable object2 = newInstance();
			manager.load(object2, id);
			assertEquals(object1, object2);
		} finally {
			manager.delete(object1);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testNullAttribute() throws Exception {
		Persistable object = newInstance();
		int id = manager.save(object);

		try {
			assertTrue("Deveria ser diferente de -1!", id != -1);
			object = newInstance();
			manager.load(object, id);
		} finally {
			manager.delete(object);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testSaveAndEdit() throws Exception {
		Persistable object = newInstance();
		int id = manager.save(object);

		try {
			assertTrue("Deveria ser diferente de -1!", id != -1);
			object = newInstance();
			manager.load(object, id);

			int tempId = manager.save(object);
			assertEquals(id, tempId);
		} finally {
			manager.delete(object);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected Class getParameterType() {
		return null;
	}
}
