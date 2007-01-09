package net.sourceforge.floggy.persistence.rms.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.array.TestByte;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class ByteTest extends AbstractTest {

    static Byte[] att = new Byte[] { new Byte((byte) -128), new Byte((byte) 2),
	    new Byte((byte) 56) };

    public Persistable newInstance() {
	return new TestByte();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
