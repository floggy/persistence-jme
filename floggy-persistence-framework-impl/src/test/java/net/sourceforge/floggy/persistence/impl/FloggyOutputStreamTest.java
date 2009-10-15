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

import java.io.IOException;

import junit.framework.TestCase;

public class FloggyOutputStreamTest extends TestCase {
	
	public void testToByteArrayEmpty() {
		FloggyOutputStream fos= new FloggyOutputStream();
		
		byte[] data= fos.toByteArray();
		
		assertNotNull(data);
		
		assertEquals(0, data.length);
	}

	public void testToByteArrayFilled() throws IOException {
		FloggyOutputStream fos= new FloggyOutputStream();
		
		byte temp= 90;
		
		fos.write(temp);
		
		byte[] data= fos.toByteArray();
		
		assertNotNull(data);
		
		assertEquals(1, data.length);
		
		assertEquals(temp, data[0]);
	}

}
