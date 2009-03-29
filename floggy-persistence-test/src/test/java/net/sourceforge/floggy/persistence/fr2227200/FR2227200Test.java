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
package net.sourceforge.floggy.persistence.fr2227200;

import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.animals.Bird;
import net.sourceforge.floggy.persistence.beans.animals.USFalcon;

public class FR2227200Test extends TestCase {

	protected PersistableManager manager = PersistableManager.getInstance();

	public void testFindLazyFalse() throws Exception {
		Person container = new Person();
		Bird field = new USFalcon();

		container.setX(field);

		manager.save(container);
		try {

			ObjectSet os = manager.find(Person.class, null, null, false);

			for (int i = 0; i < os.size(); i++) {
				Person persistable = (Person) os.get(i);
				assertNotNull(persistable.getX());
			}
		} finally {
			manager.delete(container);
		}
	}

	public void testFindLazyTrue() throws Exception {
		Person container = new Person();
		Bird field = new USFalcon();

		container.setX(field);

		manager.save(container);
		try {
			ObjectSet os = manager.find(Person.class, null, null, true);

			for (int i = 0; i < os.size(); i++) {
				Person persistable = (Person) os.get(i);
				assertNull(persistable.getX());
			}
		} finally {
			manager.delete(container);
		}
	}

	public void testLoadLazyFalse() throws Exception {
		Person container = new Person();
		Bird field = new USFalcon();

		container.setX(field);

		int containerId = manager.save(container);
		try {
			Person persistable = new Person();
			manager.load(persistable, containerId, false);
			 
			assertNotNull(persistable.getX());
		} finally {
			manager.delete(container);
		}
	}

	public void testLoadLazyTrue() throws Exception {
		Person container = new Person();
		Bird field = new USFalcon();

		container.setX(field);

		int containerId = manager.save(container);
		try {
			Person persistable = new Person();
			manager.load(persistable, containerId, true);

			assertNull(persistable.getX());
		} finally {
			manager.delete(container);
		}
	}

}
