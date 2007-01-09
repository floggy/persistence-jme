package net.sourceforge.floggy.persistence.rms.beans;

import java.util.Vector;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyVectorArray;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class VectorArrayTest extends AbstractTest {

    public Persistable newInstance() {
	return new FloggyVectorArray();
    }

    public Object getValueForSetMethod() {
	return new Vector[] { new Vector(), null, VectorTest.vector };
    }

}
