package net.sourceforge.floggy.persistence.rms.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.TestByte;

public class ByteTest extends PrimitiveAbstractTest {

    public Persistable newInstance() {
	return new TestByte();
    }

    public Object getValueForSetMethod() {
	return new Byte((byte) 23);
    }

    protected Class[] getClassesFromObjects(Object[] params) {
	return new Class[] { byte.class };
    }

    public Object getDefaultValue() {
	return new Byte((byte) 0);
    }

}
