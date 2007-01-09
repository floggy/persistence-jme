package net.sourceforge.floggy.persistence.rms.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.array.TestDouble;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class DoubleTest extends AbstractTest {

    static Double[] att = new Double[] { new Double(-3242.2342),
	    new Double(56), new Double(Double.MAX_VALUE) };

    public Persistable newInstance() {
	return new TestDouble();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
