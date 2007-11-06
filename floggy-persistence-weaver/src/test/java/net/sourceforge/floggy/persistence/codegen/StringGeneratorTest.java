package net.sourceforge.floggy.persistence.codegen;

import junit.framework.TestCase;

public class StringGeneratorTest extends TestCase {

	public void testInitLoadCode() {
		//fail("Not yet implemented");
	}

	public void testInitSaveCode() {
		//fail("Not yet implemented");
	}

	public void testIsInstanceOf() {
		//fail("Not yet implemented");
	}

	public void testStringGenerator() {
		String field= "test";
		StringGenerator generator= new StringGenerator(field, null);
		assertEquals(field, generator.fieldName);
	}

}
