package net.sourceforge.floggy.persistence;

import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;

/**
 * This is the main class of the framework. All persistence operations methods
 * (such loading, saving, deleting and searching for objects) are declared in
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
         * Returns the metadata of a persistable class.
         * 
         * @param persistableClass
         * @return
         * @throws FloggyException
         */
    public PersistableMetadata getMetadata(Class persistableClass)
	    throws FloggyException {
	// validating teh argument
	validatePersistableClassArgument(persistableClass);

	RecordStore rs = getRecordStore(persistableClass.getName());

	final int objectCount;
	final long lastModified;
	final int size;
	final int version;

	try {
	    objectCount = rs.getNumRecords();
	    lastModified = rs.getLastModified();
	    size = rs.getSize();
	    version = rs.getVersion();
	} catch (RecordStoreException e) {
	    throw new FloggyException(
		    "Error requesting information from repository.");
	}

	PersistableMetadata pm = new PersistableMetadata() {
	    public long getLastModified() {
		return lastModified;
	    }

	    public int getObjectCount() {
		return objectCount;
	    }

	    public int getSize() {
		return size;
	    }

	    public int getVersion() {
		return version;
	    }
	};

	return pm;
    }

    /**
         * Returns the RecordStore that is used to store all objects of the
         * given class.
         * 
         * @param className
         *                Class name of a persistable class.
         * @return The RecordStore corresponding to the persistable class.
         * @throws FloggyException
         *                 If the class name does not represents a persistable
         *                 class.
         */
    public static RecordStore getRecordStore(String className)
	    throws FloggyException {
	if (className == null) {
	    throw new IllegalArgumentException(
		    "The name of RecordStore cannot be null!");
	}
	try {
	    if (className.lastIndexOf('.') != -1) {
		className = className.substring(className.lastIndexOf('.') + 1);
	    }

	    className += className.hashCode();

	    return RecordStore.openRecordStore(className, true);
	} catch (Exception e) {
	    throw new FloggyException(e.getMessage());
	}
    }

    /**
         * Load an previously stored object from the repository using the object
         * ID.<br>
         * The object ID is the result of a save operation or you can obtain it
         * executing a search.
         * 
         * @param object
         *                An instance where the object data will be loaded into.
         *                Cannot be <code>null</code>.
         * @param id
         *                The ID of the object to be loaded from the repository.
         * @throws IllegalArgumentException
         *                 Exception thrown if <code>object</code> is
         *                 <code>null</code> or not an instance of
         *                 <code>Persistable</code>.
         * @throws FloggyException
         *                 Exception thrown if an error occurs while loading the
         *                 object.
         * 
         * @see #save(Persistable)
         */
    public void load(Persistable object, int id) throws FloggyException {
	if (object == null) {
	    throw new IllegalArgumentException(
		    "The persistable object cannot be null!");
	}

	if (object instanceof __Persistable) {
	    __Persistable __persistable = (__Persistable) object;

	    try {
		__persistable.__load(id);
	    } catch (Exception e) {
		throw new FloggyException(e.getMessage());
	    }
	} else {
	    throw new IllegalArgumentException(object.getClass().getName()
		    + " is not a valid persistable class.");
	}
    }

    /**
         * Store an object in the repository. If the object is already in the
         * repository, the object data will be overwritten.<br>
         * The object ID obtained from this operation can be used in the load
         * operations.
         * 
         * @param object
         *                Object to be stored.
         * @return The ID of the object.
         * @throws IllegalArgumentException
         *                 Exception thrown if <code>object</code> is
         *                 <code>null</code> or not an instance of
         *                 <code>Persistable</code>.
         * @throws FloggyException
         *                 Exception thrown if an error occurs while storing the
         *                 object.
         * 
         * @see #load(Persistable, int)
         */
    public int save(Persistable object) throws FloggyException {
	if (object == null) {
	    throw new IllegalArgumentException(
		    "The persistable object cannot be null!");
	}
	if (object instanceof __Persistable) {
	    __Persistable __persistable = (__Persistable) object;

	    try {
		return __persistable.__save();
	    } catch (Exception ex) {
		throw new FloggyException(ex.getMessage());
	    }
	} else {
	    throw new IllegalArgumentException(object.getClass().getName()
		    + "\" is not a valid persistable class.");
	}
    }

    /**
         * Removes an object from the repository. If the object is not stored in
         * the repository then a <code>FloggyException</code> will be thrown.
         * 
         * @param object
         *                Object to be removed.
         * @throws IllegalArgumentException
         *                 Exception thrown if <code>object</code> is
         *                 <code>null</code> or not an instance of
         *                 <code>Persistable</code>.
         * @throws FloggyException
         *                 Exception thrown if an error occurs while removing
         *                 the object.
         */
    public void delete(Persistable object) throws FloggyException {
	if (object == null) {
	    throw new IllegalArgumentException(
		    "The persistable object cannot be null!");
	}
	if (object instanceof __Persistable) {
	    __Persistable __persistable = (__Persistable) object;
	    try {
		__persistable.__delete();
	    } catch (Exception e) {
		throw new FloggyException(e.getMessage());
	    }
	} else {
	    throw new IllegalArgumentException(object.getClass().getName()
		    + "\" is not a valid persistable class.");
	}

    }

    /**
         * Searches objects of an especific persistable class from the
         * repository. <br>
         * <br>
         * An optional application-defined search criteria can be defined using
         * a <code>Filter</code>.<br>
         * <br>
         * An optional application-defined sort order can be defined using a
         * <code>Comparator</code>.
         * 
         * @param persistableClass
         *                The persistable class to search the objects.
         * @param filter
         *                An optional application-defined criteria for searching
         *                objects.
         * @param comparator
         *                An optional application-defined criteria for sorting
         *                objects.
         * @return List of objects that matches the defined criteria.
         */
    public ObjectSet find(Class persistableClass, Filter filter,
	    Comparator comparator) throws FloggyException {
	// validating teh argument
	validatePersistableClassArgument(persistableClass);

	ObjectFilter objectFilter = null;
	ObjectComparator objectComparator = null;

	// Creates an auxiliar filter (if necessary)
	if (filter != null) {
	    objectFilter = new ObjectFilter(persistableClass, filter);
	}

	// Creates an auxiliar comparator (if necessary)
	if (comparator != null) {
	    objectComparator = new ObjectComparator(persistableClass,
		    comparator);
	}

	// Searchs the repository and create an object set as result.
	int[] ids = null;

	RecordStore rs = getRecordStore(persistableClass.getName());

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
	} catch (RecordStoreException e) {
	    throw new FloggyException(e.getMessage());
	}

	return new ObjectSetImpl(ids, persistableClass);
    }

    /**
         * 
         * @param persistableClass
         * @param filter
         * @param comparator
         * @param createNewInstanceforEveryEntryFound
         * @return
         */
    private ObjectSet find(Class persistableClass, Filter filter,
	    Comparator comparator, boolean createNewInstanceforEveryEntryFound)
	    throws FloggyException {
	return null;
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
