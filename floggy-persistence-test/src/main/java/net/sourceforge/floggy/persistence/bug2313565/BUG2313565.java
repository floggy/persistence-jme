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

package net.sourceforge.floggy.persistence.bug2313565;

import net.sourceforge.floggy.persistence.Deletable;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.beans.animals.Bird;

public class BUG2313565 implements Persistable, Deletable {
	public boolean w;

	protected Bird x;

	public long y;

	public Bird getX() {
		return x;
	}

	public void setX(Bird x) {
		this.x = x;
	}

	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + (w ? 1231 : 1237);
		result = PRIME * result + ((x == null) ? 0 : x.hashCode());
		result = PRIME * result + (int) (y ^ (y >>> 32));
		return result;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final BUG2313565 other = (BUG2313565) obj;
		if (w != other.w)
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y != other.y)
			return false;
		return true;
	}
	
	public void delete() throws FloggyException {
		if (x != null) {
			PersistableManager.getInstance().delete(x);
		}
	}

}
