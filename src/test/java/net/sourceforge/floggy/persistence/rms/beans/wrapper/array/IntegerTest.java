package net.sourceforge.floggy.persistence.rms.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.array.TestInteger;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class IntegerTest extends AbstractTest {

    static Integer[] att = new Integer[] { new Integer(-2345),
	    new Integer(-34576), new Integer(2), new Integer(56) };

    public Persistable newInstance() {
	return new TestInteger();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
