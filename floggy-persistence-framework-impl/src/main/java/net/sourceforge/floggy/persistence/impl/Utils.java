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

import java.io.EOFException;
import java.io.IOException;
import java.io.UTFDataFormatException;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.Persistable;

public class Utils {
	
	private static Class __persistableClass;
	
	static {
		try {
			__persistableClass = Class.forName("net.sourceforge.floggy.persistence.impl.__Persistable");
		} catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	public static __Persistable checkArgumentAndCast(Persistable persistable) {
		if (persistable == null) {
			throw new IllegalArgumentException(
					"The persistable object cannot be null!");
		}
		if (persistable instanceof __Persistable) {
			return (__Persistable) persistable;
		} else {
			throw new IllegalArgumentException(
					persistable.getClass().getName()
							+ " is not a valid persistable class. Check the weaver execution!");
		}
	}

	public static __Persistable createInstance(Class persistableClass)
			throws FloggyException {
		validatePersistableClassArgument(persistableClass);
		// Try to create a new instance of the persistable class.
		try {
			return (__Persistable) persistableClass.newInstance();
		} catch (Exception ex) {
			throw Utils.handleException(ex);
		}
	}

	public static FloggyException handleException(Exception ex) {
		if (ex instanceof FloggyException) {
			return (FloggyException)ex;
		}
		String message = ex.getMessage();
		if (message == null) {
			message= ex.getClass().getName();
		}
		return new FloggyException(message, ex);
	}

	public static String readUTF8(byte[] data) throws IOException {
		int ch1 = data[0] & 0xff;
		int ch2 = data[1] & 0xff;

		if ((ch1 | ch2) < 0) {
            throw new EOFException();
        }

        int utflen = (ch1 << 8) + (ch2 << 0) + 2;
        byte[] bytearr = new byte[utflen];
        char[] chararr = new char[utflen];

        int c, char2, char3;
        int count = 2;
        int chararr_count=0;

        while (count < utflen) {
            c = (int) data[count] & 0xff;      
            if (c > 127) break;
            count++;
            chararr[chararr_count++]=(char)c;
        }

        while (count < utflen) {
            c = (int) bytearr[count] & 0xff;
            switch (c >> 4) {
                case 0: case 1: case 2: case 3: case 4: case 5: case 6: case 7:
                    /* 0xxxxxxx*/
                    count++;
                    chararr[chararr_count++]=(char)c;
                    break;
                case 12: case 13:
                    /* 110x xxxx   10xx xxxx*/
                    count += 2;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                            "malformed input: partial character at end");
                    char2 = (int) bytearr[count-1];
                    if ((char2 & 0xC0) != 0x80)
                        throw new UTFDataFormatException(
                            "malformed input around byte " + count); 
                    chararr[chararr_count++]=(char)(((c & 0x1F) << 6) | 
                                                    (char2 & 0x3F));  
                    break;
                case 14:
                    /* 1110 xxxx  10xx xxxx  10xx xxxx */
                    count += 3;
                    if (count > utflen)
                        throw new UTFDataFormatException(
                            "malformed input: partial character at end");
                    char2 = (int) bytearr[count-2];
                    char3 = (int) bytearr[count-1];
                    if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
                        throw new UTFDataFormatException(
                            "malformed input around byte " + (count-1));
                    chararr[chararr_count++]=(char)(((c     & 0x0F) << 12) |
                                                    ((char2 & 0x3F) << 6)  |
                                                    ((char3 & 0x3F) << 0));
                    break;
                default:
                    /* 10xx xxxx,  1111 xxxx */
                    throw new UTFDataFormatException(
                        "malformed input around byte " + count);
            }
        }
        // The number of chars produced may be less than utflen
        return new String(chararr, 0, chararr_count);
	}

	public static void validatePersistableClassArgument(Class persistableClass)
			throws IllegalArgumentException {
		// testing if persistableClass is null
		if (persistableClass == null) {
			throw new IllegalArgumentException(
					"The persistable class cannot be null!");
		}
		// Checks if the persistableClass is a valid persistable class.
		if (!__persistableClass.isAssignableFrom(persistableClass)) {
			throw new IllegalArgumentException(
					persistableClass.getName()
							+ " is not a valid persistable class. Check the weaver execution!");
		}
	}

}
