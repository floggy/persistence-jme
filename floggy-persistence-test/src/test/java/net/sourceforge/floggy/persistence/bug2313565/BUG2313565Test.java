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
package net.sourceforge.floggy.persistence.bug2313565;

import java.util.Hashtable;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.animals.Bird;
import net.sourceforge.floggy.persistence.beans.animals.EastUSFalcon;
import net.sourceforge.floggy.persistence.beans.animals.Falcon;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.FieldPersistableInfo;
import net.sourceforge.floggy.persistence.migration.MigrationManager;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class BUG2313565Test extends AbstractTest {
	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getNewValueForSetMethod() {
		return new Falcon();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getValueForSetMethod() {
		return new Bird();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Persistable newInstance() {
		return new BUG2313565();
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFR2422928Read() throws Exception {
		Persistable container = newInstance();
		Persistable field = (Persistable) getValueForSetMethod();
		int fieldId = manager.save(field);
		setX(container, field);
		manager.save(container);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(container.getClass(), null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				assertFalse("Should not be empty!", data.isEmpty());

				FieldPersistableInfo pi = (FieldPersistableInfo) data.get("x");
				assertEquals(pi.getId(), fieldId);
			}
		} finally {
			manager.delete(container);
			um.finish(container.getClass());
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
				assertFalse("Should not be empty!", data.isEmpty());

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
	public void testLoadLazyFalse() throws Exception {
		BUG2313565 container = new BUG2313565();
		container.w = true;
		container.y = 22;

		Bird field = new EastUSFalcon();

		container.setX(field);

		int containerId = manager.save(container);

		try {
			BUG2313565 container2 = new BUG2313565();
			manager.load(container2, containerId, false);

			assertEquals(container.w, container2.w);
			assertEquals(container.getX(), container2.getX());
			assertEquals(container.y, container2.y);
		} finally {
			manager.delete(container);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testLoadLazyTrue() throws Exception {
		BUG2313565 container = new BUG2313565();
		container.w = true;
		container.y = 22;

		Bird field = new EastUSFalcon();

		container.setX(field);

		int containerId = manager.save(container);

		try {
			BUG2313565 container2 = new BUG2313565();
			manager.load(container2, containerId, true);

			assertNull(container2.getX());
			assertEquals(container.w, container2.w);
			assertEquals(container.y, container2.y);
		} finally {
			manager.delete(container);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected Class getParameterType() {
		return Bird.class;
	}
}
