package net.sourceforge.floggy.persistence.verifier.test;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class NativeVerifierTest extends AbstractVerifierTest {
    protected void evaluate(String className, boolean createInstance) {
	Class clazz = null;
	try {
	    clazz = Class.forName(className);
	    assertTrue(true);
	} catch (Exception e) {
	    fail(e.getMessage());
	    return;
	}

	if (createInstance) {
	    try {
		clazz.newInstance();
		assertTrue(true);
	    } catch (Exception e) {
		fail(e.getMessage());
		return;
	    }
	}
    }
}
