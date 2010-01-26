/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
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

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.AbstractReflectionConverter;

import net.sourceforge.floggy.persistence.Configuration;
import net.sourceforge.floggy.persistence.Weaver;

import junit.framework.TestCase;

public class FR2937635Test extends TestCase {
	
	public void testInvalidXMLFileDuplicatedField() {
		File file = new File("target/test-classes/fr2937635/floggy-invalid-duplicated-field.xml");
		assertTrue(file.exists());
		
		Weaver weaver = new Weaver();
		
		XStream stream = weaver.getXStream();

		try {
			stream.fromXML(new FileInputStream(file));
			fail("It should throw a exception");
		} catch (Exception e) {
			assertEquals(AbstractReflectionConverter.DuplicateFieldException.class, e.getClass());
		}
	}

	public void testValidXMLFile() {
		File file = new File("target/test-classes/fr2937635/floggy-valid.xml");
		assertTrue(file.exists());
		
		Weaver weaver = new Weaver();
		
		XStream stream = weaver.getXStream();

		try {
			Configuration configuration = (Configuration) stream.fromXML(new FileInputStream(file));
			assertNotNull(configuration);
		} catch (Exception e) {
			fail(e.getMessage());
		}
	}
}
