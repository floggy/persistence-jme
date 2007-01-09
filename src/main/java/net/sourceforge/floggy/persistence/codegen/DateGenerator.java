package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

public class DateGenerator extends SourceCodeGenerator {

    public DateGenerator(String fieldName, CtClass classType) {
	super(fieldName, classType);
    }

    public void initLoadCode() throws NotFoundException {
	addLoadCode("if(dis.readByte() == 0) {");
	addLoadCode("this." + fieldName
		+ " = new java.util.Date(dis.readLong());");
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
	addSaveCode("dos.writeLong(this." + fieldName + ".getTime());");
	addSaveCode("}");
    }

    public boolean isInstanceOf() throws NotFoundException {
	return classType.getName().equals("java.util.Date");
    }

}
