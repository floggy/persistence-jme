/**
 *  Copyright (c) 2005-2008 Floggy Open Source Group. All rights reserved.
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
import java.util.Arrays;
import java.util.Vector;

public class Main {

	/**
	 * Get the classpath option.
	 * 
	 */
	private static String[] initClasspath(Vector params) {
		String[] classpath = null;

		int index = params.indexOf("-cp");
		if (index != -1) {
			try {
				String value = params.get(index + 1).toString();
				classpath = value.split(File.pathSeparator);

				// Remove classpath options
				params.remove("-cp");
				params.remove(value);
			} catch (ArrayIndexOutOfBoundsException e) {
				System.out.println("-cp requires class path specification");
				usage();
			}
		}

		return classpath;
	}

	/**
	 * Get the input file option.
	 * 
	 */
	private static File initInputFile(Vector params) {
		File inputFile = null;

		if (params.size() == 1) {
			String value = params.get(0).toString();

			inputFile = new File(value.trim());
			if (inputFile.exists()) {
				params.remove(0);
			} else {
				System.out.println("File \"" + value + "\" does not exists.");
				usage();
			}
		} else {
			System.out.println("Invalid number of parameters.");
			usage();
		}

		return inputFile;
	}

	/**
	 * Get the output file.
	 * 
	 * @param args
	 * @return
	 */
	private static File initOutputFile(Vector params) {
		File outputFile = null;

		int index = params.indexOf("-o");
		if (index != -1) {
			String value = params.get(index + 1).toString();

			outputFile = new File(value.trim());

			params.remove(index);
			params.remove(value);

		}

		return outputFile;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Vector params = new Vector(Arrays.asList(args));
		// System.out.println(params);

		String[] classpath = initClasspath(params);
		File outputFile = initOutputFile(params);
		File inputFile = initInputFile(params);
		boolean generateSource = params.indexOf("-s") != -1;

		// If no output file is defined, sets the output file the same as the
		// input file
		if (outputFile == null) {
			outputFile = inputFile;
		}

		Weaver weaver = new Weaver();

		try {
			weaver.setClasspath(classpath);
			weaver.setOutputFile(outputFile);
			weaver.setInputFile(inputFile);
			//compiler.setGenerateSource(generateSource);
			weaver.execute();
		} catch (WeaverException e) {
			e.printStackTrace();
			System.out.println(e.getMessage());
		}
	}

	/**
	 * Prints usage message.
	 */
	private static final void usage() {
		System.out.println("Usage:");
		System.out.println("java " + Main.class.getName()
				+ " [-options] jarfile | zipfile | directory");
		System.out.println();
		System.out.println("Where options are:");
		System.out
				.println("-cp\t<Class search path of directories and zip/jar files separeted by "
						+ File.pathSeparatorChar + ">");
		System.out
				.println("-o\t<Output file or directory where files will be stored>");

		System.out
				.println("-s\t if provided the code generated will be saved in a file");

		System.exit(1);
	}

}
