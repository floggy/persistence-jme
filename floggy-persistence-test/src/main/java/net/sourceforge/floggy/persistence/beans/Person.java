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

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class Person implements Persistable {
	protected int age;

	protected String name;

	protected Person x;

	/**
	 * 
	 */
	public Person() {
		super();
	}

	/**
	 * Returns <code>true</code> if this <code>Person</code> is the same as
	 * the o argument.
	 * 
	 * @return <code>true</code> if this <code>Person</code> is the same as
	 *         the o argument.
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
		Person castedObj = (Person) o;
		return ((this.age == castedObj.age)
				&& (this.name == null ? castedObj.name == null : this.name
						.equals(castedObj.name)) && (this.x == null ? castedObj.x == null
				: this.x.equals(castedObj.x)));
	}

	public int getAge() {
		return age;
	}

	public String getName() {
		return name;
	}

	public Person getX() {
		return x;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setX(Person x) {
		this.x = x;
	}
}
