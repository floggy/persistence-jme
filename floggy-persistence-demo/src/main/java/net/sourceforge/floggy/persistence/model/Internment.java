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
package net.sourceforge.floggy.persistence.model;

import java.util.Date;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class Internment implements Persistable {
	/**
	 * DOCUMENT ME!
	 */
	protected Bed bed;

	/**
	 * DOCUMENT ME!
	 */
	protected Date enterDate;

	/**
	 * DOCUMENT ME!
	 */
	protected Date exitDate;

	/**
	 * DOCUMENT ME!
	 */
	protected Doctor doctor;

	/**
	 * DOCUMENT ME!
	 */
	protected Patient patient;

	/**
	 * DOCUMENT ME!
	 */
	protected String reason;

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Bed getBed() {
		return bed;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Doctor getDoctor() {
		return doctor;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Date getEnterDate() {
		return enterDate;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Date getExitDate() {
		return exitDate;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Patient getPatient() {
		return patient;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getReason() {
		return reason;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param bed DOCUMENT ME!
	*/
	public void setBed(Bed bed) {
		this.bed = bed;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param doctor DOCUMENT ME!
	*/
	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param enterDate DOCUMENT ME!
	*/
	public void setEnterDate(Date enterDate) {
		this.enterDate = enterDate;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param exitDate DOCUMENT ME!
	*/
	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param patient DOCUMENT ME!
	*/
	public void setPatient(Patient patient) {
		this.patient = patient;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param reason DOCUMENT ME!
	*/
	public void setReason(String reason) {
		this.reason = reason;
	}
}
