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
package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.PersonArray;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class PersonArrayTest extends AbstractTest {

	static Person[] persons = new Person[2];

	static {
		Person person = new Person();
		person.setAge(21);
		person.setName("floggy");
		person.setX(new Person());
		persons[0] = person;
		persons[1] = new Person();
	}

	public Object getValueForSetMethod() {
		return persons;
	}

	public Persistable newInstance() {
		return new PersonArray();
	}

}
