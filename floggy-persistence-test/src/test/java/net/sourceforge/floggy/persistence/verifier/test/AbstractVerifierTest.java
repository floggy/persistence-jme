package net.sourceforge.floggy.persistence.verifier.test;

import junit.framework.TestCase;

/**
 * @author Thiago Leão Moreira <thiagolm@users.sourceforge.net>
 */
public abstract class AbstractVerifierTest extends TestCase {
    protected abstract void evaluate(String className, boolean createInstance);

    public void testDate() {
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyDate", true);
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyDateArray", true);
    }

    public void testFloggyException() {
	evaluate("net.sourceforge.floggy.persistence.FloggyException", false);
    }

    public void testFloggyInterface() {
	 evaluate("net.sourceforge.floggy.persistence.beans.FloggyInterface", false);
    }

    public void testNoneFields() {
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyNoneFields", true);
    }

    public void testPersistable() {
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyPersistable", true);
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyPersistableArray", true);
    }

    public void testPersistableFilter() {
	// evaluate("net.sourceforge.floggy.persistence.finder.PersistableFilter", false);
    }

    public void testPersistableFinder() {
	// evaluate("net.sourceforge.floggy.persistence.finder.PersistableFinder", false);
    }

    public void testPersistableFinderImpl() {
	// estï¿½ acontecendo um erro no BCELVerifierTest
	// evaluate("net.sourceforge.floggy.persistence.finder.PersistableFinderImpl",
	// false);
    }

    public void testPersistableInterface() {
	evaluate("net.sourceforge.floggy.persistence.Persistable", false);
    }

    public void testPerson() {
	evaluate("net.sourceforge.floggy.persistence.beans.Person", true);
	evaluate("net.sourceforge.floggy.persistence.beans.PersonArray", true);
    }

    public void testVector() {
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyVector", true);
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyVectorArray", true);
    }

    public void testPrimitiveBoolean() {
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestBoolean", true);
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestBoolean",
		true);
    }

    public void testPrimitiveByte() {
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestByte", true);
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestByte", true);
    }

    public void testPrimitiveChar() {
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestChar", true);
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestChar", true);
    }

    public void testPrimitiveDouble() {
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestDouble", true);
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestDouble",
		true);
    }

    public void testPrimitiveFloat() {
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestFloat", true);
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestFloat", true);
    }

    public void testPrimitiveInt() {
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestInt", true);
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestInt", true);
    }

    public void testPrimitiveLong() {
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestLong", true);
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestLong", true);
    }

    public void testPrimitiveShort() {
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.TestShort", true);
	evaluate("net.sourceforge.floggy.persistence.beans.primitive.array.TestShort", true);
    }

    public void testString() {
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyString", true);
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyStringArray", true);
    }

    public void testSubNoneFields() {
	evaluate("net.sourceforge.floggy.persistence.beans.FloggySubNoneFields", true);
	// evaluate("net.sourceforge.floggy.persistence.beans.FloggySubNoneFields", true);
    }

    public void testSubWithFields() {
	evaluate("net.sourceforge.floggy.persistence.beans.FloggySubWithFields", true);
	// evaluate("net.sourceforge.floggy.persistence.beans.FloggySubWithFields", true);
    }

    public void testTransientAttribute() {
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyTransient", true);
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyTransientArray", true);
    }

    public void testStaticAttribute() {
	evaluate("net.sourceforge.floggy.persistence.beans.FloggyStaticAttribute", true);
    }

    public void testWrapperBoolean() {
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestBoolean", true);
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.FloggyBoolean",
		true);
    }

    public void testWrapperByte() {
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestByte", true);
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestByte", true);
    }

    public void testWrapperCharacter() {
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestCharacter", true);
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestCharacter",
		true);
    }

    public void testWrapperDouble() {
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestDouble", true);
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestDouble", true);
    }

    public void testWrapperFloat() {
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestFloat", true);
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestFloat", true);
    }

    public void testWrapperInteger() {
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestInteger", true);
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestInteger", true);
    }

    public void testWrapperLong() {
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestLong", true);
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestLong", true);
    }

    public void testWrapperShort() {
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.TestShort", true);
	evaluate("net.sourceforge.floggy.persistence.beans.wrapper.array.TestShort", true);
    }
}
