package net.sourceforge.floggy.persistence.rms.beans.primitive.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.array.TestLong;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class LongTest extends AbstractTest {

    static long[] att = new long[] { -23543452, 56, 89375934 };

    public Persistable newInstance() {
	return new TestLong();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
