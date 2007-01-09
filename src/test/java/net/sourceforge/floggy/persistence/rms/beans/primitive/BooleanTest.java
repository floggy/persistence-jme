package net.sourceforge.floggy.persistence.rms.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.TestBoolean;

public class BooleanTest extends PrimitiveAbstractTest {

    public Persistable newInstance() {
	return new TestBoolean();
    }

    public Object getValueForSetMethod() {
	return Boolean.FALSE;
    }

    protected Class[] getClassesFromObjects(Object[] params) {
	return new Class[] { boolean.class };
    }

    public Object getDefaultValue() {
	return Boolean.FALSE;
    }

}
