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
import javassist.NotFoundException;

public class PrimitiveTypeGenerator extends SourceCodeGenerator {

	public PrimitiveTypeGenerator(String fieldName, CtClass classType) {
		super(fieldName, classType);
	}

	public void initLoadCode() throws NotFoundException {
		addLoadCode("this." + fieldName + " = dis.read"
				+ PrimitiveTypeGenerator.getType(fieldType) + "();");
	}

	public void initSaveCode() throws NotFoundException {
		addSaveCode("fos.write" + PrimitiveTypeGenerator.getType(fieldType)
				+ "(this." + fieldName + ");");
	}

	public static String getType(CtClass fieldType) throws NotFoundException {
		String name = fieldType.getName();
		
		if (name.equals(Boolean.class.getName()) || name.equals(boolean.class.getName())) {
			return "Boolean";
		}
		if (name.equals(Byte.class.getName()) || name.equals(byte.class.getName())) {
			return "Byte";
		}
		if (name.equals(Character.class.getName()) || name.equals(char.class.getName())) {
			return "Char";
		}
		if (name.equals(Double.class.getName()) || name.equals(double.class.getName())) {
			return "Double";
		}
		if (name.equals(Float.class.getName()) || name.equals(float.class.getName())) {
			return "Float";
		}
		if (name.equals(Integer.class.getName()) || name.equals(int.class.getName())) {
			return "Int";
		}
		if (name.equals(Long.class.getName()) || name.equals(long.class.getName())) {
			return "Long";
		}
		if (name.equals(Short.class.getName()) || name.equals(short.class.getName())) {
			return "Short";
		}
	
		throw new NotFoundException("Type not found: "+name);
	}

}
