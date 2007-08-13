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
package net.sourceforge.floggy.persistence;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class WeaverTask extends Task {

	protected Path classpath;

	protected File input;

	protected File output;

	protected boolean generateSource;

	public void execute() throws BuildException {
		Weaver compiler = new Weaver();
		try {
			compiler.setClasspath(classpath.list());
			compiler.setInputFile(input);
			compiler.setOutputFile(output);
			compiler.setGenerateSource(generateSource);
			compiler.execute();
		} catch (WeaverException e) {
			throw new BuildException(e);
		}
	}

	public void setBootClasspath(Path path) {
		addToClasspath(path);
	}

	/**
	 * Adds a reference to a classpath defined elsewhere.
	 * 
	 * @param r
	 *            a reference to a classpath
	 */
	public void setBootClasspathRef(Reference r) {
		addToClasspath((Path) r.getReferencedObject());

	}

	public void setClasspath(Path path) {
		addToClasspath(path);
	}

	/**
	 * Adds a reference to a classpath defined elsewhere.
	 * 
	 * @param r
	 *            a reference to a classpath
	 */
	public void setClasspathRef(Reference r) {
		addToClasspath((Path) r.getReferencedObject());
	}

	private void addToClasspath(Path path) {
		if (classpath == null) {
			classpath = new Path(getProject());
		}
		classpath.append(path);
	}

	public void setGenerateSource(boolean generateSource) {
		this.generateSource = generateSource;
	}

	public void setInput(File input) {
		this.input = input;
	}

	public void setOutput(File output) {
		this.output = output;
	}

}
