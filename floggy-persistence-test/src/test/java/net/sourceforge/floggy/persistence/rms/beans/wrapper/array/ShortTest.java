package net.sourceforge.floggy.persistence.rms.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.array.TestShort;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class ShortTest extends AbstractTest {

    static Short[] att = new Short[] { new Short((short) 2),
	    new Short((short) 56) };

    public Persistable newInstance() {
	return new TestShort();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
