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
package net.sourceforge.floggy.persistence.fr2937635;

import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

import net.sourceforge.floggy.persistence.FloggyBaseTest;
import net.sourceforge.floggy.persistence.IndexFilter;
import net.sourceforge.floggy.persistence.ObjectSet;

public class FR2937635Test extends FloggyBaseTest {

	protected Random random = new Random();

	public void testIndexFindTypeByte() throws Exception {
		byte byte1 = (byte) 257;

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setByte1(byte1);

		try {
			IndexFilter filter = new IndexFilter("byByte1", new Byte(byte1));

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(byte1, ((FR2937635) os.get(0)).getByte1());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindTypeBoolean() throws Exception {
		boolean boolean1 = false;

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setBoolean1(boolean1);

		try {
			IndexFilter filter = new IndexFilter("byBoolean1", new Boolean(boolean1));

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(boolean1, ((FR2937635) os.get(0)).getBoolean1());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeChar() throws Exception {
		char char1 = (char) 126;

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setChar1(char1);

		try {
			IndexFilter filter = new IndexFilter("byChar1",
					new Character(char1));

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(char1, ((FR2937635) os.get(0)).getChar1());

		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeDouble() throws Exception {
		double double1 = random.nextDouble();

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setDouble1(double1);

		try {
			IndexFilter filter = new IndexFilter("byDouble1", new Double(
					double1));

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(double1, ((FR2937635) os.get(0)).getDouble1(), 0);

		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}

	}

	public void testIndexFindFieldTypeInt() throws Exception {
		int int1 = random.nextInt();

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setInt1(int1);

		try {
			IndexFilter filter = new IndexFilter("byInt1", new Integer(int1));

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(int1, ((FR2937635) os.get(0)).getInt1());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeJavaLangBoolean() throws Exception {
		Boolean boolean2 = Boolean.TRUE;

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setBoolean2(boolean2);

		try {
			IndexFilter filter = new IndexFilter("byBoolean2", boolean2);

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(boolean2, ((FR2937635) os.get(0)).getBoolean2());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeJavaLangByte() throws Exception {
		Byte byte2 = new Byte((byte) 87);

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setByte2(byte2);

		try {
			IndexFilter filter = new IndexFilter("byByte2", byte2);

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(byte2, ((FR2937635) os.get(0)).getByte2());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeJavaLangChar() throws Exception {
		Character char2 = new Character((char) 87);

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setChar2(char2);

		try {
			IndexFilter filter = new IndexFilter("byChar2", char2);

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(char2, ((FR2937635) os.get(0)).getChar2());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeJavaLangDouble() throws Exception {

		Double double2 = new Double(random.nextDouble());

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setDouble2(double2);

		try {
			IndexFilter filter = new IndexFilter("byDouble2", double2);

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(double2, ((FR2937635) os.get(0)).getDouble2());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeJavaLangInteger() throws Exception {
		Integer int2 = new Integer(random.nextInt());

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setInt2(int2);

		try {
			IndexFilter filter = new IndexFilter("byInt2", int2);

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(int2, ((FR2937635) os.get(0)).getInt2());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeJavaLangLong() throws Exception {
		Long long2 = new Long(random.nextLong());

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setLong2(long2);

		try {
			IndexFilter filter = new IndexFilter("byLong2", long2);

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(long2, ((FR2937635) os.get(0)).getLong2());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeJavaLangShort() throws Exception {
		Short short2 = new Short((short) 2347);

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setShort2(short2);

		try {
			IndexFilter filter = new IndexFilter("byShort2", short2);

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(short2, ((FR2937635) os.get(0)).getShort2());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeJavaLangString() throws Exception {
		String string1 = "room 355, Parsippany, watching RSA x URU world cup 2010";

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setString1(string1);

		try {
			IndexFilter filter = new IndexFilter("byString1", string1);

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(string1, ((FR2937635) os.get(0)).getString1());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeJavaLangStringBuffer() throws Exception {
		StringBuffer stringBuffer = new StringBuffer(
				"3, Gatehall, room 355, Parsippany, watching RSA x URU world cup 2010");

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setStringBuffer(stringBuffer);

		try {
			IndexFilter filter = new IndexFilter("byStringBuffer", stringBuffer);

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(stringBuffer.toString(), ((FR2937635) os.get(0))
					.getStringBuffer().toString());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeJavaUtilDate() throws Exception {
		Date date = new Date();

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setDate(date);

		try {
			IndexFilter filter = new IndexFilter("byDate", date);

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(date, ((FR2937635) os.get(0)).getDate());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeJavaUtilTimezone() throws Exception {
		TimeZone timeZone = TimeZone.getDefault();

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setTimeZone(timeZone);

		try {
			IndexFilter filter = new IndexFilter("byTimeZone", timeZone);

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(timeZone, ((FR2937635) os.get(0)).getTimeZone());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeLong() throws Exception {
		long long1 = random.nextLong();

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setLong1(long1);

		try {
			IndexFilter filter = new IndexFilter("byLong1", new Long(long1));

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(long1, ((FR2937635) os.get(0)).getLong1());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

	public void testIndexFindFieldTypeShort() throws Exception {
		short short1 = (short) 123875;

		FR2937635 fr2937635 = new FR2937635();

		fr2937635.setShort1(short1);

		try {
			IndexFilter filter = new IndexFilter("byShort1", new Short(short1));

			ObjectSet os = manager.find(FR2937635.class, filter, true);

			assertEquals(0, os.size());

			manager.save(fr2937635);

			os = manager.find(FR2937635.class, filter, true);

			assertEquals(1, os.size());
			assertEquals(short1, ((FR2937635) os.get(0)).getShort1());
		} catch (Exception e) {
			fail(e.getMessage());
		} finally {
			manager.delete(fr2937635);
		}
	}

}
