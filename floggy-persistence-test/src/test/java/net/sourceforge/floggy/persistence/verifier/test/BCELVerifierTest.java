package net.sourceforge.floggy.persistence.verifier.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Properties;

import org.apache.bcel.Repository;
import org.apache.bcel.classfile.JavaClass;
import org.apache.bcel.classfile.Method;
import org.apache.bcel.util.ClassLoaderRepository;
import org.apache.bcel.util.ClassPath;
import org.apache.bcel.verifier.VerificationResult;
import org.apache.bcel.verifier.Verifier;
import org.apache.bcel.verifier.VerifierFactory;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public class BCELVerifierTest extends AbstractVerifierTest {

    protected void setUp() throws Exception {
	// ClassPath classPath= new ClassPath();
	// //ClassPath classPath= new ClassPath(loadClasspath());
	// System.out.println(classPath.toString());
	// Repository.setRepository(SyntheticRepository.getInstance(classPath));
	Repository.setRepository(new ClassLoaderRepository(this.getClass()
		.getClassLoader()));
    }

    private String loadClasspath() {
	String all = System.getProperty("dependencies");
	String localRepository = System.getProperty("localRepository");
	int end = 0;
	int start = 0;
	StringBuffer buffer = new StringBuffer();
	buffer.append(System.getProperty("testOutputDirectory"));
	buffer.append(File.pathSeparatorChar);
	buffer.append(ClassPath.getClassPath());
	buffer.append(File.pathSeparatorChar);
	do {
	    start = all.indexOf("Dependency {", start);
	    if (start == -1) {
		break;
	    }
	    start = start + 12;
	    end = all.indexOf("},", start);
	    if (end == -1) {
		end = all.length() - 2;
	    }
	    String property = all.substring(start, end);
	    property = property.replaceAll(",", "\n");
	    Properties properties = new Properties();
	    try {
		properties.load(new ByteArrayInputStream(property.getBytes()));
	    } catch (Exception e) {
	    }
	    buffer.append(getPath(properties, localRepository));
	    buffer.append(File.pathSeparatorChar);
	} while (end + 1 <= all.length());
	return buffer.toString();
    }

    private static String getPath(Properties properties, String localRepository) {
	StringBuffer buffer = new StringBuffer(localRepository);
	buffer.append(File.separatorChar);
	buffer.append(properties.getProperty("groupId").replace('.',
		File.separatorChar));
	buffer.append(File.separatorChar);
	buffer.append(properties.getProperty("artifactId"));
	buffer.append(File.separatorChar);
	buffer.append(properties.getProperty("version"));
	buffer.append(File.separatorChar);
	buffer.append(properties.getProperty("artifactId"));
	buffer.append('-');
	buffer.append(properties.getProperty("version"));
	buffer.append(".jar");
	return buffer.toString();
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
