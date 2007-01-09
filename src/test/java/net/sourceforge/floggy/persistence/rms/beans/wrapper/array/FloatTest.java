package net.sourceforge.floggy.persistence.rms.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.array.TestFloat;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class FloatTest extends AbstractTest {

    static Float[] att = new Float[] { new Float(452.4354F),
	    new Float(56.345F), new Float(-3545.8989F) };

    public Persistable newInstance() {
	return new TestFloat();
    }

    public Object getValueForSetMethod() {
	return att;
    }

}
