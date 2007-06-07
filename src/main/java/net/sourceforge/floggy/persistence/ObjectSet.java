package net.sourceforge.floggy.persistence;

/**
 * An interface representing a search result. The <code>ObjectSet</code>
 * logically maintains a sequence of the object's IDs. It is possible to iterate
 * over all objects (or a subset, if an optional object filter has been
 * supplied) using the <code>get(int index)</code> method. <br>
 * <br>
 * <code>
 * PersistableManager pm = PersistableManager.getInstance();<br>
 * ObjectSet os = manager.find(Customer.class, null, null);<br>
 * for(int i = 0; i < os.size(); i++) {<br>
 * &nbsp;&nbsp;Customer customer = (Customer) os.get(i);<br>
 * &nbsp;&nbsp;...<br>
 * }
 * </code><br>
 * <br>
 * By using an optional <code>Filter</code>, only the objects that matches
 * the provided filter will be avaiable in this set.<br>
 * <br>
 * By using an optional <code>Comparator</code>, the order of the objects in
 * this set will be determined by the comparator.
 * 
 * @see PersistableManager#find(Class, Filter, Comparator)
 * @see Filter
 * @see Comparator
 * 
 * @since 1.0
 */
public interface ObjectSet {

	/**
	 * Load the object of a given index into the object instance supplied.
	 * 
	 * @param index
	 *            Index of the object to be loaded.
	 * @return The id number at RMS system at the specified position in the set.
	 * @throws FloggyException
	 *             Exception thrown if a persistance error occurs.
	 */
	public int getId(int index) throws FloggyException;

	/**
	 * Load the object of a given index into the object instance.
	 * 
	 * @param index
	 *            Index of the object to be loaded.
	 * @param object
	 *            An instance of the object to be loaded. It cannot be
	 *            <code>null</code>.
	 * @throws IllegalArgumentException
	 *             Exception thrown if <code>object</code> is
	 *             <code>null</code>.
	 * @throws IndexOutOfBoundsException
	 *             Exception thrown if an invalid <code>index</code> was
	 *             given.
	 * @throws FloggyException
	 *             Exception thrown if a persistance error occurs.
	 */
	public void get(int index, Persistable object) throws FloggyException;

	/**
	 * Returns the object at the specified index.
	 * 
	 * @param index
	 *            Index of the object to return.
	 * @return The object at the specified position in the set.
	 * @throws IndexOutOfBoundsException
	 *             Exception thrown if an invalid index was given.
	 * @throws FloggyException
	 *             Exception thrown if a persistence error occurs.
	 */
	public Persistable get(int index) throws FloggyException;

	/**
	 * Returns the number of objects in this set.
	 * 
	 * @return The number of objects in this set.
	 */
	public int size();

}