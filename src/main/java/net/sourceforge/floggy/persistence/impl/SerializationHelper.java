package net.sourceforge.floggy.persistence.impl;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Vector;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;

public class SerializationHelper {

	private static PersistableManager manager = PersistableManager
			.getInstance();

	public final static Boolean readBoolean(DataInput in) throws IOException {
		Boolean b = null;
		if (in.readByte() == 0) {
			b = (in.readBoolean()) ? Boolean.TRUE : Boolean.FALSE;
		}
		return b;
	}

	public final static Byte readByte(DataInput in) throws IOException {
		Byte b = null;
		if (in.readByte() == 0) {
			b = new Byte(in.readByte());
		}
		return b;
	}

	public final static Calendar readCalendar(DataInput in) throws IOException {
		Calendar c = null;
		if (in.readByte() == 0) {
			String timeZoneID = in.readUTF();
			c = Calendar.getInstance(TimeZone.getTimeZone(timeZoneID));
			c.setTime(new Date(in.readLong()));
		}
		return c;
	}

	public final static Character readChar(DataInput in) throws IOException {
		Character c = null;
		if (in.readByte() == 0) {
			c = new Character(in.readChar());
		}
		return c;
	}

	public final static Date readDate(DataInput in) throws IOException {
		Date d = null;
		if (in.readByte() == 0) {
			d = new Date(in.readLong());
		}
		return d;
	}

	public final static Double readDouble(DataInput in) throws IOException {
		Double d = null;
		if (in.readByte() == 0) {
			d = new Double(in.readDouble());
		}
		return d;
	}

	public final static Float readFloat(DataInput in) throws IOException {
		Float f = null;
		if (in.readByte() == 0) {
			f = new Float(in.readFloat());
		}
		return f;
	}

	public final static Integer readInt(DataInput in) throws IOException {
		Integer i = null;
		if (in.readByte() == 0) {
			i = new Integer(in.readInt());
		}
		return i;
	}

	public final static Long readLong(DataInput in) throws IOException {
		Long l = null;
		if (in.readByte() == 0) {
			l = new Long(in.readLong());
		}
		return l;
	}

	public final static Persistable readPersistable(DataInput in,
			Persistable persistable) throws Exception {
		if (in.readByte() == 0) {
			manager.load(persistable, in.readInt());
		} else {
			persistable = null;
		}
		return persistable;
	}

	public final static Short readShort(DataInput in) throws IOException {
		Short s = null;
		if (in.readByte() == 0) {
			s = new Short(in.readShort());
		}
		return s;
	}

	public final static String readString(DataInput in) throws IOException {
		String s = null;
		if (in.readByte() == 0) {
			s = in.readUTF();
		}
		return s;
	}

	public final static StringBuffer readStringBuffer(DataInput in)
			throws IOException {
		StringBuffer s = null;
		if (in.readByte() == 0) {
			s = new StringBuffer(in.readUTF());
		}
		return s;
	}

	public final static Vector readVector(DataInput in) throws Exception {
		Vector v = null;
		if (in.readByte() == 0) {
			int size = in.readInt();
			v = new Vector(size);
			for (int i = 0; i < size; i++) {
				if (in.readByte() == 1) {
					v.addElement(null);
					continue;
				} else {
					String className = in.readUTF();
					if (className.equals("java.lang.Boolean")) {
						v.addElement(new Boolean(in.readBoolean()));
						continue;
					}
					if (className.equals("java.lang.Byte")) {
						v.addElement(new Byte(in.readByte()));
						continue;
					}
					if (className.equals("java.lang.Character")) {
						v.addElement(new Character(in.readChar()));
						continue;
					}
					if (className.equals("java.lang.Double")) {
						v.addElement(new Double(in.readDouble()));
						continue;
					}
					if (className.equals("java.lang.Float")) {
						v.addElement(new Float(in.readFloat()));
						continue;
					}
					if (className.equals("java.lang.Integer")) {
						v.addElement(new Integer(in.readInt()));
						continue;
					}
					if (className.equals("java.lang.Long")) {
						v.addElement(new Long(in.readLong()));
						continue;
					}
					if (className.equals("java.lang.Short")) {
						v.addElement(new Short(in.readShort()));
						continue;
					}
					if (className.equals("java.lang.String")) {
						v.addElement(new String(in.readUTF()));
						continue;
					}
					if (className.equals("java.util.Date")) {
						v.addElement(new Date(in.readLong()));
						continue;
					}
					Class persistableClass = Class.forName(className);
					Object object = persistableClass.newInstance();
					manager.load((Persistable) object, in.readInt());
					v.addElement(object);
				}
			}
		}
		return v;
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

	public final static void writeByte(DataOutput out, Byte b)
			throws IOException {
		if (b == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeByte(b.byteValue());
		}
	}

	public final static void writeCalendar(DataOutput out, Calendar c)
			throws IOException {
		if (c == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeUTF(c.getTimeZone().getID());
			out.writeLong(c.getTime().getTime());
		}
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

	public final static void writeDate(DataOutput out, Date d)
			throws IOException {
		if (d == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeLong(d.getTime());
		}
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

	public final static void writeFloat(DataOutput out, Float f)
			throws IOException {
		if (f == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeFloat(f.floatValue());
		}
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

	public final static void writeLong(DataOutput out, Long l)
			throws IOException {
		if (l == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeLong(l.longValue());
		}
	}

	public final static void writePersistable(DataOutput out,
			Persistable persistable) throws Exception {
		if (persistable == null) {
			out.writeByte(1);
		} else {
			out.writeByte(0);
			out.writeInt(manager.save(persistable));
		}
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

	public final static void writeString(DataOutput out, String s)
			throws IOException {
		if (s == null) {
			out.write(1);
		} else {
			out.write(0);
			out.writeUTF(s);
		}
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

	public final static void writeVector(DataOutput out, Vector v)
			throws Exception {
		if (v == null) {
			out.writeByte(1);
		} else {
			out.writeByte(0);
			int size = v.size();
			out.writeInt(size);
			for (int c = 0; c < size; c++) {
				Object object = v.elementAt(c);
				if (object == null) {
					out.writeByte(1);
					continue;
				}
				out.writeByte(0);
				String className = object.getClass().getName();
				out.writeUTF(className);
				if (object instanceof Boolean) {
					out.writeBoolean(((Boolean) object).booleanValue());
				} else if (object instanceof Byte) {
					out.writeByte(((Byte) object).byteValue());
				} else if (object instanceof Character) {
					out.writeChar(((Character) object).charValue());
				} else if (object instanceof Double) {
					out.writeDouble(((Double) object).doubleValue());
				} else if (object instanceof Float) {
					out.writeFloat(((Float) object).floatValue());
				} else if (object instanceof Integer) {
					out.writeInt(((Integer) object).intValue());
				} else if (object instanceof Long) {
					out.writeLong(((Long) object).longValue());
				} else if (object instanceof Short) {
					out.writeShort(((Short) object).shortValue());
				} else if (object instanceof String) {
					out.writeUTF(((String) object));
				} else if (object instanceof Date) {
					out.writeLong(((Date) object).getTime());
				} else if (object instanceof __Persistable) {
					int id = manager.save((Persistable) object);
					out.writeInt(id);
				} else {
					throw new FloggyException("The class " + className
							+ " doesn't is a persistable class!");
				}
			}
		}
	}

	private SerializationHelper() {
	}

}