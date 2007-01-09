package net.sourceforge.floggy.persistence.rms.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.TestFloat;

public class FloatTest extends PrimitiveAbstractTest {

    public Persistable newInstance() {
	return new TestFloat();
    }

    public Object getValueForSetMethod() {
	return new Float((float) 23.0987);
    }

    protected Class[] getClassesFromObjects(Object[] params) {
	return new Class[] { float.class };
    }

    public Object getDefaultValue() {
	return new Float(0.0);
    }

}
