package net.sourceforge.floggy.persistence.rms.beans.primitive.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.array.TestShort;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class ShortTest extends AbstractTest {

    static short[] att = new short[] { 2, 56 };

    public Persistable newInstance() {
	return new TestShort();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
