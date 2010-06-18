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

package net.sourceforge.floggy.persistence;

import java.io.InputStream;

import org.microemu.MIDletContext;
import org.microemu.MicroEmulator;
import org.microemu.RecordStoreManager;
import org.microemu.app.launcher.Launcher;

public class RMSMemoryMicroEmulator implements MicroEmulator {

	private RecordStoreManager rsManager ;//= new MemoryRecordStoreManager();

	public  RMSMemoryMicroEmulator(String rmsPath) {
		rsManager = new FileRecordStoreManager(rmsPath);
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

	public int checkPermission(String permission) {
		return 0;
	}

	public Launcher getLauncher() {
		return null;
	}

	public InputStream getResourceAsStream(Class clazz, String name) {
		return null;
	}
	
}
