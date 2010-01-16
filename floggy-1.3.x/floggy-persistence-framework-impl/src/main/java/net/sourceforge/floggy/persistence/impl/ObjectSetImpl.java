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

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;

/**
* An implementation of the <code>ObjectSet</code> interface.
*
* @since 1.0
 */
class ObjectSetImpl implements ObjectSet {
	/** Persistable class used in the search. */
	protected Class persistableClass;

	/** PersistableManager instance. */
	protected PersistableManagerImpl manager;

	/** The lazy flag. */
	protected boolean lazy;

	/** List of IDs. */
	private int[] ids;
	private int size;

/**
   * Creates a new instance of ObjectSetImpl.
   * 
   * @param ids
   *                The list of IDs, result of a search.
   * @param persistableClass
   *                A persistable class used to create new instances of
   *                objects.
   * @param manager TODO
   */
	protected ObjectSetImpl(int[] ids, Class persistableClass,
		PersistableManagerImpl manager, boolean lazy) {
		this.ids = ids;
		this.persistableClass = persistableClass;

		this.size = (ids == null) ? 0 : ids.length;

		this.manager = manager;

		this.lazy = lazy;
	}

	/**
	* 
	* @see net.sourceforge.floggy.persistence.ObjectSet#get(int)
	*/
	public Persistable get(int index) throws FloggyException {
		Persistable persistable =
			PersistableManagerImpl.createInstance(persistableClass);

		get(index, persistable);

		return persistable;
	}

	/**
	* 
	* @see net.sourceforge.floggy.persistence.ObjectSet#get(int, Persistable)
	*/
	public void get(int index, Persistable persistable) throws FloggyException {
		if (persistable == null) {
			throw new IllegalArgumentException(
				"The persistable object cannot be null!");
		}

		manager.load(persistable, getId(index), lazy);
	}

	/**
	* 
	* @see net.sourceforge.floggy.persistence.ObjectSet#getId(int)
	*/
	public int getId(int index) {
		if ((index < 0) || (index >= size)) {
			throw new IndexOutOfBoundsException();
		}

		return ids[index];
	}

	/**
	* 
	* @see net.sourceforge.floggy.persistence.ObjectSet#isLazy()
	*/
	public boolean isLazy() {
		return lazy;
	}

	/**
	* 
	* @see net.sourceforge.floggy.persistence.ObjectSet#setLazy(boolean)
	*/
	public void setLazy(boolean lazy) {
		this.lazy = lazy;
	}

	/**
	* 
	* @see net.sourceforge.floggy.persistence.ObjectSet#size()
	*/
	public int size() {
		return size;
	}
}
