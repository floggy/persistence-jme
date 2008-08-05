package net.sourceforge.floggy.persistence.impl;

import java.io.IOException;

import junit.framework.TestCase;

public class FloggyOutputStreamTest extends TestCase {
	
	public void testToByteArrayEmpty() {
		FloggyOutputStream fos= new FloggyOutputStream();
		
		byte[] data= fos.toByteArray();
		
		assertNotNull(data);
		
		assertEquals(0, data.length);
	}

	public void testToByteArrayFilled() throws IOException {
		FloggyOutputStream fos= new FloggyOutputStream();
		
		byte temp= 90;
		
		fos.write(temp);
		
		byte[] data= fos.toByteArray();
		
		assertNotNull(data);
		
		assertEquals(1, data.length);
		
		assertEquals(temp, data[0]);
	}

}
