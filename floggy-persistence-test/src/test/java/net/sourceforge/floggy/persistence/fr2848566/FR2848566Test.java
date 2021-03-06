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
package net.sourceforge.floggy.persistence.fr2848566;

import java.util.Hashtable;
import java.util.Vector;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.beans.FloggyHashtable;
import net.sourceforge.floggy.persistence.beans.FloggyVector;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FR2848566Test extends FloggyBaseTest {
	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testHashtableHoldingVector() throws Exception {
		Vector vector = new Vector();
		vector.add("String");

		Hashtable hashtable = new Hashtable();
		hashtable.put("vector", vector);
		hashtable.put(new Vector(), vector);

		FloggyHashtable persistable = new FloggyHashtable();
		persistable.setX(hashtable);

		try {
			int id = manager.save(persistable);
			assertTrue(id > 0);

			FloggyHashtable fake = new FloggyHashtable();
			manager.load(fake, id);
			assertEquals(persistable.getX(), fake.getX());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(persistable);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testVectorHoldingHashtable() throws Exception {
		Hashtable hashtable = new Hashtable();
		hashtable.put("String", "temp");

		Vector vector = new Vector();
		vector.add(hashtable);

		FloggyVector persistable = new FloggyVector();
		persistable.setX(vector);

		try {
			int id = manager.save(persistable);
			assertTrue(id > 0);

			FloggyVector fake = new FloggyVector();
			manager.load(fake, id);
			assertEquals(persistable.getX(), fake.getX());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(persistable);
		}
	}
}
