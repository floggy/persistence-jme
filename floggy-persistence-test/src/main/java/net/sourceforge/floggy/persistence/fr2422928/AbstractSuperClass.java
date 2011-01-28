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
package net.sourceforge.floggy.persistence.fr2422928;

import java.util.Date;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public abstract class AbstractSuperClass implements Persistable {
	/**
	 * DOCUMENT ME!
	 */
	protected Date creationDate;

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param creationDate DOCUMENT ME!
	*/
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}
}
