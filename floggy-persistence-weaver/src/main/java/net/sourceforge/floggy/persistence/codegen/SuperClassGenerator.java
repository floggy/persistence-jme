/*
 * Criado em 05/09/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;

public class SuperClassGenerator {

    public static String generateLoadSource(CtClass superClass) {
	String source = "\n";

	source += "javax.microedition.rms.RecordStore rs = net.sourceforge.floggy.PersistableManager.getRecordStore(\""
		+ superClass.getName() + "\");\n";
	source += "int superClassId = dis.readInt();\n";
	source += "byte[] superClassBuffer = rs.getRecord(superClassId);\n";
	source += "rs.closeRecordStore();\n";
	source += "super.__load(superClassBuffer);\n";
	source += "\n";

	return source;
    }

    public static String generateSaveSource(CtClass superClass) {
	String source = "\n";

	source += "int superClassId = super.__save();\n";
	source += "dos.writeInt(superClassId);\n";
	source += "\n";

	return source;
    }
}
