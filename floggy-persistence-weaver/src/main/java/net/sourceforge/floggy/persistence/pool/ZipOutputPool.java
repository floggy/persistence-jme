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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

public class ZipOutputPool implements OutputPool {

	protected ZipOutputStream out;

	public ZipOutputPool(File file) throws IOException {
		if (file == null) {
			throw new IllegalArgumentException(
					"The file parameter cannot be null!");
		}

		out = new ZipOutputStream(new FileOutputStream(file));
	}

	public void addClass(CtClass ctClass) throws NotFoundException,
		IOException, CannotCompileException {

		String fileName = ctClass.getName();
		fileName = fileName.replace('.', '/').concat(".class");
		addResource(new ByteArrayInputStream(ctClass.toBytecode()), fileName);
	}

	public void addFile(URL fileURL, String fileName) throws IOException {
		addResource(fileURL.openStream(), fileName);
	}

	public void addResource(InputStream resourceStream, String fileName)
		throws IOException {

		fileName = fileName.replace(File.separatorChar, '/');
		if (fileName.startsWith("/")) {
			fileName = fileName.substring(1);
		}

		ZipEntry entry = new ZipEntry(fileName);
		out.putNextEntry(entry);
		IOUtils.copy(resourceStream, out);
		out.closeEntry();
	}

	public void finish() throws IOException {
		out.finish();
		out.close();
	}

}
