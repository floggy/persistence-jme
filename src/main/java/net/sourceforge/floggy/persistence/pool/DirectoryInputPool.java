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
package net.sourceforge.floggy.persistence.pool;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

public class DirectoryInputPool implements InputPool {

	protected File rootDirectory;

	protected Vector files;

	public DirectoryInputPool(File directory) throws IOException {
		this.rootDirectory = directory;
		this.files = new Vector();
		this.initFiles(directory);
	}

	public int getFileCount() {
		return this.files.size();
	}

	public String getFileName(int index) {
		return this.files.get(index).toString();
	}

	private void initFiles(File directory) throws IOException {
		File[] files = directory.listFiles();

		if (files != null) {
			String rootPath = this.rootDirectory.getCanonicalPath();

			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					this.initFiles(files[i]);
				} else if (files[i].isFile()) {
					String filePath = files[i].getCanonicalPath();

					String className = filePath.substring(rootPath.length());
					if (className.startsWith(File.separator)) {
						className = className.substring(1);
					}

					this.files.add(className);
				}
			}
		}
	}

	public URL getFileURL(int index) throws IOException {
		File file = new File(rootDirectory, getFileName(index));
		return file.toURI().toURL();
	}
}
