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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;


public class Index {

	protected Hashtable valueIds = new Hashtable();
	protected Hashtable idValue = new Hashtable();

	public boolean containsId(int id) {
		return idValue.containsKey(new Integer(id));
	}

	public boolean containsValue(Object key) {
		return valueIds.containsKey(key);
	}

	public void clear() {
		idValue.clear();
		valueIds.clear();
	}

	public int[] getIds(Object value) {
		if (value instanceof String) {
			String stringValue = (String) value;
			Vector returnIds = new Vector();
			Enumeration values = valueIds.keys();
			while (values.hasMoreElements()) {
				String temp = (String) values.nextElement();
				if (temp.indexOf(stringValue) != -1) {
					IndexEntry indexEntry = (IndexEntry) valueIds.get(temp);
					if (indexEntry != null) {
						Vector ids = indexEntry.getPersistableIds();
						for (int i = 0; i < ids.size(); i++) {
							returnIds.addElement(ids.elementAt(i));
						}
					}
				}
			}
			int[] temp = new int[returnIds.size()];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = ((Integer) returnIds.elementAt(i)).intValue();
			}
			return temp;
		} else {
			IndexEntry indexEntry = (IndexEntry) valueIds.get(value);
			if (indexEntry != null) {
				Vector objectsIds = indexEntry.getPersistableIds();
				int[] temp = new int[objectsIds.size()];
				for (int i = 0; i < temp.length; i++) {
					temp[i] = ((Integer) objectsIds.elementAt(i)).intValue();
				}
				return temp;
			}
		}
		return new int[0];
	}

	public IndexEntry getIndexEntry(int id) {
		Integer idAux = new Integer(id);
		Object value = idValue.get(idAux);
		if (value == null) {
			return null;
		} else {
			return (IndexEntry) valueIds.get(value);
		}
	}

	public IndexEntry getIndexEntry(Object value) {
		return (IndexEntry) valueIds.get(value);
	}

	public void put(IndexEntry indexEntry) {
		valueIds.put(indexEntry.getValue(), indexEntry);

		for (int i = 0; i < indexEntry.getPersistableIds().size(); i++) {
			Integer idAux = (Integer) indexEntry.getPersistableIds()
					.elementAt(i);
			idValue.put(idAux, indexEntry.getValue());
		}
	}

	public void put(int id, Object value) {
		Integer idAux = new Integer(id);
		Object valueAux = idValue.get(idAux);
		
		if (valueAux != null) {
			IndexEntry indexEntry = (IndexEntry) valueIds.get(valueAux);
			indexEntry.getPersistableIds().removeElement(idAux);
		}
		IndexEntry indexEntry;
		if (valueIds.containsKey(value)) {
			indexEntry = (IndexEntry) valueIds.get(value);
		} else {
			indexEntry = new IndexEntry(value);
			valueIds.put(value, indexEntry);
		}
		indexEntry.getPersistableIds().addElement(idAux);
		idValue.put(idAux, value);
	}

	public void remove(int id) {
		Integer idAux = new Integer(id);
		Object valueAux = idValue.get(idAux);
		IndexEntry indexEntry = (IndexEntry) valueIds.get(valueAux);
		indexEntry.getPersistableIds().removeElement(idAux);
		idValue.remove(idAux);
	}

	public String toString() {
		return "Index [valueIds=" + valueIds + ", idValue=" + idValue + "]";
	}
}