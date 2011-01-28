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

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.util.Vector;

public class IndexEntry {

	protected int recordId = -1;
	protected Object value;
	protected Vector persistableIds = new Vector();

	public IndexEntry(int recordId) {
		this(recordId, null);
	}

	public IndexEntry(int recordId, Object value) {
		this.recordId = recordId;
		this.value = value;
	}

	public IndexEntry(Object value) {
		this(-1, value);
	}

	public void deserialize(byte[] buffer) throws Exception {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(
				buffer));
		setValue(SerializationManager.readObject(dis, false));
		persistableIds = SerializationManager.readIntVector(dis);
		dis.close();
	}

	public Vector getPersistableIds() {
		return persistableIds;
	}

	public int getRecordId() {
		return recordId;
	}

	public Object getValue() {
		return value;
	}

	public void serialize(FloggyOutputStream fos) throws Exception {
		SerializationManager.writeObject(fos, getValue());
		SerializationManager.writeIntVector(fos, getPersistableIds());
	}

	public void setRecordId(int id) {
		this.recordId = id;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String toString() {
		return "IndexEntry [recordId=" + recordId + ", persistableIds="
				+ persistableIds + ", value=" + value + "]";
	}
}