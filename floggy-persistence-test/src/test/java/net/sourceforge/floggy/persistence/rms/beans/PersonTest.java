package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.Person;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class PersonTest extends AbstractTest {

    static Person person = new Person();

    static {
	person.setAge(21);
	person.setName("floggy");
	person.setX(new Person());
    }

    public Persistable newInstance() {
	return new Person();
    }

    public Object getValueForSetMethod() {
	return person;
    }

}
