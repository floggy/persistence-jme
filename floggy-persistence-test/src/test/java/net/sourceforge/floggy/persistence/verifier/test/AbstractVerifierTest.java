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
package net.sourceforge.floggy.persistence.verifier.test;

import junit.framework.TestCase;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public abstract class AbstractVerifierTest extends TestCase {
	/**
	 * DOCUMENT ME!
	*/
	public void testDate() {
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyDate", true);
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyDateArray", true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testFloggyException() {
		evaluate("net.sourceforge.floggy.persistence.FloggyException", false);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testFloggyInterface() {
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyInterface", false);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testNoneFields() {
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyNoneFields", true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPersistable() {
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyPersistable", true);
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyPersistableArray",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPersistableInterface() {
		evaluate("net.sourceforge.floggy.persistence.Persistable", false);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPerson() {
		evaluate("net.sourceforge.floggy.persistence.beans.Person", true);
		evaluate("net.sourceforge.floggy.persistence.beans.PersonArray", true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPrimitiveBoolean() {
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestBoolean",
			true);
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestBoolean",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPrimitiveByte() {
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestByte", true);
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestByte",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPrimitiveChar() {
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestChar", true);
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestChar",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPrimitiveDouble() {
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestDouble",
			true);
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestDouble",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPrimitiveFloat() {
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestFloat",
			true);
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestFloat",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPrimitiveInt() {
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestInt", true);
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestInt",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPrimitiveLong() {
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestLong", true);
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestLong",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testPrimitiveShort() {
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestShort",
			true);
		evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestShort",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testStaticAttribute() {
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyStaticAttribute",
			true);
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyStaticAttributeArray",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testString() {
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyString", true);
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyStringArray", true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testSubNoneFields() {
		evaluate("net.sourceforge.floggy.persistence.beans.FloggySubNoneFields",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testSubWithFields() {
		evaluate("net.sourceforge.floggy.persistence.beans.FloggySubWithFields",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testTransientAttribute() {
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyTransient", true);
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyTransientArray",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testVector() {
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyVector", true);
		evaluate("net.sourceforge.floggy.persistence.beans.FloggyVectorArray", true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testWrapperBoolean() {
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestBoolean",
			true);
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.FloggyBoolean",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testWrapperByte() {
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestByte", true);
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestByte",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testWrapperCharacter() {
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestCharacter",
			true);
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestCharacter",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testWrapperDouble() {
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestDouble", true);
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestDouble",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testWrapperFloat() {
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestFloat", true);
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestFloat",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testWrapperInteger() {
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestInteger",
			true);
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestInteger",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testWrapperLong() {
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestLong", true);
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestLong",
			true);
	}

	/**
	 * DOCUMENT ME!
	*/
	public void testWrapperShort() {
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestShort", true);
		evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestShort",
			true);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param className DOCUMENT ME!
	* @param createInstance DOCUMENT ME!
	*/
	protected abstract void evaluate(String className, boolean createInstance);
}
