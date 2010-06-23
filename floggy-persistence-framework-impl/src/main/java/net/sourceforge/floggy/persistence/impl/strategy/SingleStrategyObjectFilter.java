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

package net.sourceforge.floggy.persistence.impl.strategy;

import javax.microedition.rms.RecordFilter;

import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.impl.Utils;
import net.sourceforge.floggy.persistence.impl.__Persistable;

/**
 * An internal implementation of <code>RecordComparator</code> for comparing two
 * objects.
 * 
 * @since 1.4.0
 */
public class SingleStrategyObjectFilter implements RecordFilter {

	private final __Persistable persistable;
	private final String className;
	private final Filter filter;
	private final boolean lazy;

	public SingleStrategyObjectFilter(__Persistable persistable, Filter filter,
			boolean lazy) {
		this.persistable = persistable;
		this.className = persistable.getClass().getName();
		this.filter = filter;
		this.lazy = lazy; 
	}

	public SingleStrategyObjectFilter(__Persistable persistable, boolean lazy) {
		this.persistable = persistable;
		this.className = persistable.getClass().getName();
		this.filter = null;
		this.lazy = lazy;
	}

	public boolean matches(byte[] data) {
		try {
			String className = Utils.readUTF8(data);
			if (!this.className.equals(className)) {
				return false;
			}
			if (filter == null) {
				return true;
			}
			persistable.__deserialize(data, lazy);
		} catch (Exception e) {
			// Ignore
		}
		return filter.matches(persistable);
	}
}
