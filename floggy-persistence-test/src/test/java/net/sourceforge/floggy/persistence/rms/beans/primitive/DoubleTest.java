package net.sourceforge.floggy.persistence.rms.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.TestDouble;

public class DoubleTest extends PrimitiveAbstractTest {

    public Persistable newInstance() {
	return new TestDouble();
    }

    public Object getValueForSetMethod() {
	return new Double(23434560.897987987);
    }

    protected Class[] getClassesFromObjects(Object[] params) {
	return new Class[] { double.class };
    }

    public Object getDefaultValue() {
	return new Double(0.0);
    }

}
