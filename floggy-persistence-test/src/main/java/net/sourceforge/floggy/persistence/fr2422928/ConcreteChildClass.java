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

import java.util.Vector;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class ConcreteChildClass extends AbstractSuperClass {
	/**
	 * DOCUMENT ME!
	 */
	protected Vector dynamicFields = new Vector();

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Vector getDynamicFields() {
		return dynamicFields;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param dynamicFields DOCUMENT ME!
	*/
	public void setDynamicFields(Vector dynamicFields) {
		this.dynamicFields = dynamicFields;
	}
}
