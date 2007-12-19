/**
 *  Copyright 2006 Floggy Open Source Group
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyNoneFields;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class NoneFieldTest extends AbstractTest {

	public Object getValueForSetMethod() {
		return null;
	}

	public Persistable newInstance() {
		return new FloggyNoneFields();
	}
	
	public void testFind() throws Exception {
		Persistable object = newInstance();
		manager.save(object);
		ObjectSet set = manager.find(object.getClass(), null, null);
		assertTrue(1 <= set.size());
		manager.delete(object);
	}

	public void testFindWithFilter() throws Exception {
	}

	public void testNotNullAttribute() throws Exception {
		Persistable object1 = newInstance();
		int id = manager.save(object1);
		assertTrue("Deveria ser diferente de -1!", id != -1);
		Persistable object2 = newInstance();
		manager.load(object2, id);
		assertEquals(object1, object2);
		manager.delete(object1);
	}

	public void testNullAttribute() throws Exception {
		Persistable object = newInstance();
		int id = manager.save(object);
		assertTrue("Deveria ser diferente de -1!", id != -1);
		object = newInstance();
		manager.load(object, id);
		manager.delete(object);
	}

}
