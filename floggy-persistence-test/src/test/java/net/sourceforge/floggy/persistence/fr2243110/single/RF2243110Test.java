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
package net.sourceforge.floggy.persistence.fr2243110.single;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PolymorphicObjectSet;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class RF2243110Test extends FloggyBaseTest {
	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAbstractClassGetNewInstance() throws Exception {
		SSuperClass superClass = new SSuperClass();
		SConcreteClass2ExtendingAbstractClass concreteClass2ExtendingAbstractClass =
			new SConcreteClass2ExtendingAbstractClass();
		SConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass2 =
			new SConcreteClass3ExtendingConcreteClass2();
		SConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass22 =
			new SConcreteClass3ExtendingConcreteClass2();

		try {
			manager.save(superClass);
			manager.save(concreteClass2ExtendingAbstractClass);
			manager.save(concreteClass3ExtendingConcreteClass2);
			manager.save(concreteClass3ExtendingConcreteClass22);

			PolymorphicObjectSet os =
				manager.polymorphicFind(SAbstractClassExtendingSuperClass.class, null,
					false);

			int instancesOfSuperClass = 0;
			int instancesOfConcreteClass2ExtendingAbstractClass = 0;
			int instancesOfConcreteClass3ExtendingConcreteClass2 = 0;

			for (int i = 0; i < os.size(); i++) {
				SSuperClass persistable = (SSuperClass) os.get(i);

				if (persistable.getClass() == SSuperClass.class) {
					instancesOfSuperClass++;
				}

				if (persistable.getClass() == SConcreteClass2ExtendingAbstractClass.class) {
					instancesOfConcreteClass2ExtendingAbstractClass++;
				}

				if (persistable.getClass() == SConcreteClass3ExtendingConcreteClass2.class) {
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

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testAbstractClassGetSharedInstance() throws Exception {
		SSuperClass superClass = new SSuperClass();
		SConcreteClass2ExtendingAbstractClass concreteClass2ExtendingAbstractClass =
			new SConcreteClass2ExtendingAbstractClass();
		SConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass2 =
			new SConcreteClass3ExtendingConcreteClass2();
		SConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass22 =
			new SConcreteClass3ExtendingConcreteClass2();

		try {
			manager.save(superClass);
			manager.save(concreteClass2ExtendingAbstractClass);
			manager.save(concreteClass3ExtendingConcreteClass2);
			manager.save(concreteClass3ExtendingConcreteClass22);

			PolymorphicObjectSet os =
				manager.polymorphicFind(SAbstractClassExtendingSuperClass.class, null,
					false);

			int instancesOfSuperClass = 0;
			int instancesOfConcreteClass2ExtendingAbstractClass = 0;
			int instancesOfConcreteClass3ExtendingConcreteClass2 = 0;

			assertEquals(3, os.size());

			Persistable temp = null;

			for (int i = 0; i < os.size(); i++) {
				SSuperClass persistable = (SSuperClass) os.getSharedInstance(i);

				if (persistable.getClass() == SSuperClass.class) {
					instancesOfSuperClass++;

					if (temp == null) {
						temp = persistable;
					} else {
						assertSame(temp, persistable);
					}
				}

				if (persistable.getClass() == SConcreteClass2ExtendingAbstractClass.class) {
					instancesOfConcreteClass2ExtendingAbstractClass++;
				}

				if (persistable.getClass() == SConcreteClass3ExtendingConcreteClass2.class) {
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

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testGetNewInstance() throws Exception {
		SSuperClass superClass = new SSuperClass();
		SConcreteClass2ExtendingAbstractClass concreteClass2ExtendingAbstractClass =
			new SConcreteClass2ExtendingAbstractClass();
		SConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass2 =
			new SConcreteClass3ExtendingConcreteClass2();
		SConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass22 =
			new SConcreteClass3ExtendingConcreteClass2();

		try {
			manager.save(superClass);
			manager.save(concreteClass2ExtendingAbstractClass);
			manager.save(concreteClass3ExtendingConcreteClass2);
			manager.save(concreteClass3ExtendingConcreteClass22);

			PolymorphicObjectSet os =
				manager.polymorphicFind(SConcreteClass2ExtendingAbstractClass.class,
					null, false);

			int instancesOfSuperClass = 0;
			int instancesOfConcreteClass2ExtendingAbstractClass = 0;
			int instancesOfConcreteClass3ExtendingConcreteClass2 = 0;

			for (int i = 0; i < os.size(); i++) {
				SSuperClass persistable = (SSuperClass) os.get(i);

				if (persistable.getClass() == SSuperClass.class) {
					instancesOfSuperClass++;
				}

				if (persistable.getClass() == SConcreteClass2ExtendingAbstractClass.class) {
					instancesOfConcreteClass2ExtendingAbstractClass++;
				}

				if (persistable.getClass() == SConcreteClass3ExtendingConcreteClass2.class) {
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

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testGetSharedInstance() throws Exception {
		SSuperClass superClass = new SSuperClass();
		SConcreteClass2ExtendingAbstractClass concreteClass2ExtendingAbstractClass =
			new SConcreteClass2ExtendingAbstractClass();
		SConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass2 =
			new SConcreteClass3ExtendingConcreteClass2();
		SConcreteClass3ExtendingConcreteClass2 concreteClass3ExtendingConcreteClass22 =
			new SConcreteClass3ExtendingConcreteClass2();

		try {
			manager.save(superClass);
			manager.save(concreteClass2ExtendingAbstractClass);
			manager.save(concreteClass3ExtendingConcreteClass2);
			manager.save(concreteClass3ExtendingConcreteClass22);

			PolymorphicObjectSet os =
				manager.polymorphicFind(SConcreteClass2ExtendingAbstractClass.class,
					null, false);

			int instancesOfSuperClass = 0;
			int instancesOfConcreteClass2ExtendingAbstractClass = 0;
			int instancesOfConcreteClass3ExtendingConcreteClass2 = 0;

			assertEquals(3, os.size());

			Persistable temp = null;

			for (int i = 0; i < os.size(); i++) {
				SSuperClass persistable = (SSuperClass) os.getSharedInstance(i);

				if (persistable.getClass() == SSuperClass.class) {
					instancesOfSuperClass++;

					if (temp == null) {
						temp = persistable;
					} else {
						assertSame(temp, persistable);
					}
				}

				if (persistable.getClass() == SConcreteClass2ExtendingAbstractClass.class) {
					instancesOfConcreteClass2ExtendingAbstractClass++;
				}

				if (persistable.getClass() == SConcreteClass3ExtendingConcreteClass2.class) {
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

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testLeafClassGetNewInstance() throws Exception {
		SConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass =
			new SConcreteClass4ExtendingSuperClass();

		int instancesOfConcreteClass4ExtendingSuperClass = 0;

		try {
			manager.save(concreteClass4ExtendingSuperClass);

			PolymorphicObjectSet os =
				manager.polymorphicFind(SConcreteClass4ExtendingSuperClass.class, null,
					false);

			for (int i = 0; i < os.size(); i++) {
				SSuperClass persistable = (SSuperClass) os.get(i);

				if (persistable.getClass() == SConcreteClass4ExtendingSuperClass.class) {
					instancesOfConcreteClass4ExtendingSuperClass++;
				}
			}

			assertEquals(1, instancesOfConcreteClass4ExtendingSuperClass);
		} finally {
			manager.delete(concreteClass4ExtendingSuperClass);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testLeafClassGetSharedInstance() throws Exception {
		SConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass =
			new SConcreteClass4ExtendingSuperClass();
		SConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass2 =
			new SConcreteClass4ExtendingSuperClass();
		SConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass3 =
			new SConcreteClass4ExtendingSuperClass();

		int instancesOfConcreteClass4ExtendingSuperClass = 0;

		try {
			manager.save(concreteClass4ExtendingSuperClass);
			manager.save(concreteClass4ExtendingSuperClass2);
			manager.save(concreteClass4ExtendingSuperClass3);

			PolymorphicObjectSet os =
				manager.polymorphicFind(SConcreteClass4ExtendingSuperClass.class, null,
					false);

			Persistable temp = null;

			for (int i = 0; i < os.size(); i++) {
				SSuperClass persistable = (SSuperClass) os.getSharedInstance(i);

				if (persistable.getClass() == SConcreteClass4ExtendingSuperClass.class) {
					instancesOfConcreteClass4ExtendingSuperClass++;

					if (temp == null) {
						temp = persistable;
					} else {
						assertSame(temp, persistable);
					}
				} else {
					fail("Only " + SConcreteClass4ExtendingSuperClass.class.getName()
						+ " instances should be returned." + " Returned type: "
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

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testRootClassGetNewInstance() throws Exception {
		SSuperClass superClass = new SSuperClass();
		SConcreteClass2ExtendingAbstractClass concreteClass2ExtendingAbstractClass =
			new SConcreteClass2ExtendingAbstractClass();
		SConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass =
			new SConcreteClass4ExtendingSuperClass();
		SConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass2 =
			new SConcreteClass4ExtendingSuperClass();

		try {
			manager.save(superClass);
			manager.save(concreteClass2ExtendingAbstractClass);
			manager.save(concreteClass4ExtendingSuperClass);
			manager.save(concreteClass4ExtendingSuperClass2);

			PolymorphicObjectSet os =
				manager.polymorphicFind(SSuperClass.class, null, false);

			int instancesOfSuperClass = 0;
			int instancesOfConcreteClass2ExtendingAbstractClass = 0;
			int instancesOfConcreteClass4ExtendingSuperClass = 0;

			for (int i = 0; i < os.size(); i++) {
				SSuperClass persistable = (SSuperClass) os.get(i);

				if (persistable.getClass() == SSuperClass.class) {
					instancesOfSuperClass++;
				}

				if (persistable.getClass() == SConcreteClass2ExtendingAbstractClass.class) {
					instancesOfConcreteClass2ExtendingAbstractClass++;
				}

				if (persistable.getClass() == SConcreteClass4ExtendingSuperClass.class) {
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

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testRootClassGetSharedInstance() throws Exception {
		SSuperClass superClass = new SSuperClass();
		SSuperClass superClass2 = new SSuperClass();
		SSuperClass superClass3 = new SSuperClass();
		SConcreteClass4ExtendingSuperClass concreteClass4ExtendingSuperClass =
			new SConcreteClass4ExtendingSuperClass();
		SConcreteClass2ExtendingAbstractClass concreteClass2ExtendingAbstractClass =
			new SConcreteClass2ExtendingAbstractClass();

		try {
			manager.save(superClass);
			manager.save(superClass2);
			manager.save(superClass3);
			manager.save(concreteClass4ExtendingSuperClass);
			manager.save(concreteClass2ExtendingAbstractClass);

			PolymorphicObjectSet os =
				manager.polymorphicFind(SSuperClass.class, null, false);

			int instancesOfSuperClass = 0;
			int instancesOfConcreteClass2ExtendingAbstractClass = 0;
			int instancesOfConcreteClass4ExtendingSuperClass = 0;

			assertEquals(5, os.size());

			Persistable temp = null;

			for (int i = 0; i < os.size(); i++) {
				SSuperClass persistable = (SSuperClass) os.getSharedInstance(i);

				if (persistable.getClass() == SSuperClass.class) {
					instancesOfSuperClass++;

					if (temp == null) {
						temp = persistable;
					} else {
						assertSame(temp, persistable);
					}
				}

				if (persistable.getClass() == SConcreteClass2ExtendingAbstractClass.class) {
					instancesOfConcreteClass2ExtendingAbstractClass++;
				}

				if (persistable.getClass() == SConcreteClass4ExtendingSuperClass.class) {
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
