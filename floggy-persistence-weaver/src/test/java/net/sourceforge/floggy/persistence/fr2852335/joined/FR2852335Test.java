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
package net.sourceforge.floggy.persistence.fr2852335.joined;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

import junit.framework.TestCase;

import net.sourceforge.floggy.persistence.Configuration;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.codegen.strategy.JoinedStrategyCodeGenerator;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FR2852335Test extends TestCase {
	/**
	 * DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	public void testWrongHierarchyPerClass() throws NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new LoaderClassPath(getClass().getClassLoader()));

		CtClass ctClass = pool.get(WrongHierarchyPerClass.class.getName());
		Configuration configuration = new Configuration();
		PersistableMetadata metadata =
			new PersistableMetadata(false, ctClass.getName(), null, null, null, null,
				null, ctClass.getSimpleName(), PersistableMetadata.JOINED_STRATEGY, null);

		configuration.addPersistableMetadata(metadata);
		configuration.setAddDefaultConstructor(true);

		try {
			new JoinedStrategyCodeGenerator(ctClass, pool, configuration);
			fail("Must throw a FloggyException");
		} catch (Exception e) {
			assertEquals(FloggyException.class, e.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	public void testWrongHierarchySingle() throws NotFoundException {
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(new LoaderClassPath(getClass().getClassLoader()));

		CtClass ctClass = pool.get(WrongHierarchySingle.class.getName());
		Configuration configuration = new Configuration();
		PersistableMetadata metadata =
			new PersistableMetadata(false, ctClass.getName(), null, null, null, null,
				null, ctClass.getSimpleName(), PersistableMetadata.JOINED_STRATEGY, null);

		configuration.addPersistableMetadata(metadata);
		configuration.setAddDefaultConstructor(true);

		try {
			new JoinedStrategyCodeGenerator(ctClass, pool, configuration);
			fail("Must throw a FloggyException");
		} catch (Exception e) {
			assertEquals(FloggyException.class, e.getClass());
		}
	}
}
