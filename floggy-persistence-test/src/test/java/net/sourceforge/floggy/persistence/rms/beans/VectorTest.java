package net.sourceforge.floggy.persistence.rms.beans;

import java.util.Date;
import java.util.Vector;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyVector;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.beans.animals.Animal;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class VectorTest extends AbstractTest {

    static final Vector vector = new Vector();

    static {
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
	person.setName("ãçõí");
	vector.add(person);
	vector.add(null);
    }

    public Persistable newInstance() {
	return new FloggyVector();
    }

    public Object getValueForSetMethod() {
	return vector;
    }

    public void testClassThatNotImplementsPersistable() {
	FloggyVector test = new FloggyVector();
	Vector vector = new Vector();
	vector.add(new Animal());
	test.setX(vector);
	try {
	    manager.save(test);
	    fail("Deveria ocorrer erro no salvamento de uma classe que n�o � Persistable!");
	} catch (Exception e) {
	    assertEquals("The class " + Animal.class.getName()
		    + " doesn't is a persistable class!", e.getMessage());
	}
    }

}
