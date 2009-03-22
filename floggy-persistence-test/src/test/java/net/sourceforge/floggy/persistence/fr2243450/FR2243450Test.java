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
package net.sourceforge.floggy.persistence.fr2243450;

import javax.microedition.rms.RecordStore;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class FR2243450Test extends AbstractTest {

	protected Class getParameterType() {
		return String.class;
	}

	public Object getValueForSetMethod() {
		return "floggy-with-dinamic-name";
	}

	public Persistable newInstance() {
		return new NamedClass();
	}

	public void testSimple() throws Exception {
		NamedClass persistable = new NamedClass();
		manager.save(persistable);
		try {


			String[] rsNames = RecordStore.listRecordStores();
			boolean found = false;
			for (int i = 0; i < rsNames.length; i++) {
				if (rsNames[i].equals(persistable.getRecordStoreName())) {
					found = true;
					break;
				}
			}
			assertTrue("The RecordStore: " + persistable.getRecordStoreName()
					+ " should have been created.", found);

		} finally {
			manager.delete(persistable);
		}
	}
	
	public void testInheritence() throws Exception {
		SuperNamedClass persistable = new ExtendedNamedClass();
		manager.save(persistable);
		try {


			String[] rsNames = RecordStore.listRecordStores();
			int foundBoth = 0;
			for (int i = 0; i < rsNames.length; i++) {
				if (rsNames[i].equals(persistable.getRecordStoreName()) ||
						rsNames[i].equals(new SuperNamedClass().getRecordStoreName())) {
					foundBoth++;
				}
			}
			assertEquals("The RecordStore: " + persistable.getRecordStoreName()
					+ " should have been created.", 2, foundBoth);

		} finally {
			manager.delete(persistable);
		}
	}

}
