/**
 *  Copyright 2006 Floggy Open Source Group
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
package net.sourceforge.floggy.persistence.rms.beans.wrapper.array;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.wrapper.array.TestByte;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class ByteTest extends AbstractTest {

	static Byte[] att = new Byte[] { new Byte((byte) -128), new Byte((byte) 2),
			new Byte((byte) 56) };

	protected Class getParameterType() {
		return Byte[].class;
	}
	
	public Object getValueForSetMethod() {
		return att;
	}

	public Persistable newInstance() {
		return new TestByte();
	}

}
