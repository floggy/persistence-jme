package net.sourceforge.floggy.persistence.rms.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.TestShort;

public class ShortTest extends PrimitiveAbstractTest {

    public Persistable newInstance() {
	return new TestShort();
    }

    public Object getValueForSetMethod() {
	return new Short((short) 23);
    }

    protected Class[] getClassesFromObjects(Object[] params) {
	return new Class[] { short.class };
    }

    public Object getDefaultValue() {
	return new Short((short) 0);
    }

}
