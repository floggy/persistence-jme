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
package net.sourceforge.floggy.persistence.fr2937635;

import java.io.File;
import java.io.FileInputStream;

import javassist.ClassPool;

import com.thoughtworks.xstream.XStream;

import junit.framework.TestCase;

import net.sourceforge.floggy.persistence.Configuration;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Weaver;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FR2937635Test extends TestCase {
	/**
	 * DOCUMENT ME!
	*/
	public void testDoesNotExistField() {
		File file =
			new File("target/test-classes/fr2937635/floggy-does-not-exist-field.xml");
		assertTrue(file.exists());

		Weaver weaver = new Weaver();

		String className = FR2937635.class.getName();
		String[] fieldNames = new String[] { "name" };
		Configuration c1 = new Configuration();
		PersistableMetadata metadata =
			new PersistableMetadata(false, className, null, fieldNames, null, null,
				null, null, 0, null);

		c1.addPersistableMetadata(metadata);

		XStream stream = weaver.getXStream();

		try {
			Configuration c2 =
				(Configuration) stream.fromXML(new FileInputStream(file));
			weaver.mergeConfigurations(c1, c2);
			fail("It should throw a exception");
		} catch (Exception e) {
			assertEquals(FloggyException.class, e.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeByte() {
		validateIndexFieldType("byte");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeChar() {
		validateIndexFieldType("char");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeDouble() {
		validateIndexFieldType("double");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeInt() {
		validateIndexFieldType("int");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeJavaLangByte() {
		validateIndexFieldType("java.lang.Byte");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeJavaLangChar() {
		validateIndexFieldType("java.lang.Character");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeJavaLangDouble() {
		validateIndexFieldType("java.lang.Double");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeJavaLangInteger() {
		validateIndexFieldType("java.lang.Integer");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeJavaLangLong() {
		validateIndexFieldType("java.lang.Long");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeJavaLangShort() {
		validateIndexFieldType("java.lang.Short");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeJavaLangString() {
		validateIndexFieldType("java.lang.String");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeJavaLangStringBuffer() {
		validateIndexFieldType("java.lang.StringBuffer");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeJavaUtilDate() {
		validateIndexFieldType("java.util.Date");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeJavaUtilTimezone() {
		validateIndexFieldType("java.util.TimeZone");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeLong() {
		validateIndexFieldType("long");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidIndexFieldTypeShort() {
		validateIndexFieldType("short");
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testValidXMLFile() {
		File file = new File("target/test-classes/fr2937635/floggy-valid.xml");
		assertTrue(file.exists());

		Weaver weaver = new Weaver();

		XStream stream = weaver.getXStream();

		try {
			Configuration configuration =
				(Configuration) stream.fromXML(new FileInputStream(file));
			assertNotNull(configuration);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param type DOCUMENT ME!
	*/
	protected void validateIndexFieldType(String type) {
		File file =
			new File("target/test-classes/fr2937635/floggy-valid-field-type-" + type
				+ ".xml");
		assertTrue(file.exists());

		try {
			ClassPool classPool = new ClassPool(true);
			classPool.appendClassPath("target/test-classes");
			classPool.appendClassPath(
				"../floggy-persistence-framework/target/classes");

			Weaver weaver = new Weaver(classPool);

			PersistableMetadata metadata =
				weaver.createPersistableMetadata(classPool.get(
						FR2937635.class.getName()));
			Configuration c1 = new Configuration();
			c1.addPersistableMetadata(metadata);

			XStream stream = weaver.getXStream();

			Configuration c2 =
				(Configuration) stream.fromXML(new FileInputStream(file));
			weaver.mergeConfigurations(c1, c2);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
