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

public class ArrayGenerator extends SourceCodeGenerator implements
	AttributeIterableGenerator {

    private char indexForIteration;
    private CtClass persistableType;

    public ArrayGenerator(CtClass persistableType, String fieldName, CtClass classType) {
    	super(fieldName, classType);
    	this.persistableType= persistableType;
    }

    public void initLoadCode() throws NotFoundException {
	SourceCodeGenerator generator;

	addLoadCode("if(dis.readByte() == 0) {");
	addLoadCode("int count = dis.readInt();");
	addLoadCode("this." + fieldName + " = new "
		+ fieldType.getComponentType().getName() + "[count];");
	addLoadCode("for(int " + indexForIteration + " = 0; "
		+ indexForIteration + " < count; " + indexForIteration
		+ "++) {");
	generator = SourceCodeGeneratorFactory.getSourceCodeGenerator(persistableType, fieldName
			+ "[" + indexForIteration + "]", fieldType.getComponentType());
	addLoadCode(generator.getLoadCode());
	addLoadCode("}");
	addLoadCode("}");
	addLoadCode("else {");
	addLoadCode(fieldName + " = null;");
	addLoadCode("}");
    }

    public void initSaveCode() throws NotFoundException {
	SourceCodeGenerator generator;

	addSaveCode("if(" + "this." + fieldName + " == null) {");
	addSaveCode("fos.writeByte(1);");
	addSaveCode("}");
	addSaveCode("else {");
	addSaveCode("fos.writeByte(0);");
	addSaveCode("int count = this." + fieldName + ".length;");
	addSaveCode("fos.writeInt(count);");
	addSaveCode("for(int " + indexForIteration + " = 0; "
		+ indexForIteration + " < count; " + indexForIteration
		+ "++) {");
	generator = SourceCodeGeneratorFactory.getSourceCodeGenerator(persistableType, fieldName
			+ "[" + indexForIteration + "]", fieldType.getComponentType());
	addSaveCode(generator.getSaveCode());
	addSaveCode("}");
	addSaveCode("}");
    }

    public void setUpInterableVariable(char indexForIteration) {
	this.indexForIteration = indexForIteration;
    }

}
