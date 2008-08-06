/**
 *  Copyright (c) 2005-2008 Floggy Open Source Group. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sourceforge.floggy.persistence.codegen;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtNewConstructor;
import javassist.LoaderClassPath;
import javassist.NotFoundException;
import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.Configuration;
import net.sourceforge.floggy.persistence.PersistableConfiguration;
import net.sourceforge.floggy.persistence.Weaver;

public class CodeGeneratorTest extends TestCase {

	private CtClass createClassWithOnlyOneConstructor(String className,
			String constructor) throws CannotCompileException,
			NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new LoaderClassPath(getClass().getClassLoader()));

		CtClass persitable = pool.get(Weaver.PERSISTABLE_CLASSNAME);
		CtClass cc = pool.makeClass(className);
		cc.addInterface(persitable);
		cc.addConstructor(CtNewConstructor.make(constructor, cc));
		return cc;
	}

	private CodeGenerator createCodeGenerator(CtClass ctClass,
			boolean addDefaultConstructor) {
		Configuration configuration = new Configuration();
		PersistableConfiguration pConfig = new PersistableConfiguration();
		pConfig.setClassName(ctClass.getName());
		pConfig.setRecordStoreName(ctClass.getSimpleName());
		configuration.addPersistable(pConfig);
		configuration.setAddDefaultConstructor(addDefaultConstructor);
		return new CodeGenerator(ctClass, configuration);
	}

	public void testAddDefaultConstructorFalseWithFriendlyConstructor()
			throws Exception {
		CtClass cc = createClassWithOnlyOneConstructor("FriendlyConstructor",
				"FriendlyConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, false);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals(
					"You must provide a public default constructor to class: "
							+ cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorFalseWithParametedConstructor()
			throws Exception {
		CtClass cc = createClassWithOnlyOneConstructor("ParametedConstructor",
				"public ParametedConstructor(int x){}");
		CodeGenerator generator = createCodeGenerator(cc, false);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals(
					"You must provide a public default constructor to class: "
							+ cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorFalseWithPrivateConstructor()
			throws Exception {
		CtClass cc = createClassWithOnlyOneConstructor("PrivateConstructor",
				"private PrivateConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, false);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals(
					"You must provide a public default constructor to class: "
							+ cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorFalseWithProtectedConstructor()
			throws Exception {
		CtClass cc = createClassWithOnlyOneConstructor("ProtectedConstructor",
				"protected ProtectedConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, false);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals(
					"You must provide a public default constructor to class: "
							+ cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorTrueWithFriendlyConstructor()
			throws Exception {
		CtClass cc = createClassWithOnlyOneConstructor("FriendlyConstructor",
				"FriendlyConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, true);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals(
					"You must provide a public default constructor to class: "
							+ cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorTrueWithParametedConstructor()
			throws Exception {
		CtClass cc = createClassWithOnlyOneConstructor("ParametedConstructor",
				"public ParametedConstructor(int x){}");
		CodeGenerator generator = createCodeGenerator(cc, true);
		try {
			generator.generateCode();
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	public void testAddDefaultConstructorTrueWithPrivateConstructor()
			throws Exception {
		CtClass cc = createClassWithOnlyOneConstructor("PrivateConstructor",
				"private PrivateConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, true);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals(
					"You must provide a public default constructor to class: "
							+ cc.getName(), e.getMessage());
		}
	}

	public void testAddDefaultConstructorTrueWithProtectedConstructor()
			throws Exception {
		CtClass cc = createClassWithOnlyOneConstructor("ProtectedConstructor",
				"protected ProtectedConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, true);
		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals(
					"You must provide a public default constructor to class: "
							+ cc.getName(), e.getMessage());
		}
	}

}
