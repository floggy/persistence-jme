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
package net.sourceforge.floggy.persistence.beans;

import java.util.Arrays;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class Patient extends Person implements Persistable {
	private static int TELEFONE_CASA = 0;
	private static int TELEFONE_CELULAR = 1;
	private static int TELEFONE_TRABALHO = 2;
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

		final Patient other = (Patient) obj;

		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;

		if (insuredByGoverment != other.insuredByGoverment)
			return false;

		if (!Arrays.equals(phones, other.phones))
			return false;

		return true;
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
	public String[] getPhones() {
		return phones;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getTelefoneCasa() {
		return this.phones[TELEFONE_CASA];
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getTelefoneCelular() {
		return this.phones[TELEFONE_CELULAR];
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getTelefoneTrabalho() {
		return this.phones[TELEFONE_TRABALHO];
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = (prime * result) + ((address == null) ? 0 : address.hashCode());
		result = (prime * result) + (insuredByGoverment ? 1231 : 1237);
		result = (prime * result) + Patient.hashCode(phones);

		return result;
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
	* @param endereco DOCUMENT ME!
	*/
	public void setAddress(String endereco) {
		this.address = endereco;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param particular DOCUMENT ME!
	*/
	public void setInsuredByGoverment(boolean particular) {
		this.insuredByGoverment = particular;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param telefones DOCUMENT ME!
	*/
	public void setPhones(String[] telefones) {
		this.phones = telefones;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param telefone DOCUMENT ME!
	*/
	public void setTelefoneCasa(String telefone) {
		this.phones[TELEFONE_CASA] = telefone;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param telefone DOCUMENT ME!
	*/
	public void setTelefoneCelular(String telefone) {
		this.phones[TELEFONE_CELULAR] = telefone;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param telefone DOCUMENT ME!
	*/
	public void setTelefoneTrabalho(String telefone) {
		this.phones[TELEFONE_TRABALHO] = telefone;
	}

	private static int hashCode(Object[] array) {
		final int prime = 31;

		if (array == null)
			return 0;

		int result = 1;

		for (int index = 0; index < array.length; index++) {
			result = (prime * result)
				+ ((array[index] == null) ? 0 : array[index].hashCode());
		}

		return result;
	}
}
