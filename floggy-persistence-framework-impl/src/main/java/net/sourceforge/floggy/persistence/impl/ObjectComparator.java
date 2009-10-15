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

import javax.microedition.rms.RecordComparator;

import net.sourceforge.floggy.persistence.Comparator;

/**
 * An internal implementation of RecordComparator for comparing two objects.
 * 
 * @since 1.0
 */
class ObjectComparator implements RecordComparator {

	private final Comparator c;

	private final __Persistable p1;

	private final __Persistable p2;
	
	private final boolean lazy;

	public ObjectComparator(Comparator c, __Persistable p1, __Persistable p2, boolean lazy) {
		this.p1 = p1;
		this.p2 = p2;
		this.c = c;
		this.lazy = lazy;
	}

	public int compare(byte[] b1, byte[] b2) {
		try {
			p1.__deserialize(b1, lazy);
			p2.__deserialize(b2, lazy);
		} catch (Exception e) {
			// Ignore
		}

		return c.compare(p1, p2);
	}
}
