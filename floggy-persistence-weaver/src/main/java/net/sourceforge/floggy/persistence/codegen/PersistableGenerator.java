/*
 * Criado em 26/08/2005.
 * 
 * Todos os direiros reservados aos autores.
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
