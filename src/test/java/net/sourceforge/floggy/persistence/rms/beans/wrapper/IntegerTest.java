package net.sourceforge.floggy.persistence.rms.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.TestInteger;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class IntegerTest extends AbstractTest {

    public Persistable newInstance() {
	return new TestInteger();
    }

    public Object getValueForSetMethod() {
	return new Integer(45676);
    }

}
