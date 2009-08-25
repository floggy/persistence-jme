/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
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
package net.sourceforge.floggy.persistence.rms.beans.animals;

import java.util.Hashtable;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.animals.Bird;
import net.sourceforge.floggy.persistence.beans.animals.Falcon;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.FieldPersistableInfo;
import net.sourceforge.floggy.persistence.migration.MigrationManager;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class FalconTest extends AbstractTest {

	protected Class getParameterType() {
		return Bird.class;
	}

	public Object getValueForSetMethod() {
		Bird bird = new Falcon();
		bird.setColor("black");
		return bird;
	}

	public Persistable newInstance() {
		return new Person();
	}

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

}
