/**
 * Copyright (c) 2006-2011 Floggy Open Source Group. All rights reserved.
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

import java.util.Random;

import javassist.CtClass;
import javassist.NotFoundException;

import net.sourceforge.floggy.persistence.Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class SourceCodeGeneratorFactory {
	private static Random random = new Random();

	/**
	 * Creates a new SourceCodeGeneratorFactory object.
	 */
	protected SourceCodeGeneratorFactory() {
	}

	/**
	 * DOCUMENT ME!
	*
	* @param persistableType DOCUMENT ME!
	* @param fieldName DOCUMENT ME!
	* @param fieldType DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	public static SourceCodeGenerator getSourceCodeGenerator(
		CtClass persistableType, String fieldName, CtClass fieldType)
		throws NotFoundException {
		SourceCodeGenerator generator = null;

		String className = fieldType.getName();

		if (fieldType.isPrimitive()) {
			generator = new PrimitiveTypeGenerator(fieldName, fieldType);
		} else if (isWrapper(fieldType)) {
			generator = new WrapperGenerator(fieldName, fieldType);
		} else if ("java.util.Calendar".equals(className)) {
			generator = new CalendarGenerator(fieldName, fieldType);
		} else if ("java.util.Date".equals(className)) {
			generator = new DateGenerator(fieldName, fieldType);
		} else if ("java.util.Hashtable".equals(className)) {
			generator = new HashtableGenerator(fieldName, fieldType);
		} else if ("java.util.Stack".equals(className)) {
			generator = new StackGenerator(fieldName, fieldType);
		} else if ("java.lang.String".equals(className)) {
			generator = new StringGenerator(fieldName, fieldType);
		} else if ("java.lang.StringBuffer".equals(className)) {
			generator = new StringBufferGenerator(fieldName, fieldType);
		} else if ("java.util.TimeZone".equals(className)) {
			generator = new TimeZoneGenerator(fieldName, fieldType);
		} else if ("java.util.Vector".equals(className)) {
			generator = new VectorGenerator(fieldName, fieldType);
			((AttributeIterableGenerator) generator).setUpInterableVariable(getNextIndexForIteration());
		} else if (fieldType.isArray()) {
			generator = new ArrayGenerator(persistableType, fieldName, fieldType);
			((AttributeIterableGenerator) generator).setUpInterableVariable(getNextIndexForIteration());
		} else if (isPersistable(fieldType)) {
			generator = new PersistableGenerator(persistableType, fieldName, fieldType);
		}

		if (generator == null) {
			throw new NotFoundException("The class " + className
				+ " is not supported by Floggy!");
		}

		return generator;
	}

	private static String getNextIndexForIteration() {
		return "_" + Math.abs(random.nextInt(256));
	}

	private static boolean isPersistable(CtClass classType)
		throws NotFoundException {
		CtClass persistableClass =
			classType.getClassPool().get(Persistable.class.getName());

		return classType.subtypeOf(persistableClass);
	}

	private static boolean isWrapper(CtClass classType) {
		String name = classType.getName();

		return name.equals(Boolean.class.getName())
		 || name.equals(Byte.class.getName())
		 || name.equals(Character.class.getName())
		 || name.equals(Double.class.getName())
		 || name.equals(Float.class.getName())
		 || name.equals(Integer.class.getName())
		 || name.equals(Long.class.getName()) || name.equals(Short.class.getName());
	}
}
