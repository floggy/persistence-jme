package net.sourceforge.floggy.persistence.rms.beans.primitive;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.TestChar;

public class CharTest extends PrimitiveAbstractTest {

    public Persistable newInstance() {
	return new TestChar();
    }

    public Object getValueForSetMethod() {
	return new Character('>');
    }

    protected Class[] getClassesFromObjects(Object[] params) {
	return new Class[] { char.class };
    }

    public Object getDefaultValue() {
	return new Character('\u0000');
    }

}
