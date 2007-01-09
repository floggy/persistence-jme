package net.sourceforge.floggy.persistence.rms.beans.primitive.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.array.TestBoolean;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class BooleanTest extends AbstractTest {

    static boolean[] att = new boolean[] { true, false, true };

    public Persistable newInstance() {
	return new TestBoolean();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
