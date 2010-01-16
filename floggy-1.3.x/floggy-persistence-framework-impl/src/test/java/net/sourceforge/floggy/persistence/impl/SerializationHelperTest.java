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
package net.sourceforge.floggy.persistence.impl;

import java.io.ByteArrayInputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.IOException;

import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Vector;

import org.jmock.Mockery;

import org.microemu.MIDletBridge;

import junit.framework.TestCase;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.RMSMemoryMicroEmulator;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class SerializationHelperTest extends TestCase {
	private static final int NOT_NULL = 0;
	private static final int NULL = 1;

	/**
	 * DOCUMENT ME!
	 */
	protected Mockery context = new Mockery();

	/**
	 * Creates a new SerializationHelperTest object.
	 */
	public SerializationHelperTest() {
		MIDletBridge.setMicroEmulator(new RMSMemoryMicroEmulator("target/rms"));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadBooleanNotNull() throws IOException {
		Boolean value = new Boolean(true);
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeBoolean(value.booleanValue());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readBoolean(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadBooleanNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readBoolean(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadByteNotNull() throws IOException {
		Byte value = new Byte((byte) 255);
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeByte(value.byteValue());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readByte(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadByteNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readByte(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadCalendarNotNull() throws IOException {
		Calendar value = Calendar.getInstance();
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeUTF(value.getTimeZone().getID());
		fos.writeLong(value.getTimeInMillis());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readCalendar(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadCalendarNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readCalendar(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadCharNotNull() throws IOException {
		Character value = new Character('e');
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeChar(value.charValue());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readChar(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadCharNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readChar(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadDateNotNull() throws IOException {
		Date value = new Date();
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeLong(value.getTime());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readDate(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadDateNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readDate(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadDoubleNotNull() throws IOException {
		Double value = new Double(234.65);
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeDouble(value.doubleValue());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readDouble(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadDoubleNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readDouble(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadFloatNotNull() throws IOException {
		Float value = new Float(234.65);
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeFloat(value.floatValue());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readFloat(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadFloatNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readFloat(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadHashtableNotNull() throws Exception {
		Hashtable expected = new Hashtable();
		expected.put(Boolean.TRUE, Boolean.TRUE);
		expected.put(new Byte((byte) 23), new Byte((byte) 23));
		expected.put(new Character('1'), new Character('1'));

		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeInt(3);

		fos.writeUTF("java.lang.Boolean");
		fos.writeBoolean(true);
		fos.writeUTF("java.lang.Boolean");
		fos.writeBoolean(true);

		fos.writeUTF("java.lang.Byte");
		fos.writeByte((byte) 23);
		fos.writeUTF("java.lang.Byte");
		fos.writeByte((byte) 23);

		fos.writeUTF("java.lang.Character");
		fos.writeChar('1');
		fos.writeUTF("java.lang.Character");
		fos.writeChar('1');

		DataInput in = getDataInput(fos);

		Hashtable actual = SerializationHelper.readHashtable(in);

		assertEquals(expected, actual);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadHashtableNull() throws Exception {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readHashtable(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadIntNotNull() throws IOException {
		Integer value = new Integer(2345);
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeInt(value.intValue());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readInt(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadIntNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readInt(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadLongNotNull() throws IOException {
		Long value = new Long(2345);
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeLong(value.longValue());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readLong(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadLongNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readLong(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadObjectCLDC10NotNull() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Boolean expectedBoolean = Boolean.TRUE;
		fos.writeUTF(expectedBoolean.getClass().getName());
		fos.writeBoolean(expectedBoolean.booleanValue());

		Byte expectedByte = new Byte((byte) 34);
		fos.writeUTF(expectedByte.getClass().getName());
		fos.writeByte(expectedByte.byteValue());

		Character expectedCharacter = new Character('d');
		fos.writeUTF(expectedCharacter.getClass().getName());
		fos.writeChar(expectedCharacter.charValue());

		Integer expectedInteger = new Integer(99);
		fos.writeUTF(expectedInteger.getClass().getName());
		fos.writeInt(expectedInteger.intValue());

		Long expectedLong = new Long(898798798);
		fos.writeUTF(expectedLong.getClass().getName());
		fos.writeLong(expectedLong.longValue());

		Short expectedShort = new Short((short) 234);
		fos.writeUTF(expectedShort.getClass().getName());
		fos.writeShort(expectedShort.shortValue());

		String expectedString = "flogggy";
		fos.writeUTF(expectedString.getClass().getName());
		fos.writeUTF(expectedString);

		StringBuffer expectedStringBuffer = new StringBuffer("floggy");
		fos.writeUTF(expectedStringBuffer.getClass().getName());
		fos.writeUTF(expectedStringBuffer.toString());

		Calendar expectedCalendar = Calendar.getInstance();
		fos.writeUTF("java.util.Calendar");
		fos.writeUTF(expectedCalendar.getTimeZone().getID());
		fos.writeLong(expectedCalendar.getTimeInMillis());

		Date expectedDate = new Date();
		fos.writeUTF(expectedDate.getClass().getName());
		fos.writeLong(expectedDate.getTime());

		TimeZone expectedTimeZone = TimeZone.getDefault();
		fos.writeUTF("java.util.TimeZone");
		fos.writeUTF(expectedTimeZone.getID());

		DataInput in = getDataInput(fos);

		Object actual = SerializationHelper.readObjectCLDC10(in, false);
		assertEquals(expectedBoolean, actual);

		actual = SerializationHelper.readObjectCLDC10(in, false);
		assertEquals(expectedByte, actual);

		actual = SerializationHelper.readObjectCLDC10(in, false);
		assertEquals(expectedCharacter, actual);

		actual = SerializationHelper.readObjectCLDC10(in, false);
		assertEquals(expectedInteger, actual);

		actual = SerializationHelper.readObjectCLDC10(in, false);
		assertEquals(expectedLong, actual);

		actual = SerializationHelper.readObjectCLDC10(in, false);
		assertEquals(expectedShort, actual);

		actual = SerializationHelper.readObjectCLDC10(in, false);
		assertEquals(expectedString, actual);

		actual = SerializationHelper.readObjectCLDC10(in, false);
		assertEquals(expectedStringBuffer.toString(), actual.toString());

		actual = SerializationHelper.readObjectCLDC10(in, false);
		assertEquals(expectedCalendar, actual);

		actual = SerializationHelper.readObjectCLDC10(in, false);
		assertEquals(expectedDate, actual);

		actual = SerializationHelper.readObjectCLDC10(in, false);
		assertEquals(expectedTimeZone, actual);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadObjectNotNull() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Boolean expectedBoolean = Boolean.TRUE;
		fos.writeUTF(expectedBoolean.getClass().getName());
		fos.writeBoolean(expectedBoolean.booleanValue());

		Byte expectedByte = new Byte((byte) 34);
		fos.writeUTF(expectedByte.getClass().getName());
		fos.writeByte(expectedByte.byteValue());

		Character expectedCharacter = new Character('d');
		fos.writeUTF(expectedCharacter.getClass().getName());
		fos.writeChar(expectedCharacter.charValue());

		Double expectedDouble = new Double(23d);
		fos.writeUTF(expectedDouble.getClass().getName());
		fos.writeDouble(expectedDouble.doubleValue());

		Float expectedFloat = new Float(2343f);
		fos.writeUTF(expectedFloat.getClass().getName());
		fos.writeFloat(expectedFloat.floatValue());

		Integer expectedInteger = new Integer(99);
		fos.writeUTF(expectedInteger.getClass().getName());
		fos.writeInt(expectedInteger.intValue());

		Long expectedLong = new Long(898798798);
		fos.writeUTF(expectedLong.getClass().getName());
		fos.writeLong(expectedLong.longValue());

		Short expectedShort = new Short((short) 234);
		fos.writeUTF(expectedShort.getClass().getName());
		fos.writeShort(expectedShort.shortValue());

		String expectedString = "flogggy";
		fos.writeUTF(expectedString.getClass().getName());
		fos.writeUTF(expectedString);

		StringBuffer expectedStringBuffer = new StringBuffer("floggy");
		fos.writeUTF(expectedStringBuffer.getClass().getName());
		fos.writeUTF(expectedStringBuffer.toString());

		Calendar expectedCalendar = Calendar.getInstance();
		fos.writeUTF("java.util.Calendar");
		fos.writeUTF(expectedCalendar.getTimeZone().getID());
		fos.writeLong(expectedCalendar.getTimeInMillis());

		Date expectedDate = new Date();
		fos.writeUTF(expectedDate.getClass().getName());
		fos.writeLong(expectedDate.getTime());

		TimeZone expectedTimeZone = TimeZone.getDefault();
		fos.writeUTF("java.util.TimeZone");
		fos.writeUTF(expectedTimeZone.getID());

		DataInput in = getDataInput(fos);

		Object actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedBoolean, actual);

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedByte, actual);

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedCharacter, actual);

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedDouble, actual);

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedFloat, actual);

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedInteger, actual);

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedLong, actual);

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedShort, actual);

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedString, actual);

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedStringBuffer.toString(), actual.toString());

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedCalendar, actual);

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedDate, actual);

		actual = SerializationHelper.readObject(in, false);
		assertEquals(expectedTimeZone, actual);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadPersistableNotNull() throws Exception {
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadPersistableNull() throws Exception {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readPersistable(in, null, false);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadShortNotNull() throws IOException {
		Short value = new Short((short) 345);
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeShort(value.shortValue());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readShort(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadShortNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readShort(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadStackNotNull() throws IOException {
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadStackNotNullEmpty() throws Exception {
		Stack value = new Stack();
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeInt(0);

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readStack(in, false);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadStackNotNullNotEmptyNullObjects()
		throws Exception {
		Stack value = new Stack();
		value.add(null);
		value.add(null);
		value.add(null);

		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeInt(3);
		fos.writeByte(NULL);
		fos.writeByte(NULL);
		fos.writeByte(NULL);

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readStack(in, false);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadStackNull() throws Exception {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readStack(in, false);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadStringBufferNotNull() throws IOException {
		StringBuffer value = new StringBuffer("floggy");
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeUTF(value.toString());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readStringBuffer(in);

		assertEquals(value.toString(), result.toString());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadStringBufferNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readStringBuffer(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadStringNotNull() throws IOException {
		String value = "floggy";
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeUTF(value);

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readString(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadStringNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readString(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadTimeZoneBufferNull() throws IOException {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readTimeZone(in);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testReadTimeZoneNotNull() throws IOException {
		TimeZone value = TimeZone.getDefault();
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeUTF(value.getID());

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readTimeZone(in);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadVectorCLDC10NotNullNotEmptyThrowException()
		throws Exception {
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadVectorNotNullEmpty() throws Exception {
		Vector value = new Vector();
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeInt(0);

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readVector(in, false);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadVectorNotNullNotEmptyNotNullObjects()
		throws Exception {
		Vector value = new Vector();
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeInt(13);
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Boolean");
		fos.writeBoolean(true);
		value.add(Boolean.TRUE);

		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Byte");
		fos.writeByte(255);
		value.add(new Byte((byte) 255));

		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Character");
		fos.writeChar('q');
		value.add(new Character('q'));

		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Double");
		fos.writeDouble(3434.56);
		value.add(new Double(3434.56));

		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Float");
		fos.writeFloat(2323.45f);
		value.add(new Float(2323.45f));

		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Integer");
		fos.writeInt(2323);
		value.add(new Integer(2323));

		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Long");
		fos.writeLong(2342343);
		value.add(new Long(2342343));

		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.Short");
		fos.writeShort(23);
		value.add(new Short((short) 23));

		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.lang.String");
		fos.writeUTF("floggy");
		value.add("floggy");

		Calendar calendar = Calendar.getInstance();
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.util.Calendar");
		fos.writeUTF(calendar.getTimeZone().getID());
		fos.writeLong(calendar.getTimeInMillis());
		value.add(calendar);

		Date date = new Date();
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.util.Date");
		fos.writeLong(date.getTime());
		value.add(date);

		TimeZone timeZone = TimeZone.getDefault();
		fos.writeByte(NOT_NULL);
		fos.writeUTF("java.util.TimeZone");
		fos.writeUTF(timeZone.getID());
		value.add(timeZone);

		fos.writeByte(NULL);
		value.add(null);

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readVector(in, false);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadVectorNotNullNotEmptyNullObjects()
		throws Exception {
		Vector value = new Vector();
		value.add(null);
		value.add(null);
		value.add(null);

		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NOT_NULL);
		fos.writeInt(3);
		fos.writeByte(NULL);
		fos.writeByte(NULL);
		fos.writeByte(NULL);

		DataInput in = getDataInput(fos);

		Object result = SerializationHelper.readVector(in, false);

		assertEquals(value, result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testReadVectorNull() throws Exception {
		DataInput in = getDataInputToNullTestMethods();

		Object result = SerializationHelper.readVector(in, false);

		assertNull(result);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteBooleanNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		Boolean value = Boolean.TRUE;
		SerializationHelper.writeBoolean(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.booleanValue(), dis.readBoolean());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteBooleanNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeBoolean(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteByteNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		Byte value = new Byte((byte) 235);
		SerializationHelper.writeByte(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.byteValue(), dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteByteNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeByte(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteCalendarNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		Calendar value = Calendar.getInstance();
		SerializationHelper.writeCalendar(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.getTimeZone().getID(), dis.readUTF());
		assertEquals(value.getTimeInMillis(), dis.readLong());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteCalendarNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeCalendar(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteCharNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		Character value = new Character('q');
		SerializationHelper.writeChar(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.charValue(), dis.readChar());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteCharNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeChar(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteDateNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		Date value = new Date();
		SerializationHelper.writeDate(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.getTime(), dis.readLong());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteDateNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeDate(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteDoubleNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		Double value = new Double(1212.32);
		SerializationHelper.writeDouble(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.doubleValue(), dis.readDouble(), 0.0);
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteDoubleNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeDouble(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteFloatNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		Float value = new Float(1212.32);
		SerializationHelper.writeFloat(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.floatValue(), dis.readFloat(), 0.0);
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteFloatNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeFloat(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteHashtableEmptyNotNull() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Hashtable value = new Hashtable();
		SerializationHelper.writeHashtable(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteHashtableNotEmptyNotNull() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Hashtable value = new Hashtable();
		value.put("1", Boolean.TRUE);

		SerializationHelper.writeHashtable(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.size(), dis.readInt());

		assertEquals("java.lang.String", dis.readUTF());
		assertEquals("1", dis.readUTF());
		assertEquals("java.lang.Boolean", dis.readUTF());
		assertEquals(Boolean.TRUE, new Boolean(dis.readBoolean()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteHashtableNull() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeHashtable(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteIntNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		Integer value = new Integer(23165465);
		SerializationHelper.writeInt(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.intValue(), dis.readInt());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteIntNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeInt(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteLongNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		Long value = new Long(23165465);
		SerializationHelper.writeLong(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.longValue(), dis.readLong());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteLongNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeLong(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteObjectCLDC10NotNull() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Boolean expectedBoolean = Boolean.FALSE;
		SerializationHelper.writeObjectCLDC10(fos, expectedBoolean);

		Byte expectedByte = new Byte((byte) 34);
		SerializationHelper.writeObjectCLDC10(fos, expectedByte);

		Character expectedCharacter = new Character('w');
		SerializationHelper.writeObjectCLDC10(fos, expectedCharacter);

		Integer expectedInteger = new Integer(987);
		SerializationHelper.writeObjectCLDC10(fos, expectedInteger);

		Long expectedLong = new Long(34563457);
		SerializationHelper.writeObjectCLDC10(fos, expectedLong);

		Short expectedShort = new Short((short) 34);
		SerializationHelper.writeObjectCLDC10(fos, expectedShort);

		String expectedString = "asdkljf";
		SerializationHelper.writeObjectCLDC10(fos, expectedString);

		StringBuffer expectedStringBuffer = new StringBuffer("flogggyter");
		SerializationHelper.writeObjectCLDC10(fos, expectedStringBuffer);

		Calendar expectedCalendar = Calendar.getInstance();
		SerializationHelper.writeObjectCLDC10(fos, expectedCalendar);

		Date expectedDate = new Date();
		SerializationHelper.writeObjectCLDC10(fos, expectedDate);

		TimeZone expectedTimeZone = TimeZone.getDefault();
		SerializationHelper.writeObjectCLDC10(fos, expectedTimeZone);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals("java.lang.Boolean", dis.readUTF());
		assertEquals(expectedBoolean, new Boolean(dis.readBoolean()));

		assertEquals("java.lang.Byte", dis.readUTF());
		assertEquals(expectedByte, new Byte(dis.readByte()));

		assertEquals("java.lang.Character", dis.readUTF());
		assertEquals(expectedCharacter, new Character(dis.readChar()));

		assertEquals("java.lang.Integer", dis.readUTF());
		assertEquals(expectedInteger, new Integer(dis.readInt()));

		assertEquals("java.lang.Long", dis.readUTF());
		assertEquals(expectedLong, new Long(dis.readLong()));

		assertEquals("java.lang.Short", dis.readUTF());
		assertEquals(expectedShort, new Short(dis.readShort()));

		assertEquals("java.lang.String", dis.readUTF());
		assertEquals(expectedString, dis.readUTF());

		assertEquals("java.lang.StringBuffer", dis.readUTF());
		assertEquals(expectedStringBuffer.toString(),
			new StringBuffer(dis.readUTF()).toString());

		assertEquals("java.util.Calendar", dis.readUTF());

		Calendar actual = Calendar.getInstance(TimeZone.getTimeZone(dis.readUTF()));
		actual.setTimeInMillis(dis.readLong());
		assertEquals(expectedCalendar, actual);

		assertEquals("java.util.Date", dis.readUTF());
		assertEquals(expectedDate, new Date(dis.readLong()));

		assertEquals("java.util.TimeZone", dis.readUTF());
		assertEquals(expectedTimeZone, TimeZone.getTimeZone(dis.readUTF()));
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteObjectCLDC10ThrowingException()
		throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		try {
			SerializationHelper.writeObjectCLDC10(fos, new Object());
			fail("Must throw a FloggyException");
		} catch (Exception e) {
			assertEquals(FloggyException.class, e.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteObjectNotNull() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Boolean expectedBoolean = Boolean.FALSE;
		SerializationHelper.writeObject(fos, expectedBoolean);

		Byte expectedByte = new Byte((byte) 34);
		SerializationHelper.writeObject(fos, expectedByte);

		Character expectedCharacter = new Character('w');
		SerializationHelper.writeObject(fos, expectedCharacter);

		Double expectedDouble = new Double(23465d);
		SerializationHelper.writeObject(fos, expectedDouble);

		Float expectedFloat = new Float(654887f);
		SerializationHelper.writeObject(fos, expectedFloat);

		Integer expectedInteger = new Integer(987);
		SerializationHelper.writeObject(fos, expectedInteger);

		Long expectedLong = new Long(34563457);
		SerializationHelper.writeObject(fos, expectedLong);

		Short expectedShort = new Short((short) 34);
		SerializationHelper.writeObject(fos, expectedShort);

		String expectedString = "asdkljf";
		SerializationHelper.writeObject(fos, expectedString);

		StringBuffer expectedStringBuffer = new StringBuffer("flogggyter");
		SerializationHelper.writeObject(fos, expectedStringBuffer);

		Calendar expectedCalendar = Calendar.getInstance();
		SerializationHelper.writeObject(fos, expectedCalendar);

		Date expectedDate = new Date();
		SerializationHelper.writeObject(fos, expectedDate);

		TimeZone expectedTimeZone = TimeZone.getDefault();
		SerializationHelper.writeObject(fos, expectedTimeZone);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals("java.lang.Boolean", dis.readUTF());
		assertEquals(expectedBoolean, new Boolean(dis.readBoolean()));

		assertEquals("java.lang.Byte", dis.readUTF());
		assertEquals(expectedByte, new Byte(dis.readByte()));

		assertEquals("java.lang.Character", dis.readUTF());
		assertEquals(expectedCharacter, new Character(dis.readChar()));

		assertEquals("java.lang.Double", dis.readUTF());
		assertEquals(expectedDouble, new Double(dis.readDouble()));

		assertEquals("java.lang.Float", dis.readUTF());
		assertEquals(expectedFloat, new Float(dis.readFloat()));

		assertEquals("java.lang.Integer", dis.readUTF());
		assertEquals(expectedInteger, new Integer(dis.readInt()));

		assertEquals("java.lang.Long", dis.readUTF());
		assertEquals(expectedLong, new Long(dis.readLong()));

		assertEquals("java.lang.Short", dis.readUTF());
		assertEquals(expectedShort, new Short(dis.readShort()));

		assertEquals("java.lang.String", dis.readUTF());
		assertEquals(expectedString, dis.readUTF());

		assertEquals("java.lang.StringBuffer", dis.readUTF());
		assertEquals(expectedStringBuffer.toString(),
			new StringBuffer(dis.readUTF()).toString());

		assertEquals("java.util.Calendar", dis.readUTF());

		Calendar actual = Calendar.getInstance(TimeZone.getTimeZone(dis.readUTF()));
		actual.setTimeInMillis(dis.readLong());
		assertEquals(expectedCalendar, actual);

		assertEquals("java.util.Date", dis.readUTF());
		assertEquals(expectedDate, new Date(dis.readLong()));

		assertEquals("java.util.TimeZone", dis.readUTF());
		assertEquals(expectedTimeZone, TimeZone.getTimeZone(dis.readUTF()));
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteObjectThrowingException() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		try {
			SerializationHelper.writeObject(fos, new Object());
			fail("Must throw a FloggyException");
		} catch (Exception e) {
			assertEquals(FloggyException.class, e.getClass());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWritePersistableNotNull() throws IOException {
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWritePersistableNull() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writePersistable(fos, null, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteShortNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		Short value = new Short((short) 465);
		SerializationHelper.writeShort(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.shortValue(), dis.readShort());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteShortNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeShort(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteStackEmpty() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Stack value = new Stack();
		SerializationHelper.writeStack(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.size(), dis.readInt());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteStackNotEmptyNullObjects() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Stack value = new Stack();
		value.push(null);
		value.push(null);
		value.push(null);
		SerializationHelper.writeStack(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.size(), dis.readInt());
		assertEquals(NULL, dis.readByte());
		assertEquals(NULL, dis.readByte());
		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteStackNotNull() throws IOException {
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteStackNotNullNotEmptyNotNullObjects()
		throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Stack value = new Stack();
		value.push(null);
		value.push(null);
		value.push(null);
		SerializationHelper.writeStack(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.size(), dis.readInt());
		assertEquals(NULL, dis.readByte());
		assertEquals(NULL, dis.readByte());
		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteStackNull() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeStack(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteStringBufferNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		StringBuffer value = new StringBuffer("floggy");
		SerializationHelper.writeStringBuffer(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.toString(), dis.readUTF());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteStringBufferNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeStringBuffer(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteStringNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		String value = "floggy";
		SerializationHelper.writeString(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value, dis.readUTF());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteStringNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeString(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteTimeZoneNotNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		TimeZone value = TimeZone.getDefault();
		SerializationHelper.writeTimeZone(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.getID(), dis.readUTF());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testWriteTimeZoneNull() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeTimeZone(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteVectorEmpty() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Vector value = new Vector();
		SerializationHelper.writeVector(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.size(), dis.readInt());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteVectorNotEmptyNullObjects() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Vector value = new Vector();
		value.add(null);
		value.add(null);
		value.add(null);
		SerializationHelper.writeVector(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.size(), dis.readInt());
		assertEquals(NULL, dis.readByte());
		assertEquals(NULL, dis.readByte());
		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteVectorNotNullNotEmptyNotNullObjects()
		throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		Vector value = new Vector();
		value.add(Boolean.TRUE);
		value.add(new Byte((byte) 3));
		value.add(new Character('q'));
		SerializationHelper.writeVector(fos, value);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NOT_NULL, dis.readByte());
		assertEquals(value.size(), dis.readInt());
		assertEquals(NOT_NULL, dis.readByte());
		assertEquals("java.lang.Boolean", dis.readUTF());
		assertEquals(Boolean.TRUE, new Boolean(dis.readBoolean()));
		assertEquals(NOT_NULL, dis.readByte());
		assertEquals("java.lang.Byte", dis.readUTF());
		assertEquals(new Byte((byte) 3), new Byte(dis.readByte()));
		assertEquals(NOT_NULL, dis.readByte());
		assertEquals("java.lang.Character", dis.readUTF());
		assertEquals(new Character('q'), new Character(dis.readChar()));
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testWriteVectorNull() throws Exception {
		FloggyOutputStream fos = new FloggyOutputStream();

		SerializationHelper.writeVector(fos, null);

		DataInputStream dis =
			new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));

		assertEquals(NULL, dis.readByte());
		assertEquals(0, dis.available());
	}

	/**
	 * DOCUMENT ME!
	*
	* @param fos DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected DataInput getDataInput(FloggyOutputStream fos) {
		return new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	protected DataInput getDataInputToNullTestMethods() throws IOException {
		FloggyOutputStream fos = new FloggyOutputStream();

		fos.writeByte(NULL);

		return new DataInputStream(new ByteArrayInputStream(fos.toByteArray()));
	}
}
