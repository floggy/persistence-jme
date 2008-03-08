/**
 *  Copyright 2006 Floggy Open Source Group
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sourceforge.floggy.persistence.beans;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.animals.Bird;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class PersonArray implements Persistable {
	protected int[] age;

	protected String[] name;

	protected Bird[] x;

	/**
	 * 
	 */
	public PersonArray() {
		super();
	}

	/**
	 * Returns <code>true</code> if this <code>PersonArray</code> is the
	 * same as the o argument.
	 * 
	 * @return <code>true</code> if this <code>PersonArray</code> is the
	 *         same as the o argument.
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		PersonArray castedObj = (PersonArray) o;
		return (java.util.Arrays.equals(this.age, castedObj.age)
				&& java.util.Arrays.equals(this.name, castedObj.name) && java.util.Arrays
				.equals(this.x, castedObj.x));
	}

	public int[] getAge() {
		return age;
	}

	public String[] getName() {
		return name;
	}

	public Bird[] getX() {
		return x;
	}

	public void setAge(int[] age) {
		this.age = age;
	}

	public void setName(String[] name) {
		this.name = name;
	}

	public void setX(Bird[] x) {
		this.x = x;
	}
}
