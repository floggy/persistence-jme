/**
 *  Copyright 2006 Floggy Open Source Group
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;

public class SuperClassGenerator {

    public static String generateLoadSource(CtClass superClass) {
	String source = "\n";

	source += "javax.microedition.rms.RecordStore rs = net.sourceforge.floggy.persistence.PersistableManager.getRecordStore(super.__getPersistableMetadata());\n";
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
