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

import java.io.File;
import java.io.FileInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javassist.ClassPool;
import javassist.CtClass;
import junit.framework.TestCase;

public class ZipOutputPoolTest extends TestCase {

	public void testAddClass() throws Exception {
		File tempFile = File.createTempFile("floggy", null, new File("target"));
		ZipOutputPool pool = new ZipOutputPool(tempFile);

		CtClass clazz = ClassPool.getDefault().get("java.lang.String");
		pool.addClass(clazz);
		pool.finish();

		ZipFile zipFile = new ZipFile(tempFile);
		ZipEntry entry = zipFile.getEntry("java/lang/String.class");

		assertNotNull(entry);
	}

	public void testAddFile() throws Exception {
		File tempFile = File.createTempFile("floggy", null, new File("target"));
		ZipOutputPool pool = new ZipOutputPool(tempFile);

		pool.addFile(new File("src/test/resources/test.jar").toURL(),
			"/resources/test.jar");
		pool.finish();

		ZipFile zipFile = new ZipFile(tempFile);
		ZipEntry entry = zipFile.getEntry("resources/test.jar");

		assertNotNull(entry);
	}

	public void testAddResource() throws Exception {
		File tempFile = File.createTempFile("floggy", null, new File("target"));
		ZipOutputPool pool = new ZipOutputPool(tempFile);

		pool.addResource(new FileInputStream("src/test/resources/test.jar"),
			"/resources/test.jar");
		pool.finish();

		ZipFile zipFile = new ZipFile(tempFile);
		ZipEntry entry = zipFile.getEntry("resources/test.jar");

		assertNotNull(entry);
	}

}
