/**
 *  Copyright (c) 2005-2008 Floggy Open Source Group. All rights reserved.
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
package net.sourceforge.floggy.persistence.model;

import net.sourceforge.floggy.persistence.Persistable;

public class Patient extends Person implements Persistable {

	private static int HOME_PHONE = 0;

	private static int CELL_PHONE = 1;

	private static int WORK_PHONE = 2;

	private String address;

	private String[] phones;

	private boolean insuredByGoverment;

	public Patient() {
		this.phones = new String[3];
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public boolean isInsuredByGoverment() {
		return insuredByGoverment;
	}

	public void setInsuredByGoverment(boolean insuredByGoverment) {
		this.insuredByGoverment = insuredByGoverment;
	}

	public String[] getPhones() {
		return phones;
	}

	public void setPhones(String[] phones) {
		this.phones = phones;
	}

	public String getHomePhone() {
		return this.phones[HOME_PHONE];
	}

	public void setHomePhone(String homePhone) {
		this.phones[HOME_PHONE] = homePhone;
	}

	public String getCellPhone() {
		return this.phones[CELL_PHONE];
	}

	public void setCellPhone(String cellPhone) {
		this.phones[CELL_PHONE] = cellPhone;
	}

	public String getWorkPhone() {
		return this.phones[WORK_PHONE];
	}

	public void setWorkPhone(String workPhone) {
		this.phones[WORK_PHONE] = workPhone;
	}

}
