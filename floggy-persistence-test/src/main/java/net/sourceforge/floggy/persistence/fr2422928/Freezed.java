package net.sourceforge.floggy.persistence.fr2422928;

import java.util.Date;

import net.sourceforge.floggy.persistence.Persistable;

public class Freezed implements Persistable {

	public static final Date DEADLINE = new Date(987654321);
	public static final String DESCRIPTION = "This is sample class that is freezed between versions!";
	public static final Freezed NESTED = new Freezed();
	public static final String UUID = "2573882d-163c-11dd-98c7-b9f43dcf5330";
	public static final short CODE = (short)56734;

	protected String uuid;
	protected Date deadline;
	protected String description;
	protected Freezed nested;
	protected short code;

	/**
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
	 * @return the code
	 */
	public short getCode() {
		return code;
	}

	/**
	 * @return the deadline
	 */
	public Date getDeadline() {
		return deadline;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @return the nested
	 */
	public Freezed getNested() {
		return nested;
	}

	/**
	 * @return the uuid
	 */
	public String getUuid() {
		return uuid;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + code;
		result = prime * result
				+ ((deadline == null) ? 0 : deadline.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((nested == null) ? 0 : nested.hashCode());
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	/**
	 * @param code the code to set
	 */
	public void setCode(short code) {
		this.code = code;
	}

	/**
	 * @param deadline
	 *            the deadline to set
	 */
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	/**
	 * @param description
	 *            the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @param nested
	 *            the nested to set
	 */
	public void setNested(Freezed nested) {
		this.nested = nested;
	}

	/**
	 * @param uuid
	 *            the uuid to set
	 */
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	
}
