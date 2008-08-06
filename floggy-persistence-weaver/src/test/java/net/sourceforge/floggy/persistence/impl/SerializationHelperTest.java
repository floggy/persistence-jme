/**
 *  Copyright (c) 2005-2008 Floggy Open Source Group. All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.sourceforge.floggy.persistence.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Vector;

import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.RMSMemoryMicroEmulator;

import org.jmock.Mockery;
import org.microemu.MIDletBridge;

public class SerializationHelperTest extends TestCase {
	
	protected Mockery context= new Mockery();
	
	private static final int NOT_NULL=0;
	private static final int NULL=1;
	
	public SerializationHelperTest() {
		MIDletBridge.setMicroEmulator(RMSMemoryMicroEmulator.getInstance());
	}
	
	protected DataInput getDataInput(FloggyOutputStream fos) {
		return new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));
	}
	
	protected DataInput getDataInputToNullTestMethods() throws IOException {
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NULL);
		
		return new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));
	}
	
	public void testReadBooleanNotNull() throws IOException {
		Boolean value= new Boolean(true);
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeBoolean(value.booleanValue());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readBoolean(in);
		
		assertEquals(value, result);
	}

	public void testReadBooleanNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readBoolean(in);
		
		assertNull(result);
	}

	public void testReadByteNotNull() throws IOException {
		Byte value= new Byte((byte)255);
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeByte(value.byteValue());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readByte(in);
		
		assertEquals(value, result);
	}

	public void testReadByteNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readByte(in);
		
		assertNull(result);
	}

	public void testReadCalendarNotNull() throws IOException {
		Calendar value= Calendar.getInstance();
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeUTF(value.getTimeZone().getID());
		fos.writeLong(value.getTimeInMillis());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readCalendar(in);
		
		assertEquals(value, result);
	}

	public void testReadCalendarNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readCalendar(in);
		
		assertNull(result);
	}

	public void testReadCharNotNull() throws IOException {
		Character value= new Character('e');
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeChar(value.charValue());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readChar(in);
		
		assertEquals(value, result);
	}

	public void testReadCharNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readChar(in);
		
		assertNull(result);
	}

	public void testReadDateNotNull() throws IOException {
		Date value= new Date();
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeLong(value.getTime());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readDate(in);
		
		assertEquals(value, result);
	}

	public void testReadDateNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readDate(in);
		
		assertNull(result);
	}

	public void testReadDoubleNotNull() throws IOException {
		Double value= new Double(234.65);
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeDouble(value.doubleValue());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readDouble(in);
		
		assertEquals(value, result);
	}

	public void testReadDoubleNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readDouble(in);
		
		assertNull(result);
	}

	public void testReadFloatNotNull() throws IOException {
		Float value= new Float(234.65);
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeFloat(value.floatValue());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readFloat(in);
		
		assertEquals(value, result);
	}

	public void testReadFloatNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readFloat(in);
		
		assertNull(result);
	}

	public void testReadHashtableNotNull() throws IOException {
	}

	public void testReadHashtableNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readHashtable(in);
		
		assertNull(result);
	}

	public void testReadIntNotNull() throws IOException {
		Integer value= new Integer(2345);
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeInt(value.intValue());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readInt(in);
		
		assertEquals(value, result);
	}

	public void testReadIntNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readInt(in);
		
		assertNull(result);
	}

	public void testReadLongNotNull() throws IOException {
		Long value= new Long(2345);
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeLong(value.longValue());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readLong(in);
		
		assertEquals(value, result);
	}

	public void testReadLongNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readLong(in);
		
		assertNull(result);
	}

	public void testReadPersistableNotNull() throws Exception {
	}

	public void testReadPersistableNull() throws Exception {
		DataInput in= getDataInputToNullTestMethods();

		Object result= SerializationHelper.readPersistable(in, null);
		
		assertNull(result);
	}

	public void testReadShortNotNull() throws IOException {
		Short value= new Short((short)345);
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeShort(value.shortValue());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readShort(in);
		
		assertEquals(value, result);
	}

	public void testReadShortNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readShort(in);
		
		assertNull(result);
	}

	public void testReadStackNotNull() throws IOException {
	}

	public void testReadStackNull() throws Exception {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readStack(in);
		
		assertNull(result);
	}

	public void testReadStackNotNullEmpty() throws Exception {
		Stack value= new Stack();
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeInt(0);
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readStack(in);
		
		assertEquals(value, result);
	}

	public void testReadStackNotNullNotEmptyNullObjects() throws Exception {
		Stack value= new Stack();
		value.add(null);
		value.add(null);
		value.add(null);
		
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeInt(3);
		fos.writeByte(NULL);
		fos.writeByte(NULL);
		fos.writeByte(NULL);
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readStack(in);
		
		assertEquals(value, result);
	}
	
	public void testReadStringNotNull() throws IOException {
		String value= "floggy";
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeUTF(value);
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readString(in);
		
		assertEquals(value, result);
	}

	public void testReadStringNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readString(in);
		
		assertNull(result);
	}

	public void testReadStringBufferNotNull() throws IOException {
		StringBuffer value= new StringBuffer("floggy");
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeUTF(value.toString());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readStringBuffer(in);
		
		assertEquals(value.toString(), result.toString());
	}

	public void testReadStringBufferNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readStringBuffer(in);
		
		assertNull(result);
	}

	public void testReadTimeZoneNotNull() throws IOException {
		TimeZone value= TimeZone.getDefault();
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeUTF(value.getID());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readTimeZone(in);
		
		assertEquals(value, result);
	}

	public void testReadTimeZoneBufferNull() throws IOException {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readTimeZone(in);
		
		assertNull(result);
	}

	public void testReadVectorNotNullEmpty() throws Exception {
		Vector value= new Vector();
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeInt(0);
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readVector(in);
		
		assertEquals(value, result);
	}

	public void testReadVectorCLDC10NotNullEmpty() throws Exception {
		Vector value= new Vector();
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeInt(0);
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readVectorCLDC10(in);
		
		assertEquals(value, result);
	}

	public void testReadVectorNotNullNotEmptyNullObjects() throws Exception {
		Vector value= new Vector();
		value.add(null);
		value.add(null);
		value.add(null);
		
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeInt(3);
		fos.writeByte(NULL);
		fos.writeByte(NULL);
		fos.writeByte(NULL);
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readVector(in);
		
		assertEquals(value, result);
	}
	
	public void testReadVectorCLDC10NotNullNotEmptyNullObjects() throws Exception {
		Vector value= new Vector();
		value.add(null);
		value.add(null);
		value.add(null);
		
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeInt(3);
		fos.writeByte(NULL);
		fos.writeByte(NULL);
		fos.writeByte(NULL);
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readVectorCLDC10(in);
		
		assertEquals(value, result);
	}

	public void testReadVectorNotNullNotEmptyNotNullObjects() throws Exception {
		Vector value= new Vector();
		FloggyOutputStream fos= new FloggyOutputStream();
		
		fos.writeByte(NOT_NULL);
		fos.writeInt(13);
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Boolean");
		fos.writeBoolean(true);
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Byte");
		fos.writeByte(255);
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Character");
		fos.writeChar('q');
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Double");
		fos.writeDouble(3434.56);
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Float");
		fos.writeFloat(2323.45f);
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Integer");
		fos.writeInt(2323);
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Long");
		fos.writeLong(2342343);
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Short");
		fos.writeShort(23);
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.String");
		fos.writeUTF("floggy");
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.StringBuffer");
		fos.writeUTF("floggy");
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.util.Calendar");
		fos.writeUTF(TimeZone.getDefault().getID());
		fos.writeLong(System.currentTimeMillis());
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.util.Date");
		fos.writeLong(System.currentTimeMillis());
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.util.TimeZone");
		fos.writeUTF(TimeZone.getDefault().getID());
		
		DataInput in= getDataInput(fos);
		
		Object result= SerializationHelper.readVector(in);
		
		assertEquals(value, result);
	}

	public void testReadVectorNull() throws Exception {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readVector(in);
		
		assertNull(result);
	}

	public void testReadVectorCLDC10Null() throws Exception {
		DataInput in= getDataInputToNullTestMethods();
		
		Object result= SerializationHelper.readVectorCLDC10(in);
		
		assertNull(result);
	}

}
