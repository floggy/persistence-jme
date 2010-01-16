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

import junit.framework.TestCase;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class JarInputPoolTest extends TestCase {
	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	public void testGetFileCount() throws IOException {
		File file = new File("target/test-classes/test.jar");
		JarInputPool pool = new JarInputPool(file);

		assertEquals(2, pool.getFileCount());
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testGetFileName() throws Exception {
		File file = new File("target/test-classes/test.jar");
		JarInputPool pool = new JarInputPool(file);

		String nameExpected = "META-INF"+ File.separator + "MANIFEST.MF";
		assertEquals(nameExpected, pool.getFileName(0));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws Exception DOCUMENT ME!
	*/
	public void testGetFileURL() throws Exception {
		File file = new File("target/test-classes/test.jar");
		JarInputPool pool = new JarInputPool(file);

		URL urlExpected = new URL("jar:" + file.toURL() + "!/META-INF/MANIFEST.MF");
		assertEquals(urlExpected, pool.getFileURL(0));
	}
}
