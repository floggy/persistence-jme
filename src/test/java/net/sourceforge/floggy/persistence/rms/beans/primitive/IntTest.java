package net.sourceforge.floggy.persistence.rms.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.TestInt;

public class IntTest extends PrimitiveAbstractTest {

    public Persistable newInstance() {
	return new TestInt();
    }

    public Object getValueForSetMethod() {
	return new Integer(45676);
    }

    protected Class[] getClassesFromObjects(Object[] params) {
	return new Class[] { int.class };
    }

    public Object getDefaultValue() {
	return new Integer(0);
    }

}
