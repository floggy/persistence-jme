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

import net.sourceforge.floggy.persistence.Nameable;
import net.sourceforge.floggy.persistence.Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class Bed implements Persistable, Nameable {
	private int floor;
	private int number;

	/**
	 * Creates a new Bed object.
	 */
	public Bed() {
	}

	/**
	 * Creates a new Bed object.
	 *
	 * @param floor DOCUMENT ME!
	 * @param number DOCUMENT ME!
	 */
	public Bed(int floor, int number) {
		this.floor = floor;
		this.number = number;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getFloor() {
		return floor;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getNumber() {
		return number;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getRecordStoreName() {
		return "Bed";
	}

	/**
	 * DOCUMENT ME!
	*
	* @param floor DOCUMENT ME!
	*/
	public void setFloor(int floor) {
		this.floor = floor;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param number DOCUMENT ME!
	*/
	public void setNumber(int number) {
		this.number = number;
	}
}
