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

package net.sourceforge.floggy.persistence.fr2937635;

import java.util.Date;
import java.util.TimeZone;

import net.sourceforge.floggy.persistence.Persistable;

public class FR2937635 implements Persistable {

	protected boolean boolean1;
	protected byte byte1;
	protected char char1;
	protected double double1;
	protected int int1;
	protected long long1;
	protected short short1;
	protected Boolean boolean2;
	protected Byte byte2;
	protected Character char2;
	protected Double double2;
	protected Integer int2;
	protected Long long2;
	protected Short short2;
	protected String string1;
	protected Date date;
	protected StringBuffer stringBuffer;
	protected TimeZone timeZone;

	public boolean getBoolean1() {
		return boolean1;
	}

	public Boolean getBoolean2() {
		return boolean2;
	}

	public byte getByte1() {
		return byte1;
	}

	public Byte getByte2() {
		return byte2;
	}

	public char getChar1() {
		return char1;
	}

	public Character getChar2() {
		return char2;
	}

	public Date getDate() {
		return date;
	}

	public double getDouble1() {
		return double1;
	}

	public Double getDouble2() {
		return double2;
	}

	public int getInt1() {
		return int1;
	}

	public Integer getInt2() {
		return int2;
	}

	public long getLong1() {
		return long1;
	}

	public Long getLong2() {
		return long2;
	}

	public short getShort1() {
		return short1;
	}

	public Short getShort2() {
		return short2;
	}

	public String getString1() {
		return string1;
	}

	public StringBuffer getStringBuffer() {
		return stringBuffer;
	}

	public TimeZone getTimeZone() {
		return timeZone;
	}

	public boolean isBoolean1() {
		return boolean1;
	}

	public void setBoolean1(boolean boolean1) {
		this.boolean1 = boolean1;
	}

	public void setBoolean2(Boolean boolean2) {
		this.boolean2 = boolean2;
	}

	public void setByte1(byte byte1) {
		this.byte1 = byte1;
	}

	public void setByte2(Byte byte2) {
		this.byte2 = byte2;
	}

	public void setChar1(char char1) {
		this.char1 = char1;
	}

	public void setChar2(Character char2) {
		this.char2 = char2;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public void setDouble1(double double1) {
		this.double1 = double1;
	}

	public void setDouble2(Double double2) {
		this.double2 = double2;
	}

	public void setInt1(int int1) {
		this.int1 = int1;
	}

	public void setInt2(Integer int2) {
		this.int2 = int2;
	}

	public void setLong1(long long1) {
		this.long1 = long1;
	}

	public void setLong2(Long long2) {
		this.long2 = long2;
	}

	public void setShort1(short short1) {
		this.short1 = short1;
	}

	public void setShort2(Short short2) {
		this.short2 = short2;
	}

	public void setString1(String string1) {
		this.string1 = string1;
	}

	public void setStringBuffer(StringBuffer stringBuffer) {
		this.stringBuffer = stringBuffer;
	}

	public void setTimeZone(TimeZone timeZone) {
		this.timeZone = timeZone;
	}

}
