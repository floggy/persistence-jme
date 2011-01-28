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
package net.sourceforge.floggy.persistence.fr3081023;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.PersistableManager;

public class FR3081023Test extends FloggyBaseTest {

	public void testGetPropertyBatchMode() throws Exception {
		Boolean oldBatchMode = (Boolean) manager.getProperty(PersistableManager.BATCH_MODE);
		Boolean batchMode = oldBatchMode;
		
		assertEquals(Boolean.FALSE, batchMode);
		
		manager.setProperty(PersistableManager.BATCH_MODE, Boolean.TRUE);
		
		batchMode = (Boolean) manager.getProperty(PersistableManager.BATCH_MODE);
		
		assertEquals(Boolean.TRUE, batchMode);
		
		manager.setProperty(PersistableManager.BATCH_MODE, oldBatchMode);
	}

	public void testGetPropertyEmpty() throws Exception {
		try {
			manager.getProperty("");
			fail("Must throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}
		
	}

	public void testGetPropertyNull() throws Exception {
		try {
			manager.getProperty(null);
			fail("Must throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}
		
	}

	public void testGetPropertyStoreIndexAfterSaveOperation() throws Exception {
		Boolean oldStoreIndexAfterSaveOperation = (Boolean) manager.getProperty(PersistableManager.STORE_INDEX_AFTER_SAVE_OPERATION);
		Boolean storeIndexAfterSaveOperation = oldStoreIndexAfterSaveOperation;
		
		assertEquals(Boolean.FALSE, storeIndexAfterSaveOperation);
		
		manager.setProperty(PersistableManager.STORE_INDEX_AFTER_SAVE_OPERATION, Boolean.TRUE);
		
		storeIndexAfterSaveOperation = (Boolean) manager.getProperty(PersistableManager.STORE_INDEX_AFTER_SAVE_OPERATION);
		
		assertEquals(Boolean.TRUE, storeIndexAfterSaveOperation);
		
		manager.setProperty(PersistableManager.STORE_INDEX_AFTER_SAVE_OPERATION, oldStoreIndexAfterSaveOperation);
	}

	public void testGetPropertyUnreconizedPropertyName() throws Exception {
		try {
			manager.getProperty("batch-mode");
			fail("Must throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}
		
	}

}
