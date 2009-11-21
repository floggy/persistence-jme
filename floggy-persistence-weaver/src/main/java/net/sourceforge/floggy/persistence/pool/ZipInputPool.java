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
package net.sourceforge.floggy.persistence.pool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipInputPool implements InputPool {

	protected File file;

	protected List files;

 	public ZipInputPool(File file) throws IOException {
		this.file= file;
		this.files = new ArrayList();
		this.initFiles(file);
	}

	public int getFileCount() {
		return this.files.size();
	}

	public String getFileName(int index) {
		return this.files.get(index).toString();
	}

	private void initFiles(File file) throws IOException {
		ZipInputStream in = null;
		try {
			in = new ZipInputStream(new FileInputStream(file));
			for (ZipEntry entry = in.getNextEntry(); entry != null; entry = in
					.getNextEntry()) {
				if(!entry.isDirectory()) {
					String name = entry.getName();
					name = name.replace('/', File.separatorChar);
					this.files.add(name);
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
	}

	public URL getFileURL(int index) throws IOException {
		String name = getFileName(index);
		// jar:http://www.foo.com/bar/baz.jar!/COM/foo/Quux.class
		name= "jar:" + file.toURL() + "!/" + name;
		//System.out.println(name);
		return new URL(name);
	}

}
