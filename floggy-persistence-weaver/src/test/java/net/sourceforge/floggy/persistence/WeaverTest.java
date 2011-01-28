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
package net.sourceforge.floggy.persistence;

import junit.framework.TestCase;

import net.sourceforge.floggy.persistence.impl.PersistableMetadata;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class WeaverTest extends TestCase {
	/**
	 * DOCUMENT ME!
	*/
	public void testCreatePersistableMetadata() {
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testMerge() {
		String className = "test";
		String recordStoreName = "Floggy";
		int persistableStrategy = 2;
		Configuration c1 = new Configuration();
		PersistableMetadata metadata1 =
			new PersistableMetadata(false, className, null, null, null, null, null,
				null, persistableStrategy, null);

		c1.addPersistableMetadata(metadata1);

		Configuration c2 = new Configuration();
		PersistableMetadata metadata2 =
			new PersistableMetadata(false, className, null, null, null, null, null,
				recordStoreName, 0, null);

		c2.addPersistableMetadata(metadata2);

		Weaver weaver = new Weaver();

		try {
			weaver.mergeConfigurations(c1, c2);
		} catch (Exception e) {
			fail(e.getMessage());
		}

		metadata1 = c1.getPersistableMetadata(className);

		assertEquals(recordStoreName, metadata1.getRecordStoreName());
		assertEquals(persistableStrategy, metadata1.getPersistableStrategy());
	}
}
