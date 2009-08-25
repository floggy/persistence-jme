/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
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

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Stack;
import java.util.TimeZone;
import java.util.Vector;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;

public class SerializationHelper {

	public static final int NOT_NULL = 0;

	public static final int NULL = 1;

	private static PersistableManager pm = null;
	
	public static void setPersistableManager(PersistableManager pm) {
		SerializationHelper.pm = pm;
	}

	public final static Boolean readBoolean(DataInput in) throws IOException {
		Boolean b = null;
		if (in.readByte() == NOT_NULL) {
			b = new Boolean(in.readBoolean());
		}
		return b;
	}

	public final static Byte readByte(DataInput in) throws IOException {
		Byte b = null;
		if (in.readByte() == NOT_NULL) {
			b = new Byte(in.readByte());
		}
		return b;
	}

	public final static Calendar readCalendar(DataInput in) throws IOException {
		Calendar c = null;
		if (in.readByte() == NOT_NULL) {
			String timeZoneID = in.readUTF();
			c = Calendar.getInstance(TimeZone.getTimeZone(timeZoneID));
			c.setTime(new Date(in.readLong()));
		}
		return c;
	}

	public final static Character readChar(DataInput in) throws IOException {
		Character c = null;
		if (in.readByte() == NOT_NULL) {
			c = new Character(in.readChar());
		}
		return c;
	}

	public final static Date readDate(DataInput in) throws IOException {
		Date d = null;
		if (in.readByte() == NOT_NULL) {
			d = new Date(in.readLong());
		}
		return d;
	}

	public final static Double readDouble(DataInput in) throws IOException {
		Double d = null;
		if (in.readByte() == NOT_NULL) {
			d = new Double(in.readDouble());
		}
		return d;
	}

	public final static Float readFloat(DataInput in) throws IOException {
		Float f = null;
		if (in.readByte() == NOT_NULL) {
			f = new Float(in.readFloat());
		}
		return f;
	}

	public final static Hashtable readHashtable(DataInput in)
			throws Exception {
		Hashtable h = null;
		if (in.readByte() == NOT_NULL) {
			int size = in.readInt();
			h = new Hashtable(size);
			for (int i = 0; i < size; i++) {
				Object key = readObject(in);
				Object value = readObject(in);
				h.put(key, value);
			}
		}
		return h;
	}

	public final static Integer readInt(DataInput in) throws IOException {
		Integer i = null;
		if (in.readByte() == NOT_NULL) {
			i = new Integer(in.readInt());
		}
		return i;
	}

	public final static Long readLong(DataInput in) throws IOException {
		Long l = null;
		if (in.readByte() == NOT_NULL) {
			l = new Long(in.readLong());
		}
		return l;
	}

	public final static Object readObject(DataInput in) throws Exception {
		Object o = null;
		String className = in.readUTF();
		if (className.equals("java.lang.Boolean")) {
			o = new Boolean(in.readBoolean());
		} else if (className.equals("java.lang.Byte")) {
			o = new Byte(in.readByte());
		} else if (className.equals("java.lang.Character")) {
			o = new Character(in.readChar());
		} else if (className.equals("java.lang.Double")) {
			o = new Double(in.readDouble());
		} else if (className.equals("java.lang.Float")) {
			o = new Float(in.readFloat());
		} else if (className.equals("java.lang.Integer")) {
			o = new Integer(in.readInt());
		} else if (className.equals("java.lang.Long")) {
			o = new Long(in.readLong());
		} else if (className.equals("java.lang.Short")) {
			o = new Short(in.readShort());
		} else if (className.equals("java.lang.String")) {
			o = in.readUTF();
		} else if (className.equals("java.lang.StringBuffer")) {
			o = new StringBuffer(in.readUTF());
		} else if (className.equals("java.util.Calendar")) {
			String timeZoneID = in.readUTF();
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZoneID));
			c.setTime(new Date(in.readLong()));
			o = c;
		} else if (className.equals("java.util.Date")) {
			o = new Date(in.readLong());
		} else if (className.equals("java.util.TimeZone")) {
			o = TimeZone.getTimeZone(in.readUTF());
		} else {
			o = PersistableManagerImpl.createInstance(Class.forName(className));
			pm.load((Persistable) o, in.readInt());
		}
		return o;
	}

	public final static Object readObjectCLDC10(DataInput in) throws Exception {
		Object o = null;
		String className = in.readUTF();
		if (className.equals("java.lang.Boolean")) {
			o = new Boolean(in.readBoolean());
		} else if (className.equals("java.lang.Byte")) {
			o = new Byte(in.readByte());
		} else if (className.equals("java.lang.Character")) {
			o = new Character(in.readChar());
		} else if (className.equals("java.lang.Integer")) {
			o = new Integer(in.readInt());
		} else if (className.equals("java.lang.Long")) {
			o = new Long(in.readLong());
		} else if (className.equals("java.lang.Short")) {
			o = new Short(in.readShort());
		} else if (className.equals("java.lang.String")) {
			o = in.readUTF();
		} else if (className.equals("java.lang.StringBuffer")) {
			o = new StringBuffer(in.readUTF());
		} else if (className.equals("java.util.Calendar")) {
			String timeZoneID = in.readUTF();
			Calendar c = Calendar.getInstance(TimeZone.getTimeZone(timeZoneID));
			c.setTime(new Date(in.readLong()));
			o = c;
		} else if (className.equals("java.util.Date")) {
			o = new Date(in.readLong());
		} else if (className.equals("java.util.TimeZone")) {
			o = TimeZone.getTimeZone(in.readUTF());
		} else {
			o = PersistableManagerImpl.createInstance(Class.forName(className));
			pm.load((Persistable) o, in.readInt());
		}
		return o;
	}

	public final static Persistable readPersistable(DataInput in,
			Persistable persistable, boolean lazy) throws Exception {
		if (lazy) {
			switch (in.readByte()) {
			case -1:
				in.readUTF();
			case NOT_NULL:
				in.readInt();
			case NULL:
				persistable = null;
			}
		} else {
			switch (in.readByte()) {
			case -1:
				String className = in.readUTF();
				persistable = (Persistable) Class.forName(className).newInstance();
			case NOT_NULL:
				pm.load(persistable, in.readInt());
				break;
			case NULL:
				persistable = null;
				break;
			}
		}
		return persistable;
	}

	public final static Short readShort(DataInput in) throws IOException {
		Short s = null;
		if (in.readByte() == NOT_NULL) {
			s = new Short(in.readShort());
		}
		return s;
	}

	public final static Stack readStack(DataInput in) throws Exception {
		Stack s = null;
		Vector v = readVector(in);
		if (v != null) {
			s = new Stack();
			for (int i = 0; i < v.size(); i++) {
				s.push(v.elementAt(i));
			}
		}
		return s;
	}

	public final static String readString(DataInput in) throws IOException {
		String s = null;
		if (in.readByte() == NOT_NULL) {
			s = in.readUTF();
		}
		return s;
	}

	public final static StringBuffer readStringBuffer(DataInput in)
			throws IOException {
		StringBuffer s = null;
		if (in.readByte() == NOT_NULL) {
			s = new StringBuffer(in.readUTF());
		}
		return s;
	}

	public final static TimeZone readTimeZone(DataInput in) throws IOException {
		TimeZone t = null;
		if (in.readByte() == NOT_NULL) {
			t = TimeZone.getTimeZone(in.readUTF());
		}
		return t;
	}

	public final static Vector readVector(DataInput in) throws Exception {
		Vector v = null;
		if (in.readByte() == NOT_NULL) {
			int size = in.readInt();
			v = new Vector(size);
			for (int i = 0; i < size; i++) {
				if (in.readByte() == NULL) {
					v.addElement(null);
				} else {
					v.addElement(readObject(in));
				}
			}
		}
		return v;
	}

	public final static void writeBoolean(DataOutput out, Boolean b)
			throws IOException {
		if (b == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeBoolean(b.booleanValue());
		}
	}

	public final static void writeByte(DataOutput out, Byte b)
			throws IOException {
		if (b == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeByte(b.byteValue());
		}
	}

	public final static void writeCalendar(DataOutput out, Calendar c)
			throws IOException {
		if (c == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeUTF(c.getTimeZone().getID());
			out.writeLong(c.getTime().getTime());
		}
	}

	public final static void writeChar(DataOutput out, Character c)
			throws IOException {
		if (c == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeChar(c.charValue());
		}
	}

	public final static void writeDate(DataOutput out, Date d)
			throws IOException {
		if (d == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeLong(d.getTime());
		}
	}

	public final static void writeDouble(DataOutput out, Double d)
			throws IOException {
		if (d == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeDouble(d.doubleValue());
		}
	}

	public final static void writeFloat(DataOutput out, Float f)
			throws IOException {
		if (f == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeFloat(f.floatValue());
		}
	}

	public final static void writeHashtable(DataOutput out, Hashtable h)
			throws Exception {
		if (h == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeInt(h.size());
			Enumeration keys = h.keys();
			while (keys.hasMoreElements()) {
				Object o = keys.nextElement();
				writeObject(out, o);
				writeObject(out, h.get(o));
			}
		}
	}

	public final static void writeInt(DataOutput out, Integer i)
			throws IOException {
		if (i == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeInt(i.intValue());
		}
	}

	public final static void writeLong(DataOutput out, Long l)
			throws IOException {
		if (l == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeLong(l.longValue());
		}
	}

	public final static void writeObject(DataOutput out, Object o)
			throws Exception {
		String className = o.getClass().getName();
		if (o instanceof Calendar) {
			className = "java.util.Calendar";
		} else if (o instanceof TimeZone) {
			className = "java.util.TimeZone";
		}
		out.writeUTF(className);

		if (o instanceof Boolean) {
			out.writeBoolean(((Boolean) o).booleanValue());
		} else if (o instanceof Byte) {
			out.writeByte(((Byte) o).byteValue());
		} else if (o instanceof Character) {
			out.writeChar(((Character) o).charValue());
		} else if (o instanceof Double) {
			out.writeDouble(((Double) o).doubleValue());
		} else if (o instanceof Float) {
			out.writeFloat(((Float) o).floatValue());
		} else if (o instanceof Integer) {
			out.writeInt(((Integer) o).intValue());
		} else if (o instanceof Long) {
			out.writeLong(((Long) o).longValue());
		} else if (o instanceof Short) {
			out.writeShort(((Short) o).shortValue());
		} else if (o instanceof String) {
			out.writeUTF(o.toString());
		} else if (o instanceof StringBuffer) {
			out.writeUTF(o.toString());
		} else if (o instanceof Calendar) {
			Calendar c = (Calendar) o;
			out.writeUTF(c.getTimeZone().getID());
			out.writeLong(c.getTime().getTime());
		} else if (o instanceof Date) {
			out.writeLong(((Date) o).getTime());
		} else if (o instanceof TimeZone) {
			TimeZone t = (TimeZone) o;
			out.writeUTF(t.getID());
		} else if (o instanceof __Persistable) {
			int id = pm.save((Persistable) o);
			out.writeInt(id);
		} else {
			throw new FloggyException("The class " + className
					+ " doesn't is a persistable class!");
		}
	}

	public final static void writeObjectCLDC10(DataOutput out, Object o)
			throws Exception {
		String className = o.getClass().getName();
		if (o instanceof Calendar) {
			className = "java.util.Calendar";
		} else if (o instanceof TimeZone) {
			className = "java.util.TimeZone";
		}
		out.writeUTF(className);

		if (o instanceof Boolean) {
			out.writeBoolean(((Boolean) o).booleanValue());
		} else if (o instanceof Byte) {
			out.writeByte(((Byte) o).byteValue());
		} else if (o instanceof Character) {
			out.writeChar(((Character) o).charValue());
		} else if (o instanceof Integer) {
			out.writeInt(((Integer) o).intValue());
		} else if (o instanceof Long) {
			out.writeLong(((Long) o).longValue());
		} else if (o instanceof Short) {
			out.writeShort(((Short) o).shortValue());
		} else if (o instanceof String) {
			out.writeUTF(o.toString());
		} else if (o instanceof StringBuffer) {
			out.writeUTF(o.toString());
		} else if (o instanceof Calendar) {
			Calendar c = (Calendar) o;
			out.writeUTF(c.getTimeZone().getID());
			out.writeLong(c.getTime().getTime());
		} else if (o instanceof Date) {
			out.writeLong(((Date) o).getTime());
		} else if (o instanceof TimeZone) {
			TimeZone t = (TimeZone) o;
			out.writeUTF(t.getID());
		} else if (o instanceof __Persistable) {
			int id = pm.save((Persistable) o);
			out.writeInt(id);
		} else {
			throw new FloggyException("The class " + className
					+ " doesn't is a persistable class!");
		}
	}

	public final static void writePersistable(DataOutput out,
			String defaultClassName, Persistable persistable) throws Exception {
		if (persistable == null) {
			out.writeByte(NULL);
		} else {
			String className = persistable.getClass().getName();
			if (!defaultClassName.equals(className)) {
				out.writeByte(-1);
				out.writeUTF(className);
			} else {
				out.writeByte(NOT_NULL);
			}
			out.writeInt(pm.save(persistable));
		}
	}

	public final static void writeShort(DataOutput out, Short s)
			throws IOException {
		if (s == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeChar(s.shortValue());
		}
	}

	public final static void writeStack(DataOutput out, Stack s)
			throws Exception {
		writeVector(out, s);
	}

	public final static void writeString(DataOutput out, String s)
			throws IOException {
		if (s == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeUTF(s);
		}
	}

	public final static void writeStringBuffer(DataOutput out, StringBuffer s)
			throws IOException {
		if (s == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeUTF(s.toString());
		}
	}

	public final static void writeTimeZone(DataOutput out, TimeZone t)
			throws IOException {
		if (t == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			out.writeUTF(t.getID());
		}
	}

	public final static void writeVector(DataOutput out, Vector v)
			throws Exception {
		if (v == null) {
			out.writeByte(NULL);
		} else {
			out.writeByte(NOT_NULL);
			int size = v.size();
			out.writeInt(size);
			for (int i = 0; i < size; i++) {
				Object object = v.elementAt(i);
				if (object == null) {
					out.writeByte(NULL);
				} else {
					out.writeByte(NOT_NULL);
					writeObject(out, object);
				}
			}
		}
	}

	private SerializationHelper() {
	}

}
