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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import net.sourceforge.floggy.persistence.codegen.CodeGenerator;
import net.sourceforge.floggy.persistence.pool.InputPool;
import net.sourceforge.floggy.persistence.pool.OutputPool;
import net.sourceforge.floggy.persistence.pool.PoolFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Main compiler class!
 * 
 * @author Thiago Rossato <thiagorossato@users.sourceforge.net>
 * @author Thiago Moreira <thiagolm@users.sourceforge.net>
 */
public class Weaver {

	public static final String __PERSISTABLE_CLASSNAME = "net.sourceforge.floggy.persistence.impl.__Persistable";
	private static final Log LOG = LogFactory.getLog(Weaver.class);
	public static final String PERSISTABLE_CLASSNAME = "net.sourceforge.floggy.persistence.Persistable";

	private boolean addDefaultConstructor = true;

	private ClassPool classpathPool;

	private boolean generateSource = false;

	private InputPool inputPool;

	private OutputPool outputPool;

	/**
	 * Creates a new instance
	 * 
	 * @param args
	 */
	public Weaver() {
		this(ClassPool.getDefault());
	}

	/**
	 * Creates a new instance
	 * 
	 * @param args
	 */
	public Weaver(ClassPool classPool) {
		this.classpathPool = classPool;
	}

	private List buildClassTree(CtClass ctClass) throws NotFoundException {
		final CtClass persistable = classpathPool
				.get(Weaver.PERSISTABLE_CLASSNAME);
		List list = new ArrayList();
		CtClass superClass = ctClass;
		String superName = ctClass.getName();
		do {
			list.add(superName);
			superClass = superClass.getSuperclass();
			superName = superClass.getName();
		} while (!superName.equals("java.lang.Object")
				&& superClass.subtypeOf(persistable));
		return list;
	}

	private void embeddedUnderlineCoreClasses() throws IOException {
		embeddedClass("/net/sourceforge/floggy/persistence/impl/FloggyOutputStream.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/ObjectComparator.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/ObjectFilter.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/ObjectSetImpl.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/__Persistable.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/PersistableManagerImpl.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/PersistableMetadata.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/SerializationHelper.class");
	}

	private void embeddedClass(String fileName) throws IOException {
		URL fileURL = getClass().getResource(fileName);
		fileName = fileName.replace('/', File.separatorChar);
		outputPool.addFile(fileURL, fileName);
		classpathPool.makeClass(fileURL.openStream());
	}

	public void execute() throws WeaverException {
		long time = System.currentTimeMillis();
		LOG.info("CLDC version: " + ((isCLDC10()) ? "1.0" : "1.1"));
		try {
			embeddedUnderlineCoreClasses();
			List list = getClassThatImplementsPersistable();
			int classCount = list.size();
			LOG.info("Processing " + classCount + " bytecodes!");
			for (int i = 0; i < classCount; i++) {
				String className = (String) list.get(i);

				CtClass ctClass = this.classpathPool.get(className);

				LOG.info("Processing bytecode " + className + "!");

				CodeGenerator codeGenerator = new CodeGenerator(ctClass,
						generateSource, addDefaultConstructor);
				codeGenerator.generateCode();
				if (generateSource) {
					byte[] source = codeGenerator.getSource().getBytes();
					String fileName = className
							.replace('.', File.separatorChar)
							+ ".txt";
					outputPool.addResource(new ByteArrayInputStream(source),
							fileName);
				}

				// Adds modified class to output pool
				this.outputPool.addClass(ctClass);
				LOG.debug("Bytecode modified.");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new WeaverException(e.getMessage());
		}

		// Status
		time = System.currentTimeMillis() - time;
		LOG.info("Time elapsed: " + time + "ms");
	}

	/**
	 * Returns the class name given a file name.
	 * 
	 * @param fileName
	 * @return
	 */
	private String getClassName(String fileName) {
		if (fileName.endsWith(".class")) {
			String className = fileName.replace(File.separatorChar, '.');
			return className.substring(0, className.length() - 6);
		}

		// File name does not represents a class file.
		return null;
	}

	private List getClassThatImplementsPersistable() throws NotFoundException,
			IOException {
		int classCount = this.inputPool.getFileCount();
		LOG.info("Look up for classes that implements Persistable!");
		List list = new LinkedList();
		final CtClass persistable = classpathPool
				.get(Weaver.PERSISTABLE_CLASSNAME);
		final CtClass __persistable = classpathPool
				.get(Weaver.__PERSISTABLE_CLASSNAME);
		for (int i = 0; i < classCount; i++) {
			String fileName = this.inputPool.getFileName(i);
			String className = getClassName(fileName);
			// Adds non-class files to output pool
			if (className == null) {
				this.outputPool.addFile(inputPool.getFileURL(i), fileName);
				continue;
			}

			CtClass ctClass = this.classpathPool.get(className);
			// verifing if the bytecode are already processed.
			if (ctClass.subtypeOf(persistable)
					&& !ctClass.subtypeOf(__persistable)
					&& !ctClass.isInterface()) {
				// LOG.info(className);
				List tree = buildClassTree(ctClass);
				Collections.reverse(tree);
				for (int j = 0; j < tree.size(); j++) {
					Object object = tree.get(j);
					if (!list.contains(object)) {
						list.add(object);
					}
				}
			} else {
				LOG.debug("Bytecode NOT modified.");
				// Adds non-persistable class to output pool
				this.outputPool.addFile(inputPool.getFileURL(i), fileName);
			}
		}
		return list;
	}

	private boolean isCLDC10() {
		try {
			CtClass ctClass = classpathPool.get("java.io.DataInput");
			ctClass.getMethod("readFloat", "()F");
		} catch (NotFoundException nfex) {
			return true;
		}
		return false;
	}

	public void setAddDefaultConstructor(boolean addDefaultConstructor) {
		this.addDefaultConstructor = addDefaultConstructor;
	}

	/**
	 * Sets the classpath.
	 * 
	 */
	public void setClasspath(String[] classpath) {
		if (classpath != null && classpath.length > 0) {
			for (int i = classpath.length - 1; i >= 0; i--) {
				try {
					this.classpathPool.insertClassPath(classpath[i]);
				} catch (NotFoundException e) {
					// Ignore
				}
			}
		}
	}

	/**
	 * @param generateSource
	 *            the generateSource to set
	 */
	public void setGenerateSource(boolean generateSource) {
		this.generateSource = generateSource;
	}

	/**
	 * Sets the input file.
	 * 
	 */
	public void setInputFile(File inputFile) throws WeaverException {
		this.inputPool = PoolFactory.createInputPool(inputFile);

		try {
			this.classpathPool.insertClassPath(inputFile.getCanonicalPath());
		} catch (NotFoundException e) {
			// Ignore
		} catch (IOException e) {
			// Ignore
		}
	}

	/**
	 * Sets the output file.
	 * 
	 * @param outputFile
	 */
	public void setOutputFile(File outputFile) throws WeaverException {
		this.outputPool = PoolFactory.createOutputPool(outputFile);
	}

}
