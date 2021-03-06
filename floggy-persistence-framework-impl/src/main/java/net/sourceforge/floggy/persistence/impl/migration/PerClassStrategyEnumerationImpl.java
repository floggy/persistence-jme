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
package net.sourceforge.floggy.persistence.impl.migration;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import java.util.Hashtable;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.Utils;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class PerClassStrategyEnumerationImpl extends AbstractEnumerationImpl {
	/**
	 * Creates a new PerClassStrategyEnumerationImpl object.
	 *
	 * @param rmsBasedMetadata DOCUMENT ME!
	 * @param classBasedMetadata DOCUMENT ME!
	 * @param enumeration DOCUMENT ME!
	 * @param recordStore DOCUMENT ME!
	 * @param lazy DOCUMENT ME!
	 * @param iterationMode DOCUMENT ME!
	 *
	 * @throws IOException DOCUMENT ME!
	 * @throws RecordStoreException DOCUMENT ME!
	 */
	public PerClassStrategyEnumerationImpl(PersistableMetadata rmsBasedMetadata,
		PersistableMetadata classBasedMetadata, RecordEnumeration enumeration,
		RecordStore recordStore, boolean lazy, boolean iterationMode)
		throws IOException, RecordStoreException {
		super(rmsBasedMetadata, classBasedMetadata, enumeration, recordStore, lazy,
			iterationMode);
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public int delete() throws FloggyException {
		if (recordId != -1) {
			try {
				recordStore.deleteRecord(recordId);

				int temp = recordId;
				recordId = -1;

				return temp;
			} catch (RecordStoreException ex) {
				throw Utils.handleException(ex);
			}
		}

		throw new FloggyException(
			"There isn't a register to delete. You have to iterate over the enumeration before call delete.");
	}

	/**
	 * DOCUMENT ME!
	*
	* @param rmsBasedMetadata DOCUMENT ME!
	* @param data DOCUMENT ME!
	* @param hashtable DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	protected void buildPersistable(PersistableMetadata rmsBasedMetadata,
		byte[] data, Hashtable hashtable) throws Exception {
		String[] fieldNames = rmsBasedMetadata.getFieldNames();
		int[] fieldTypes = rmsBasedMetadata.getFieldTypes();

		if (data != null) {
			DataInputStream dis = new DataInputStream(new ByteArrayInputStream(data));

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
}
