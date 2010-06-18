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

package net.sourceforge.floggy.persistence.fr3010799;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.beans.animals.Bird;
import net.sourceforge.floggy.persistence.impl.__Persistable;

public class FR3010799Test extends FloggyBaseTest {

	public void testUseTheRegularSaveMethod() throws Exception {
		Bird bird = new Bird();

		manager.save(bird);

		try {
			manager.batchSave(bird);

			fail("It must throw a FloggyException");
		} catch (Exception ex) {
			assertEquals(FloggyException.class, ex.getClass());
		} finally {
			manager.delete(bird);
		}

	}

	public void testBatchSaveMethod() throws Exception {
		Bird bird = new Bird();

		int id = 0;
		
		try {
			assertFalse(manager.isPersisted(bird));

			id = manager.batchSave(bird);
			
			assertTrue(id > 0);

			assertFalse(manager.isPersisted(bird));
		} catch (Exception ex) {
			fail(ex.getMessage());
		} finally {
			((__Persistable) bird).__setId(id);
			manager.delete(bird);
		}
	}
}
