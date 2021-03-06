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
package net.sourceforge.floggy.maven;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Dependency;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import org.codehaus.plexus.archiver.jar.JarArchiver;

/**
* The preverify MoJo runs the preverify command from the given J2ME SDK.<p>Preverification
* takes place in the phase process-classes, that means after successfull
* compilation. The MoJo takes the classes to preverify from
* ${project.build.outputDirectory} and places the preverified classes into
* the same directory. Therefore no change to the packaging phase is
* neccessary.</p>
*
* @author <a href="thiago.leao.moreira@gmail.com">Thiago Moreira</a>
*
* @goal package
 */
public class PackageMojo extends AbstractMojo {
	/**
	* The Maven project reference.
	*
	* @parameter expression="${project}"
	* @required
	 */
	private MavenProject project;

	/**
	* The final of the package who contains all dependencies classes.
	*
	* @parameter
	 */
	private String embbededFinalName;

	/**
	 * DOCUMENT ME!
	*
	* @throws MojoExecutionException DOCUMENT ME!
	* @throws MojoFailureException DOCUMENT ME!
	*/
	public void execute() throws MojoExecutionException, MojoFailureException {
		File targetDirectory = new File(project.getBuild().getDirectory());

		String finalName = project.getBuild().getFinalName();
		File mainFinalFile =
			new File(targetDirectory, finalName + "." + project.getPackaging());

		if (!mainFinalFile.exists()) {
			throw new MojoFailureException("Unable to find the package "
				+ mainFinalFile.getName() + ". Run mvn package to build it!");
		}

		File embbededOutput = new File(targetDirectory, "embbeded");

		if (embbededFinalName == null) {
			embbededFinalName = finalName + "-embbeded";
		} else {
			if (embbededFinalName.endsWith(".jar")) {
				embbededFinalName = embbededFinalName.substring(0,
						embbededFinalName.length() - 3);
			}
		}

		File embbededFinalFile =
			new File(targetDirectory, embbededFinalName + "."
				+ project.getPackaging());

		File embbededJADFinalFile =
			new File(targetDirectory, embbededFinalName + ".jad");
		File mainJADFinalFile = new File(targetDirectory, finalName + ".jad");

		try {
			unzipDependencies(embbededOutput);

			ZipUtils.unzip(mainFinalFile, embbededOutput);

			File[] directories = new File[] { embbededOutput };
			this.getLog()
			 .info("Creating the embbeded package: " + embbededFinalFile.getName());

			JarArchiver archiver = new JarArchiver();

			for (int i = 0; i < directories.length; i++) {
				archiver.addDirectory(directories[i]);
			}

			archiver.setManifest(new File(embbededOutput + File.separator
					+ "META-INF", "MANIFEST.MF"));
			archiver.setDestFile(embbededFinalFile);
			archiver.createArchive();

			if (mainJADFinalFile.exists()) {
				Properties jad = new Properties();
				InputStream in = new FileInputStream(mainJADFinalFile);

				jad.load(in);
				jad.put("MIDlet-Jar-URL", embbededFinalFile.getName());
				jad.put("MIDlet-Jar-Size", String.valueOf(embbededFinalFile.length()));
				this.getLog()
				 .info("Creating the embbeded JAD file: "
					 + embbededJADFinalFile.getName());
				saveJAD(jad, embbededJADFinalFile);

				in.close();
			}
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	private void saveJAD(Properties jad, File embbededJADFinalFile)
		throws IOException {
		Enumeration keys = jad.keys();
		PrintWriter writer = new PrintWriter(new FileWriter(embbededJADFinalFile));

		while (keys.hasMoreElements()) {
			String key = (String) keys.nextElement();
			writer.println(key + ": " + jad.getProperty(key));
		}

		writer.close();
	}

	private void unzipDependencies(File outputDirectory)
		throws IOException, DependencyResolutionRequiredException {
		List compileClasspathElements = project.getCompileClasspathElements();
		List compileDependecies = project.getCompileDependencies();

		for (Iterator iter = compileDependecies.iterator(); iter.hasNext();) {
			Dependency dependency = (Dependency) iter.next();

			if ("COMPILE".equalsIgnoreCase(dependency.getScope())) {
				String groupId =
					dependency.getGroupId().replace('.', File.separatorChar);
				String artifactId = dependency.getArtifactId();
				String version = dependency.getVersion();
				String type = dependency.getType();
				String dependencyName = artifactId + '-' + version + '.' + type;
				String suffix =
					groupId + File.separatorChar + artifactId + File.separatorChar
					+ version + File.separatorChar + dependencyName;

				for (Iterator iterator = compileClasspathElements.iterator();
					 iterator.hasNext();) {
					String path = (String) iterator.next();

					if (path.endsWith(suffix)) {
						this.getLog()
						 .info("Embbeding the " + dependencyName
							 + " dependency in the final package.");
						ZipUtils.unzip(new File(path), outputDirectory);
					}
				}
			}
		}
	}
}
