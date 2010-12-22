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

package net.sourceforge.floggy.persistence.impl.migration;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.impl.PersistableMetadataManager;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.RecordStoreManager;
import net.sourceforge.floggy.persistence.impl.Utils;
import net.sourceforge.floggy.persistence.impl.__Persistable;

public class JoinedStrategyEnumerationImpl extends AbstractEnumerationImpl {

	protected Hashtable superClassesIDs = new Hashtable();

	public JoinedStrategyEnumerationImpl(PersistableMetadata rmsBasedMetadata,
		PersistableMetadata classBasedMetadata,
		RecordEnumeration enumeration, RecordStore recordStore,
		boolean lazy, boolean iterationMode) throws IOException, RecordStoreException {

		super(rmsBasedMetadata, classBasedMetadata, enumeration, recordStore, lazy,
			iterationMode);
	}

	protected void buildPersistable(PersistableMetadata rmsBasedMetadata, byte[] data, Hashtable hashtable) throws Exception {
		String[] fieldNames = rmsBasedMetadata.getFieldNames();
		int[] fieldTypes = rmsBasedMetadata.getFieldTypes();

		if (data != null) {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
					data));
			
			if (PersistableMetadataManager.getRMSVersion().equals(PersistableMetadataManager.VERSION_1_4_0)) {
				dis.skipBytes(4);
			}
			
			String superClassName = rmsBasedMetadata.getSuperClassName();
			if (superClassName != null) {
				int id = dis.readInt();
				PersistableMetadata superMetadata = PersistableMetadataManager.getRMSBasedMetadata(superClassName);
				if (superMetadata == null) {
					superMetadata = PersistableMetadataManager.getClassBasedMetadata(superClassName);
				}
				RecordStore store = RecordStoreManager.getRecordStore(superMetadata.getRecordStoreName(), superMetadata);
				byte[] superData = store.getRecord(id);
				buildPersistable(superMetadata, superData, hashtable);
				superClassesIDs.put(superClassName, new Integer(id));
				RecordStoreManager.closeRecordStore(store);
			}

			for (int i = 0; i < fieldNames.length; i++) {
				int type = fieldTypes[i];
				if (lazy) {
					if (((type & PersistableMetadata.PERSISTABLE) != PersistableMetadata.PERSISTABLE)) {
						Object object;
						if ((type & PersistableMetadata.ARRAY) == PersistableMetadata.ARRAY) {
							type = type & ~PersistableMetadata.ARRAY;
							object = readArray(type, fieldNames[i], dis);
						} else if ((type & PersistableMetadata.PRIMITIVE) == PersistableMetadata.PRIMITIVE) {
							type = type & ~PersistableMetadata.PRIMITIVE;
							object = readPrimitive(type, dis);
						} else {
							object = readObject(type, fieldNames[i], dis);
						}
						hashtable.put(fieldNames[i], object);
					}
				} else {
					Object object;
					if ((type & PersistableMetadata.ARRAY) == PersistableMetadata.ARRAY) {
						type = type & ~PersistableMetadata.ARRAY;
						object = readArray(type, fieldNames[i], dis);
					} else if ((type & PersistableMetadata.PRIMITIVE) == PersistableMetadata.PRIMITIVE) {
						type = type & ~PersistableMetadata.PRIMITIVE;
						object = readPrimitive(type, dis);
					} else {
						object = readObject(type, fieldNames[i], dis);
					}
					hashtable.put(fieldNames[i], object);
				}
			}
		}
	}

	public int delete() throws FloggyException {
		if (recordId != -1) {
			try {
				deleteSuperClassesRegisters();
				recordStore.deleteRecord(recordId);
				int temp = recordId;
				recordId = -1;
				return temp;
			} catch (RecordStoreException ex) {
				throw Utils.handleException(ex);
			}
		}
		throw new FloggyException("There isn't a register to delete. You have to iterate over the enumeration before call delete.");
	}

	protected void deleteSuperClassesRegisters() throws FloggyException, RecordStoreException  {
		if (!superClassesIDs.isEmpty()) {
			java.util.Enumeration keys = superClassesIDs.keys();
			while (keys.hasMoreElements()) {
				String className = (String) keys.nextElement();
				PersistableMetadata metadata = PersistableMetadataManager.getRMSBasedMetadata(className);
				if (metadata != null) {
					RecordStore superRecordStore = RecordStoreManager.getRecordStore(metadata.getRecordStoreName(), metadata);
					superRecordStore.deleteRecord(((Integer)superClassesIDs.get(className)).intValue());
					RecordStoreManager.closeRecordStore(superRecordStore);
				}
			}
		}
	}

	public Hashtable nextElement() throws FloggyException {
		if (recordId != -1 && !iterationMode) {
			throw new FloggyException("You should delete or update the current register before step into the next one."); 
		}
		superClassesIDs.clear();
		Hashtable hashtable = new HashtableValueNullable();
		try {
			recordId = enumeration.nextRecordId();
			byte[] data = recordStore.getRecord(recordId);
			buildPersistable(rmsBasedMetadata, data, hashtable);
		} catch (Exception ex) {
			ex.printStackTrace();
			throw Utils.handleException(ex);
		}
		return hashtable;
	}

	public int update(Persistable persistable) throws FloggyException {
		if (recordId != -1) {
			__Persistable __persistable = Utils.checkArgumentAndCast(persistable);
			try {
				__persistable.__setId(recordId);
				deleteSuperClassesRegisters();
				int temp = manager.save(__persistable);
				recordId = -1;
				return temp;
			} catch (RecordStoreException ex) {
				throw Utils.handleException(ex);
			}
		}
		throw new FloggyException("There isn't a register to update. You have to iterate over the enumeration before call update.");
	}
}
