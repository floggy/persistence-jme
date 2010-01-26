/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
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
     * Load the object at the specified index. A new instance will be 
     * created to each invocation of this method.
     *
     * @param index Index of the object to be loaded.
     *
     * @return The object at the specified position in the set.
     *
     * @throws FloggyException Exception thrown if a persistence error occurs.
     */
    Persistable get(int index) throws FloggyException;

    /**
     * Load the object at the specified index into the object instance supplied.
     *
     * @param index Index of the object to be loaded.
     * @param object An instance of the object to be loaded. It cannot be
     *        <code>null</code>.
     *
     * @throws FloggyException Exception thrown if a persistence error occurs.
     */
    void get(int index, Persistable object) throws FloggyException;

    /**
     * Get the object's id at the specified index.
     *
     * @param index Index of the object.
     *
     * @return The id number at RMS system at the specified position in the
     *         set.
     *
     * @throws FloggyException Exception thrown if a persistence error occurs.
     */
    int getId(int index) throws FloggyException;

    /**
     * Gets the lazy property.
     * 
     * @return The flag indicating the the type of fetch made by 
     * the PersistableManager.load method. 
     */
    boolean isLazy();
    
    /**
     * Sets the lazy property.
     */
    void setLazy(boolean lazy);
    
    /**
     * Returns the number of objects in this set.
     *
     * @return The number of objects in this set.
     */
    int size();
}
