package net.sourceforge.floggy.persistence.rms.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.TestLong;

public class LongTest extends PrimitiveAbstractTest {

    public Persistable newInstance() {
	return new TestLong();
    }

    public Object getValueForSetMethod() {
	return new Long(456789);
    }

    protected Class[] getClassesFromObjects(Object[] params) {
	return new Class[] { long.class };
    }

    public Object getDefaultValue() {
	return new Long(0);
    }

}
