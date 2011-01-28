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
package net.sourceforge.floggy.persistence.impl;

import org.jmock.Expectations;
import org.jmock.Mockery;

import junit.framework.TestCase;

import net.sourceforge.floggy.persistence.Comparator;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class ObjectComparatorTest extends TestCase {
	/**
	 * DOCUMENT ME!
	 */
	protected Mockery context = new Mockery();

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testCompare() throws Exception {
		final byte[] data = new byte[] {  };

		final __Persistable p1 = (__Persistable) context.mock(__Persistable.class);
		final __Persistable p2 = (__Persistable) context.mock(__Persistable.class);

		final Comparator comparator = (Comparator) context.mock(Comparator.class);

		ObjectComparator objectComparator =
			new ObjectComparator(comparator, p1, p2, false);

		context.checking(new Expectations() {

				{
					((__Persistable) one(p1)).__deserialize(data, false);
					((__Persistable) one(p2)).__deserialize(data, false);
					((Comparator) one(comparator)).compare(p1, p2);
					will(returnValue(new Integer(0)));
				}
			});

		assertEquals(0, objectComparator.compare(data, data));

		context.assertIsSatisfied();
	}
}
