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
package net.sourceforge.floggy.persistence.fr2886470;

import java.io.File;
import java.io.FileInputStream;

import com.thoughtworks.xstream.XStream;

import junit.framework.TestCase;

import net.sourceforge.floggy.persistence.Configuration;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Weaver;
import net.sourceforge.floggy.persistence.fr2937635.FR2937635;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FR2886470Test extends TestCase {
	/**
	 * DOCUMENT ME!
	*/
	public void testDoesNotExistSuiteNameAttribute() {
		File file =
			new File(
				"target/test-classes/fr2886470/valid-vendor-name-missing-suite-name.xml");
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
	public void testDoesNotExistVendorNameAttribute() {
		File file =
			new File(
				"target/test-classes/fr2886470/valid-suite-name-missing-vendor-name.xml");
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
	public void testValidSuiteNameAndVendorNameAttribute() {
		File file =
			new File(
				"target/test-classes/fr2886470/valid-vendor-name-and-suite-name.xml");
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
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
