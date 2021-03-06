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
package net.sourceforge.floggy.persistence.rms.beans.animals;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.beans.animals.Reptile;
import net.sourceforge.floggy.persistence.beans.animals.Sucuri;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class SucuriTest extends FloggyBaseTest {
	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testDeleteWithSuperClassBeenPersistable()
		throws Exception {
		Sucuri sucuri = new Sucuri();
		manager.save(sucuri);
		assertTrue(manager.isPersisted(sucuri));

		ObjectSet set = manager.find(Sucuri.class, null, null);
		assertEquals(1, set.size());

		set = manager.find(Reptile.class, null, null);
		assertEquals(0, set.size());

		manager.delete(sucuri);
		assertFalse(manager.isPersisted(sucuri));

		set = manager.find(Reptile.class, null, null);
		assertEquals(0, set.size());

		set = manager.find(Sucuri.class, null, null);
		assertEquals(0, set.size());
	}
}
