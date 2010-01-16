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

import java.util.Hashtable;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import net.sourceforge.floggy.persistence.Comparator;
import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;

/**
* This is the main class of the framework. All persistence operations
* methods (such as loading, saving, deleting and searching for objects) are
* declared in this class.
*
* @since 1.0
 */
public class PersistableManagerImpl extends PersistableManager {
	private static Class __persistableClass;
	private static Class deletableClass;
	private static Hashtable references = new Hashtable();

	static {
		try {
			__persistableClass = Class.forName(
					"net.sourceforge.floggy.persistence.impl.__Persistable");
			deletableClass = Class.forName(
					"net.sourceforge.floggy.persistence.Deletable");
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}
	}

/**
   * Creates a new instance of PersistableManager.
   */
	public PersistableManagerImpl() throws Exception {
		SerializationHelper.setPersistableManager(this);
		MetadataManagerUtil.init();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param persistable DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public static __Persistable checkArgumentAndCast(Persistable persistable) {
		if (persistable == null) {
			throw new IllegalArgumentException(
				"The persistable object cannot be null!");
		}

		if (persistable instanceof __Persistable) {
			return (__Persistable) persistable;
		} else {
			throw new IllegalArgumentException(persistable.getClass().getName()
				+ " is not a valid persistable class. Check the weaver execution!");
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param rs DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public static void closeRecordStore(RecordStore rs) throws FloggyException {
		try {
			if (rs != null) {
				RecordStoreReference rsr =
					(RecordStoreReference) references.get(rs.getName());

				if (rsr != null) {
					rsr.references--;

					if (rsr.references == 0) {
						rs.closeRecordStore();
						rsr.recordStore = null;
					}
				}
			}
		} catch (RecordStoreException ex) {
			throw handleException(ex);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param persistableClass DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public static __Persistable createInstance(Class persistableClass)
		throws FloggyException {
		validatePersistableClassArgument(persistableClass);

		try {
			return (__Persistable) persistableClass.newInstance();
		} catch (Exception ex) {
			throw handleException(ex);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param recordStoreName DOCUMENT ME!
	* @param metadata DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public static RecordStore getRecordStore(String recordStoreName,
		PersistableMetadata metadata) throws FloggyException {
		return getRecordStore(recordStoreName, metadata, false);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param recordStoreName DOCUMENT ME!
	* @param metadata DOCUMENT ME!
	* @param isUpdateProcess DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public static RecordStore getRecordStore(String recordStoreName,
		PersistableMetadata metadata, boolean isUpdateProcess)
		throws FloggyException {
		try {
			RecordStoreReference rsr =
				(RecordStoreReference) references.get(recordStoreName);

			if (rsr == null) {
				rsr = new RecordStoreReference();
				rsr.recordStore = RecordStore.openRecordStore(recordStoreName, true);

				PersistableMetadata rmsMetadata =
					MetadataManagerUtil.getRMSBasedMetadata(metadata.getClassName());

				if (rmsMetadata == null) {
					if (!MetadataManagerUtil.getBytecodeVersion()
						 .equals(MetadataManagerUtil.getRMSVersion()) && !isUpdateProcess) {
						throw new FloggyException(
							"You are trying to access a Persistable ("
							+ metadata.getClassName()
							+ ") entity that was not migrate. Please execute a migration first.");
					}

					MetadataManagerUtil.saveRMSStructure(metadata);
				} else {
					if (!metadata.equals(rmsMetadata) && !isUpdateProcess) {
						throw new FloggyException(
							"Class and RMS description doesn't match for class "
							+ metadata.getClassName() + ". Please execute a migration first.");
					}
				}

				references.put(recordStoreName, rsr);
			} else {
				if (rsr.references == 0) {
					rsr.recordStore = RecordStore.openRecordStore(recordStoreName, true);
				}
			}

			rsr.references++;

			return rsr.recordStore;
		} catch (Exception ex) {
			throw handleException(ex);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public static void reset() {
		references.clear();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param persistableClass DOCUMENT ME!
	*
	* @throws IllegalArgumentException DOCUMENT ME!
	*/
	public static void validatePersistableClassArgument(Class persistableClass)
		throws IllegalArgumentException {
		if (persistableClass == null) {
			throw new IllegalArgumentException(
				"The persistable class cannot be null!");
		}

		if (!__persistableClass.isAssignableFrom(persistableClass)) {
			throw new IllegalArgumentException(persistableClass.getName()
				+ " is not a valid persistable class. Check the weaver execution!");
		}
	}

	/**
	* Removes an object from the repository. If the object is not stored in
	* the repository then a <code>FloggyException</code> will be thrown.
	*
	* @param persistable Object to be removed.
	*
	* @throws FloggyException Exception thrown if an error occurs while removing
	* 				the object.
	*/
	public void delete(Persistable persistable) throws FloggyException {
		__Persistable __persistable = checkArgumentAndCast(persistable);
		int id = __persistable.__getId();

		if (id > 0) {
			RecordStore rs = PersistableManagerImpl.getRecordStore(__persistable);

			try {
				__persistable.__delete();
				rs.deleteRecord(id);
				__persistable.__setId(0);
			} catch (RecordStoreException ex) {
				throw handleException(ex);
			} finally {
				PersistableManagerImpl.closeRecordStore(rs);
			}
		}
	}

	/**
	* Removes all objects from the repository.
	*
	* @throws FloggyException Exception thrown if an error occurs while removing
	* 				the object.
	*/
	public void deleteAll() throws FloggyException {
		try {
			String[] recordStoreNames = RecordStore.listRecordStores();

			for (int i = 0; i < recordStoreNames.length; i++) {
				if (recordStoreNames[i].equals("FloggyProperties")) {
					continue;
				}

				RecordStore.deleteRecordStore(recordStoreNames[i]);
			}
		} catch (Exception ex) {
			throw handleException(ex);
		}
	}

	/**
	* Removes all objects from the repository.
	*
	* @param persistableClass The persistable class to search the objects.
	*
	* @throws FloggyException Exception thrown if an error occurs while removing
	* 				the object.
	*/
	public void deleteAll(Class persistableClass) throws FloggyException {
		__Persistable persistable = createInstance(persistableClass);
		closeRecordStore(getRecordStore(persistable));

		PersistableMetadata metadata =
			MetadataManagerUtil.getClassBasedMetadata(persistableClass.getName());

		if (deletableClass.isAssignableFrom(persistableClass)
			 || (metadata.getSuperClassName() != null)) {
			ObjectSet os = find(persistableClass, null, null);

			for (int i = 0; i < os.size(); i++) {
				os.get(i, persistable);
				delete(persistable);
			}
		} else {
			try {
				RecordStore.deleteRecordStore(persistable.getRecordStoreName());
			} catch (Exception ex) {
				throw handleException(ex);
			}
		}
	}

	/**
	* Searches objects of an especific persistable class from the
	* repository. <br>
	* <br>
	* An optional application-defined search criteria can be defined using a <code>Filter</code>.<br>
	* <br>
	* An optional application-defined sort order can be defined using a
	* <code>Comparator</code>.
	*
	* @param persistableClass The persistable class to search the objects.
	* @param filter An optional application-defined criteria for searching
	* 			 objects.
	* @param comparator An optional application-defined criteria for sorting
	* 			 objects.
	*
	* @return List of objects that matches the defined criteria.
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public ObjectSet find(Class persistableClass, Filter filter,
		Comparator comparator) throws FloggyException {
		return find(persistableClass, filter, comparator, false);
	}

	/**
	* Searches objects of an especific persistable class from the
	* repository. <br>
	* <br>
	* An optional application-defined search criteria can be defined using a <code>Filter</code>.<br>
	* <br>
	* An optional application-defined sort order can be defined using a
	* <code>Comparator</code>.
	*
	* @param persistableClass The persistable class to search the objects.
	* @param filter An optional application-defined criteria for searching
	* 			 objects.
	* @param comparator An optional application-defined criteria for sorting
	* 			 objects.
	* @param lazy DOCUMENT ME!
	*
	* @return List of objects that matches the defined criteria.
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public ObjectSet find(Class persistableClass, Filter filter,
		Comparator comparator, boolean lazy) throws FloggyException {
		ObjectFilter objectFilter = null;
		ObjectComparator objectComparator = null;

		/*
		 * this is a auxiliary object used to return the name of the
		 * RecordStore. If the argument filter is not null this object is passed
		 * to the ObjectFilter constructor
		 */
		__Persistable persistable = createInstance(persistableClass);

		if (filter != null) {
			objectFilter = new ObjectFilter(persistable, filter, lazy);
		}

		if (comparator != null) {
			objectComparator = new ObjectComparator(comparator,
					createInstance(persistableClass), createInstance(persistableClass),
					lazy);
		}

		int[] ids = null;

		RecordStore rs = PersistableManagerImpl.getRecordStore(persistable);

		try {
			RecordEnumeration en =
				rs.enumerateRecords(objectFilter, objectComparator, false);
			int numRecords = en.numRecords();

			if (numRecords > 0) {
				ids = new int[numRecords];

				for (int i = 0; i < numRecords; i++) {
					ids[i] = en.nextRecordId();
				}
			}

			en.destroy();
		} catch (RecordStoreException ex) {
			throw handleException(ex);
		} finally {
			PersistableManagerImpl.closeRecordStore(rs);
		}

		return new ObjectSetImpl(ids, persistableClass, this, lazy);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param persistable DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getId(Persistable persistable) {
		__Persistable __persistable = checkArgumentAndCast(persistable);

		return __persistable.__getId();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param ex DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public static FloggyException handleException(Exception ex) {
		if (ex instanceof FloggyException) {
			return (FloggyException) ex;
		}

		String message = ex.getMessage();

		if (message == null) {
			message = ex.getClass().getName();
		}

		return new FloggyException(message, ex);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param persistable DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean isPersisted(Persistable persistable) {
		__Persistable __persistable = checkArgumentAndCast(persistable);

		return __persistable.__getId() > 0;
	}

	/**
	* Load an previously stored object from the repository using the object ID.<br>
	* The object ID is the result of a save operation or you can obtain it
	* executing a search.
	*
	* @param persistable An instance where the object data will be loaded into.
	* 			 Cannot be <code>null</code>.
	* @param id The ID of the object to be loaded from the repository.
	*
	* @throws FloggyException Exception thrown if an error occurs while loading
	* 				the object.
	*
	* @see #save(Persistable)
	*/
	public void load(Persistable persistable, int id) throws FloggyException {
		load(persistable, id, false);
	}

	/**
	* Load an previously stored object from the repository using the object ID.<br>
	* The object ID is the result of a save operation or you can obtain it
	* executing a search.
	*
	* @param persistable An instance where the object data will be loaded into.
	* 			 Cannot be <code>null</code>.
	* @param id The ID of the object to be loaded from the repository.
	* @param lazy DOCUMENT ME!
	*
	* @throws FloggyException Exception thrown if an error occurs while loading
	* 				the object.
	*
	* @see #save(Persistable)
	*/
	public void load(Persistable persistable, int id, boolean lazy)
		throws FloggyException {
		__Persistable __persistable = checkArgumentAndCast(persistable);

		RecordStore rs = PersistableManagerImpl.getRecordStore(__persistable);

		try {
			byte[] buffer = rs.getRecord(id);

			if (buffer != null) {
				__persistable.__deserialize(buffer, lazy);
			}

			__persistable.__setId(id);
		} catch (Exception ex) {
			throw handleException(ex);
		} finally {
			PersistableManagerImpl.closeRecordStore(rs);
		}
	}

	/**
	* Store an object in the repository. If the object is already in the
	* repository, the object data will be overwritten.<br>
	* The object ID obtained from this operation can be used in the load
	* operations.
	*
	* @param persistable Object to be stored.
	*
	* @return The ID of the object.
	*
	* @throws FloggyException Exception thrown if an error occurs while storing
	* 				the object.
	*
	* @see #load(Persistable, int)
	*/
	public int save(Persistable persistable) throws FloggyException {
		__Persistable __persistable = checkArgumentAndCast(persistable);

		RecordStore rs = PersistableManagerImpl.getRecordStore(__persistable);

		try {
			byte[] buffer = __persistable.__serialize();
			int id = __persistable.__getId();

			if (id <= 0) {
				id = rs.addRecord(buffer, 0, buffer.length);
				__persistable.__setId(id);
			} else {
				rs.setRecord(id, buffer, 0, buffer.length);
			}

			return id;
		} catch (Exception ex) {
			throw handleException(ex);
		} finally {
			PersistableManagerImpl.closeRecordStore(rs);
		}
	}

	private static RecordStore getRecordStore(__Persistable persistable)
		throws FloggyException {
		PersistableMetadata metadata =
			MetadataManagerUtil.getClassBasedMetadata(persistable.getClass().getName());

		return getRecordStore(persistable.getRecordStoreName(), metadata, false);
	}

	private static class RecordStoreReference {
		RecordStore recordStore;
		int references = 0;
	}
}
