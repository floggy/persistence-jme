/**
 * Copyright (c) 2006-2010 Floggy Open Source Group. All rights reserved.
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

package net.sourceforge.floggy.persistence.bug2168632;

import net.sourceforge.floggy.persistence.FloggyBaseTest;

public class BUG2168632Test extends FloggyBaseTest {

	public void testIt() throws Exception {
		ConcreteElement persistable = new ConcreteElement();
		persistable.setName("floggy");

		try {
			int id = manager.save(persistable);

			ConcreteElement persistable2 = new ConcreteElement();

			manager.load(persistable2, id);
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(persistable);
		}
	}

	public void testNorton() throws Exception {
		String name = "XYZ";
		int age = 23;
		ExtendedConcreteElement persistable =new ExtendedConcreteElement();
		persistable.setName(name);
		persistable.setAge(age);

		try {
			int id = manager.save(persistable);

			ExtendedConcreteElement persistable2 = new ExtendedConcreteElement();

			manager.load(persistable2, id);
			assertEquals(name, persistable2.getName());
			assertEquals(age, persistable2.getAge());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(persistable);
		}
	}
}
