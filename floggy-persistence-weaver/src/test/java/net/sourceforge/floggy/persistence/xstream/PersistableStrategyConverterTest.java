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

package net.sourceforge.floggy.persistence.xstream;

import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import junit.framework.TestCase;

public class PersistableStrategyConverterTest extends TestCase {

	public void testFromString() {
		PersistableStrategyConverter converter = new PersistableStrategyConverter();

		assertEquals(new Integer(PersistableMetadata.JOINED_STRATEGY),
				converter.fromString(PersistableStrategyConverter.JOINED));
		assertEquals(new Integer(PersistableMetadata.PER_CLASS_STRATEGY),
				converter.fromString(PersistableStrategyConverter.PER_CLASS));
		assertEquals(new Integer(PersistableMetadata.SINGLE_STRATEGY),
				converter.fromString(PersistableStrategyConverter.SINGLE));

		try {
			converter.fromString(null);
			fail("It should throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
			assertEquals(null, ex.getMessage());
		}

		String str = "test";
		try {
			converter.fromString(str);
			fail("It should throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
			assertEquals(str, ex.getMessage());
		}
	}
	
	public void testToString() {
		PersistableStrategyConverter converter = new PersistableStrategyConverter();

		assertEquals(PersistableStrategyConverter.JOINED,
				converter.toString(new Integer(PersistableMetadata.JOINED_STRATEGY)));
		assertEquals(PersistableStrategyConverter.PER_CLASS,
				converter.toString(new Integer(PersistableMetadata.PER_CLASS_STRATEGY)));
		assertEquals(PersistableStrategyConverter.SINGLE,
				converter.toString(new Integer(PersistableMetadata.SINGLE_STRATEGY)));

		try {
			converter.toString(null);
			fail("It should throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
			assertEquals(null, ex.getMessage());
		}

		Integer obj = new Integer(3);
		try {
			converter.toString(obj);
			fail("It should throw a IllegalArgumentException");
		} catch (Exception ex) {
			assertEquals(IllegalArgumentException.class, ex.getClass());
			assertEquals(obj.toString(), ex.getMessage());
		}
	}

}
