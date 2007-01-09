package net.sourceforge.floggy.persistence.rms.beans.primitive.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.array.TestInt;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class IntTest extends AbstractTest {

    static int[] att = new int[] { -2345, -34576, 2, 56 };

    public Persistable newInstance() {
	return new TestInt();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
