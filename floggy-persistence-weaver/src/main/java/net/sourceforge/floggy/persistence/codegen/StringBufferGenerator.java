package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

public class StringBufferGenerator extends SourceCodeGenerator {

	public StringBufferGenerator(String fieldName, CtClass fieldType) {
		super(fieldName, fieldType);
	}

	public void initLoadCode() throws NotFoundException {
		addLoadCode("this."
				+ fieldName
				+ "= net.sourceforge.floggy.persistence.impl.SerializationHelper.readStringBuffer(dis);");
	}

	public void initSaveCode() throws NotFoundException {
		addSaveCode("net.sourceforge.floggy.persistence.impl.SerializationHelper.writeStringBuffer(fos, "
				+ fieldName + ");");
	}

}
