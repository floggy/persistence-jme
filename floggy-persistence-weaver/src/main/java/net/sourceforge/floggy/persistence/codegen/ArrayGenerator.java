package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

public class ArrayGenerator extends SourceCodeGenerator implements
	AttributeIterableGenerator {

    private char indexForIteration;

    public ArrayGenerator(String fieldName, CtClass classType) {
	super(fieldName, classType);
    }

    public void initLoadCode() throws NotFoundException {
	SourceCodeGenerator generator;

	addLoadCode("if(dis.readByte() == 0) {");
	addLoadCode("int count = dis.readInt();");
	addLoadCode("this." + fieldName + " = new "
		+ classType.getComponentType().getName() + "[count];");
	addLoadCode("for(int " + indexForIteration + " = 0; "
		+ indexForIteration + " < count; " + indexForIteration
		+ "++) {");
	generator = SourceCodeGeneratorFactory.getSourceCodeGenerator(fieldName
		+ "[" + indexForIteration + "]", classType.getComponentType());
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
	addSaveCode("dos.writeByte(1);");
	addSaveCode("}");
	addSaveCode("else {");
	addSaveCode("dos.writeByte(0);");
	addSaveCode("int count = this." + fieldName + ".length;");
	addSaveCode("dos.writeInt(count);");
	addSaveCode("for(int " + indexForIteration + " = 0; "
		+ indexForIteration + " < count; " + indexForIteration
		+ "++) {");
	generator = SourceCodeGeneratorFactory.getSourceCodeGenerator(fieldName
		+ "[" + indexForIteration + "]", classType.getComponentType());
	addSaveCode(generator.getSaveCode());
	addSaveCode("}");
	addSaveCode("}");
    }

    public boolean isInstanceOf() {
	return classType.isArray();
    }

    public void setUpInterableVariable(char indexForIteration) {
	this.indexForIteration = indexForIteration;
    }

}
