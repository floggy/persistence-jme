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
package net.sourceforge.floggy.persistence.beans;

import java.util.Arrays;

import net.sourceforge.floggy.persistence.Deletable;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.beans.animals.Bird;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class PersonArray implements Persistable, Deletable {
	/**
	 * DOCUMENT ME!
	 */
	protected int[] age;

	/**
	 * DOCUMENT ME!
	 */
	protected String[] name;

	/**
	 * DOCUMENT ME!
	 */
	protected Bird[] x;

/**
   * 
   */
	public PersonArray() {
		super();
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public void delete() throws FloggyException {
		if (x != null) {
			for (int i = 0; i < x.length; i++) {
				if (x[i] != null) {
					PersistableManager.getInstance().delete(x[i]);
				}
			}
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param obj DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		final PersonArray other = (PersonArray) obj;

		if (!Arrays.equals(age, other.age))
			return false;

		if (!Arrays.equals(name, other.name))
			return false;

		if (!Arrays.equals(x, other.x))
			return false;

		return true;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int[] getAge() {
		return age;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String[] getName() {
		return name;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Bird[] getX() {
		return x;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + PersonArray.hashCode(age);
		result = (prime * result) + PersonArray.hashCode(name);
		result = (prime * result) + PersonArray.hashCode(x);

		return result;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param age DOCUMENT ME!
	*/
	public void setAge(int[] age) {
		this.age = age;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param name DOCUMENT ME!
	*/
	public void setName(String[] name) {
		this.name = name;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param x DOCUMENT ME!
	*/
	public void setX(Bird[] x) {
		this.x = x;
	}

	private static int hashCode(int[] array) {
		final int prime = 31;

		if (array == null)
			return 0;

		int result = 1;

		for (int index = 0; index < array.length; index++) {
			result = (prime * result) + array[index];
		}

		return result;
	}

	private static int hashCode(Object[] array) {
		final int prime = 31;

		if (array == null)
			return 0;

		int result = 1;

		for (int index = 0; index < array.length; index++) {
			result = (prime * result)
				+ ((array[index] == null) ? 0 : array[index].hashCode());
		}

		return result;
	}
}
