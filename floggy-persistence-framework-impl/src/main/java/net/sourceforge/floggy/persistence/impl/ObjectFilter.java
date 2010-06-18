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

import javax.microedition.rms.RecordFilter;

import net.sourceforge.floggy.persistence.Filter;

/**
 * An internal implementation of <code>RecordComparator</code> for comparing two objects.
 * 
 * @since 1.0
 */
class ObjectFilter implements RecordFilter {

	private final __Persistable p;

	private final Filter f;

	private final boolean lazy;

	ObjectFilter(__Persistable p, Filter f, boolean lazy) {
		this.p = p;
		this.f = f;
		this.lazy = lazy;
	}

	public boolean matches(byte[] b) {
		try {
			p.__deserialize(b, lazy);
		} catch (Exception e) {
			// Ignore
		}
		return f.matches(p);
	}
}
