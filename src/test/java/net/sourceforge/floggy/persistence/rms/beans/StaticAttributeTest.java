package net.sourceforge.floggy.persistence.rms.beans;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyStaticAttribute;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class StaticAttributeTest extends AbstractTest {

    static final Object object = new Object();

    public Persistable newInstance() {
	return new FloggyStaticAttribute();
    }

    public void testNotNullAttribute() throws Exception {
	// como o atributo não vai ser salvo ele tem q retornar null!!!
	testNullAttribute();
    }

    public Object getValueForSetMethod() {
	return object;
    }

    protected void tearDown() throws Exception {
	FloggyStaticAttribute.x = null;
    }

}
