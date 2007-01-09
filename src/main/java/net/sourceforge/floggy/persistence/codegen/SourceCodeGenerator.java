/*
 * Criado em 05/09/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

public abstract class SourceCodeGenerator {

    protected String fieldName;

    protected CtClass classType;

    private StringBuffer loadCode;

    private StringBuffer saveCode;

    protected SourceCodeGenerator(String fieldName, CtClass classType) {
	this.fieldName = fieldName;
	this.classType = classType;
	this.loadCode = new StringBuffer();
	this.saveCode = new StringBuffer();
    }

    protected void addLoadCode(String part) {
	this.loadCode.append(part);
	this.loadCode.append('\n');
    }

    protected void addSaveCode(String part) {
	this.saveCode.append(part);
	this.saveCode.append('\n');
    }

    public String getLoadCode() throws NotFoundException {
	if (this.loadCode.length() == 0) {
	    this.initLoadCode();
	}

	return this.loadCode.toString();
    }

    public String getSaveCode() throws NotFoundException {
	if (this.saveCode.length() == 0) {
	    this.initSaveCode();
	}

	return this.saveCode.toString();
    }

    public abstract void initLoadCode() throws NotFoundException;

    public abstract void initSaveCode() throws NotFoundException;

    public abstract boolean isInstanceOf() throws NotFoundException;
}
