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

package net.sourceforge.floggy.persistence.fr2857247;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreNotOpenException;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.impl.MetadataManagerUtil;
import net.sourceforge.floggy.persistence.impl.PersistableManagerImpl;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.__Persistable;

public class FR2857247Test extends FloggyBaseTest {

	public void testBatchModeTrue() throws Exception {
		manager.setProperty(PersistableManager.BATCH_MODE, Boolean.TRUE);

		__Persistable object = (__Persistable) new Person();
		PersistableMetadata metadata = 
			MetadataManagerUtil.getClassBasedMetadata(object.getClass().getName());
		RecordStore rs = 
			PersistableManagerImpl.getRecordStore(object.getRecordStoreName(), metadata);

		PersistableManagerImpl.closeRecordStore(rs);

		try {
			assertEquals(0, rs.getNumRecords());
			rs.closeRecordStore();
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
		
	}

	public void testBatchModeTrueShutdown() throws Exception {
		manager.setProperty(PersistableManager.BATCH_MODE, Boolean.TRUE);

		__Persistable object = (__Persistable) new Person();
		PersistableMetadata metadata = 
			MetadataManagerUtil.getClassBasedMetadata(object.getClass().getName());
		RecordStore rs = 
			PersistableManagerImpl.getRecordStore(object.getRecordStoreName(), metadata);
		
		PersistableManagerImpl.closeRecordStore(rs);

		manager.shutdown();

		try {
			assertEquals(0, rs.getNumRecords());
			fail("Must throw a RecordStoreNotOpenException");
		} catch (Exception ex) {
			assertEquals(RecordStoreNotOpenException.class, ex.getClass());
		}
	}

	public void testBatchModeFalse() throws Exception {
		manager.setProperty(PersistableManager.BATCH_MODE, Boolean.FALSE);

		__Persistable object = (__Persistable) new Person();
		PersistableMetadata metadata = 
			MetadataManagerUtil.getClassBasedMetadata(object.getClass().getName());
		RecordStore rs = 
			PersistableManagerImpl.getRecordStore(object.getRecordStoreName(), metadata);

		PersistableManagerImpl.closeRecordStore(rs);

		try {
			rs.getNumRecords();
			fail("Must throw a RecordStoreNotOpenException");
		} catch (Exception ex) {
			assertEquals(RecordStoreNotOpenException.class, ex.getClass());
		}
	}

}
