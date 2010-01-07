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
package net.sourceforge.floggy.eclipse;

import org.eclipse.core.runtime.QualifiedName;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class SetAddDefaultConstructorAction extends AbstractSetPropertyAction {
	public static final QualifiedName PROPERTY_NAME =
		new QualifiedName(Activator.PLUGIN_ID, "addDefaultConstructor");

	/**
	 * Creates a new SetAddDefaultConstructorAction object.
	 */
	public SetAddDefaultConstructorAction() {
		propertyName = PROPERTY_NAME;
	}
}
