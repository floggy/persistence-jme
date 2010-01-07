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
import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.List;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class DirectoryInputPool implements InputPool {
	/**
	 * DOCUMENT ME!
	 */
	protected File rootDirectory;

	/**
	 * DOCUMENT ME!
	 */
	protected List files;

	/**
	 * Creates a new DirectoryInputPool object.
	 *
	 * @param directory DOCUMENT ME!
	 *
	 * @throws IOException DOCUMENT ME!
	 */
	public DirectoryInputPool(File directory) throws IOException {
		this.rootDirectory = directory;
		this.files = new ArrayList();
		this.initFiles(directory);
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
		return this.files.get(index).toString();
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
		File file = new File(rootDirectory, getFileName(index));

		return file.toURI().toURL();
	}

	private void initFiles(File directory) throws IOException {
		File[] temp = directory.listFiles();

		if (temp != null) {
			String rootPath = this.rootDirectory.getCanonicalPath();

			for (int i = 0; i < temp.length; i++) {
				if (temp[i].isDirectory()) {
					this.initFiles(temp[i]);
				} else if (temp[i].isFile()) {
					String filePath = temp[i].getCanonicalPath();

					String className = filePath.substring(rootPath.length());

					if (className.startsWith(File.separator)) {
						className = className.substring(1);
					}

					this.files.add(className);
				}
			}
		}
	}
}
