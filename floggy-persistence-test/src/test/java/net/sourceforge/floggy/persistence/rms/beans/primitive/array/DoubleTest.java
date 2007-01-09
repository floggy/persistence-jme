package net.sourceforge.floggy.persistence.rms.beans.primitive.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.array.TestDouble;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class DoubleTest extends AbstractTest {

    static double[] att = new double[] { -3242.2342, 56, Double.MAX_VALUE };

    public Persistable newInstance() {
	return new TestDouble();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
