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
package net.sourceforge.floggy.persistence.impl.migration;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.impl.PersistableMetadataManager;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.RecordStoreManager;
import net.sourceforge.floggy.persistence.impl.Utils;

public class SingleStrategyEnumerationImpl extends AbstractEnumerationImpl {

	protected Vector datas;
	protected Vector ids;
	protected int currentIndex = 0;

	public SingleStrategyEnumerationImpl(PersistableMetadata rmsBasedMetadata,
			PersistableMetadata classBasedMetadata,
			RecordEnumeration enumeration, RecordStore recordStore,
			boolean lazy, boolean iterationMode) throws IOException,
			RecordStoreException {

		super(rmsBasedMetadata, classBasedMetadata, enumeration, recordStore,
				lazy, iterationMode);

		datas = new Vector(enumeration.numRecords());
		ids = new Vector(datas.size());

		while (enumeration.hasNextElement()) {
			int id = enumeration.nextRecordId();
			byte[] data = recordStore.getRecord(id);
			String className = Utils.readUTF8(data);

			if (className.equals(rmsBasedMetadata.getClassName())) {
				ids.addElement(new Integer(id));
				datas.addElement(data);
			}
		}
	}

	protected void buildPersistable(PersistableMetadata rmsBasedMetadata, byte[] data, Hashtable hashtable) throws Exception {
		String[] fieldNames = rmsBasedMetadata.getFieldNames();
		int[] fieldTypes = rmsBasedMetadata.getFieldTypes();

		if (data != null) {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
					data));

			dis.readUTF();
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

	void finish() throws FloggyException {
		if (!enumeration.hasNextElement()) {
			enumeration.destroy();
			RecordStoreManager.closeRecordStore(recordStore);
			if (rmsBasedMetadata != classBasedMetadata) {
				try {
					classBasedMetadata.setRecordId(rmsBasedMetadata
							.getRecordId());
					PersistableMetadataManager.saveRMSStructure(classBasedMetadata);
				} catch (Exception ex) {
					throw Utils.handleException(ex);
				}
			}
		} else {
			throw new FloggyException(
					"The enumeration must be whole processed before finish the process!");
		}
	}

	public int getSize() {
		return datas.size();
	}

	public boolean hasMoreElements() {
		return currentIndex < getSize();
	}

	public Hashtable nextElement() throws FloggyException {
		if (recordId != -1 && !iterationMode) {
			throw new FloggyException(
					"You should delete or update the current register before step into the next one.");
		}

		Hashtable hashtable = new HashtableValueNullable();
		try {
			byte[] data = (byte[]) datas.elementAt(currentIndex);
			recordId = ((Integer) ids.elementAt(currentIndex)).intValue();

			buildPersistable(rmsBasedMetadata, data, hashtable);

			currentIndex++;

		} catch (Exception ex) {
			throw Utils.handleException(ex);
		}
		return hashtable;
	}

}
