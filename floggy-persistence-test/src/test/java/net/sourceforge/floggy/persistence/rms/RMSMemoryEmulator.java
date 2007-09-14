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
package net.sourceforge.floggy.persistence.rms;

import org.microemu.MIDletContext;
import org.microemu.MicroEmulator;
import org.microemu.RecordStoreManager;
import org.microemu.util.MemoryRecordStoreManager;

public class RMSMemoryEmulator implements MicroEmulator {
	
	private static RMSMemoryEmulator instance= new RMSMemoryEmulator();
	public static RMSMemoryEmulator getInstance() {
		return instance;
	}
	
	private RecordStoreManager rsManager= new MemoryRecordStoreManager();

	private RMSMemoryEmulator() {
		
	}

	public void destroyMIDletContext(MIDletContext midletContext) {

	}
	
	public String getAppProperty(String key) {
		return null;
	}

	public RecordStoreManager getRecordStoreManager() {
		return rsManager;
	}

	public void notifyDestroyed(MIDletContext midletContext) {
	}

	public boolean platformRequest(String URL) {
		return false;
	}

}
