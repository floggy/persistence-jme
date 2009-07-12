/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import net.sourceforge.floggy.persistence.codegen.CodeGenerator;
import net.sourceforge.floggy.persistence.impl.FloggyProperties;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
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
						.addPersistableMetadata(createPersistableMetadata(superClass));
			}

			superClass = superClass.getSuperclass();
			superName = superClass.getName();
		} while (!superName.equals("java.lang.Object")
				&& superClass.subtypeOf(persistable));
		return list;
	}

	protected Integer buildFloggyFieldType(CtClass fieldType)
			throws NotFoundException {
		int floggyFieldType = 0;
		if (fieldType.isArray()) {
			fieldType = fieldType.getComponentType();
			if (fieldType.isPrimitive()) {
				floggyFieldType = PersistableMetadata.PRIMITIVE
						| buildFloggyPrimitiveFieldType(fieldType);
			} else {
				floggyFieldType = buildFloggyObjectFieldType(fieldType);
			}
			floggyFieldType = floggyFieldType | PersistableMetadata.ARRAY;
		} else {
			if (fieldType.isPrimitive()) {
				floggyFieldType = PersistableMetadata.PRIMITIVE
						| buildFloggyPrimitiveFieldType(fieldType);
			} else {
				floggyFieldType = buildFloggyObjectFieldType(fieldType);
			}
		}
		if (floggyFieldType == 0) {
			throw new NotFoundException(fieldType.getName());
		}
		return new Integer(floggyFieldType);
	}

	protected int buildFloggyObjectFieldType(CtClass fieldType)
			throws NotFoundException {
		int floggyFieldType = 0;
		String typeName = fieldType.getName();
		if (typeName.equals("java.lang.Boolean")) {
			floggyFieldType = PersistableMetadata.BOOLEAN;
		}
		if (typeName.equals("java.lang.Byte")) {
			floggyFieldType = PersistableMetadata.BYTE;
		}
		if (typeName.equals("java.lang.Character")) {
			floggyFieldType = PersistableMetadata.CHARACTER;
		}
		if (typeName.equals("java.lang.Double")) {
			floggyFieldType = PersistableMetadata.DOUBLE;
		}
		if (typeName.equals("java.lang.Float")) {
			floggyFieldType = PersistableMetadata.FLOAT;
		}
		if (typeName.equals("java.lang.Integer")) {
			floggyFieldType = PersistableMetadata.INT;
		}
		if (typeName.equals("java.lang.Long")) {
			floggyFieldType = PersistableMetadata.LONG;
		}
		if (typeName.equals("java.lang.Short")) {
			floggyFieldType = PersistableMetadata.SHORT;
		}
		if (typeName.equals("java.lang.String")) {
			floggyFieldType = PersistableMetadata.STRING;
		}
		if (typeName.equals("java.util.Calendar")) {
			floggyFieldType = PersistableMetadata.CALENDAR;
		}
		if (typeName.equals("java.util.Date")) {
			floggyFieldType = PersistableMetadata.DATE;
		}
		if (typeName.equals("java.util.Hashtable")) {
			floggyFieldType = PersistableMetadata.HASHTABLE;
		}
		if (fieldType.subtypeOf(fieldType.getClassPool().get(
				Weaver.PERSISTABLE_CLASSNAME))) {
			floggyFieldType = PersistableMetadata.PERSISTABLE;
		}
		if (typeName.equals("java.lang.StringBuffer")) {
			floggyFieldType = PersistableMetadata.STRINGBUFFER;
		}
		if (typeName.equals("java.util.Stack")) {
			floggyFieldType = PersistableMetadata.STACK;
		}
		if (typeName.equals("java.util.TimeZone")) {
			floggyFieldType = PersistableMetadata.TIMEZONE;
		}
		if (typeName.equals("java.util.Vector")) {
			floggyFieldType = PersistableMetadata.VECTOR;
		}
		return floggyFieldType;
	}

	protected int buildFloggyPrimitiveFieldType(CtClass fieldType) {
		int floggyFieldType = 0;
		if (CtClass.booleanType.equals(fieldType)) {
			floggyFieldType = PersistableMetadata.BOOLEAN;
		}
		if (CtClass.byteType.equals(fieldType)) {
			floggyFieldType = PersistableMetadata.BYTE;
		}
		if (CtClass.charType.equals(fieldType)) {
			floggyFieldType = PersistableMetadata.CHARACTER;
		}
		if (CtClass.doubleType.equals(fieldType)) {
			floggyFieldType = PersistableMetadata.DOUBLE;
		}
		if (CtClass.floatType.equals(fieldType)) {
			floggyFieldType = PersistableMetadata.FLOAT;
		}
		if (CtClass.intType.equals(fieldType)) {
			floggyFieldType = PersistableMetadata.INT;
		}
		if (CtClass.longType.equals(fieldType)) {
			floggyFieldType = PersistableMetadata.LONG;
		}
		if (CtClass.shortType.equals(fieldType)) {
			floggyFieldType = PersistableMetadata.SHORT;
		}
		return floggyFieldType;
	}

	protected boolean ignoreField(CtField field) {
		int modifier = field.getModifiers();

		return field.getName().equals("__id")
				|| field.getName().equals("__persistableMetadata")
				|| Modifier.isTransient(modifier)
				|| Modifier.isStatic(modifier);
	}

	private PersistableMetadata createPersistableMetadata(CtClass ctClass) throws NotFoundException {
		String className = ctClass.getName();
		String superClassName = null;
		ClassVerifier superClassVerifier = new ClassVerifier(ctClass.getSuperclass(), classpathPool);
		if (superClassVerifier.isPersistable()) {
			superClassName = ctClass.getSuperclass().getName();
		}
		CtField[] fields = ctClass.getDeclaredFields();
		List fieldNames = new ArrayList(fields.length);
		List fieldTypes = new ArrayList(fields.length);
		for (int i = 0; i < fields.length; i++) {
			CtField field = (CtField) fields[i];
			if (ignoreField(field)) {
				continue;
			}
			fieldNames.add(field.getName());
			fieldTypes.add(buildFloggyFieldType(field.getType()));
		}
		
		int[] temp = new int[fieldTypes.size()];
		for (int i = 0; i < fieldTypes.size(); i++) {
			temp[i] = ((Integer)fieldTypes.get(i)).intValue();
		}

		String recordStoreName = ctClass.getSimpleName() + className.hashCode();
		if (recordStoreName.length() > 32) {
			LOG.warn("The recordStore name " + recordStoreName + " is bigger than 32 characters. It will be truncated to " + recordStoreName.substring(0, 32));
			recordStoreName = recordStoreName.substring(0, 32);
		}
		PersistableMetadata metadata = new PersistableMetadata(className, superClassName, 
			(String[])fieldNames.toArray(new String[0]), temp, recordStoreName);
		return metadata;
	}

	private void embeddedClass(String fileName) throws IOException {
		URL fileURL = getClass().getResource(fileName);
		if (fileURL != null) {
			fileName = fileName.replace('/', File.separatorChar);
			outputPool.addFile(fileURL, fileName);
			classpathPool.makeClass(fileURL.openStream());
		}
	}

	private void embeddedUnderlineCoreClasses() throws IOException {
		embeddedClass("/net/sourceforge/floggy/persistence/impl/FloggyOutputStream.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/FloggyProperties.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/MetadataManagerUtil.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/ObjectComparator.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/ObjectFilter.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/ObjectSetImpl.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/__Persistable.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/PersistableManagerImpl.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/PersistableManagerImpl$1.class");
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

				CodeGenerator codeGenerator = new CodeGenerator(ctClass, classpathPool,
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
			addMetadataManagerUtilClass();
//			writeConfiguration();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new WeaverException(e.getMessage());
		}
		// Status
		time = System.currentTimeMillis() - time;
		LOG.info("Time elapsed: " + time + "ms");
	}

	private void addMetadataManagerUtilClass() throws  CannotCompileException, IOException, NotFoundException {

		List metadatas = configuration.getPersistableMetadatas();
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("public static void init() {\n");
		buffer.append("metadatas = new java.util.Hashtable();\n");
		
		for (Iterator iterator = metadatas.iterator(); iterator.hasNext();) {
			PersistableMetadata metadata = (PersistableMetadata) iterator.next();
			String className = metadata.getClassName();
			String superClassName = metadata.getSuperClassName();
			String[] fieldNames = metadata.getFieldNames();
			int[] fieldTypes = metadata.getFieldTypes();
			String recordStoreName = metadata.getRecordStoreName();

			StringBuffer fieldNamesBuffer = new StringBuffer("new String[");
			StringBuffer fieldTypesBuffer = new StringBuffer("new int[");
			boolean addHeader = true;
			for (int i = 0; i < fieldNames.length; i++) {
				if (addHeader) {
					fieldNamesBuffer.append("]{");
					fieldTypesBuffer.append("]{");
					addHeader = false;
				}
				fieldNamesBuffer.append("\"");
				fieldNamesBuffer.append(fieldNames[i]);
				fieldNamesBuffer.append("\",");

				fieldTypesBuffer.append(fieldTypes[i]);
				fieldTypesBuffer.append(",");
			}
			if (addHeader) {
				fieldNamesBuffer.append("0]");
				fieldTypesBuffer.append("0]");
			} else {
				fieldNamesBuffer.deleteCharAt(fieldNamesBuffer.length() - 1);
				fieldNamesBuffer.append("}");
				fieldTypesBuffer.deleteCharAt(fieldTypesBuffer.length() - 1);
				fieldTypesBuffer.append("}");
			}

			buffer.append("metadatas.put(\"" + className 
				+ "\", new net.sourceforge.floggy.persistence.impl.PersistableMetadata(\"" 
				+ className + "\", " 
				+ ((superClassName != null) ? ("\"" + superClassName + "\", ") : ("null, "))
				+ fieldNamesBuffer.toString() + ", " 
				+ fieldTypesBuffer.toString() + ", "
				+ "\"" + recordStoreName + "\""
				+ "));\n");
		}

		buffer.append("}\n");

		CtClass ctClass= this.classpathPool.get("net.sourceforge.floggy.persistence.impl.MetadataManagerUtil");
		CtMethod[] methods= ctClass.getMethods();
		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals("init")) {
				ctClass.removeMethod(methods[i]);
			}
		}
		
		ctClass.addMethod(CtNewMethod.make(buffer.toString(), ctClass));
		outputPool.addClass(ctClass);
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
