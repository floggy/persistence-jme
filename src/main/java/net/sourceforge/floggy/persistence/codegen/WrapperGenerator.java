/**
 *  Copyright 2006 Floggy Open Source Group
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

public class WrapperGenerator extends SourceCodeGenerator {

    public WrapperGenerator(String fieldName, CtClass classType) {
	super(fieldName, classType);
    }

    public void initLoadCode() throws NotFoundException {
	addLoadCode("if(dis.readByte() == 0) {");
	addLoadCode("this." + fieldName + " = new " + classType.getName()
		+ "(dis.read" + getType() + "());");
	addLoadCode("}");
    }

    public void initSaveCode() throws NotFoundException {
	String type = getType();

	addSaveCode("if(this." + fieldName + " == null) {");
	addSaveCode("dos.writeByte(1);");
	addSaveCode("}");
	addSaveCode("else {");
	addSaveCode("dos.writeByte(0);");
	addSaveCode("dos.write" + type + "(this." + fieldName + "."
		+ type.toLowerCase() + "Value());");
	addSaveCode("}");
    }

    public boolean isInstanceOf() throws NotFoundException {
	String name = classType.getName();
	return name.equals(Boolean.class.getName())
		|| name.equals(Byte.class.getName())
		|| name.equals(Character.class.getName())
		|| name.equals(Double.class.getName())
		|| name.equals(Float.class.getName())
		|| name.equals(Integer.class.getName())
		|| name.equals(Long.class.getName())
		|| name.equals(Short.class.getName());
    }

    private String getType() throws NotFoundException {
	String name = classType.getName();

	if (name.equals(Boolean.class.getName())) {
	    return "Boolean";
	}
	if (name.equals(Byte.class.getName())) {
	    return "Byte";
	}
	if (name.equals(Character.class.getName())) {
	    return "Char";
	}
	if (name.equals(Double.class.getName())) {
	    return "Double";
	}
	if (name.equals(Float.class.getName())) {
	    return "Float";
	}
	if (name.equals(Integer.class.getName())) {
	    return "Int";
	}
	if (name.equals(Long.class.getName())) {
	    return "Long";
	}
	if (name.equals(Short.class.getName())) {
	    return "Short";
	}

	throw new NotFoundException("Type not found!");
    }

}
