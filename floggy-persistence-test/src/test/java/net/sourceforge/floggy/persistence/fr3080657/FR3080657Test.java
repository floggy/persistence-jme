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
package net.sourceforge.floggy.persistence.fr3080657;

import java.util.Vector;

import javax.microedition.rms.RecordStore;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.impl.IndexMetadata;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.PersistableMetadataManager;
import net.sourceforge.floggy.persistence.impl.__Persistable;

public class FR3080657Test extends FloggyBaseTest {

	public void testStoreIndexAfterSaveOperationFalse() throws Exception {
		manager.setProperty(PersistableManager.STORE_INDEX_AFTER_SAVE_OPERATION, Boolean.FALSE);

		FR3080657 fr3080657 =new FR3080657();
		
		fr3080657.setName("São Paulo - 04/10/2010");
		
		__Persistable object = (__Persistable) fr3080657;
		PersistableMetadata metadata = 
			PersistableMetadataManager.getClassBasedMetadata(object.getClass().getName());
		Vector indexMetadatas = metadata.getIndexMetadatas();
		
		IndexMetadata indexMetadata = (IndexMetadata) indexMetadatas.elementAt(0);
		
		RecordStore recordStore = RecordStore.openRecordStore(indexMetadata.getRecordStoreName(), true);
		
		assertEquals(0, recordStore.getNumRecords());
		
		recordStore.closeRecordStore();
		
		manager.save(object);

		recordStore = RecordStore.openRecordStore(indexMetadata.getRecordStoreName(), false);
		
		assertEquals(0, recordStore.getNumRecords());
		
		recordStore.closeRecordStore();
	}

	public void testStoreIndexAfterSaveOperationString() throws Exception {

		try {
			manager.setProperty(PersistableManager.STORE_INDEX_AFTER_SAVE_OPERATION, "true");
			fail("Must throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}
		
	}

	public void testStoreIndexAfterSaveOperationTrue() throws Exception {
		manager.setProperty(PersistableManager.STORE_INDEX_AFTER_SAVE_OPERATION, Boolean.TRUE);

		FR3080657 fr3080657 =new FR3080657();
		
		fr3080657.setName("São Paulo - 04/10/2010");
		
		__Persistable object = (__Persistable) fr3080657;
		PersistableMetadata metadata = 
			PersistableMetadataManager.getClassBasedMetadata(object.getClass().getName());
		Vector indexMetadatas = metadata.getIndexMetadatas();
		IndexMetadata indexMetadata = (IndexMetadata) indexMetadatas.elementAt(0);

		try {
			RecordStore recordStore = RecordStore.openRecordStore(indexMetadata.getRecordStoreName(), true);
			
			assertEquals(0, recordStore.getNumRecords());
			
			recordStore.closeRecordStore();
			
			manager.save(object);

			recordStore = RecordStore.openRecordStore(indexMetadata.getRecordStoreName(), false);
			
			assertEquals(1, recordStore.getNumRecords());
			
			recordStore.closeRecordStore();
		} finally {
			manager.setProperty(PersistableManager.STORE_INDEX_AFTER_SAVE_OPERATION, Boolean.FALSE);
		}

	}

}
