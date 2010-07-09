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

package net.sourceforge.floggy.persistence;

/**
 * This is the main class of the framework. All persistence operations
 * methods (such as loading, saving, deleting and searching for objects) are
 * declared in this class.
 *
 * @since 1.0
 */
public abstract class PersistableManager {
	
	public static final String BATCH_MODE = "BATCH_MODE";
    /**
     * The single instance of PersistableManager.
     */
    private static PersistableManager instance;

    /**
     * Returns the current instance of PersistableManager.
     *
     * @return The current instance of PersistableManager.
     *
     * @throws RuntimeException DOCUMENT ME!
     */
    public static PersistableManager getInstance() {
        if (instance == null) {
            try {
                Class pmClass =
                    Class.forName("net.sourceforge.floggy.persistence.impl.PersistableManagerImpl");
                instance = (PersistableManager) pmClass.newInstance();
            } catch (ClassNotFoundException cnfex) {
            	throw new RuntimeException("No PersistableManager implementation was found. Please check the weaver execution.");
            } catch (RuntimeException rex) {
		throw rex;
	    } catch (Exception ex) {
            	String message = ex.getMessage();
            	if (message == null) {
            		message = ex.getClass().getName();
            	}
            	throw new RuntimeException(message);
            }
        }

        return instance;
    }

    /**
     * Store an object in the repository, it will always create a new entry 
     * on the repository.<br> The main use of this method is to import data 
     * from a remote source.<br> Set the property BATCH_MODE to true to improve 
     * the performance and create only one object to save the data.
     * <br><br>
     * <code>
     * PersistableManager manager = PersistableManager.getInstance();<br>
     * manager.setProperty(PersistableManager.BATCH_MODE, Boolean.TRUE);<br>
     * Customer customer = new Customer(); <br>
     * for(int i = 0; i < ...; i++) {<br>
     * &nbsp;&nbsp;loadDataFromServerOnCustomer(customer);<br>
     * &nbsp;&nbsp;manager.batchSave(customer);<br>
     * }
     * manager.setProperty(PersistableManager.BATCH_MODE, Boolean.FALSE);<br>
     * </code>
     * <br><br>
     * The object ID obtained from this operation can be used in the load
     * operations.
     *
     * @param persistable Object to be stored.
     *
     * @return The ID of the object.
     *
     * @throws FloggyException Exception thrown if an error occurs while
     *         storing the object.
     *
     * @see #load(Persistable, int)
     */
    public abstract int batchSave(Persistable persistable) throws FloggyException;

    /**
     * Load an previously stored object from the repository using the
     * object ID.<br>
     * The object ID is the result of a save operation or you can obtain it
     * executing a search.
     *
     * @param persistable An instance where the object data will be loaded
     *        into. Cannot be <code>null</code>.
     * @param id The ID of the object to be loaded from the repository.
     *
     * @throws FloggyException Exception thrown if an error occurs while
     *         loading the object.
     *
     * @see #save(Persistable)
     */
    public abstract void load(Persistable persistable, int id)
        throws FloggyException;

    /**
     * Load an previously stored object from the repository using the
     * object ID.<br>
     * The object ID is the result of a save operation or you can obtain it
     * executing a search.
     *
     * @param persistable An instance where the object data will be loaded
     *        into. Cannot be <code>null</code>.
     * @param id The ID of the object to be loaded from the repository.
     * @param lazy A flag indicating to load or not all composite
     *        relationships.
     *
     * @throws FloggyException Exception thrown if an error occurs while
     *         loading the object.
     *
     * @see #save(Persistable)
     */
    public abstract void load(Persistable persistable, int id, boolean lazy)
        throws FloggyException;

    /**
     * Store an object in the repository. If the object is already in
     * the repository, the object data will be overwritten.<br>
     * The object ID obtained from this operation can be used in the load
     * operations.
     *
     * @param persistable Object to be stored.
     *
     * @return The ID of the object.
     *
     * @throws FloggyException Exception thrown if an error occurs while
     *         storing the object.
     *
     * @see #load(Persistable, int)
     */
    public abstract int save(Persistable persistable) throws FloggyException;

    /**
     * Removes an object from the repository. If the object is not
     * stored in the repository then a <code>FloggyException</code> will be
     * thrown.
     *
     * @param persistable Object to be removed.
     *
     * @throws FloggyException Exception thrown if an error occurs while
     *         removing the object.
     */
    public abstract void delete(Persistable persistable)
        throws FloggyException;

    /**
     * Removes all objects from the repository.
     *
     * @throws FloggyException Exception thrown if an error occurs while
     *         removing the objects.
     */
    public abstract void deleteAll() throws FloggyException;

    /**
     * Removes all objects that belongs to the class passed as
     * parameter  from the repository.
     *
     * @param persistableClass The persistable class to search the objects.
     *
     * @throws FloggyException Exception thrown if an error occurs while
     *         removing the objects.
     */
    public abstract void deleteAll(Class persistableClass)
        throws FloggyException;

    /**
     * Searches objects of an especific persistable class from the
     * repository. <br>
     * <br>
     * An optional application-defined search criteria can be  defined using a <code>Filter</code>.<br>
     * <br>
     * An optional application-defined sort order can be defined using a
     * <code>Comparator</code>.
     *
     * @param persistableClass The persistable class to search the objects.
     * @param filter An optional application-defined criteria for searching
     *        objects.
     * @param comparator An optional application-defined criteria for sorting
     *        objects.
     *
     * @return List of objects that matches the defined criteria.
     *
     * @throws FloggyException DOCUMENT ME!
     */
    public abstract SingleObjectSet find(Class persistableClass, Filter filter,
                                   Comparator comparator)
        throws FloggyException;

    /**
     * Searches objects of a specific persistable class from the
     * repository. It uses an index as the base mechanism to evaluate 
     * the registers in the repository<br>
     *
     * @param persistableClass The persistable class to search the objects.
     * @param indexFilter The filter object based on an index.
     * @param lazy A flag indicating to load or not all composite
     *        relationships.
     *
     * @return List of objects that matches the defined criteria.
     *
     * @throws FloggyException DOCUMENT ME!
     */
	public abstract SingleObjectSet find(Class persistableClass, 
		IndexFilter indexFilter, boolean lazy) throws FloggyException;
	
    /**
     * Searches objects of a specific persistable class from the
     * repository. <br>
     * <br>
     * An optional application-defined search criteria can be  defined using a <code>Filter</code>.<br>
     * <br>
     * An optional application-defined sort order can be defined using a
     * <code>Comparator</code>.
     *
     * @param persistableClass The persistable class to search the objects.
     * @param filter An optional application-defined criteria for searching
     *        objects.
     * @param comparator An optional application-defined criteria for sorting
     *        objects.
     * @param lazy A flag indicating to load or not all composite
     *        relationships.
     *
     * @return List of objects that matches the defined criteria.
     *
     * @throws FloggyException DOCUMENT ME!
     */
    public abstract SingleObjectSet find(Class persistableClass, Filter filter,
                                   Comparator comparator, boolean lazy)
        throws FloggyException;

    /**
     * Searches objects of an persistable class and its subclasses from the
     * repository. <br>
     * <br>
     * An optional application-defined search criteria can be  defined using a <code>Filter</code>.<br>
     * <br>
     * An optional application-defined sort order can be defined using a
     * <code>Comparator</code>.
     *
     * @param persistableClass The persistable class to search the objects.
     * @param filter An optional application-defined criteria for searching
     *        objects.
     * @param lazy A flag indicating to load or not all composite relationships.
     * @return List of objects that matches the defined criteria.
     *
     * @throws FloggyException DOCUMENT ME!
     */
    public abstract PolymorphicObjectSet polymorphicFind(Class persistableClass, Filter filter,
                                   boolean lazy)
        throws FloggyException;

    /**
     * Gets the id under the persistable is stored. <br>
     *
     * @param persistable Object to be retrieved the id.
     *
     * @return the id under the persistable is stored
     */
    public abstract int getId(Persistable persistable);

    /**
     * Check if the object is already persisted. <br>
     * <b>WARNING</b> The method only checks if the underline system has an
     * entry for the  given persistable object. The method doesn't checks if
     * the fields have changed.
     *
     * @param persistable Object to be checked the persistable state.
     *
     * @return true if the object is already persisted in the underline system,
     *         false otherwise.
     */
    public abstract boolean isPersisted(Persistable persistable);

    /**
     * Set a property
     *  
     * @param property the property's name
     * @param value the property's value
     */
    public abstract void setProperty(String name, Object value);
    
    /**
     * Shutdown the PersistableManager.
     */
    public abstract void shutdown() throws FloggyException;
}
