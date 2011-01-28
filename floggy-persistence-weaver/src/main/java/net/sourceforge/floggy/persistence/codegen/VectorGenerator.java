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
package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class VectorGenerator extends SourceCodeGenerator
	implements AttributeIterableGenerator {
	/**
	 * Creates a new VectorGenerator object.
	 *
	 * @param fieldName DOCUMENT ME!
	 * @param classType DOCUMENT ME!
	 */
	public VectorGenerator(String fieldName, CtClass classType) {
		super(fieldName, classType);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	public void initLoadCode() throws NotFoundException {
		addLoadCode("this." + fieldName
			+ "= net.sourceforge.floggy.persistence.impl.SerializationManager.readVector(dis, lazy);");
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	public void initSaveCode() throws NotFoundException {
		addSaveCode(
			"net.sourceforge.floggy.persistence.impl.SerializationManager.writeVector(fos, "
			+ fieldName + ");");
	}

	/**
	 * DOCUMENT ME!
	*
	* @param indexForIteration DOCUMENT ME!
	*/
	public void setUpInterableVariable(String indexForIteration) {
	}
}
