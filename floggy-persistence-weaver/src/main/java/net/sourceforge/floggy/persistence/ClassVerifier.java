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
package net.sourceforge.floggy.persistence;

import java.util.Date;
import java.util.Vector;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;

public class ClassVerifier {

	private CtClass ctClass;
	private ClassPool classPool;

	public ClassVerifier(CtClass ctClass, ClassPool classPool) {
		this.ctClass = ctClass;
		this.classPool = classPool;
	}

	/**
	 * Check if the class implements the persistable interface
	 * (net.sourceforge.floggy.Persistable).
	 * 
	 * @return True if the class implements such interface, false otherwise.
	 * @throws NotFoundException
	 */
	public boolean isPersistable() throws NotFoundException {
		CtClass persistableClass = classPool.get(Weaver.PERSISTABLE_CLASSNAME);
		return ctClass.subtypeOf(persistableClass);
	}

	public boolean isModified() throws NotFoundException {
		CtClass persistableClass = classPool.get(Weaver.__PERSISTABLE_CLASSNAME);
		return ctClass.subtypeOf(persistableClass);
	}

	/**
	 * Check if all attributes are persistable.
	 * 
	 * @throws NotFoundException
	 */
	public void checkDependencies() throws NotFoundException, WeaverException {
		CtField[] fields = this.ctClass.getDeclaredFields();
		ClassPool classPool = this.ctClass.getClassPool();

		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				CtClass type = fields[i].getType();

				// Transient attributes
				int modifiers = fields[i].getModifiers();
				if ((Modifier.isTransient(modifiers))) {
					continue;
				}

				// Primitive types.
				if (type.isPrimitive()) {
					continue;
				}

				// String, Boolean, Integer, Long, Double, Float, Date,
				// Vector
				String name = type.getName();
				if (isPersistable(name)) {
					continue;
				}

				// Persistable arrays
				if (type.isArray()) {
					ClassVerifier verifier = new ClassVerifier(type
							.getComponentType(), classPool);
					name = type.getName();
					name = name.substring(0, name.indexOf("[]"));
					if (verifier.isPersistable() || isPersistable(name)) {
						continue;
					}
				}

				// Persistable attributes
				CtClass attributeClass = classPool.get(type.getName());
				ClassVerifier verifier = new ClassVerifier(attributeClass,
						classPool);
				if (verifier.isPersistable()) {
					continue;
				}

				throw new WeaverException("Attribute \"" + fields[i].getName()
						+ "\" does not implements "
						+ Persistable.class.getName());
			}
		}
	}

	private boolean isPersistable(String className) {
		return className.equals(Boolean.class.getName())
				|| className.equals(Byte.class.getName())
				|| className.equals(Character.class.getName())
				|| className.equals(Date.class.getName())
				|| className.equals(Double.class.getName())
				|| className.equals(Float.class.getName())
				|| className.equals(Integer.class.getName())
				|| className.equals(Long.class.getName())
				|| className.equals(Short.class.getName())
				|| className.equals(String.class.getName())
				|| className.equals(Vector.class.getName())
				// primitivos
				|| className.equals(boolean.class.getName())
				|| className.equals(byte.class.getName())
				|| className.equals(char.class.getName())
				|| className.equals(double.class.getName())
				|| className.equals(float.class.getName())
				|| className.equals(int.class.getName())
				|| className.equals(long.class.getName())
				|| className.equals(short.class.getName());
	}
}
