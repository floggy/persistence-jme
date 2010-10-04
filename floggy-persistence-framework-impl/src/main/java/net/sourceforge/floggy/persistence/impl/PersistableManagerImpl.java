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

import javax.microedition.rms.RecordComparator;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordFilter;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

import net.sourceforge.floggy.persistence.Comparator;
import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.IndexFilter;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.PolymorphicObjectSet;
import net.sourceforge.floggy.persistence.SingleObjectSet;
import net.sourceforge.floggy.persistence.impl.strategy.JoinedStrategyObjectFilter;
import net.sourceforge.floggy.persistence.impl.strategy.PerClassStrategyObjectFilter;
import net.sourceforge.floggy.persistence.impl.strategy.SingleStrategyObjectFilter;

/**
 * This is the main class of the framework. All persistence operations methods
 * (such as loading, saving, deleting and searching for objects) are declared in
 * this class.
 * 
 * @since 1.0
 */
public class PersistableManagerImpl extends PersistableManager {

	protected Class deletableClass;
	protected Class perClassStrategyClass;
	protected Class singleStrategyClass;

	/**
	 * Creates a new instance of PersistableManager.
	 */
	public PersistableManagerImpl() throws Exception {
		deletableClass = Class.forName("net.sourceforge.floggy.persistence.Deletable");
		perClassStrategyClass = Class.forName("net.sourceforge.floggy.persistence.strategy.PerClassStrategy");
		singleStrategyClass = Class.forName("net.sourceforge.floggy.persistence.strategy.SingleStrategy");
		SerializationManager.setPersistableManager(this);
		PersistableMetadataManager.init();
		IndexManager.init();
	}
	
	public int batchSave(Persistable persistable) throws FloggyException {
		__Persistable __persistable = Utils.checkArgumentAndCast(persistable);
		int id = __persistable.__getId();
		if (id > 0) {
			throw new FloggyException("You should not use this method to update the persistable object.");
		} else {
			id = save(persistable);
			__persistable.__setId(0);
		}
		return id;
	}

	/**
	 * Removes an object from the repository. If the object is not stored in the
	 * repository then a <code>FloggyException</code> will be thrown.
	 * 
	 * @param persistable
	 *            Object to be removed.
	 * @throws IllegalArgumentException
	 *             Exception thrown if <code>object</code> is <code>null</code>
	 *             or not an instance of <code>Persistable</code>.
	 * @throws FloggyException
	 *             Exception thrown if an error occurs while removing the
	 *             object.
	 */
	public void delete(Persistable persistable) throws FloggyException {
		__Persistable __persistable = Utils.checkArgumentAndCast(persistable);
		int id = __persistable.__getId();
		if (id > 0) {
			RecordStore rs = RecordStoreManager
					.getRecordStore(__persistable);
			try {
				__persistable.__delete();
				rs.deleteRecord(id);
				IndexManager.afterDelete(__persistable);
				__persistable.__setId(0);
			} catch (Exception ex) {
				throw Utils.handleException(ex);
			} finally {
				RecordStoreManager.closeRecordStore(rs);
			}
		}
	}

	/**
	 * Removes all objects from the repository.
	 * 
	 * @param persistableClass
	 *            The persistable class to search the objects.
	 * @throws IllegalArgumentException
	 *             Exception thrown if <code>object</code> is <code>null</code>
	 *             or not an instance of <code>Persistable</code>.
	 * @throws FloggyException
	 *             Exception thrown if an error occurs while removing the
	 *             object.
	 */
	public void deleteAll() throws FloggyException {
		try {
			Enumeration metadatas = PersistableMetadataManager.getClassBasedMetadatas();
			while (metadatas.hasMoreElements()) {
				PersistableMetadata metadata = (PersistableMetadata) metadatas.nextElement();
				IndexManager.deleteIndex(metadata.getClassName());
				RecordStoreManager.deleteRecordStore(metadata.getRecordStoreName());
			}
		} catch (Exception ex) {
			throw Utils.handleException(ex);
		}
	}

	/**
	 * Removes all objects from the repository.
	 * 
	 * @param persistableClass
	 *            The persistable class to search the objects.
	 * @throws IllegalArgumentException
	 *             Exception thrown if <code>object</code> is <code>null</code>
	 *             or not an instance of <code>Persistable</code>.
	 * @throws FloggyException
	 *             Exception thrown if an error occurs while removing the
	 *             object.
	 */
	public void deleteAll(Class persistableClass) throws FloggyException {
		__Persistable persistable = Utils.createInstance(persistableClass);
		PersistableMetadata metadata = PersistableMetadataManager.getClassBasedMetadata(persistableClass.getName());
		if (deletableClass.isAssignableFrom(persistableClass) || metadata.getSuperClassName() != null) {
			ObjectSet os = find(persistableClass, null, null);
			for (int i = 0; i < os.size(); i++) {
				persistable = (__Persistable) os.get(i);
				delete(persistable);
			}
		} else {
			try {
				RecordStoreManager.deleteRecordStore(persistable.getRecordStoreName());
				IndexManager.deleteIndex(persistableClass.getName());
			} catch (Exception ex) {
				throw Utils.handleException(ex);
			}
		}
	}
	
	public int getId(Persistable persistable) {
		__Persistable __persistable = Utils.checkArgumentAndCast(persistable);
		return __persistable.__getId();
	}

	/**
	 * Searches objects of an specific persistable class from the repository. <br>
	 * <br>
	 * An optional application-defined search criteria can be defined using a
	 * <code>Filter</code>.<br>
	 * <br>
	 * An optional application-defined sort order can be defined using a
	 * <code>Comparator</code>.
	 * 
	 * @param persistableClass
	 *            The persistable class to search the objects.
	 * @param filter
	 *            An optional application-defined criteria for searching
	 *            objects.
	 * @param comparator
	 *            An optional application-defined criteria for sorting objects.
	 * @return List of objects that matches the defined criteria.
	 */
	public SingleObjectSet find(Class persistableClass, Filter filter,
			Comparator comparator) throws FloggyException {
		return find(persistableClass, filter, comparator, false);
	}

	/**
	 * Searches objects of an specific persistable class from the repository. <br>
	 * <br>
	 * An optional application-defined search criteria can be defined using a
	 * <code>Filter</code>.<br>
	 * <br>
	 * An optional application-defined sort order can be defined using a
	 * <code>Comparator</code>.
	 * 
	 * @param persistableClass
	 *            The persistable class to search the objects.
	 * @param filter
	 *            An optional application-defined criteria for searching
	 *            objects.
	 * @param comparator
	 *            An optional application-defined criteria for sorting objects.
	 * @return List of objects that matches the defined criteria.
	 */
	public SingleObjectSet find(Class persistableClass, Filter filter,
			Comparator comparator, boolean lazy) throws FloggyException {

		RecordFilter objectFilter = null;
		RecordComparator objectComparator = null;

		/*
		 * this is a auxiliary object used to return the name of the
		 * RecordStore. If the argument filter is not null this object is passed
		 * to the ObjectFilter constructor
		 */
		__Persistable persistable = Utils.createInstance(persistableClass);

		objectFilter = getFilter(persistable, filter, lazy);

		// Creates an auxiliary comparator (if necessary)
		if (comparator != null) {
			objectComparator = new ObjectComparator(comparator,
					Utils.createInstance(persistableClass),
					Utils.createInstance(persistableClass), lazy);
		}

		// Searchs the repository and create an object set as result.
		int[] ids = null;

		RecordStore rs = RecordStoreManager.getRecordStore(persistable);

		try {
			RecordEnumeration en = rs.enumerateRecords(objectFilter,
					objectComparator, false);
			int numRecords = en.numRecords();
			if (numRecords > 0) {
				ids = new int[numRecords];
				for (int i = 0; i < numRecords; i++) {
					ids[i] = en.nextRecordId();
				}
			}
			en.destroy();
		} catch (RecordStoreException ex) {
			throw Utils.handleException(ex);
		} finally {
			RecordStoreManager.closeRecordStore(rs);
		}

		return new ObjectSetImpl(ids, persistableClass, this, lazy);
	}

	/**
	 * Searches objects of an especific persistable class from the repository. <br>
	 * <br>
	 * An optional application-defined search criteria can be defined using a
	 * <code>IndexFilter</code>.<br>
	 * <br>
	 * An optional application-defined sort order can be defined using a
	 * <code>Comparator</code>.
	 * 
	 * @param persistableClass
	 *            The persistable class to search the objects.
	 * @param filter
	 *            An optional application-defined criteria for searching
	 *            objects.
	 * @param comparator
	 *            An optional application-defined criteria for sorting objects.
	 * @return List of objects that matches the defined criteria.
	 */
	public SingleObjectSet find(Class persistableClass, IndexFilter indexFilter, boolean lazy) throws FloggyException {

		if(indexFilter == null)
			throw new IllegalArgumentException("The indexFilter cannot be null");
		
		Utils.validatePersistableClassArgument(persistableClass);
	
		// Searchs the index repository for objects that contains index value.
		try {
			int[] ids = IndexManager.getId(persistableClass, indexFilter.getIndexName(), indexFilter.getIndexValue());
			return new ObjectSetImpl(ids, persistableClass, this, lazy);
		} catch (Exception ex) {
			throw Utils.handleException(ex);
		}
	}

	public PolymorphicObjectSet polymorphicFind(Class persistableClass,
			Filter filter, boolean lazy)
			throws FloggyException {

		RecordFilter objectFilter = null;
		__Persistable persistable = null;
		/*
		 * this is a auxiliary object used to return the name of the
		 * RecordStore. If the argument filter is not null this object is passed
		 * to the ObjectFilter constructor
		 */

		// Searchs the repository and create an object set as result.
		int[] ids = null;

		PersistableMetadata metadata = PersistableMetadataManager
				.getClassBasedMetadata(persistableClass.getName());

		PolymorphicObjectSetImpl objectSet = new PolymorphicObjectSetImpl(this, lazy);

		String[] persistables = metadata.getDescendents();

		if (persistables == null || persistables.length == 0) {
			persistables = new String[]{persistableClass.getName()};
		}

		for (int i = 0; i < persistables.length; i++) {
			try {
				persistableClass = Class.forName(persistables[i]);
				persistable = Utils.createInstance(persistableClass);
				objectFilter = getFilter(persistable, filter, lazy);
			} catch (ClassNotFoundException ex) {
				throw Utils.handleException(ex);
			}

			RecordStore rs = RecordStoreManager.getRecordStore(persistable);

			try {
				RecordEnumeration en = rs.enumerateRecords(objectFilter,
						null, false);
				int numRecords = en.numRecords();
				if (numRecords > 0) {
					ids = new int[numRecords];
					for (int j = 0; j < numRecords; j++) {
						ids[j] = en.nextRecordId();
					}
					objectSet.addList(ids, persistableClass);
				}
				en.destroy();
			} catch (RecordStoreException ex) {
				throw Utils.handleException(ex);
			} finally {
				RecordStoreManager.closeRecordStore(rs);
			}
		}

		return objectSet;
	}

	public boolean isPersisted(Persistable persistable) {
		__Persistable __persistable = Utils.checkArgumentAndCast(persistable);
		return __persistable.__getId() > 0;
	}
	
	/**
	 * Load an previously stored object from the repository using the object ID.<br>
	 * The object ID is the result of a save operation or you can obtain it
	 * executing a search.
	 * 
	 * @param persistable
	 *            An instance where the object data will be loaded into. Cannot
	 *            be <code>null</code>.
	 * @param id
	 *            The ID of the object to be loaded from the repository.
	 * @throws IllegalArgumentException
	 *             Exception thrown if <code>object</code> is <code>null</code>
	 *             or not an instance of <code>Persistable</code>.
	 * @throws FloggyException
	 *             Exception thrown if an error occurs while loading the object.
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
	 * @param persistable
	 *            An instance where the object data will be loaded into. Cannot
	 *            be <code>null</code>.
	 * @param id
	 *            The ID of the object to be loaded from the repository.
	 * @throws IllegalArgumentException
	 *             Exception thrown if <code>object</code> is <code>null</code>
	 *             or not an instance of <code>Persistable</code>.
	 * @throws FloggyException
	 *             Exception thrown if an error occurs while loading the object.
	 * 
	 * @see #save(Persistable)
	 */
	public void load(Persistable persistable, int id, boolean lazy)
			throws FloggyException {
		__Persistable __persistable = Utils.checkArgumentAndCast(persistable);
		// posso fazer cache do metadata
		RecordStore rs = RecordStoreManager.getRecordStore(__persistable);
		try {
			byte[] buffer = rs.getRecord(id);
			if (buffer != null) {
				__persistable.__deserialize(buffer, lazy);
			}
			__persistable.__setId(id);
		} catch (Exception ex) {
			throw Utils.handleException(ex);
		} finally {
			RecordStoreManager.closeRecordStore(rs);
		}
	}

	/**
	 * Store an object in the repository. If the object is already in the
	 * repository, the object data will be overwritten.<br>
	 * The object ID obtained from this operation can be used in the load
	 * operations.
	 * 
	 * @param persistable
	 *            Object to be stored.
	 * @return The ID of the object.
	 * @throws IllegalArgumentException
	 *             Exception thrown if <code>object</code> is <code>null</code>
	 *             or not an instance of <code>Persistable</code>.
	 * @throws FloggyException
	 *             Exception thrown if an error occurs while storing the object.
	 * 
	 * @see #load(Persistable, int)
	 */
	public int save(Persistable persistable) throws FloggyException {
		__Persistable __persistable = Utils.checkArgumentAndCast(persistable);
		RecordStore rs = RecordStoreManager.getRecordStore(__persistable);
		try {
			byte[] buffer = __persistable.__serialize(true);
			int id = __persistable.__getId();

			if(id <= 0) {
				id = rs.addRecord(buffer, 0, buffer.length);
				__persistable.__setId(id);
			} else {
				rs.setRecord(id, buffer, 0, buffer.length);
			}
			IndexManager.afterSave(__persistable);
			return id;
		} catch (Exception ex) {
			throw Utils.handleException(ex);
		} finally {
			RecordStoreManager.closeRecordStore(rs);
		}
	}

	public void setProperty(String name, Object value) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("The property name cannot be null or empty");
		} else if (value == null) {
			throw new IllegalArgumentException("The property value cannot be null");
		} else if (name.equals(PersistableManager.BATCH_MODE)) {
			if (value instanceof Boolean) {
				RecordStoreManager.setBatchMode(((Boolean)value).booleanValue());
			} else {
				throw new IllegalArgumentException("The property PersistableManager.BATCH_MODE must be an instance of Boolean");
			}
		} else if (name.equals(PersistableManager.STORE_INDEX_AFTER_SAVE_OPERATION)) {
			if (value instanceof Boolean) {
				IndexManager.setStoreIndexAfterSave(((Boolean)value).booleanValue());
			} else {
				throw new IllegalArgumentException("The property PersistableManager.STORE_INDEX_AFTER_SAVE_OPERATION must be an instance of Boolean");
			}
		} else {
			throw new IllegalArgumentException("Unreconized property: " + name);
		}
	}
	
	public void shutdown() throws FloggyException {
		try {
			IndexManager.shutdown();
			RecordStoreManager.shutdown();
		} catch (Exception ex) {
			throw Utils.handleException(ex);
		}
	}
	
	protected RecordFilter getFilter(__Persistable persistable, Filter filter, boolean lazy) {
		Class persistableClass = persistable.getClass();

		RecordFilter recordFilter = null;
		if (perClassStrategyClass.isAssignableFrom(persistableClass)) {
			// Creates an auxiliar filter (if necessary)
			if (filter != null) {
				recordFilter = new PerClassStrategyObjectFilter(persistable, filter, lazy);
			}
		}
		else if (singleStrategyClass.isAssignableFrom(persistableClass)) {
			// Creates an auxiliar filter (if necessary)
			if (filter != null) {
				recordFilter = new SingleStrategyObjectFilter(persistable, filter, lazy);
			}
			else {
				recordFilter = new SingleStrategyObjectFilter(persistable, lazy);
			}
		}
		else {
			// Creates an auxiliar filter (if necessary)
			if (filter != null) {
				recordFilter = new JoinedStrategyObjectFilter(persistable, filter, lazy);
			} else {
				recordFilter = new JoinedStrategyObjectFilter(lazy);
			}
		}
		
		return recordFilter;
	}

}
