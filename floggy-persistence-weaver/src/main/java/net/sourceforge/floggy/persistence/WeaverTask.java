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

import java.io.File;

import org.apache.commons.logging.LogFactory;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class WeaverTask extends Task {
	/**
	 * DOCUMENT ME!
	 */
	protected File configurationFile;

	/**
	 * DOCUMENT ME!
	 */
	protected File input;

	/**
	 * DOCUMENT ME!
	 */
	protected File output;

	/**
	 * DOCUMENT ME!
	 */
	protected Path classpath;

	/**
	 * DOCUMENT ME!
	 */
	protected boolean addDefaultConstructor = true;

	/**
	 * DOCUMENT ME!
	 */
	protected boolean generateSource = false;

	/**
	 * DOCUMENT ME!
	*
	* @throws BuildException DOCUMENT ME!
	*/
	public void execute() throws BuildException {
		AntLog.setTask(this);
		LogFactory.getFactory()
		 .setAttribute("org.apache.commons.logging.Log", AntLog.class.getName());

		Weaver weaver = new Weaver();

		try {
			weaver.setClasspath(classpath.list());
			weaver.setInputFile(input);
			weaver.setOutputFile(output);

			if (configurationFile == null) {
				Configuration configuration = new Configuration();
				configuration.setAddDefaultConstructor(addDefaultConstructor);
				configuration.setGenerateSource(generateSource);
				weaver.setConfiguration(configuration);
			} else {
				weaver.setConfigurationFile(configurationFile);
			}

			weaver.execute();
		} catch (WeaverException e) {
			throw new BuildException(e);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param addDefaultConstructor DOCUMENT ME!
	*/
	public void setAddDefaultConstructor(boolean addDefaultConstructor) {
		this.addDefaultConstructor = addDefaultConstructor;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param path DOCUMENT ME!
	*/
	public void setBootClasspath(Path path) {
		addToClasspath(path);
	}

	/**
	* Adds a reference to a classpath defined elsewhere.
	*
	* @param r a reference to a classpath
	*/
	public void setBootClasspathRef(Reference r) {
		addToClasspath((Path) r.getReferencedObject());
	}

	/**
	 * DOCUMENT ME!
	*
	* @param path DOCUMENT ME!
	*/
	public void setClasspath(Path path) {
		addToClasspath(path);
	}

	/**
	* Adds a reference to a classpath defined elsewhere.
	*
	* @param r a reference to a classpath
	*/
	public void setClasspathRef(Reference r) {
		addToClasspath((Path) r.getReferencedObject());
	}

	/**
	 * DOCUMENT ME!
	*
	* @param configurationFile DOCUMENT ME!
	*/
	public void setConfigurationFile(File configurationFile) {
		this.configurationFile = configurationFile;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param generateSource DOCUMENT ME!
	*/
	public void setGenerateSource(boolean generateSource) {
		this.generateSource = generateSource;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param input DOCUMENT ME!
	*/
	public void setInput(File input) {
		this.input = input;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param output DOCUMENT ME!
	*/
	public void setOutput(File output) {
		this.output = output;
	}

	private void addToClasspath(Path path) {
		if (classpath == null) {
			classpath = new Path(getProject());
		}

		classpath.append(path);
	}
}
