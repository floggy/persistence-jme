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

import java.util.Calendar;
import java.util.Date;

import net.sourceforge.floggy.persistence.Deletable;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.beans.animals.Bird;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class Person implements Persistable, Deletable {
	public char sex;

	/**
	 * DOCUMENT ME!
	 */
	protected Date dataNascimento;

	/**
	 * DOCUMENT ME!
	 */
	protected String cpf;

	/**
	 * DOCUMENT ME!
	 */
	protected String nome;

	/**
	 * DOCUMENT ME!
	 */
	protected transient int idade;
	private Bird x;

	/**
	 * Creates a new Person object.
	 */
	public Person() {
	}

	/**
	 * Creates a new Person object.
	 *
	 * @param cpf DOCUMENT ME!
	 * @param nome DOCUMENT ME!
	 * @param dataNascimento DOCUMENT ME!
	 */
	public Person(String cpf, String nome, Date dataNascimento) {
		setCpf(cpf);
		setNome(nome);
		setDataNascimento(dataNascimento);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public void delete() throws FloggyException {
		if (x != null) {
			PersistableManager.getInstance().delete(x);
		}
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

		if (obj == null)
			return false;

		if (getClass() != obj.getClass())
			return false;

		final Person other = (Person) obj;

		if (cpf == null) {
			if (other.cpf != null)
				return false;
		} else if (!cpf.equals(other.cpf))
			return false;

		if (dataNascimento == null) {
			if (other.dataNascimento != null)
				return false;
		} else if (!dataNascimento.equals(other.dataNascimento))
			return false;

		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;

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
	public String getCpf() {
		return cpf;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Date getDataNascimento() {
		return dataNascimento;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getIdade() {
		return idade;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getNome() {
		return nome;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Bird getX() {
		return x;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((cpf == null) ? 0 : cpf.hashCode());
		result = (prime * result)
			+ ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = (prime * result) + ((nome == null) ? 0 : nome.hashCode());
		result = (prime * result) + ((x == null) ? 0 : x.hashCode());

		return result;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param cpf DOCUMENT ME!
	*/
	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param dataNascimento DOCUMENT ME!
	*/
	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;

		if (dataNascimento != null) {
			Calendar c1 = Calendar.getInstance();
			Calendar c2 = Calendar.getInstance();

			c2.setTime(dataNascimento);
			this.idade = c1.get(Calendar.YEAR) - c2.get(Calendar.YEAR);
		} else {
			this.idade = 0;
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param nome DOCUMENT ME!
	*/
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param x DOCUMENT ME!
	*/
	public void setX(Bird x) {
		this.x = x;
	}
}
