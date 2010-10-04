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

package net.sourceforge.floggy.persistence.impl;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;

import net.sourceforge.floggy.persistence.FloggyException;

public class IndexManager {

	private static Hashtable indexes = new Hashtable();
	private static boolean storeIndexAfterSave = false;
	
	public static void setStoreIndexAfterSave(boolean storeIndexAfterSave) {
		IndexManager.storeIndexAfterSave = storeIndexAfterSave;
	}
	
	public static boolean getStoreIndexAfterSave() {
		return IndexManager.storeIndexAfterSave;
	}

	/**
	 * After object delete the cache of the index could be updated.
	 * 
	 * @param persistable
	 * @throws FloggyException
	 */
	public static void afterDelete(__Persistable persistable) throws Exception {
		PersistableMetadata metadata = PersistableMetadataManager
				.getClassBasedMetadata(persistable.getClass().getName());
		Vector indexMetadatas = metadata.getIndexMetadatas();

		if (indexMetadatas != null) {
			int size = indexMetadatas.size();

			for (int i = 0; i < size; i++) {
				IndexMetadata indexMetadata = (IndexMetadata) indexMetadatas.elementAt(i);
				afterDelete(persistable, indexMetadata);
			}
		}
	}

	/**
	 * After object delete the cache of the index could be updated.
	 * 
	 * @param persistable
	 * @throws FloggyException
	 */
	public static void afterDelete(__Persistable persistable,
			IndexMetadata indexMetadata) throws Exception {

		if (persistable.__getId() > 0) {
			//TODO use indexes.get() instead of contains
			if (indexes.containsKey(indexMetadata.getId())) {
				Object value = persistable.__getIndexValue(indexMetadata.getName());
				if (value != null) {
					Index index = (Index) indexes.get(indexMetadata.getId());
					IndexEntry indexEntry = index.getIndexEntry(value);
					if (indexEntry != null) {
						index.remove(persistable.__getId());
					}
				}
			}
		} else {
			throw new IllegalArgumentException(
					"The persistable object does not have a reference to the RMS system.");
		}
	}
	
	/**
	 * After object save the cache of the index could be updated.
	 * 
	 * @param persistable
	 * @throws FloggyException
	 */
	public static void afterSave(__Persistable persistable) throws Exception {
		PersistableMetadata metadata = PersistableMetadataManager
				.getClassBasedMetadata(persistable.getClass().getName());
		Vector indexMetadatas = metadata.getIndexMetadatas();

		if (indexMetadatas != null) {
			int size = indexMetadatas.size();

			for (int i = 0; i < size; i++) {
				IndexMetadata indexMetadata = (IndexMetadata) indexMetadatas.elementAt(i);
				afterSave(persistable, indexMetadata);
			}
		}
	}
	
	/**
	 * After object delete the cache and recordStore of the index must be
	 * updated
	 * 
	 * @param persistable
	 * @throws FloggyException
	 */
	public static void afterSave(__Persistable persistable, IndexMetadata indexMetadata)
			throws Exception {

		if (persistable.__getId() > 0) {
			Index index = null;

			Object value = persistable.__getIndexValue(indexMetadata.getName());
			String indexId = indexMetadata.getId();

			if (indexes.containsKey(indexId)) {
				index = (Index) indexes.get(indexId);
			} else {
				index = new Index();
				indexes.put(indexId, index);
			}

			if (value == null) {
				// Valor anterior poderia ser diferente de nulo.
				IndexEntry indexEntry = index.getIndexEntry(persistable.__getId());
				if (indexEntry != null) {
					index.remove(persistable.__getId());
				}
			} else {
				index.put(persistable.__getId(), value);
			}

			if (storeIndexAfterSave) {
				save(indexId, index);
			}
		} else {
			throw new IllegalArgumentException(
					"The persistable object does not have a reference to the RMS system.");
		}
	}

	public static void deleteIndex(String persistableClassName) throws Exception {
		PersistableMetadata metadata = PersistableMetadataManager
				.getClassBasedMetadata(persistableClassName);
		Vector indexMetadatas = metadata.getIndexMetadatas();

		if (indexMetadatas != null) {
			int size = indexMetadatas.size();

			for (int i = 0; i < size; i++) {
				IndexMetadata indexMetadata = (IndexMetadata) indexMetadatas.elementAt(i);
				
				String id = indexMetadata.getId();
				if (indexes.containsKey(id)) {
					Index index = (Index) indexes.get(id);
					index.clear();
				}
				RecordStoreManager.deleteRecordStore(id);
			}
		}
	}

	public static int[] getId(Class persistableClass, String indexName,
			Object indexValue) throws Exception {

		IndexMetadata indexMetadata = getIndexMetadata(persistableClass, indexName);
		Index index = (Index) indexes.get(indexMetadata.getId());
		return index.getIds(indexValue);
	}

	public static IndexMetadata getIndexMetadata(Class persistableClass,
			String indexName) throws FloggyException {
		PersistableMetadata metadata = PersistableMetadataManager
				.getClassBasedMetadata(persistableClass.getName());
		Vector indexMetadatas = metadata.getIndexMetadatas();
		int size = indexMetadatas.size();
		IndexMetadata indexMetadata = null;

		for (int i = 0; i < size; i++) {
			indexMetadata = (IndexMetadata) indexMetadatas.elementAt(i);
			if (indexMetadata.getName().equals(indexName)) {
				return indexMetadata;
			}
		}
		throw new FloggyException("The " + indexName + " index does not exist to " + persistableClass.getName() + " persistable.");
	}

	public static void init() throws Exception {
		Enumeration metadatas = PersistableMetadataManager.getClassBasedMetadatas();

		while (metadatas.hasMoreElements()) {
			PersistableMetadata metadata = (PersistableMetadata) metadatas.nextElement();
			Vector indexMetadatas = metadata.getIndexMetadatas();

			if (indexMetadatas != null) {
				int indexMetadatasSize = indexMetadatas.size();

				for (int j = 0; j < indexMetadatasSize; j++) {
					IndexMetadata indexMetadata = (IndexMetadata) indexMetadatas.elementAt(j);
					
					loadIndex(metadata, indexMetadata);
				}
			}
		}
	}

	public static void loadIndex(PersistableMetadata metadata,
			IndexMetadata indexMetadata) throws Exception {
		if (indexes.containsKey(indexMetadata.getId())) {
			return;
		}

		RecordStore rs = RecordStoreManager.getRecordStore(indexMetadata,
				metadata);
		Index index = new Index();
		
		RecordEnumeration enumeration = rs.enumerateRecords(null, null, true);
		while (enumeration.hasNextElement()) {
			int id = enumeration.nextRecordId();
			byte[] buffer = rs.getRecord(id);
			IndexEntry indexEntry = new IndexEntry(id);
			indexEntry.deserialize(buffer);
			index.put(indexEntry);
		}

		indexes.put(indexMetadata.getId(), index);
		RecordStoreManager.closeRecordStore(rs);
	}

	public static void reset() {
		Enumeration elements = indexes.elements();
		while (elements.hasMoreElements()) {
			Index index = (Index) elements.nextElement();
			index.idValue.clear();
			index.valueIds.clear();
		}
		indexes.clear();
	}
	
	private static void save(String indexId, Index index) throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		RecordStore rs = RecordStore.openRecordStore(indexId, true);
		Enumeration indexValueEnumeration = index.valueIds.elements();
		
		while (indexValueEnumeration.hasMoreElements()) {
			IndexEntry indexEntry = (IndexEntry) indexValueEnumeration.nextElement();
			indexEntry.serialize(fos);
			int id = indexEntry.getRecordId();
			byte[] data = fos.toByteArray();

			if (id != -1) {
				rs.setRecord(id, data, 0, data.length);
			} else {
				id = rs.addRecord(data, 0, data.length);
				indexEntry.setRecordId(id);
			}
			fos.reset();
		}
		rs.closeRecordStore();
	}
	
	public static void shutdown() throws Exception {
		Enumeration indexIds = indexes.keys();
		while(indexIds.hasMoreElements()) {
			String indexId = (String) indexIds.nextElement();
			Index index = (Index) indexes.get(indexId);
			
			save(indexId, index);
		}
		
	}

	private IndexManager() {
	}
}
