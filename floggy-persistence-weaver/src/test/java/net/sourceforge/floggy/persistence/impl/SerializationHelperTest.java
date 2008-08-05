package net.sourceforge.floggy.persistence.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import junit.framework.TestCase;

import net.sourceforge.floggy.persistence.RMSMemoryMicroEmulator;

import org.microemu.MIDletBridge;

public class SerializationHelperTest extends TestCase {
	
	private static final int NOT_NULL=0;
	private static final int NULL=1;
	
	public SerializationHelperTest() {
		MIDletBridge.setMicroEmulator(RMSMemoryMicroEmulator.getInstance());
	}

	
	protected DataInput getDataInput(FloggyOutputStream fos) {
		return new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));
	}
	
	public void testReadBooleanNotNull() throws IOException {
		Boolean value= new Boolean(true);
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeBoolean(value.booleanValue());
		
		DataInput in= getDataInput(fos);
		
		Boolean result= SerializationHelper.readBoolean(in);
		
		assertEquals(value, result);
	}

	public void testReadBooleanNull() throws IOException {
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NULL);
		
		DataInput in= getDataInput(fos);
		
		Boolean result= SerializationHelper.readBoolean(in);
		
		assertNull(result);
	}

}
