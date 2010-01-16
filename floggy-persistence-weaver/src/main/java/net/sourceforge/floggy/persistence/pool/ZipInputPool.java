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
package net.sourceforge.floggy.persistence.pool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class ZipInputPool implements InputPool {
	/**
	 * DOCUMENT ME!
	 */
	protected File file;

	/**
	 * DOCUMENT ME!
	 */
	protected List files;

	/**
	 * Creates a new ZipInputPool object.
	 *
	 * @param file DOCUMENT ME!
	 *
	 * @throws IOException DOCUMENT ME!
	 */
	public ZipInputPool(File file) throws IOException {
		this.file = file;
		this.files = new ArrayList();
		this.initFiles(file);
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getFileCount() {
		return this.files.size();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param index DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getFileName(int index) {
		String fileName = (String) this.files.get(index);
		return fileName.replace('/', File.separatorChar);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param index DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public URL getFileURL(int index) throws IOException {
		String name = (String) this.files.get(index);

		name = "jar:" + file.toURL() + "!/" + name;

		return new URL(name);
	}

	private void initFiles(File file) throws IOException {
		ZipInputStream in = null;

		try {
			in = new ZipInputStream(new FileInputStream(file));

			for (ZipEntry entry = in.getNextEntry(); entry != null;
				 entry = in.getNextEntry()) {
				if (!entry.isDirectory()) {
					this.files.add(entry.getName());
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}
}
