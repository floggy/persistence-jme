/**
 *  Copyright 2006 Floggy Open Source Group
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sourceforge.floggy.persistence;

import javax.microedition.rms.RecordFilter;

/**
 * An internal implementation of <code>RecordComparator</code> for comparing two objects.
 * 
 * @since 1.0
 */
class ObjectFilter implements RecordFilter {

	private final Persistable p;

	private final Filter f;

	ObjectFilter(Persistable p, Filter f) {
		this.p = p;
		this.f = f;
	}

	public boolean matches(byte[] b) {
		try {
			((__Persistable) this.p).__load(b);
		} catch (Exception e) {
			// Ignore
		}
		return f.matches(p);
	}
}
