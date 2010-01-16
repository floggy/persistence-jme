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
package net.sourceforge.floggy.barbecuecalculator.core;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FlowManagerAlreadyStartedException extends FlowManagerException {
	/**
	 * Creates a new FlowManagerAlreadyStartedException object.
	 */
	public FlowManagerAlreadyStartedException() {
	}

	/**
	 * Creates a new FlowManagerAlreadyStartedException object.
	 *
	 * @param s DOCUMENT ME!
	 */
	public FlowManagerAlreadyStartedException(String s) {
		super(s);
	}
}
