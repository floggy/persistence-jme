package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyStaticAttributeArray;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class StaticAttributeArrayTest extends AbstractTest {

    public Persistable newInstance() {
	return new FloggyStaticAttributeArray();
    }

    public void testNotNullAttribute() throws Exception {
	// como o atributo não vai ser salvo ele tem q retornar null!!!
	super.testNullAttribute();
    }

    public Object getValueForSetMethod() {
	return new Object[] { "floggy-framework", TransientTest.object };
    }

    protected void tearDown() throws Exception {
	FloggyStaticAttributeArray.x = null;
    }

}
