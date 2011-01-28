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

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class IndexEntry {
	/**
	 * DOCUMENT ME!
	 */
	protected Object value;

	/**
	 * DOCUMENT ME!
	 */
	protected Vector persistableIds = new Vector();

	/**
	 * DOCUMENT ME!
	 */
	protected int recordId = -1;

	/**
	 * Creates a new IndexEntry object.
	 *
	 * @param recordId DOCUMENT ME!
	 */
	public IndexEntry(int recordId) {
		this(recordId, null);
	}

	/**
	 * Creates a new IndexEntry object.
	 *
	 * @param recordId DOCUMENT ME!
	 * @param value DOCUMENT ME!
	 */
	public IndexEntry(int recordId, Object value) {
		this.recordId = recordId;
		this.value = value;
	}

	/**
	 * Creates a new IndexEntry object.
	 *
	 * @param value DOCUMENT ME!
	 */
	public IndexEntry(Object value) {
		this(-1, value);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param buffer DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void deserialize(byte[] buffer) throws Exception {
		DataInputStream dis = new DataInputStream(new ByteArrayInputStream(buffer));
		setValue(SerializationManager.readObject(dis, false));
		persistableIds = SerializationManager.readIntVector(dis);
		dis.close();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Vector getPersistableIds() {
		return persistableIds;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getRecordId() {
		return recordId;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Object getValue() {
		return value;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param fos DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void serialize(FloggyOutputStream fos) throws Exception {
		SerializationManager.writeObject(fos, getValue());
		SerializationManager.writeIntVector(fos, getPersistableIds());
	}

	/**
	 * DOCUMENT ME!
	*
	* @param id DOCUMENT ME!
	*/
	public void setRecordId(int id) {
		this.recordId = id;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param value DOCUMENT ME!
	*/
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String toString() {
		return "IndexEntry [recordId=" + recordId + ", persistableIds="
		+ persistableIds + ", value=" + value + "]";
	}
}
