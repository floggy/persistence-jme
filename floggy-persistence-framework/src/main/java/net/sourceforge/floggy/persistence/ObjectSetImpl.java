package net.sourceforge.floggy.persistence;

/**
 * An implementation of the <code>ObjectSet</code> interface.
 * 
 * @author Thiago Rossato
 * @since 1.0
 */
class ObjectSetImpl implements ObjectSet {

	/**
	 * List of IDs.
	 */
	private int[] ids;

	/**
	 * The number of objects.
	 */
	private int size;

	/**
	 * Persistable class used in the search.
	 */
	protected Class persistableClass;

	/**
	 * PersistableManager instance.
	 */
	protected PersistableManager manager;

	/**
	 * Creates a new instance of ObjectSetImpl.
	 * 
	 * @param ids
	 *                The list of IDs, result of a search.
	 * @param persistableClass
	 *                A persistable class used to create new instances of
	 *                objects.
	 */
	protected ObjectSetImpl(int[] ids, Class persistableClass) {
		this.ids = ids;
		this.persistableClass = persistableClass;

		// Init attributes
		this.size = (ids == null) ? 0 : ids.length;

		// Retrieve the manager instance
		this.manager = PersistableManager.getInstance();
	}

	/**
	 * 
	 * 
	 * @see net.sourceforge.floggy.persistence.ObjectSet#get(int)
	 */
	public void get(int index, Persistable persistable) throws FloggyException {

		// Checks if the index is valid.
		if (index < 0 || index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		if (persistable == null) {
			throw new IllegalArgumentException("The persistable object cannot be null!");
		}

		// Load the data from the repository.
		manager.load(persistable, ids[index]);
	}

	/**
	 * 
	 * 
	 * @see net.sourceforge.floggy.persistence.ObjectSet#get(int)
	 */
	public Persistable get(int index) throws FloggyException {
		Persistable persistable = null;

		// Try to create a new instance of the persistable class.
		try {
			persistable = (Persistable) persistableClass.newInstance();
		} catch (InstantiationException e) {
			throw new FloggyException(
					"Error creating a new instance of the persistable class: "
							+ e.getMessage());
		} catch (IllegalAccessException e) {
			throw new FloggyException(
					"Error creating a new instance of the persistable class: "
							+ e.getMessage());
		}

		// Load the data from the repository.
		get(index, persistable);
		return persistable;
	}

	/**
	 * 
	 * 
	 * @see net.sourceforge.floggy.persistence.ObjectSet#size()
	 */
	public int size() {
		return size;
	}
}
