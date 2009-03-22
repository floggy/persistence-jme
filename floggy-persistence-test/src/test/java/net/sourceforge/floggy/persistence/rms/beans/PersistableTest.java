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
package net.sourceforge.floggy.persistence.rms.beans;

import javax.microedition.rms.InvalidRecordIDException;

import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyPersistable;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class PersistableTest extends AbstractTest {

	public final static FloggyPersistable persistable = new FloggyPersistable();

	protected Class getParameterType() {
		return FloggyPersistable.class;
	}

	public Object getNewValueForSetMethod() {
		return new FloggyPersistable();
	}

	public Object getValueForSetMethod() {
		return persistable;
	}

	public Persistable newInstance() {
		return new FloggyPersistable();
	}

	public void testFind() throws Exception {
		Persistable object = newInstance();
		setX(object, getValueForSetMethod());
		manager.save(object);
		try {
			ObjectSet set = manager.find(object.getClass(), null, null);
			// should be equals a 2 because we have a aggregate entity.
			assertEquals(2, set.size());
		} finally {
			manager.delete(object);
		}
	}

	public void testFieldDeleted() throws Exception {
		FloggyPersistable container = new FloggyPersistable();
		FloggyPersistable field = new FloggyPersistable();

		container.setX(field);

		int containerId = manager.save(container);
		try {

			manager.delete(field);

			manager.load(container, containerId);
			fail();

		} catch (Exception ex) {
			assertEquals(InvalidRecordIDException.class.getName(), ex
					.getMessage());
		} finally {
			manager.delete(container);
		}
	}

}
