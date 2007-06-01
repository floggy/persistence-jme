package net.sourceforge.floggy.persistence;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * This is the main class of the framework. All persistence operations methods
 * (such as loading, saving, deleting and searching for objects) are declared in
 * this class.
 * 
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 * @author Thiago Rossato <thiagorossato@users.sourceforge.net>
 * @since 1.0
 */
public class PersistableManager {

	/**
	 * The single instance of PersistableManager.
	 */
	private static PersistableManager instance;

	/**
	 * Creates a new instance of PersistableManager.
	 */
	private PersistableManager() {
	}

	/**
	 * Returns the current instance of PersistableManager.
	 * 
	 * @return The current instace of PersistableManager.
	 */
	public static PersistableManager getInstance() {
		if (instance == null) {
			instance = new PersistableManager();
		}
		return instance;
	}

	/**
	 * Returns the RecordStore that is used to store all objects of the given
	 * class.
	 * 
	 * @param className
	 *            Class name of a persistable class.
	 * @return The RecordStore corresponding to the persistable class.
	 * @throws FloggyException
	 *             If the class name does not represents a persistable class.
	 */
	public static RecordStore getRecordStore(PersistableMetadata metadata)
			throws FloggyException {
		try {
			return RecordStore.openRecordStore(metadata.getRecordStoreName(),
					true);
		} catch (Exception e) {
			throw new FloggyException(e.getMessage());
		}
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
	 *             Exception thrown if <code>object</code> is
	 *             <code>null</code> or not an instance of
	 *             <code>Persistable</code>.
	 * @throws FloggyException
	 *             Exception thrown if an error occurs while loading the object.
	 * 
	 * @see #save(Persistable)
	 */
	public void load(Persistable persistable, int id) throws FloggyException {
		try {
			checkArgumentsAndCast(persistable).__load(id);
		} catch (Exception e) {
			throw new FloggyException(e.getMessage());
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
	 *             Exception thrown if <code>object</code> is
	 *             <code>null</code> or not an instance of
	 *             <code>Persistable</code>.
	 * @throws FloggyException
	 *             Exception thrown if an error occurs while storing the object.
	 * 
	 * @see #load(Persistable, int)
	 */
	public int save(Persistable persistable) throws FloggyException {
		try {
			return checkArgumentsAndCast(persistable).__save();
		} catch (Exception ex) {
			throw new FloggyException(ex.getMessage());
		}
	}

	/**
	 * Removes an object from the repository. If the object is not stored in the
	 * repository then a <code>FloggyException</code> will be thrown.
	 * 
	 * @param persistable
	 *            Object to be removed.
	 * @throws IllegalArgumentException
	 *             Exception thrown if <code>object</code> is
	 *             <code>null</code> or not an instance of
	 *             <code>Persistable</code>.
	 * @throws FloggyException
	 *             Exception thrown if an error occurs while removing the
	 *             object.
	 */
	public void delete(Persistable persistable) throws FloggyException {
		try {
			checkArgumentsAndCast(persistable).__delete();
		} catch (Exception e) {
			throw new FloggyException(e.getMessage());
		}
	}

	/**
	 * Searches objects of an especific persistable class from the repository.
	 * <br>
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
	public ObjectSet find(Class persistableClass, Filter filter,
			Comparator comparator) throws FloggyException {
		
		ObjectFilter objectFilter = null;
		ObjectComparator objectComparator = null;

		/*
		 * this is a auxiliary object used to return the name of the
		 * RecordStore. If the argument filter is not null this object is passed
		 * to the ObjectFilter constructor
		 */
		Persistable persistable = createInstance(persistableClass);

		// Creates an auxiliar filter (if necessary)
		if (filter != null) {
			objectFilter = new ObjectFilter(persistable, filter);
		}

		// Creates an auxiliar comparator (if necessary)
		if (comparator != null) {
			objectComparator = new ObjectComparator(comparator,
					createInstance(persistableClass),
					createInstance(persistableClass));
		}

		// Searchs the repository and create an object set as result.
		int[] ids = null;

		RecordStore rs = getRecordStore(checkArgumentsAndCast(persistable)
				.__getPersistableMetadata());

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
		} catch (RecordStoreException e) {
			throw new FloggyException(e.getMessage());
		}

		return new ObjectSetImpl(ids, persistableClass);
	}

	static Persistable createInstance(Class persistableClass)
			throws FloggyException {
		validatePersistableClassArgument(persistableClass);
		// Try to create a new instance of the persistable class.
		try {
			return (Persistable) persistableClass.newInstance();
		} catch (Exception e) {
			throw new FloggyException(
					"Error creating a new instance of the persistable class: "
							+ e.getMessage());
		}
	}

	private static __Persistable checkArgumentsAndCast(Persistable persistable) {
		if (persistable == null) {
			throw new IllegalArgumentException(
					"The persistable object cannot be null!");
		}
		if (persistable instanceof __Persistable) {
			return (__Persistable) persistable;
		} else {
			throw new IllegalArgumentException(persistable.getClass().getName()
					+ "\" is not a valid persistable class.");
		}
	}

	static void validatePersistableClassArgument(Class persistableClass) {
		// testing if persistableClass is null
		if (persistableClass == null) {
			throw new IllegalArgumentException(
					"The persistable class cannot be null!");
		}
		// Checks if the persistableClass is a valid persistable class.
		if (!__Persistable.class.isAssignableFrom(persistableClass)) {
			throw new IllegalArgumentException(persistableClass.getName()
					+ " is not a valid persistable class.");
		}
	}
}
