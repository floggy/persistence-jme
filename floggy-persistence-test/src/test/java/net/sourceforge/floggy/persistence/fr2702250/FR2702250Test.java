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
package net.sourceforge.floggy.persistence.fr2702250;

import javax.microedition.rms.InvalidRecordIDException;

import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.beans.Person;

public class FR2702250Test extends TestCase {
	
	public void testIt() throws Exception {
		PersistableManager pm = PersistableManager.getInstance();

		Person person = new Person();
		person.setNome("FR2702250Test");
		int personId = pm.save(person);
		
		FR2702250 fr2702250 = new FR2702250();
		fr2702250.setX(person);
		pm.save(fr2702250);
		
		try {
			pm.delete(fr2702250);
			
			pm.load(new Person(), personId);
			fail("It must throw a InvalidRecordIdException");
		} catch (Exception ex) {
			assertEquals(InvalidRecordIDException.class.getName(), ex.getMessage());
		}
	}

}
