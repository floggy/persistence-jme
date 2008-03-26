package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

public class StackGenerator extends SourceCodeGenerator {

	public StackGenerator(String fieldName, CtClass fieldType) {
		super(fieldName, fieldType);
	}

	public void initLoadCode() throws NotFoundException {
		addLoadCode("this."
				+ fieldName
				+ "= net.sourceforge.floggy.persistence.impl.SerializationHelper.readStack(dis);");
	}

	public void initSaveCode() throws NotFoundException {
		addSaveCode("net.sourceforge.floggy.persistence.impl.SerializationHelper.writeStack(fos, "
				+ fieldName + ");");
	}

}
