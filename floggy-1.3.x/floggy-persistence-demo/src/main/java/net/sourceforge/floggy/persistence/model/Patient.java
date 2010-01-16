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
package net.sourceforge.floggy.persistence.model;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class Patient extends Person implements Persistable {
	private static int HOME_PHONE = 0;
	private static int CELL_PHONE = 1;
	private static int WORK_PHONE = 2;
	private String address;
	private String[] phones;
	private boolean insuredByGoverment;

	/**
	 * Creates a new Patient object.
	 */
	public Patient() {
		this.phones = new String[3];
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getAddress() {
		return address;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getCellPhone() {
		return this.phones[CELL_PHONE];
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getHomePhone() {
		return this.phones[HOME_PHONE];
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String[] getPhones() {
		return phones;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getWorkPhone() {
		return this.phones[WORK_PHONE];
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean isInsuredByGoverment() {
		return insuredByGoverment;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param address DOCUMENT ME!
	*/
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param cellPhone DOCUMENT ME!
	*/
	public void setCellPhone(String cellPhone) {
		this.phones[CELL_PHONE] = cellPhone;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param homePhone DOCUMENT ME!
	*/
	public void setHomePhone(String homePhone) {
		this.phones[HOME_PHONE] = homePhone;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param insuredByGoverment DOCUMENT ME!
	*/
	public void setInsuredByGoverment(boolean insuredByGoverment) {
		this.insuredByGoverment = insuredByGoverment;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param phones DOCUMENT ME!
	*/
	public void setPhones(String[] phones) {
		this.phones = phones;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param workPhone DOCUMENT ME!
	*/
	public void setWorkPhone(String workPhone) {
		this.phones[WORK_PHONE] = workPhone;
	}
}
