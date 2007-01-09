package net.sourceforge.floggy.persistence.rms.beans.primitive.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.primitive.array.TestChar;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class CharTest extends AbstractTest {

    static char[] att = new char[] { 'u', ',', 2, 56 };

    public Persistable newInstance() {
	return new TestChar();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
