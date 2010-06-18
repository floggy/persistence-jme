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

public abstract class SourceCodeGenerator {

    protected String fieldName;

    protected CtClass fieldType;

    private StringBuffer loadCode;

    private StringBuffer saveCode;

    protected SourceCodeGenerator(String fieldName, CtClass fieldType) {
	this.fieldName = fieldName;
	this.fieldType = fieldType;
	this.loadCode = new StringBuffer();
	this.saveCode = new StringBuffer();
    }

    protected void addLoadCode(String part) {
	this.loadCode.append(part);
	this.loadCode.append('\n');
    }

    protected void addSaveCode(String part) {
	this.saveCode.append(part);
	this.saveCode.append('\n');
    }

    public String getLoadCode() throws NotFoundException {
	if (this.loadCode.length() == 0) {
	    this.initLoadCode();
	}

	return this.loadCode.toString();
    }

    public String getSaveCode() throws NotFoundException {
	if (this.saveCode.length() == 0) {
	    this.initSaveCode();
	}

	return this.saveCode.toString();
    }

    public abstract void initLoadCode() throws NotFoundException;

    public abstract void initSaveCode() throws NotFoundException;

}
