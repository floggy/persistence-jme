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

import javax.microedition.rms.RecordComparator;

import net.sourceforge.floggy.persistence.internal.__Persistable;

/**
 * An internal implementation of RecordComparator for comparing two objects.
 * 
 * @since 1.0
 */
class ObjectComparator implements RecordComparator {

	private final Comparator c;

	private final Persistable p1;

	private final Persistable p2;

	public ObjectComparator(Comparator c, Persistable p1, Persistable p2) {
		this.p1 = p1;
		this.p2 = p2;
		this.c = c;
	}

	public int compare(byte[] b1, byte[] b2) {
		try {
			((__Persistable) p1).__load(b1);
			((__Persistable) p2).__load(b2);
		} catch (Exception e) {
			// Ignore
		}

		return c.compare(p1, p2);
	}
}
