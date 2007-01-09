/*
 * Criado em 26/08/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

public class PrimitiveTypeGenerator extends SourceCodeGenerator {

    public PrimitiveTypeGenerator(String fieldName, CtClass classType) {
	super(fieldName, classType);
    }

    public void initLoadCode() throws NotFoundException {
	addLoadCode("this." + fieldName + " = dis.read" + getType() + "();");
    }

    public void initSaveCode() throws NotFoundException {
	addSaveCode("dos.write" + getType() + "(this." + fieldName + ");");
    }

    public boolean isInstanceOf() {
	return classType.isPrimitive();
    }

    private String getType() throws NotFoundException {
	if (classType == CtClass.booleanType) {
	    return "Boolean";
	}
	if (classType == CtClass.byteType) {
	    return "Byte";
	}
	if (classType == CtClass.charType) {
	    return "Char";
	}
	if (classType == CtClass.doubleType) {
	    return "Double";
	}
	if (classType == CtClass.floatType) {
	    return "Float";
	}
	if (classType == CtClass.intType) {
	    return "Int";
	}
	if (classType == CtClass.longType) {
	    return "Long";
	}
	if (classType == CtClass.shortType) {
	    return "Short";
	}

	throw new NotFoundException("Type not found!");
    }

}
