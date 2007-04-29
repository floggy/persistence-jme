package net.sourceforge.floggy.persistence;

/**
 * An interface defining a filter which examines a object to see if it matches
 * (based on an application-defined criteria). The application implements the
 * <code>match(Persistable object)</code> method to select objects. Returns
 * true if the candidate record is selected by the RecordFilter. This interface
 * is used in the record store for searching or subsetting records.
 * 
 * @since 1.0
 * 
 * @see PersistableManager#find(Class, Filter, Comparator)
 * @see Comparator
 */
public interface Filter {

	/**
	 * Returns true if the candidate object matches the implemented criterion.
	 * 
	 * @param o
	 *            The candidate object.
	 * @return True if the candidate object matches the implemented criterion.
	 */
	public boolean matches(Persistable o);

}
