package net.sourceforge.floggy.persistence.rms.beans.wrapper;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.TestByte;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class ByteTest extends AbstractTest {

    public Persistable newInstance() {
	return new TestByte();
    }

    public Object getValueForSetMethod() {
	return new Byte((byte) 23);
    }

}
