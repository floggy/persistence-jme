package net.sourceforge.floggy.persistence.verifier.test;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.ClassLoaderRepository;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class BCELVerifierTest extends AbstractVerifierTest {

    protected void setUp() throws Exception {
	Repository.setRepository(new ClassLoaderRepository(this.getClass()
		.getClassLoader()));
    }

    protected void evaluate(String className, boolean createInstance) {
	Verifier verifier = VerifierFactory.getVerifier(className);

	// pass 1
	VerificationResult vr = verifier.doPass1();

	assertEquals(vr.getMessage(), vr, VerificationResult.VR_OK);

	// pass 2
	vr = verifier.doPass2();
	assertEquals(vr.getMessage(), vr, VerificationResult.VR_OK);

	if (vr == VerificationResult.VR_OK) {
	    JavaClass jc = Repository.lookupClass(className);
	    Method[] methods = jc.getMethods();

	    for (int i = 0; i < methods.length; i++) {
		// pass 3a
		vr = verifier.doPass3a(i);
		assertEquals("Method: " + methods[i].getName() + "\n"
			+ vr.getMessage(), vr, VerificationResult.VR_OK);

		// pass 3b
		vr = verifier.doPass3b(i);
		assertEquals(vr.getMessage(), vr, VerificationResult.VR_OK);
	    }
	}

	// avoid swapping.
	verifier.flush();
	Repository.clearCache();
	System.gc();
    }
}
