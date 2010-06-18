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

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotFoundException;

import net.sourceforge.floggy.persistence.FloggyException;

public class RecordStoreManager {

	private static class RecordStoreReference {
		RecordStore recordStore;
		int references = 0;
	}

	private static Hashtable references = new Hashtable();
	private static boolean batchMode = false;
	
	public static void setBatchMode(boolean batchMode) {
		RecordStoreManager.batchMode = batchMode;
	}

	private static void check(PersistableMetadata metadata, boolean isUpdateProcess) throws Exception {
	
		PersistableMetadata rmsMetadata = PersistableMetadataManager.getRMSBasedMetadata(metadata.getClassName());
	
		if (rmsMetadata == null) {
			if (!PersistableMetadataManager.getBytecodeVersion().equals(PersistableMetadataManager.getRMSVersion()) && !isUpdateProcess) {
				throw new FloggyException("You are trying to access a Persistable (" + metadata.getClassName() + ") entity that was not migrate. Please execute a migration first.");
			}
			PersistableMetadataManager.saveRMSStructure(metadata);
		} else {
			if (!metadata.equals(rmsMetadata) && !isUpdateProcess) {
				throw new FloggyException("Class and RMS description doesn't match for class " + metadata.getClassName() + ". Please execute a migration first.");
			}
		}
	}

	public static void closeRecordStore(RecordStore rs) throws FloggyException {
		try {
			if (rs != null) {
				RecordStoreReference rsr = (RecordStoreReference) references
						.get(rs.getName());
				if (rsr != null) {
					rsr.references--;
					if (rsr.references == 0) {
						if (!batchMode) {
							rs.closeRecordStore();
							rsr.recordStore = null;
						}
					}
				}
			}
		} catch (RecordStoreException ex) {
			throw Utils.handleException(ex);
		}
	}
	
	public static void init() {
		
	}

	public static void deleteRecordStore(String recordStoreName) throws Exception{
		try {
			RecordStore.deleteRecordStore(recordStoreName);
		} catch (RecordStoreNotFoundException e) {
		}
	}

	public static RecordStore getRecordStore(__Persistable persistable)
			throws FloggyException {
	
		PersistableMetadata metadata = PersistableMetadataManager.getClassBasedMetadata(persistable.getClass().getName());
		return getRecordStore(persistable.getRecordStoreName(), metadata, false);
	}
	
	public static RecordStore getRecordStore(IndexMetadata indexMetadata,
			PersistableMetadata metadata) throws Exception {
		return getRecordStore(indexMetadata.getRecordStoreName(), metadata, false);
	}

	public static RecordStore getRecordStore(String recordStoreName,
			PersistableMetadata metadata) throws FloggyException {
		return getRecordStore(recordStoreName, metadata, false);
	}
	
	public static RecordStore getRecordStore(String recordStoreName,
			PersistableMetadata metadata, boolean isUpdateProcess) throws FloggyException {
		try {
			RecordStoreReference rsr = (RecordStoreReference) references.get(recordStoreName);
			if (rsr == null) {
				check(metadata, isUpdateProcess);
	
				rsr = new RecordStoreReference();

				String suiteName = metadata.getSuiteName();
				String vendorName = metadata.getVendorName();
				
				if (suiteName != null && vendorName != null) {
					rsr.recordStore = RecordStore.openRecordStore(recordStoreName, vendorName, suiteName);
				} else {
					rsr.recordStore = RecordStore.openRecordStore(recordStoreName, true);
				}

				references.put(recordStoreName, rsr);
			} else {
				if (metadata.getPersistableStrategy() == PersistableMetadata.SINGLE_STRATEGY) {
					check(metadata, isUpdateProcess);
				}
	
				if (rsr.references == 0) {
					String suiteName = metadata.getSuiteName();
					String vendorName = metadata.getVendorName();
					
					if (suiteName != null && vendorName != null) {
						rsr.recordStore = RecordStore.openRecordStore(recordStoreName, vendorName, suiteName);
					} else {
						rsr.recordStore = RecordStore.openRecordStore(recordStoreName, true);
					}
				}
			}
			rsr.references++;
			return rsr.recordStore;
		} catch (Exception ex) {
			throw Utils.handleException(ex);
		}
	}

	public static void reset() {
		references.clear();
	}

	public static void shutdown() throws FloggyException {
		Enumeration recordStoreReferences = references.elements();
		while (recordStoreReferences.hasMoreElements()) {
			RecordStoreReference rsr = (RecordStoreReference)recordStoreReferences.nextElement(); 
			RecordStore rs = rsr.recordStore;
			if (rs != null) {
				try {
					rs.closeRecordStore();
					rsr.recordStore = null;
				} catch (Exception ex) {
					throw Utils.handleException(ex);
				}
			}
		}
	}
	
	protected RecordStoreManager() {
	}

}
