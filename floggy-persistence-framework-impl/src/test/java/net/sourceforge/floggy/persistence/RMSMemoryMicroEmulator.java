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
package net.sourceforge.floggy.persistence;

import java.io.InputStream;

import org.microemu.MIDletContext;
import org.microemu.MicroEmulator;
import org.microemu.RecordStoreManager;

import org.microemu.app.launcher.Launcher;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class RMSMemoryMicroEmulator implements MicroEmulator {
	private RecordStoreManager rsManager;

	/**
	 * Creates a new RMSMemoryMicroEmulator object.
	 *
	 * @param rmsPath DOCUMENT ME!
	 */
	public RMSMemoryMicroEmulator(String rmsPath) {
		rsManager = new FileRecordStoreManager(rmsPath);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param permission DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int checkPermission(String permission) {
		return 0;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param midletContext DOCUMENT ME!
	*/
	public void destroyMIDletContext(MIDletContext midletContext) {
	}

	/**
	 * DOCUMENT ME!
	*
	* @param key DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getAppProperty(String key) {
		return null;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public Launcher getLauncher() {
		return null;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public RecordStoreManager getRecordStoreManager() {
		return rsManager;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param clazz DOCUMENT ME!
	* @param name DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public InputStream getResourceAsStream(Class clazz, String name) {
		return null;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param midletContext DOCUMENT ME!
	*/
	public void notifyDestroyed(MIDletContext midletContext) {
	}

	/**
	 * DOCUMENT ME!
	*
	* @param URL DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean platformRequest(String URL) {
		return false;
	}
}
