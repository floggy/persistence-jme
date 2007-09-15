package net.sourceforge.floggy.persistence.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Date;


public class SerializationHelper {

	private SerializationHelper() {
	}

	public final static Boolean readBoolean(DataInput in) throws IOException {
		Boolean b = null;
		if (in.readByte() == 0) {
			b = (in.readBoolean()) ? Boolean.TRUE : Boolean.FALSE;
		}
		return b;
	}

	public final static void writeBoolean(DataOutput out, Boolean b)
			throws IOException {
		if (b == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeBoolean(b.booleanValue());
		}
	}

	public final static Byte readByte(DataInput in) throws IOException {
		Byte b = null;
		if (in.readByte() == 0) {
			b = new Byte(in.readByte());
		}
		return b;
	}

	public final static void writeByte(DataOutput out, Byte b)
			throws IOException {
		if (b == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeByte(b.byteValue());
		}
	}

	public final static Character readChar(DataInput in) throws IOException {
		Character c = null;
		if (in.readByte() == 0) {
			c = new Character(in.readChar());
		}
		return c;
	}

	public final static void writeChar(DataOutput out, Character c)
			throws IOException {
		if (c == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeChar(c.charValue());
		}
	}

	public final static Short readShort(DataInput in) throws IOException {
		Short s = null;
		if (in.readByte() == 0) {
			s = new Short(in.readShort());
		}
		return s;
	}

	public final static void writeShort(DataOutput out, Short s)
			throws IOException {
		if (s == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeChar(s.shortValue());
		}
	}

	public final static Integer readInt(DataInput in) throws IOException {
		Integer i = null;
		if (in.readByte() == 0) {
			i = new Integer(in.readInt());
		}
		return i;
	}

	public final static void writeInt(DataOutput out, Integer i)
			throws IOException {
		if (i == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeInt(i.intValue());
		}
	}

	public final static Long readLong(DataInput in) throws IOException {
		Long l = null;
		if (in.readByte() == 0) {
			l = new Long(in.readLong());
		}
		return l;
	}

	public final static void writeLong(DataOutput out, Long l)
			throws IOException {
		if (l == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeLong(l.longValue());
		}
	}

	public final static Double readDouble(DataInput in) throws IOException {
		Double d = null;
		if (in.readByte() == 0) {
			d = new Double(in.readDouble());
		}
		return d;
	}

	public final static void writeDouble(DataOutput out, Double d)
			throws IOException {
		if (d == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeDouble(d.doubleValue());
		}
	}

	public final static Float readFloat(DataInput in) throws IOException {
		Float f = null;
		if (in.readByte() == 0) {
			f = new Float(in.readFloat());
		}
		return f;
	}

	public final static void writeFloat(DataOutput out, Float f)
			throws IOException {
		if (f == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeFloat(f.floatValue());
		}
	}

	public final static String readString(DataInput in) throws IOException {
		String s = null;
		if (in.readByte() == 0) {
			s = in.readUTF();
		}
		return s;
	}

	public final static void writeString(DataOutput out, String s)
			throws IOException {
		if (s == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeUTF(s);
		}
	}

	public final static StringBuffer readStringBuffer(DataInput in)
			throws IOException {
		StringBuffer s = null;
		if (in.readByte() == 0) {
			s = new StringBuffer(in.readUTF());
		}
		return s;
	}

	public final static void writeStringBuffer(DataOutput out, StringBuffer s)
			throws IOException {
		if (s == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeUTF(s.toString());
		}
	}

	public final static Date readDate(DataInput in) throws IOException {
		Date d = null;
		if (in.readByte() == 0) {
			d = new Date(in.readLong());
		}
		return d;
	}

	public final static void writeDate(DataOutput out, Date d)
			throws IOException {
		if (d == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeLong(d.getTime());
		}
	}

}
