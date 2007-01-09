package net.sourceforge.floggy.persistence.rms.beans.primitive.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.array.TestFloat;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class FloatTest extends AbstractTest {

    static float[] att = new float[] { 452.4354F, 56.345F, -3545.8989F };

    public Persistable newInstance() {
	return new TestFloat();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
