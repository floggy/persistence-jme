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

import java.util.Calendar;

import net.sourceforge.floggy.persistence.Nameable;
import net.sourceforge.floggy.persistence.Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FR2422928 implements Persistable, Nameable {
	/**
	 * DOCUMENT ME!
	 */
	protected Calendar checkpoint;

	/**
	 * DOCUMENT ME!
	 */
	protected Persistable node;

	/**
	 * DOCUMENT ME!
	 */
	protected String name;

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Calendar getCheckpoint() {
		return checkpoint;
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
	public Persistable getNode() {
		return node;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getRecordStoreName() {
		return "FR2422928";
	}

	/**
	 * DOCUMENT ME!
	*
	* @param checkpoint DOCUMENT ME!
	*/
	public void setCheckpoint(Calendar checkpoint) {
		this.checkpoint = checkpoint;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param name DOCUMENT ME!
	*/
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param node DOCUMENT ME!
	*/
	public void setNode(Persistable node) {
		this.node = node;
	}
}
