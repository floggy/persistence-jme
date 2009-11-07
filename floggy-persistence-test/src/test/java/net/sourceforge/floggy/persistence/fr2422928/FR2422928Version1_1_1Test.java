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
package net.sourceforge.floggy.persistence.fr2422928;

import net.sourceforge.floggy.persistence.impl.MetadataManagerUtil;


public class FR2422928Version1_1_1Test extends FR2422928AbstractVersionTest {

	public String getVersion() {
		return "1.1.1";
	}
	
	public void testGetRMSVersion() {
		assertEquals("1.1.0", MetadataManagerUtil.getRMSVersion());
	}
}