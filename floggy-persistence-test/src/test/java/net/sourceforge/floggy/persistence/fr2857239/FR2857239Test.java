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
package net.sourceforge.floggy.persistence.fr2857239;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.animals.Bird;
import net.sourceforge.floggy.persistence.beans.animals.Falcon;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FR2857239Test extends FloggyBaseTest {
	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testIt() throws Exception {
		Bird bird = new Falcon();
		Person person = new Person();
		person.setX(bird);

		try {
			int id = manager.save(person);
			assertTrue("Deveria ser diferente de -1!", id != -1);

			ObjectSet os = manager.find(person.getClass(), null, null);
			assertFalse(os.isLazy());
			os.setLazy(true);
			assertTrue(os.isLazy());

			for (int i = 0; i < os.size(); i++) {
				assertNull(((Person) os.get(i)).getX());
			}

			os = manager.find(person.getClass(), null, null);
			assertFalse(os.isLazy());

			for (int i = 0; i < os.size(); i++) {
				assertNotNull(((Person) os.get(i)).getX());
			}
		} finally {
			manager.delete(person);
		}
	}
}
