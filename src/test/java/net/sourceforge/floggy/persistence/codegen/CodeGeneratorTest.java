package net.sourceforge.floggy.persistence.codegen;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewConstructor;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.Weaver;

public class CodeGeneratorTest extends TestCase {
	
	private CtClass createClassWithOnlyOneConstructor(String className, String constructor) throws CannotCompileException, NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new LoaderClassPath(getClass().getClassLoader()));
		
		CtClass persitable=pool.get(Weaver.PERSISTABLE_CLASSNAME);
		CtClass cc = pool.makeClass(className);
		cc.addInterface(persitable);
		cc.addConstructor(CtNewConstructor.make(constructor, cc));
		return cc;
	}

	public void testAddDefaultConstructorFalseWithPrivateConstructor() throws Exception {
		CtClass cc= createClassWithOnlyOneConstructor("PrivateConstructor", "private PrivateConstructor(){}");
		CodeGenerator generator= new CodeGenerator(cc, false, false);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "+cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorTrueWithPrivateConstructor() throws Exception {
		CtClass cc= createClassWithOnlyOneConstructor("PrivateConstructor", "private PrivateConstructor(){}");
		CodeGenerator generator= new CodeGenerator(cc, false, true);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "+cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorFalseWithProtectedConstructor() throws Exception {
		CtClass cc= createClassWithOnlyOneConstructor("ProtectedConstructor", "protected ProtectedConstructor(){}");
		CodeGenerator generator= new CodeGenerator(cc, false, false);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "+cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorTrueWithProtectedConstructor() throws Exception {
		CtClass cc= createClassWithOnlyOneConstructor("ProtectedConstructor", "protected ProtectedConstructor(){}");
		CodeGenerator generator= new CodeGenerator(cc, false, true);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "+cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorFalseWithFriendlyConstructor() throws Exception {
		CtClass cc= createClassWithOnlyOneConstructor("FriendlyConstructor", "FriendlyConstructor(){}");
		CodeGenerator generator= new CodeGenerator(cc, false, false);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "+cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorTrueWithFriendlyConstructor() throws Exception {
		CtClass cc= createClassWithOnlyOneConstructor("FriendlyConstructor", "FriendlyConstructor(){}");
		CodeGenerator generator= new CodeGenerator(cc, false, true);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "+cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorFalseWithParametedConstructor() throws Exception {
		CtClass cc= createClassWithOnlyOneConstructor("ParametedConstructor", "public ParametedConstructor(int x){}");
		CodeGenerator generator= new CodeGenerator(cc, false, false);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "+cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorTrueWithParametedConstructor() throws Exception {
		CtClass cc= createClassWithOnlyOneConstructor("ParametedConstructor", "public ParametedConstructor(int x){}");
		CodeGenerator generator= new CodeGenerator(cc, false, true);
		try {
			generator.generateCode();
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

}
