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
import net.sourceforge.floggy.persistence.Persistable;

public class PersistableGenerator extends SourceCodeGenerator {

    public PersistableGenerator(String fieldName, CtClass classType) {
	super(fieldName, classType);
    }

    public void initLoadCode() throws NotFoundException {
	addLoadCode("if(dis.readByte() == 0) {");
	addLoadCode("net.sourceforge.floggy.persistence.__Persistable someClass = new "
		+ classType.getName() + "();");
	addLoadCode("someClass.__load(dis.readInt());");
	addLoadCode("this." + fieldName + " = someClass;");
	addLoadCode("}");
	addLoadCode("else {");
	addLoadCode("this." + fieldName + " = null;");
	addLoadCode("}");
    }

    public void initSaveCode() throws NotFoundException {
	addSaveCode("if(this." + fieldName + " == null) {");
	addSaveCode("dos.writeByte(1);");
	addSaveCode("}");
	addSaveCode("else {");
	addSaveCode("dos.writeByte(0);");
	// NÃO OTIMIZADO
	// addSaveCode("((net.sourceforge.floggy.persistence.__Persistable) this." +
	// fieldName
	// + ").__save();");
	// addSaveCode("dos.writeInt(((net.sourceforge.floggy.persistence.__Persistable)
	// this."
	// + fieldName + ").__getId());");

	// OTIMIZADO
	addSaveCode("dos.writeInt(((net.sourceforge.floggy.persistence.__Persistable) this."
		+ fieldName + ").__save());");
	addSaveCode("}");
    }

    public boolean isInstanceOf() throws NotFoundException {
	CtClass persistableClass = classType.getClassPool().get(
		Persistable.class.getName());
	return classType.subtypeOf(persistableClass);
    }
}
