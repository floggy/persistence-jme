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

package net.sourceforge.floggy.persistence.bug2903826;

import java.util.Enumeration;
import java.util.Vector;

import net.sourceforge.floggy.persistence.Deletable;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;

public class BUG2903826VectorHolder implements Persistable, Deletable {
	
	protected Vector vector;
	
	public Vector getVector() {
		return vector;
	}
	
	public void setVector(Vector vector) {
		this.vector = vector;
	}

	public void delete() throws FloggyException {
		if (vector != null) {
			Enumeration enumeration = vector.elements();
			while (enumeration.hasMoreElements()) {
				Object object = (Object) enumeration.nextElement();
				if (object instanceof Persistable) {
					PersistableManager.getInstance().delete((Persistable)object);
				}
			}
		}
	}

}
