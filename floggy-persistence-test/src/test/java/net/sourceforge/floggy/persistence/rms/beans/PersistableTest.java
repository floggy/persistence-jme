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

import java.util.Date;
import java.util.Hashtable;

import javax.microedition.rms.InvalidRecordIDException;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyPersistable;
import net.sourceforge.floggy.persistence.beans.Person;
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
public class PersistableTest extends AbstractTest {
	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getNewValueForSetMethod() {
		return new Person();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getValueForSetMethod() {
		return new Person();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Persistable newInstance() {
		return new FloggyPersistable();
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testFR2422928Read() throws Exception {
		FloggyPersistable container = new FloggyPersistable();
		Person field = new Person("000.345.999-00", "Floggy Open", new Date());

		int fieldId = manager.save(field);
		container.setX(field);
		manager.save(container);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(container.getClass(), null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				FieldPersistableInfo fieldInfo = (FieldPersistableInfo) data.get("x");

				if (fieldInfo != null) {
					assertEquals(fieldId, fieldInfo.getId());
				}
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
		FloggyPersistable container = new FloggyPersistable();
		Person field = new Person("000.345.999-00", "Floggy Open", new Date());

		int fieldId = manager.save(field);
		container.setX(field);
		manager.save(container);

		MigrationManager um = MigrationManager.getInstance();
		Enumeration enumeration = um.start(container.getClass(), null);

		try {
			while (enumeration.hasMoreElements()) {
				Hashtable data = (Hashtable) enumeration.nextElement();
				FieldPersistableInfo fieldInfo = (FieldPersistableInfo) data.get("x");

				if (fieldInfo != null) {
					assertEquals(fieldId, fieldInfo.getId());
				}
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
	public void testFieldDeleted() throws Exception {
		FloggyPersistable container = new FloggyPersistable();
		Person field = new Person("000.345.999-00", "Floggy Open", new Date());

		container.setX(field);

		int containerId = manager.save(container);

		try {
			manager.delete(field);

			manager.load(container, containerId);
			fail();
		} catch (Exception ex) {
			assertEquals(InvalidRecordIDException.class.getName(), ex.getMessage());
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
		return Person.class;
	}
}
