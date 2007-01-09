package net.sourceforge.floggy.persistence.rms.beans.primitive.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.array.TestByte;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class ByteTest extends AbstractTest {

    static byte[] att = new byte[] { -128, 2, 56 };

    public Persistable newInstance() {
	return new TestByte();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
