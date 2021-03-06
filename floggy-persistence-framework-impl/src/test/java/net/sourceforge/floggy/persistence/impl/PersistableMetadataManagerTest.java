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
package net.sourceforge.floggy.persistence.impl;

import org.microemu.MIDletBridge;

import junit.framework.TestCase;

import net.sourceforge.floggy.persistence.RMSMemoryMicroEmulator;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class PersistableMetadataManagerTest extends TestCase {
	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testGetBytecodeVersion() throws Exception {
		assertEquals(PersistableMetadataManager.CURRENT_VERSION,
			PersistableMetadataManager.getBytecodeVersion());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testGetRMSVersion() throws Exception {
		MIDletBridge.setMicroEmulator(new RMSMemoryMicroEmulator("target/rms"));
		PersistableMetadataManager.load();
		assertEquals(PersistableMetadataManager.getBytecodeVersion(),
			PersistableMetadataManager.getRMSVersion());
	}
}
