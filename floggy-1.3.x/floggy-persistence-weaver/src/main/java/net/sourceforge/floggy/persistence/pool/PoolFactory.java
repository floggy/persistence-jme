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

import net.sourceforge.floggy.persistence.WeaverException;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class PoolFactory {
	/**
	 * Creates a new PoolFactory object.
	 */
	protected PoolFactory() {
	}

	/**
	 * DOCUMENT ME!
	*
	* @param file DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws WeaverException DOCUMENT ME!
	*/
	public static InputPool createInputPool(File file) throws WeaverException {
		if (!file.exists()) {
			throw new WeaverException(file.getName()
				+ " does not exists. (Full path: " + file.getPath() + ")");
		}

		if (file.isFile() && file.getName().endsWith(".jar")) {
			try {
				return new JarInputPool(file);
			} catch (IOException e) {
				throw new WeaverException("Invalid input file type.", e);
			}
		} else if (file.isFile() && file.getName().endsWith(".zip")) {
			try {
				return new ZipInputPool(file);
			} catch (IOException e) {
				throw new WeaverException("Invalid input file type.", e);
			}
		} else if (file.isDirectory()) {
			try {
				return new DirectoryInputPool(file);
			} catch (IOException e) {
				throw new WeaverException(e);
			}
		}

		throw new WeaverException(file.getName()
			+ " is a invalid file type. Valid types are: .jar, .zip and directories.");
	}

	/**
	 * DOCUMENT ME!
	*
	* @param file DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws WeaverException DOCUMENT ME!
	*/
	public static OutputPool createOutputPool(File file)
		throws WeaverException {
		String fileName = file.getName();

		try {
			if (fileName.endsWith(".jar")) {
				return new ZipOutputPool(file);
			} else if (fileName.endsWith(".zip")) {
				return new ZipOutputPool(file);
			} else {
				return new DirectoryOutputPool(file);
			}
		} catch (Exception e) {
			throw new WeaverException(e.getMessage(), e);
		}
	}
}
