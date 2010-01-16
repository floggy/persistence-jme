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

import java.util.Vector;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class Doctor extends Person implements Persistable {
	/**
	 * DOCUMENT ME!
	 */
	protected String crm;

	/**
	 * DOCUMENT ME!
	 */
	protected Vector formations;

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getCrm() {
		return crm;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Vector getFormations() {
		return formations;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param crm DOCUMENT ME!
	*/
	public void setCrm(String crm) {
		this.crm = crm;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param formations DOCUMENT ME!
	*/
	public void setFormations(Vector formations) {
		this.formations = formations;
	}
}
