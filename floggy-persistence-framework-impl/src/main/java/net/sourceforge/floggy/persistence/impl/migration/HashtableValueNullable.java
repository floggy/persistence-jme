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
package net.sourceforge.floggy.persistence.impl.migration;

import java.util.Enumeration;
import java.util.Hashtable;

public class HashtableValueNullable extends Hashtable {

	protected static final Object NULL = new Object();

	public synchronized Object put(Object key, Object value) {
		if (value == null) {
			value = NULL;
		}
		return super.put(key, value);
	}

	public synchronized Object get(Object key) {
		Object value = super.get(key);
		if (value == NULL) {
			value = null;
		}
		return value;
	}
	
	public synchronized String toString() {
		int max = size() - 1;
		if (max == -1)
		    return "{}";

		StringBuffer sb = new StringBuffer();
		Enumeration keys = keys();

		sb.append('{');
		while(keys.hasMoreElements()) {
			Object key = keys.nextElement();
            Object value = get(key);
	            
            sb.append(key == this ? "(this Map)" : key.toString());
		    sb.append('=');
		    if (value == null) {
			    sb.append("null");
		    } else {
			    sb.append(value == this ? "(this Map)" : value.toString());
		    }

		    sb.append(", ");
		}
		return sb.append('}').toString();
	}
	
}
