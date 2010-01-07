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
package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public abstract class SourceCodeGenerator {
	/**
	 * DOCUMENT ME!
	 */
	protected CtClass fieldType;

	/**
	 * DOCUMENT ME!
	 */
	protected String fieldName;
	private StringBuffer loadCode;
	private StringBuffer saveCode;

	/**
	 * Creates a new SourceCodeGenerator object.
	 *
	 * @param fieldName DOCUMENT ME!
	 * @param fieldType DOCUMENT ME!
	 */
	protected SourceCodeGenerator(String fieldName, CtClass fieldType) {
		this.fieldName = fieldName;
		this.fieldType = fieldType;
		this.loadCode = new StringBuffer();
		this.saveCode = new StringBuffer();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	public String getLoadCode() throws NotFoundException {
		if (this.loadCode.length() == 0) {
			this.initLoadCode();
		}

		return this.loadCode.toString();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	public String getSaveCode() throws NotFoundException {
		if (this.saveCode.length() == 0) {
			this.initSaveCode();
		}

		return this.saveCode.toString();
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	public abstract void initLoadCode() throws NotFoundException;

	/**
	 * DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	public abstract void initSaveCode() throws NotFoundException;

	/**
	 * DOCUMENT ME!
	*
	* @param part DOCUMENT ME!
	*/
	protected void addLoadCode(String part) {
		this.loadCode.append(part);
		this.loadCode.append('\n');
	}

	/**
	 * DOCUMENT ME!
	*
	* @param part DOCUMENT ME!
	*/
	protected void addSaveCode(String part) {
		this.saveCode.append(part);
		this.saveCode.append('\n');
	}
}
