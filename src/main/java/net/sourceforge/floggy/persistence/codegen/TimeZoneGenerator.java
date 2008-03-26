package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

public class TimeZoneGenerator extends SourceCodeGenerator {

	public TimeZoneGenerator(String fieldName, CtClass fieldType) {
		super(fieldName, fieldType);
	}

	public void initLoadCode() throws NotFoundException {
		addLoadCode("this."
				+ fieldName
				+ "= net.sourceforge.floggy.persistence.impl.SerializationHelper.readTimeZone(dis);");
	}

	public void initSaveCode() throws NotFoundException {
		addSaveCode("net.sourceforge.floggy.persistence.impl.SerializationHelper.writeTimeZone(fos, "
				+ fieldName + ");");
	}

}
