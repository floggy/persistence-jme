/**
 * Copyright (c) 2006-2011 Floggy Open Source Group. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import net.sourceforge.floggy.persistence.Weaver;
import net.sourceforge.floggy.persistence.codegen.strategy.JoinedStrategyCodeGenerator;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class CodeGeneratorTest extends TestCase {
	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAddDefaultConstructorFalseWithFriendlyConstructor()
		throws Exception {
		CtClass cc =
			createClassWithOnlyOneConstructor("FriendlyConstructor",
				"FriendlyConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, false);

		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "
				+ cc.getName(), e.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAddDefaultConstructorFalseWithParametedConstructor()
		throws Exception {
		CtClass cc =
			createClassWithOnlyOneConstructor("ParametedConstructor",
				"public ParametedConstructor(int x){}");
		CodeGenerator generator = createCodeGenerator(cc, false);

		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "
				+ cc.getName(), e.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAddDefaultConstructorFalseWithPrivateConstructor()
		throws Exception {
		CtClass cc =
			createClassWithOnlyOneConstructor("PrivateConstructor",
				"private PrivateConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, false);

		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "
				+ cc.getName(), e.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAddDefaultConstructorFalseWithProtectedConstructor()
		throws Exception {
		CtClass cc =
			createClassWithOnlyOneConstructor("ProtectedConstructor",
				"protected ProtectedConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, false);

		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "
				+ cc.getName(), e.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAddDefaultConstructorTrueWithFriendlyConstructor()
		throws Exception {
		CtClass cc =
			createClassWithOnlyOneConstructor("FriendlyConstructor",
				"FriendlyConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, true);

		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "
				+ cc.getName(), e.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAddDefaultConstructorTrueWithParametedConstructor()
		throws Exception {
		CtClass cc =
			createClassWithOnlyOneConstructor("ParametedConstructor",
				"public ParametedConstructor(int x){}");
		CodeGenerator generator = createCodeGenerator(cc, true);

		try {
			generator.generateCode();
			assertTrue(true);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAddDefaultConstructorTrueWithPrivateConstructor()
		throws Exception {
		CtClass cc =
			createClassWithOnlyOneConstructor("PrivateConstructor",
				"private PrivateConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, true);

		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "
				+ cc.getName(), e.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAddDefaultConstructorTrueWithProtectedConstructor()
		throws Exception {
		CtClass cc =
			createClassWithOnlyOneConstructor("ProtectedConstructor",
				"protected ProtectedConstructor(){}");
		CodeGenerator generator = createCodeGenerator(cc, true);

		try {
			generator.generateCode();
			fail();
		} catch (Exception e) {
			assertEquals("You must provide a public default constructor to class: "
				+ cc.getName(), e.getMessage());
		}
	}

	private CtClass createClassWithOnlyOneConstructor(String className,
		String constructor) throws CannotCompileException, NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new LoaderClassPath(getClass().getClassLoader()));

		CtClass persitable = pool.get(Weaver.PERSISTABLE_CLASSNAME);
		CtClass cc = pool.makeClass(className);
		cc.addInterface(persitable);
		cc.addConstructor(CtNewConstructor.make(constructor, cc));

		return cc;
	}

	private CodeGenerator createCodeGenerator(CtClass ctClass,
		boolean addDefaultConstructor) throws Exception {
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new LoaderClassPath(getClass().getClassLoader()));

		Configuration configuration = new Configuration();
		PersistableMetadata metadata =
			new PersistableMetadata(false, ctClass.getName(), null, null, null, null,
				null, ctClass.getSimpleName(), PersistableMetadata.JOINED_STRATEGY, null);

		configuration.addPersistableMetadata(metadata);
		configuration.setAddDefaultConstructor(addDefaultConstructor);

		return new JoinedStrategyCodeGenerator(ctClass, pool, configuration);
	}
}
