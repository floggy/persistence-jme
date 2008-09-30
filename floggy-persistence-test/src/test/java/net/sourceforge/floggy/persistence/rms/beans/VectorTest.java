/**
 *  Copyright (c) 2005-2008 Floggy Open Source Group. All rights reserved.
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

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyVector;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.animals.Animal;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class VectorTest extends AbstractTest {

	private Vector vector = new Vector();

	public VectorTest() {
		vector.addElement("floggy-framework");
		vector.addElement(new Boolean(true));
		vector.addElement(new Byte((byte) 34));
		vector.addElement(new Character('f'));
		vector.addElement(null);
		vector.addElement(new Double(23.87));
		vector.addElement(new Float(23.87));
		vector.addElement(new Integer(234));
		vector.addElement(new Long(Long.MAX_VALUE));
		vector.addElement(new Short(Short.MIN_VALUE));
		vector.addElement(new Date(12345678));
		vector.addElement(TimeZone.getDefault());
		Calendar c= Calendar.getInstance();
		c.setTimeInMillis(233456567);
		vector.addElement(c);
//		break the build because the class StringBuffer doesn't implements the equals method. 
//		vector.addElement(new StringBuffer("testestringbuffer"));
		Person person = new Person();
		person.setCpf("23321654");
		person.setNome("„√ß√µ√≠");
		vector.addElement(person);
		vector.addElement(null);
	}

	protected Class getParameterType() {
		return Vector.class;
	}
	
	public Object getNewValueForSetMethod() {
		return new Vector();
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
		vector.addElement(new Animal());
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
