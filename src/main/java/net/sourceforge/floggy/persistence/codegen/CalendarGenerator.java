package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

public class CalendarGenerator extends SourceCodeGenerator {

	public CalendarGenerator(String fieldName, CtClass fieldType) {
		super(fieldName, fieldType);
	}

	public void initLoadCode() throws NotFoundException {
		addLoadCode("this."
				+ fieldName
				+ "= net.sourceforge.floggy.persistence.impl.SerializationHelper.readCalendar(dis);");
	}

	public void initSaveCode() throws NotFoundException {
		addSaveCode("net.sourceforge.floggy.persistence.impl.SerializationHelper.writeCalendar(fos, "
				+ fieldName + ");");
	}

}
