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
package net.sourceforge.floggy.persistence.beans.animals;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class Falcon extends Bird {
	private Boolean x;

	/**
	 * DOCUMENT ME!
	*
	* @param obj DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (!super.equals(obj))
			return false;

		if (getClass() != obj.getClass())
			return false;

		final Falcon other = (Falcon) obj;

		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;

		return true;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Boolean getX() {
		return x;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((x == null) ? 0 : x.hashCode());

		return result;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param prey DOCUMENT ME!
	*/
	public void setX(Boolean prey) {
		this.x = prey;
	}
}
