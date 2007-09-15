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

public class VectorGenerator extends SourceCodeGenerator implements
	AttributeIterableGenerator {

    private char indexForIteration;

    public VectorGenerator(String fieldName, CtClass classType) {
	super(fieldName, classType);
    }

    public void initLoadCode() throws NotFoundException {
	addLoadCode("if(dis.readByte() == 1) {");
	addLoadCode("this." + fieldName + " = null;");
	addLoadCode("}");
	addLoadCode("else {");
	addLoadCode("int size = dis.readInt();");
	addLoadCode("this." + fieldName + " = new java.util.Vector(size);");
	addLoadCode("for(int " + indexForIteration + " = 0; "
		+ indexForIteration + " < size; " + indexForIteration + "++) {");
	addLoadCode("if(dis.readByte() == 1) {");
	addLoadCode("this." + fieldName + ".addElement((Object) null);");
	addLoadCode("continue;");
	addLoadCode("}");
	addLoadCode("else {");
	addLoadCode("String className = dis.readUTF();");
	addLoadCode("if(className.equals(\"java.lang.Boolean\")) {");
	addLoadCode("this." + fieldName
		+ ".addElement(new java.lang.Boolean(dis.readBoolean()));");
	addLoadCode("continue;");
	addLoadCode("}");
	addLoadCode("if(className.equals(\"java.lang.Byte\")) {");
	addLoadCode("this." + fieldName
		+ ".addElement(new java.lang.Byte(dis.readByte()));");
	addLoadCode("continue;");
	addLoadCode("}");
	addLoadCode("if(className.equals(\"java.lang.Character\")) {");
	addLoadCode("this." + fieldName
		+ ".addElement(new java.lang.Character(dis.readChar()));");
	addLoadCode("continue;");
	addLoadCode("}");
	addLoadCode("if(className.equals(\"java.lang.Double\")) {");
	addLoadCode("this." + fieldName
		+ ".addElement(new java.lang.Double(dis.readDouble()));");
	addLoadCode("continue;");
	addLoadCode("}");
	addLoadCode("if(className.equals(\"java.lang.Float\")) {");
	addLoadCode("this." + fieldName
		+ ".addElement(new java.lang.Float(dis.readFloat()));");
	addLoadCode("continue;");
	addLoadCode("}");
	addLoadCode("if(className.equals(\"java.lang.Integer\")) {");
	addLoadCode("this." + fieldName
		+ ".addElement(new java.lang.Integer(dis.readInt()));");
	addLoadCode("continue;");
	addLoadCode("}");
	addLoadCode("if(className.equals(\"java.lang.Long\")) {");
	addLoadCode("this." + fieldName
		+ ".addElement(new java.lang.Long(dis.readLong()));");
	addLoadCode("continue;");
	addLoadCode("}");
	addLoadCode("if(className.equals(\"java.lang.Short\")) {");
	addLoadCode("this." + fieldName
		+ ".addElement(new java.lang.Short(dis.readShort()));");
	addLoadCode("continue;");
	addLoadCode("}");
	addLoadCode("if(className.equals(\"java.lang.String\")) {");
	addLoadCode("this." + fieldName
		+ ".addElement(new java.lang.String(dis.readUTF()));");
	addLoadCode("continue;");
	addLoadCode("}");
	addLoadCode("if(className.equals(\"java.util.Date\")) {");
	addLoadCode("this." + fieldName
		+ ".addElement(new java.util.Date(dis.readLong()));");
	addLoadCode("continue;");
	addLoadCode("}");
	addLoadCode("Class persistableClass = java.lang.Class.forName(className);");
	addLoadCode("Object object = persistableClass.newInstance();");
	addLoadCode("if(object instanceof net.sourceforge.floggy.persistence.impl.__Persistable) {");
	addLoadCode("((net.sourceforge.floggy.persistence.impl.__Persistable) object).__load(dis.readInt());");
	addLoadCode("this." + fieldName + ".addElement(object);");
	addLoadCode("}");
	addLoadCode("}"); // else
	addLoadCode("}"); // for
	addLoadCode("}"); // else
    }

    public void initSaveCode() throws NotFoundException {
	addSaveCode("if(this." + fieldName + " == null) {");
	addSaveCode("fos.writeByte(1);");
	addSaveCode("}");
	addSaveCode("else {");
	addSaveCode("fos.writeByte(0);");
	addSaveCode("int size = this." + fieldName + ".size();");
	addSaveCode("fos.writeInt(size);");
	addSaveCode("for(int " + indexForIteration + " = 0; "
		+ indexForIteration + " < size; " + indexForIteration + "++) {");
	addSaveCode("Object object = this." + fieldName + ".elementAt("
		+ indexForIteration + ");");
	addSaveCode("if(object == null) {");
	addSaveCode("fos.writeByte(1);");
	addSaveCode("continue;");
	addSaveCode("}"); // if(object == null) {
	addSaveCode("fos.writeByte(0);");
	addSaveCode("String className= object.getClass().getName();");
	addSaveCode("fos.writeUTF(className);");
	addSaveCode("if(object instanceof java.lang.Boolean) {");
	addSaveCode("fos.writeBoolean(((java.lang.Boolean) object).booleanValue());");
	addSaveCode("}");
	addSaveCode("else if(object instanceof java.lang.Byte) {");
	addSaveCode("fos.writeByte(((java.lang.Byte) object).byteValue());");
	addSaveCode("}");
	addSaveCode("else if(object instanceof java.lang.Character) {");
	addSaveCode("fos.writeChar(((java.lang.Character) object).charValue());");
	addSaveCode("}");
	addSaveCode("else if(object instanceof java.lang.Double) {");
	addSaveCode("fos.writeDouble(((java.lang.Double) object).doubleValue());");
	addSaveCode("}");
	addSaveCode("else if(object instanceof java.lang.Float) {");
	addSaveCode("fos.writeFloat(((java.lang.Float) object).floatValue());");
	addSaveCode("}");
	addSaveCode("else if(object instanceof java.lang.Integer) {");
	addSaveCode("fos.writeInt(((java.lang.Integer) object).intValue());");
	addSaveCode("}");
	addSaveCode("else if(object instanceof java.lang.Long) {");
	addSaveCode("fos.writeLong(((java.lang.Long) object).longValue());");
	addSaveCode("}");
	addSaveCode("else if(object instanceof java.lang.Short) {");
	addSaveCode("fos.writeShort(((java.lang.Short) object).shortValue());");
	addSaveCode("}");
	addSaveCode("else if(object instanceof java.lang.String) {");
	addSaveCode("fos.writeUTF(((java.lang.String) object));");
	addSaveCode("}");
	addSaveCode("else if(object instanceof java.util.Date) {");
	addSaveCode("fos.writeLong(((java.util.Date) object).getTime());");
	addSaveCode("}");
	addSaveCode("else if(object instanceof net.sourceforge.floggy.persistence.impl.__Persistable) {");
	addSaveCode("int id = ((net.sourceforge.floggy.persistence.impl.__Persistable) object).__save();");
	addSaveCode("fos.writeInt(id);");
	addSaveCode("}");
	addSaveCode("else {");
	addSaveCode("throw new net.sourceforge.floggy.persistence.FloggyException(\"The class \"+className+\" doesn't is a persistable class!\");");
	addSaveCode("}"); // else
	addSaveCode("}"); // for
	addSaveCode("}"); // else
    }

    public void setUpInterableVariable(char indexForIteration) {
	this.indexForIteration = indexForIteration;
    }

}
