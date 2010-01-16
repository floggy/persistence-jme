/**
 * Copyright (c) 2006-2010 Floggy Open Source Group. All rights reserved.
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

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class ClassVerifier {
	private ClassPool classPool;
	private CtClass ctClass;

	/**
	 * Creates a new ClassVerifier object.
	 *
	 * @param ctClass DOCUMENT ME!
	 * @param classPool DOCUMENT ME!
	 */
	public ClassVerifier(CtClass ctClass, ClassPool classPool) {
		this.ctClass = ctClass;
		this.classPool = classPool;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	public boolean isModified() throws NotFoundException {
		CtClass persistableClass = classPool.get(Weaver.__PERSISTABLE_CLASSNAME);

		return ctClass.subtypeOf(persistableClass);
	}

	/**
	* Check if the class implements the persistable interface
	* (net.sourceforge.floggy.Persistable).
	*
	* @return True if the class implements such interface, false otherwise.
	*
	* @throws NotFoundException
	*/
	public boolean isPersistable() throws NotFoundException {
		CtClass persistableClass = classPool.get(Weaver.PERSISTABLE_CLASSNAME);

		return ctClass.subtypeOf(persistableClass);
	}
}
