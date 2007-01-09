package net.sourceforge.floggy.persistence;

/**
 * The result of a search is an <code>ObjectSet</code>. It is possible to
 * iterate over all objects using the <code>get(int index)</code> method. <br>
 * <br>
 * <code>
 * PersistableManager manager = PersistableManager.getInstance();<br>
 * ObjectSet objectSet = manager.find(Customer.class, null, null);<br>
 * for(int i = 0; i < set.size(); i++) {<br>
 * &nbsp;&nbsp;Customer customer = (Customer) objectSet.get(i);<br>
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
 * @author Thiago Rossato
 * @since 1.0
 */
public interface ObjectSet {

    /**
         * Returns the object at the specified position in the set.
         * 
         * @param index
         *                Index of the object to return.
         * @return The object at the specified position in the set.
         * @throws IndexOutOfBoundsException
         *                 Exception thrown if an invalid index was given.
         * @throws FloggyException
         *                 Exception thrown if a persistence error occurs.
         */
    public Persistable get(int index) throws FloggyException;

    /**
         * Returns the number of objects in this set.
         * 
         * @return The number of objects in this set.
         */
    public int size();

}