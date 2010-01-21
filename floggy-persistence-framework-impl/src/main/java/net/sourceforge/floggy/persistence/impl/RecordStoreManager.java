package net.sourceforge.floggy.persistence.impl;

import java.util.Enumeration;
import java.util.Hashtable;

import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

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
	
		PersistableMetadata rmsMetadata = MetadataManagerUtil.getRMSBasedMetadata(metadata.getClassName());
	
		if (rmsMetadata == null) {
			if (!MetadataManagerUtil.getBytecodeVersion().equals(MetadataManagerUtil.getRMSVersion()) && !isUpdateProcess) {
				throw new FloggyException("You are trying to access a Persistable (" + metadata.getClassName() + ") entity that was not migrate. Please execute a migration first.");
			}
			MetadataManagerUtil.saveRMSStructure(metadata);
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

	public static RecordStore getRecordStore(__Persistable persistable)
			throws FloggyException {
	
		PersistableMetadata metadata = MetadataManagerUtil.getClassBasedMetadata(persistable.getClass().getName());
		return getRecordStore(persistable.getRecordStoreName(), metadata, false);
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
				rsr.recordStore = RecordStore.openRecordStore(recordStoreName, true);
				references.put(recordStoreName, rsr);
			} else {
				if (metadata.getPersistableStrategy() == PersistableMetadata.SINGLE_STRATEGY) {
					check(metadata, isUpdateProcess);
				}
	
				if (rsr.references == 0) {
					rsr.recordStore = RecordStore.openRecordStore(recordStoreName, true);
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

}
