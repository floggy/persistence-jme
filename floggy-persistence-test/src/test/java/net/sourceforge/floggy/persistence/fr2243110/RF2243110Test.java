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

package net.sourceforge.floggy.persistence.fr2243110;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PolymorphicObjectSet;

public class RF2243110Test extends FloggyBaseTest {

	public void testGetNewInstance() throws Exception {
		JSuperClass superClass = new JSuperClass();
		JConcreteClass2ExtendingAbstractClass concreteClass2ExtendingAbstractClass = new JConcreteClass2ExtendingAbstractClass();
		JConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass2 = new JConcreteClass3ExtendingConcreteClass2();
		JConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass22 = new JConcreteClass3ExtendingConcreteClass2();

		try {

			manager.save(superClass);
			manager.save(concreteClass2ExtendingAbstractClass);
			manager.save(concreteClass3ExtendingConcreteClass2);
			manager.save(concreteClass3ExtendingConcreteClass22);

			PolymorphicObjectSet os = manager.polymorphicFind(JConcreteClass2ExtendingAbstractClass.class,
					null, false);

			int instancesOfSuperClass = 0;
			int instancesOfConcreteClass2ExtendingAbstractClass = 0;
			int instancesOfConcreteClass3ExtendingConcreteClass2 = 0;
			for (int i = 0; i < os.size(); i++) {
				JSuperClass persistable = (JSuperClass) os.get(i);

				if (persistable.getClass() == JSuperClass.class) {
					instancesOfSuperClass++;
				}
				if (persistable.getClass() == JConcreteClass2ExtendingAbstractClass.class) {
					instancesOfConcreteClass2ExtendingAbstractClass++;
				}
				if (persistable.getClass() == JConcreteClass3ExtendingConcreteClass2.class) {
					instancesOfConcreteClass3ExtendingConcreteClass2++;
				}
			}

			assertEquals(0, instancesOfSuperClass);
			assertEquals(1, instancesOfConcreteClass2ExtendingAbstractClass);
			assertEquals(2, instancesOfConcreteClass3ExtendingConcreteClass2);

		} finally {
			manager.delete(superClass);
			manager.delete(concreteClass2ExtendingAbstractClass);
			manager.delete(concreteClass3ExtendingConcreteClass2);
			manager.delete(concreteClass3ExtendingConcreteClass22);
		}

	}

	public void testGetSharedInstance() throws Exception {
		JSuperClass superClass = new JSuperClass();
		JConcreteClass2ExtendingAbstractClass concreteClass2ExtendingAbstractClass = new JConcreteClass2ExtendingAbstractClass();
		JConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass2 = new JConcreteClass3ExtendingConcreteClass2();
		JConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass22 = new JConcreteClass3ExtendingConcreteClass2();

		try {

			manager.save(superClass);
			manager.save(concreteClass2ExtendingAbstractClass);
			manager.save(concreteClass3ExtendingConcreteClass2);
			manager.save(concreteClass3ExtendingConcreteClass22);

			PolymorphicObjectSet os = manager.polymorphicFind(JConcreteClass2ExtendingAbstractClass.class,
					null, false);

			int instancesOfSuperClass = 0;
			int instancesOfConcreteClass2ExtendingAbstractClass = 0;
			int instancesOfConcreteClass3ExtendingConcreteClass2 = 0;

			assertEquals(3, os.size());
			
			Persistable temp = null;
			for (int i = 0; i < os.size(); i++) {
				JSuperClass persistable = (JSuperClass) os.getSharedInstance(i);

				if (persistable.getClass() == JSuperClass.class) {
					instancesOfSuperClass++;
					if (temp == null) {
						temp = persistable;
					} else {
						assertSame(temp, persistable);
					}
				}
				if (persistable.getClass() == JConcreteClass2ExtendingAbstractClass.class) {
					instancesOfConcreteClass2ExtendingAbstractClass++;
				}
				if (persistable.getClass() == JConcreteClass3ExtendingConcreteClass2.class) {
					instancesOfConcreteClass3ExtendingConcreteClass2++;
				}
			}

			assertEquals(0, instancesOfSuperClass);
			assertEquals(1, instancesOfConcreteClass2ExtendingAbstractClass);
			assertEquals(2, instancesOfConcreteClass3ExtendingConcreteClass2);
		} finally {
			manager.delete(superClass);
			manager.delete(concreteClass2ExtendingAbstractClass);
			manager.delete(concreteClass3ExtendingConcreteClass2);
			manager.delete(concreteClass3ExtendingConcreteClass22);
		}

	}

	public void testLeafClassGetNewInstance() throws Exception {
		JConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass = new JConcreteClass4ExtendingSuperClass();

		int instancesOfConcreteClass4ExtendingSuperClass = 0;
		try {

			manager.save(concreteClass4ExtendingSuperClass);

			PolymorphicObjectSet os = manager.polymorphicFind(
					JConcreteClass4ExtendingSuperClass.class, null, false);

			for (int i = 0; i < os.size(); i++) {
				JSuperClass persistable = (JSuperClass) os.get(i);
				if (persistable.getClass() == JConcreteClass4ExtendingSuperClass.class) {
					instancesOfConcreteClass4ExtendingSuperClass++;
				}
			}

			assertEquals(1, instancesOfConcreteClass4ExtendingSuperClass);
		} finally {
			manager.delete(concreteClass4ExtendingSuperClass);
		}

	}

	public void testLeafClassGetSharedInstance() throws Exception {
		JConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass = new JConcreteClass4ExtendingSuperClass();
		JConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass2 = new JConcreteClass4ExtendingSuperClass();
		JConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass3 = new JConcreteClass4ExtendingSuperClass();

		int instancesOfConcreteClass4ExtendingSuperClass = 0;
		try {

			manager.save(concreteClass4ExtendingSuperClass);
			manager.save(concreteClass4ExtendingSuperClass2);
			manager.save(concreteClass4ExtendingSuperClass3);

			PolymorphicObjectSet os = manager.polymorphicFind(
					JConcreteClass4ExtendingSuperClass.class, null, false);

			Persistable temp = null;
			for (int i = 0; i < os.size(); i++) {
				JSuperClass persistable = (JSuperClass) os.getSharedInstance(i);
				if (persistable.getClass() == JConcreteClass4ExtendingSuperClass.class) {
					instancesOfConcreteClass4ExtendingSuperClass++;

					if (temp == null) {
						temp = persistable;
					} else {
						assertSame(temp, persistable);
					}
				} else {
					fail("Only "
							+ JConcreteClass4ExtendingSuperClass.class.getName()
							+ " instances should be returned."
							+ " Returned type: "
							+ persistable.getClass().getName());
				}
			}

			assertEquals(3, instancesOfConcreteClass4ExtendingSuperClass);
		} finally {
			manager.delete(concreteClass4ExtendingSuperClass);
			manager.delete(concreteClass4ExtendingSuperClass2);
			manager.delete(concreteClass4ExtendingSuperClass3);
		}
	}

	public void testRootClassGetNewInstance() throws Exception {
		JSuperClass superClass = new JSuperClass();
		JConcreteClass2ExtendingAbstractClass concreteClass2ExtendingAbstractClass = new JConcreteClass2ExtendingAbstractClass();
		JConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass = new JConcreteClass4ExtendingSuperClass();
		JConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass2 = new JConcreteClass4ExtendingSuperClass();

		try {

			manager.save(superClass);
			manager.save(concreteClass2ExtendingAbstractClass);
			manager.save(concreteClass4ExtendingSuperClass);
			manager.save(concreteClass4ExtendingSuperClass2);

			PolymorphicObjectSet os = manager.polymorphicFind(JSuperClass.class,
					null, false);

			int instancesOfSuperClass = 0;
			int instancesOfConcreteClass2ExtendingAbstractClass = 0;
			int instancesOfConcreteClass4ExtendingSuperClass = 0;
			for (int i = 0; i < os.size(); i++) {
				JSuperClass persistable = (JSuperClass) os.get(i);

				if (persistable.getClass() == JSuperClass.class) {
					instancesOfSuperClass++;
				}
				if (persistable.getClass() == JConcreteClass2ExtendingAbstractClass.class) {
					instancesOfConcreteClass2ExtendingAbstractClass++;
				}
				if (persistable.getClass() == JConcreteClass4ExtendingSuperClass.class) {
					instancesOfConcreteClass4ExtendingSuperClass++;
				}
			}

			assertEquals(1, instancesOfSuperClass);
			assertEquals(1, instancesOfConcreteClass2ExtendingAbstractClass);
			assertEquals(2, instancesOfConcreteClass4ExtendingSuperClass);

		} finally {
			manager.delete(superClass);
			manager.delete(concreteClass2ExtendingAbstractClass);
			manager.delete(concreteClass4ExtendingSuperClass);
			manager.delete(concreteClass4ExtendingSuperClass2);
		}

	}

	public void testRootClassGetSharedInstance() throws Exception {
		JSuperClass superClass = new JSuperClass();
		JSuperClass superClass2 = new JSuperClass();
		JSuperClass superClass3 = new JSuperClass();
		JConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass = new JConcreteClass4ExtendingSuperClass();
		JConcreteClass2ExtendingAbstractClass concreteClass2ExtendingAbstractClass = new JConcreteClass2ExtendingAbstractClass();

		try {

			manager.save(superClass);
			manager.save(superClass2);
			manager.save(superClass3);
			manager.save(concreteClass4ExtendingSuperClass);
			manager.save(concreteClass2ExtendingAbstractClass);

			PolymorphicObjectSet os = manager.polymorphicFind(JSuperClass.class,
					null, false);

			int instancesOfSuperClass = 0;
			int instancesOfConcreteClass2ExtendingAbstractClass = 0;
			int instancesOfConcreteClass4ExtendingSuperClass = 0;

			assertEquals(5, os.size());
			
			Persistable temp = null;
			for (int i = 0; i < os.size(); i++) {
				JSuperClass persistable = (JSuperClass) os.getSharedInstance(i);

				if (persistable.getClass() == JSuperClass.class) {
					instancesOfSuperClass++;
					if (temp == null) {
						temp = persistable;
					} else {
						assertSame(temp, persistable);
					}
				}
				if (persistable.getClass() == JConcreteClass2ExtendingAbstractClass.class) {
					instancesOfConcreteClass2ExtendingAbstractClass++;
				}
				if (persistable.getClass() == JConcreteClass4ExtendingSuperClass.class) {
					instancesOfConcreteClass4ExtendingSuperClass++;
				}
			}

			assertEquals(3, instancesOfSuperClass);
			assertEquals(1, instancesOfConcreteClass2ExtendingAbstractClass);
			assertEquals(1, instancesOfConcreteClass4ExtendingSuperClass);
		} finally {
			manager.delete(superClass);
			manager.delete(superClass2);
			manager.delete(superClass3);
			manager.delete(concreteClass2ExtendingAbstractClass);
			manager.delete(concreteClass4ExtendingSuperClass);
		}

	}

}
