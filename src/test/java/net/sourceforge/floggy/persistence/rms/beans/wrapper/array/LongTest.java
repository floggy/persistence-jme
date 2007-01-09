package net.sourceforge.floggy.persistence.rms.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.array.TestLong;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class LongTest extends AbstractTest {

    static Long[] att = new Long[] { new Long(-23543452), new Long(56),
	    new Long(89375934) };

    public Persistable newInstance() {
	return new TestLong();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
