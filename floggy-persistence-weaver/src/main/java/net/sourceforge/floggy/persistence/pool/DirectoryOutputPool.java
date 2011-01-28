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
package net.sourceforge.floggy.persistence.pool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

import org.apache.commons.io.IOUtils;

public class DirectoryOutputPool implements OutputPool {

    protected File directory;

    public DirectoryOutputPool(File directory) {
	this.directory = directory;
	if (this.directory != null && !this.directory.exists()) {
	    this.directory.mkdirs();
	}
    }

    public void addClass(CtClass ctClass) throws NotFoundException,
	    IOException, CannotCompileException {
	File classFile = this.getOutputFile(ctClass);
	classFile.getParentFile().mkdirs();

	if (classFile.exists()) {
	    classFile.delete();
	}

	write(new ByteArrayInputStream(ctClass.toBytecode()), classFile);
    }

    protected File getOutputFile(CtClass ctClass) {
	File outputFile = new File(directory.getPath() + File.separatorChar
		+ ctClass.getName().replace('.', File.separatorChar) + ".class");
	return outputFile;
    }

    private void write(InputStream in, File file) throws IOException {
	// copy to memory the resource
	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	IOUtils.copy(in, baos);
	baos.flush();

	ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	FileOutputStream out = new FileOutputStream(file);
	IOUtils.copy(bais, out);
	out.flush();
	out.close();
	in.close();
    }

    public void addResource(InputStream resourceStream, String fileName)
	    throws IOException {
	File temp;
	int lastIndex = fileName.lastIndexOf(File.separatorChar);
	if (lastIndex == -1) {
	    temp = directory;
	} else {
	    temp = new File(directory, fileName.substring(0, lastIndex));
	}
	if (!temp.exists()) {
	    temp.mkdirs();
	}
	write(resourceStream, new File(temp, fileName.substring(lastIndex + 1)));
    }

    public void addFile(URL fileURL, String fileName) throws IOException {
	addResource(fileURL.openStream(), fileName);
    }

    public void finish() {

    }

}
