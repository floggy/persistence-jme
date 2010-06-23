/**
 * Copyright (c) 2006-2010 Floggy Open Source Group. All rights reserved.
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

package net.sourceforge.floggy.persistence.impl;

import java.io.IOException;

import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;

public class UtilsTest extends TestCase {

	public static class Person implements Persistable {
	}

	public static class Person2 implements Persistable, __Persistable {
		public void __delete() throws FloggyException {
		}

		public void __deserialize(byte[] buffer, boolean lazy) throws Exception {
		}

		public int __getId() {
			return -1;
		}

		public Object __getIndexValue(String indexName) {
			return null;
		}

		public byte[] __serialize(boolean isRealObject) throws Exception {
			return null;
		}

		public void __setId(int id) {
		}

		public String getRecordStoreName() {
			return null;
		}
	}

	public void testCheckArgumentAndCast() {
		Persistable persistable = null;
		try {
			Utils.checkArgumentAndCast(persistable);
			fail("It should throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}

		persistable = new Person();

		try {
			Utils.checkArgumentAndCast(persistable);
			fail("It should throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}

		persistable = new Person2();

		try {
			__Persistable persistable2 = Utils
					.checkArgumentAndCast(persistable);
			assertSame(persistable, persistable2);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	public void testCreateInstance() {
		Class persistableClass = null;
		try {
			Utils.createInstance(persistableClass);
			fail("It should throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}

		persistableClass = Person.class;

		try {
			Utils.createInstance(persistableClass);
			fail("It should throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}

		persistableClass = Person2.class;

		try {
			__Persistable persistable2 = Utils.createInstance(persistableClass);
			assertEquals(persistableClass, persistable2.getClass());
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	public void testHandleException() {
		Exception expected = new FloggyException("test");
		Exception actual = Utils.handleException(expected);
		assertSame(expected, actual);

		expected = new IOException("test2");
		actual = Utils.handleException(expected);
		assertEquals(expected.getMessage(), actual.getMessage());

		expected = new IOException();
		actual = Utils.handleException(expected);
		assertEquals(expected.getClass().getName(), actual.getMessage());
	}

	public void testReadUTF8() throws Exception {
		String expected = "";
		FloggyOutputStream fos = new FloggyOutputStream();
		fos.writeUTF(expected);

		String actual = Utils.readUTF8(fos.toByteArray());
		assertEquals(expected, actual);

		expected = "1";
		fos.reset();

		fos.writeUTF(expected);
		actual = Utils.readUTF8(fos.toByteArray());
		assertEquals(expected, actual);

		expected = "1floggy_jsdhaskdjkfh kasjdhlf alskjdhfkajsdhf_";
		fos.reset();

		fos.writeUTF(expected);
		actual = Utils.readUTF8(fos.toByteArray());
		assertEquals(expected, actual);
	}

	public void testValidatePersistableClassArgument() {
		Class persistableClass = null;
		try {
			Utils.validatePersistableClassArgument(persistableClass);
			fail("It should throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}

		persistableClass = Person.class;

		try {
			Utils.validatePersistableClassArgument(persistableClass);
			fail("It should throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
		}

		persistableClass = Person2.class;

		try {
			Utils.validatePersistableClassArgument(persistableClass);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	public void testIsEmpty() {
		String str = null;
		assertTrue(Utils.isEmpty(str));

		str = "     	";
		assertTrue(Utils.isEmpty(str));

		str = "    test	";
		assertFalse(Utils.isEmpty(str));
	}

}
