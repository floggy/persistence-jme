/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;

public class SuperClassGenerator {

	public static String generateLoadSource(CtClass superClass) {
		String source = "\n";
		source += "javax.microedition.rms.RecordStore superRS = net.sourceforge.floggy.persistence.impl.PersistableManagerImpl.getRecordStore(super.getRecordStoreName(), net.sourceforge.floggy.persistence.impl.MetadataManagerUtil.getClassBasedMetadata(\"" + superClass.getName() + "\"));\n";
		source += "int superClassId = dis.readInt();\n";
		source += "byte[] superClassBuffer = superRS.getRecord(superClassId);\n";
		source += "net.sourceforge.floggy.persistence.impl.PersistableManagerImpl.closeRecordStore(superRS);\n";
		source += "super.__deserialize(superClassBuffer, lazy);\n";
		source += "super.__setId(superClassId);\n";
		source += "\n";
		return source;
	}

	public static String generateSaveSource(CtClass superClass) {
		String source = "\n";
		source += "javax.microedition.rms.RecordStore superRS = net.sourceforge.floggy.persistence.impl.PersistableManagerImpl.getRecordStore(super.getRecordStoreName(), net.sourceforge.floggy.persistence.impl.MetadataManagerUtil.getClassBasedMetadata(\"" + superClass.getName() + "\"));\n";
		source += "byte[] superBuffer= super.__serialize();\n";
		source += "int superId= super.__getId();\n";
		source += "if(superId <= 0) {\n";
		source += "superId = superRS.addRecord(superBuffer, 0, superBuffer.length);\n";
		source += "super.__setId(superId);\n";
		source += "}\n";
		source += "else {\n";
		source += "superRS.setRecord(superId, superBuffer, 0, superBuffer.length);\n";
		source += "}\n";
		source += "net.sourceforge.floggy.persistence.impl.PersistableManagerImpl.closeRecordStore(superRS);\n";
		source += "fos.writeInt(superId);\n";
		source += "\n";
		return source;
	}

	protected SuperClassGenerator() {
	}

}
