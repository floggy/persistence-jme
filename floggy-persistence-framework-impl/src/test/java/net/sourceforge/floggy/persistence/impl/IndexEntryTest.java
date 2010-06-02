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

package net.sourceforge.floggy.persistence.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Date;
import java.util.Vector;

import junit.framework.TestCase;

public class IndexEntryTest extends TestCase {
	
	public void testDeserialize() throws Exception {
		IndexEntry indexEntry = new IndexEntry(-1);

		Date date = new Date();
		indexEntry.setValue(date);

		Vector ids = indexEntry.getPersistableIds();
		ids.add(new Integer(34));
		ids.add(new Integer(209));
		ids.add(new Integer(334));
		ids.add(new Integer(Integer.MAX_VALUE));

		FloggyOutputStream out = new FloggyOutputStream();
		indexEntry.serialize(out);
		
		DataInputStream in = new DataInputStream(new ByteArrayInputStream(out.toByteArray()));
		assertEquals(date, SerializationManager.readObject(in, false));
		assertEquals(ids, SerializationManager.readIntVector(in));
		
	}

	public void testSerialize() throws Exception {
		Date date = new Date();
		Vector ids = new Vector();
		ids.add(new Integer(34));
		ids.add(new Integer(209));
		ids.add(new Integer(334));
		ids.add(new Integer(Integer.MAX_VALUE));

		FloggyOutputStream out = new FloggyOutputStream();
		SerializationManager.writeObject(out, date);
		SerializationManager.writeIntVector(out, ids);
		
		IndexEntry indexEntry = new IndexEntry(-1);
		indexEntry.deserialize(out.toByteArray());
		
		assertEquals(date, indexEntry.getValue());
		assertEquals(ids, indexEntry.getPersistableIds());
		
	}

}
