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

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class Person implements Persistable {
	/**
	 * DOCUMENT ME!
	 */
	protected Date bornDate;

	/**
	 * DOCUMENT ME!
	 */
	protected String name;

	/**
	 * DOCUMENT ME!
	 */
	protected String passport;

	/**
	 * DOCUMENT ME!
	 */
	protected transient int age;

	/**
	 * Creates a new Person object.
	 */
	public Person() {
	}

	/**
	 * Creates a new Person object.
	 *
	 * @param passport DOCUMENT ME!
	 * @param nome DOCUMENT ME!
	 * @param dataNascimento DOCUMENT ME!
	 */
	public Person(String passport, String nome, Date dataNascimento) {
		setPassport(passport);
		setName(nome);
		setBornDate(dataNascimento);
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getAge() {
		return age;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Date getBornDate() {
		return bornDate;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getName() {
		return name;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getPassport() {
		return passport;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param dataNascimento DOCUMENT ME!
	*/
	public void setBornDate(Date dataNascimento) {
		this.bornDate = dataNascimento;

		if (dataNascimento != null) {
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();

			c2.setTime(dataNascimento);
			this.age = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
		} else {
			this.age = 0;
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param nome DOCUMENT ME!
	*/
	public void setName(String nome) {
		this.name = nome;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param passport DOCUMENT ME!
	*/
	public void setPassport(String passport) {
		this.passport = passport;
	}
}
