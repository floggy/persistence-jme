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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import net.sourceforge.floggy.persistence.codegen.CodeGenerator;
import net.sourceforge.floggy.persistence.impl.FloggyProperties;
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

	private ClassPool classpathPool;
	
	private Configuration configuration = new Configuration();

	private InputPool inputPool;

	private OutputPool outputPool;

	/**
	 * Creates a new instance
	 * 
	 * @param args
	 */
	public Weaver() {
		this(new ClassPool());
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
			// adicionando
			if (!configuration.containsPersistable(superName)) {
				configuration
						.addPersistable(createPersistableConfig(superClass));
			}

			superClass = superClass.getSuperclass();
			superName = superClass.getName();
		} while (!superName.equals("java.lang.Object")
				&& superClass.subtypeOf(persistable));
		return list;
	}

	private PersistableConfiguration createPersistableConfig(CtClass ctClass) {
		String className = ctClass.getName();
		String recordStoreName = ctClass.getSimpleName() + className.hashCode();
		PersistableConfiguration pConfig = new PersistableConfiguration();
		pConfig.setClassName(className);
		pConfig.setRecordStoreName(recordStoreName);
		return pConfig;
	}

	private void embeddedClass(String fileName) throws IOException {
		URL fileURL = getClass().getResource(fileName);
		fileName = fileName.replace('/', File.separatorChar);
		outputPool.addFile(fileURL, fileName);
		classpathPool.makeClass(fileURL.openStream());
	}

	private void embeddedUnderlineCoreClasses() throws IOException {
		embeddedClass("/net/sourceforge/floggy/persistence/impl/FloggyOutputStream.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/FloggyProperties.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/ObjectComparator.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/ObjectFilter.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/ObjectSetImpl.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/__Persistable.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/PersistableManagerImpl.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/PersistableManagerImpl$RecordStoreReference.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/PersistableMetadata.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/SerializationHelper.class");
	}
	
	protected void adaptFrameworkToTargetCLDC() throws IOException, CannotCompileException, NotFoundException {
		CtClass ctClass= this.classpathPool.get("net.sourceforge.floggy.persistence.impl.SerializationHelper");
		if (isCLDC10()) {
			CtMethod[] methods= ctClass.getMethods();
			for (int i = 0; i < methods.length; i++) {
				String methodName= methods[i].getName(); 
				if (methodName.indexOf("Float") != -1 || methodName.indexOf("Double") != -1 || methodName.equals("readObject") || methodName.equals("writeObject")) {
					ctClass.removeMethod(methods[i]);
				}
			}
			//this is done in two steps because we can't guarantee that the read/writeVector methods will be removed before the rename step.   
			for (int i = 0; i < methods.length; i++) {
				String methodName= methods[i].getName(); 
				if (methodName.equals("readObjectCLDC10")) {
					methods[i].setName("readObject");
				}
				if (methodName.equals("writeObjectCLDC10")) {
					methods[i].setName("writeObject");
				}
			}
		} else {
			CtMethod[] methods= ctClass.getMethods();
			for (int i = 0; i < methods.length; i++) {
				String methodName= methods[i].getName(); 
				if (methodName.indexOf("CLDC10") != -1) {
					ctClass.removeMethod(methods[i]);
				}
			}
		}
		outputPool.addClass(ctClass);
	}

	public void execute() throws WeaverException {
		long time = System.currentTimeMillis();
		LOG.info("Floggy Persistence Weaver - "+FloggyProperties.CURRENT_VERSION);
		LOG.info("CLDC version: " + ((isCLDC10()) ? "1.0" : "1.1"));
		try {
//			readConfiguration();
			embeddedUnderlineCoreClasses();
			adaptFrameworkToTargetCLDC();
			List list = getClassThatImplementsPersistable();
			int classCount = list.size();
			LOG.info("Processing " + classCount + " bytecodes!");
			for (int i = 0; i < classCount; i++) {
				String className = (String) list.get(i);

				CtClass ctClass = this.classpathPool.get(className);

				LOG.info("Processing bytecode " + className + "!");

				CodeGenerator codeGenerator = new CodeGenerator(ctClass,
						configuration);
				codeGenerator.generateCode();
				if (configuration.isGenerateSource()) {
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
//			writeConfiguration();
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

//	private XStream getXStream() {
//		XStream stream = new XStream();
//		stream.alias("floggy", Configuration.class);
//		stream.useAttributeFor(Configuration.class, "generateSource");
//		stream.useAttributeFor(Configuration.class, "addDefaultConstructor");
//
//		stream.aliasType("persistables", ArrayList.class);
//
//		stream.alias("persistable", PersistableConfiguration.class);
//		stream.useAttributeFor(PersistableConfiguration.class, "className");
//		return stream;
//	}

	private boolean isCLDC10() {
		try {
			CtClass ctClass = classpathPool.get("java.io.DataInput");
			ctClass.getMethod("readFloat", "()F");
		} catch (NotFoundException nfex) {
			return true;
		}
		return false;
	}

//	private void readConfiguration() throws IOException {
//		if (configFile != null && configFile.exists()) {
//			XStream stream = getXStream();
//			configuration = (Configuration) stream
//					.fromXML(new FileReader(configFile));
//		}
//	}

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

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
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

//	private void writeConfiguration() throws IOException {
//		if (configFile == null) {
//			configFile= new File("floggy.xml");
//		}
//		XStream stream = getXStream();
//		stream.toXML(configuration, new FileWriter(configFile));
//	}

}
