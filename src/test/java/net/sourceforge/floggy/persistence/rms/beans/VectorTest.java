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

import java.util.Date;
import java.util.Vector;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyVector;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.animals.Animal;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class VectorTest extends AbstractTest {

	private Vector vector = new Vector();

	public VectorTest() {
		vector.add("floggy-framework");
		vector.add(new Boolean(true));
		vector.add(new Byte((byte) 34));
		vector.add(new Character('f'));
		vector.add(null);
		vector.add(new Double(23.87));
		vector.add(new Float(23.87));
		vector.add(new Integer(234));
		vector.add(new Long(Long.MAX_VALUE));
		vector.add(new Short(Short.MIN_VALUE));
		vector.add(new Date(12345678));
		Person person = new Person();
		person.setAge(23);
		person.setName("„√ß√µ√≠");
		vector.add(person);
		vector.add(null);
	}

	public Object getValueForSetMethod() {
		return vector;
	}

	public Persistable newInstance() {
		return new FloggyVector();
	}

	public void testClassThatNotImplementsPersistable() {
		FloggyVector test = new FloggyVector();
		Vector vector = new Vector();
		vector.add(new Animal());
		test.setX(vector);
		try {
			manager.save(test);
			fail("Deveria ocorrer erro no salvamento de uma classe que n„o È Persistable!");
		} catch (Exception e) {
			assertEquals("The class " + Animal.class.getName()
					+ " doesn't is a persistable class!", e.getMessage());
		}
	}

}
