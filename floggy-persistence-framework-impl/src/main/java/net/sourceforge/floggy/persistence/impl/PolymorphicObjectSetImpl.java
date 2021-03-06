/**
 * Copyright (c) 2006-2011 Floggy Open Source Group. All rights reserved.
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
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PolymorphicObjectSet;

/**
* An implementation of the <code>ObjectSet</code> interface.
*
* @author Thiago Moreira
*
* @since 1.4.0
 */
public class PolymorphicObjectSetImpl implements PolymorphicObjectSet {
	/** PersistableManager instance. */
	protected PersistableManagerImpl manager;

	/** The lazy flag. */
	protected boolean lazy;
	private ObjectList[] list = new ObjectList[0];
	private int size;

/**
   * Creates a new instance of ObjectSetImpl.
   * 
   * @param manager The PersistableManager instance
   * @param lazy Flag indicating lazy or not load
   */
	public PolymorphicObjectSetImpl(PersistableManagerImpl manager, boolean lazy) {
		this.manager = manager;
		this.lazy = lazy;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param ids DOCUMENT ME!
	* @param persistableClass DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public void addList(int[] ids, Class persistableClass)
		throws FloggyException {
		if (ids != null) {
			ObjectList objectList = new ObjectList();
			objectList.ids = ids;
			objectList.persistableClass = persistableClass;
			objectList.sharedInstance = Utils.createInstance(persistableClass);

			size = size + ids.length;

			ObjectList[] temp = new ObjectList[list.length + 1];
			System.arraycopy(list, 0, temp, 0, list.length);
			temp[list.length] = objectList;
			list = temp;
		}
	}

	/**
	* 
	* @see net.sourceforge.floggy.persistence.ObjectSet#get(int, Persistable)
	*/
	public void get(int index, Persistable persistable) throws FloggyException {
		throw new IllegalStateException(
			"You can't call this method on a PolymorphicObjectSet.");
	}

	/**
	* 
	* @see net.sourceforge.floggy.persistence.ObjectSet#get(int)
	*/
	public Persistable get(int index) throws FloggyException {
		if ((index < 0) || (index >= size)) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}

		int sumOfLength = 0;
		ObjectList currentObjectList = null;
		Persistable persistable = null;
		int realIndex = 0;

		for (int i = 0; i < list.length; i++) {
			int length = list[i].ids.length;

			if ((sumOfLength <= index) && (index < (sumOfLength + length))) {
				currentObjectList = list[i];
				realIndex = index - sumOfLength;

				break;
			}

			sumOfLength = sumOfLength + length;
		}

		if (currentObjectList != null) {
			persistable = Utils.createInstance(currentObjectList.persistableClass);

			manager.load(persistable, currentObjectList.ids[realIndex]);
		} else {
			throw new IndexOutOfBoundsException();
		}

		return persistable;
	}

	/**
	* 
	* @see net.sourceforge.floggy.persistence.ObjectSet#getId(int)
	*/
	public int getId(int index) {
		throw new IllegalStateException(
			"You can't call this method on a PolymorphicObjectSet.");
	}

	/**
	* 
	* @see net.sourceforge.floggy.persistence.ObjectSet#get(int)
	*/
	public Persistable getSharedInstance(int index) throws FloggyException {
		if ((index < 0) || (index >= size)) {
			throw new IndexOutOfBoundsException(String.valueOf(index));
		}

		int sumOfLength = 0;
		ObjectList currentObjectList = null;
		Persistable persistable = null;
		int realIndex = 0;

		for (int i = 0; i < list.length; i++) {
			int length = list[i].ids.length;

			if ((sumOfLength <= index) && (index < (sumOfLength + length))) {
				currentObjectList = list[i];
				realIndex = index - sumOfLength;

				break;
			}

			sumOfLength = sumOfLength + length;
		}

		if (currentObjectList != null) {
			manager.load(currentObjectList.sharedInstance,
				currentObjectList.ids[realIndex]);
			persistable = currentObjectList.sharedInstance;
		} else {
			throw new IndexOutOfBoundsException();
		}

		return persistable;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean isLazy() {
		return lazy;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param lazy DOCUMENT ME!
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

	class ObjectList {
		Class persistableClass;
		int[] ids;
		__Persistable sharedInstance;
		int currentIndex;
	}
}
