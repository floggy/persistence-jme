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

    public Persistable newInstance() {
	return new PersonArray();
    }

    public Object getValueForSetMethod() {
	return persons;
    }

}
