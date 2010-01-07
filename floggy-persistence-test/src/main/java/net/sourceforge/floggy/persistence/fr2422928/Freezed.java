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
package net.sourceforge.floggy.persistence.fr2422928;

import java.util.Date;

import net.sourceforge.floggy.persistence.Deletable;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class Freezed implements Persistable, Deletable {
	public static final Date DEADLINE = new Date(987654321);
	public static final String DESCRIPTION =
		"This is sample class that is freezed between versions!";
	public static final Freezed NESTED = new Freezed();
	public static final String UUID = "2573882d-163c-11dd-98c7-b9f43dcf5330";
	public static final short CODE = (short) 56734;

	/**
	 * DOCUMENT ME!
	 */
	protected Date deadline;

	/**
	 * DOCUMENT ME!
	 */
	protected Freezed nested;

	/**
	 * DOCUMENT ME!
	 */
	protected String description;

	/**
	 * DOCUMENT ME!
	 */
	protected String uuid;

	/**
	 * DOCUMENT ME!
	 */
	protected short code;

	/**
	 * DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public void delete() throws FloggyException {
		if (nested != null) {
			PersistableManager.getInstance().delete(nested);
		}
	}

	/**
	* 
	* @see java.lang.Object#equals(java.lang.Object)
	*/
	public boolean equals(Object obj) {
		if (this == obj)
			return true;

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		final Freezed other = (Freezed) obj;

		if (code != other.code)
			return false;

		if (deadline == null) {
			if (other.deadline != null)
				return false;
		} else if (!deadline.equals(other.deadline))
			return false;

		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;

		if (nested == null) {
			if (other.nested != null)
				return false;
		} else if (!nested.equals(other.nested))
			return false;

		if (uuid == null) {
			if (other.uuid != null)
				return false;
		} else if (!uuid.equals(other.uuid))
			return false;

		return true;
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @return the code
	*/
	public short getCode() {
		return code;
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @return the deadline
	*/
	public Date getDeadline() {
		return deadline;
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @return the description
	*/
	public String getDescription() {
		return description;
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @return the nested
	*/
	public Freezed getNested() {
		return nested;
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @return the uuid
	*/
	public String getUuid() {
		return uuid;
	}

	/**
	* 
	* @see java.lang.Object#hashCode()
	*/
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + code;
		result = (prime * result) + ((deadline == null) ? 0 : deadline.hashCode());
		result = (prime * result)
			+ ((description == null) ? 0 : description.hashCode());
		result = (prime * result) + ((nested == null) ? 0 : nested.hashCode());
		result = (prime * result) + ((uuid == null) ? 0 : uuid.hashCode());

		return result;
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @param code the code to set
	*/
	public void setCode(short code) {
		this.code = code;
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @param deadline the deadline to set
	*/
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @param description the description to set
	*/
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @param nested the nested to set
	*/
	public void setNested(Freezed nested) {
		this.nested = nested;
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @param uuid the uuid to set
	*/
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
}
