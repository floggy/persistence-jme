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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;

import javassist.bytecode.AccessFlag;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.MethodInfo;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import net.sourceforge.floggy.persistence.codegen.CodeGenerator;
import net.sourceforge.floggy.persistence.codegen.strategy.JoinedStrategyCodeGenerator;
import net.sourceforge.floggy.persistence.codegen.strategy.PerClassStrategyCodeGenerator;
import net.sourceforge.floggy.persistence.codegen.strategy.SingleStrategyCodeGenerator;
import net.sourceforge.floggy.persistence.impl.IndexMetadata;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.impl.PersistableMetadataManager;
import net.sourceforge.floggy.persistence.impl.Utils;
import net.sourceforge.floggy.persistence.pool.InputPool;
import net.sourceforge.floggy.persistence.pool.OutputPool;
import net.sourceforge.floggy.persistence.pool.PoolFactory;
import net.sourceforge.floggy.persistence.strategy.PerClassStrategy;
import net.sourceforge.floggy.persistence.strategy.SingleStrategy;
import net.sourceforge.floggy.persistence.xstream.PersistableStrategyConverter;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class Weaver {
	private static final Log LOG = LogFactory.getLog(Weaver.class);
	public static final String __PERSISTABLE_CLASSNAME =
		"net.sourceforge.floggy.persistence.impl.__Persistable";
	public static final String PERSISTABLE_CLASSNAME =
		"net.sourceforge.floggy.persistence.Persistable";

	/**
	 * DOCUMENT ME!
	 */
	protected ClassPool classpathPool;

	/**
	 * DOCUMENT ME!
	 */
	protected Configuration configuration = new Configuration();

	/**
	 * DOCUMENT ME!
	 */
	protected File configurationFile;

	/**
	 * DOCUMENT ME!
	 */
	protected InputPool inputPool;

	/**
	 * DOCUMENT ME!
	 */
	protected OutputPool embeddedClassesOutputPool;

	/**
	 * DOCUMENT ME!
	 */
	protected OutputPool outputPool;

	/**
	 * DOCUMENT ME!
	 */
	protected Set alreadyProcessedMetadatas = new HashSet();

	/**
	 * DOCUMENT ME!
	 */
	protected boolean invocationOfShutdownMethodFound = false;

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

	/**
	 * DOCUMENT ME!
	*
	* @param ctClass DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	public PersistableMetadata createPersistableMetadata(CtClass ctClass)
		throws NotFoundException {
		CtClass singleRecordStoreStrategy =
			classpathPool.get(SingleStrategy.class.getName());
		CtClass recordStorePerClassStrategy =
			classpathPool.get(PerClassStrategy.class.getName());

		int persistableStrategy = PersistableMetadata.JOINED_STRATEGY;

		if (ctClass.subtypeOf(singleRecordStoreStrategy)) {
			persistableStrategy = PersistableMetadata.SINGLE_STRATEGY;
		} else if (ctClass.subtypeOf(recordStorePerClassStrategy)) {
			persistableStrategy = PersistableMetadata.PER_CLASS_STRATEGY;
		}

		String className = ctClass.getName();
		String superClassName = null;
		ClassVerifier superClassVerifier =
			new ClassVerifier(ctClass.getSuperclass(), classpathPool);

		if (superClassVerifier.isPersistable()) {
			superClassName = ctClass.getSuperclass().getName();
		}

		CtField[] fields = ctClass.getDeclaredFields();

		if (persistableStrategy != PersistableMetadata.JOINED_STRATEGY) {
			fields = ctClass.getFields();
		}

		List fieldNames = new ArrayList(fields.length);
		List fieldTypes = new ArrayList(fields.length);
		Hashtable persistableImplementations = null;
		Vector indexMetadatas = null;

		for (int i = 0; i < fields.length; i++) {
			CtField field = (CtField) fields[i];

			if (ignoreField(field)) {
				continue;
			}

			fieldNames.add(field.getName());

			Integer type = buildFloggyFieldType(field.getType());
			fieldTypes.add(type);

			if ((type.intValue() & PersistableMetadata.PERSISTABLE) == PersistableMetadata.PERSISTABLE) {
				if (persistableImplementations == null) {
					persistableImplementations = new Hashtable();
				}

				persistableImplementations.put(field.getName(),
					field.getType().getName());
			}
		}

		int[] temp = new int[fieldTypes.size()];

		for (int i = 0; i < fieldTypes.size(); i++) {
			temp[i] = ((Integer) fieldTypes.get(i)).intValue();
		}

		String recordStoreName = ctClass.getSimpleName() + className.hashCode();

		if (persistableStrategy == PersistableMetadata.SINGLE_STRATEGY) {
			String tempSuperClassName = superClassName;

			PersistableMetadata oldSuperMetadata = null;

			while (tempSuperClassName != null) {
				PersistableMetadata superMetadata =
					configuration.getPersistableMetadata(tempSuperClassName);

				if (superMetadata != null) {
					oldSuperMetadata = superMetadata;
					tempSuperClassName = oldSuperMetadata.getSuperClassName();
				} else {
					tempSuperClassName = null;
				}
			}

			if (oldSuperMetadata != null) {
				recordStoreName = oldSuperMetadata.getRecordStoreName();
			}
		}

		if (recordStoreName.length() > 32) {
			LOG.warn("The recordStore name " + recordStoreName
				+ " is bigger than 32 characters. It will be truncated to "
				+ recordStoreName.substring(0, 32));
			recordStoreName = recordStoreName.substring(0, 32);
		}

		PersistableMetadata metadata =
			new PersistableMetadata(Modifier.isAbstract(ctClass.getModifiers()),
				className, superClassName,
				(String[]) fieldNames.toArray(new String[fieldNames.size()]), temp,
				persistableImplementations, indexMetadatas, recordStoreName,
				persistableStrategy, null);

		return metadata;
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws WeaverException DOCUMENT ME!
	*/
	public void execute() throws WeaverException {
		long time = System.currentTimeMillis();
		LOG.info("Floggy Persistence Weaver - "
			+ PersistableMetadataManager.getBytecodeVersion());
		LOG.info("CLDC version: " + ((isCLDC10()) ? "1.0" : "1.1"));

		try {
			URL fileURL =
				getClass()
				 .getResource("/net/sourceforge/floggy/persistence/impl/PersistableMetadataManager.class");
			classpathPool.makeClass(fileURL.openStream());

			embeddedUnderlineCoreClasses();
			adaptFrameworkToTargetCLDC();

			List list = getClassThatImplementsPersistable();

			excludeAbstractDescendents();

			readConfiguration();

			int classCount = list.size();
			LOG.info("Processing " + classCount + " bytecodes!");

			for (int i = 0; i < classCount; i++) {
				String className = (String) list.get(i);

				CtClass ctClass = this.classpathPool.get(className);

				LOG.info("Processing bytecode " + className + "!");

				PersistableMetadata metadata =
					configuration.getPersistableMetadata(className);
				CodeGenerator codeGenerator = null;

				switch (metadata.getPersistableStrategy()) {
				case PersistableMetadata.SINGLE_STRATEGY:
					codeGenerator = new SingleStrategyCodeGenerator(ctClass,
							classpathPool, configuration);

					break;

				case PersistableMetadata.PER_CLASS_STRATEGY:
					codeGenerator = new PerClassStrategyCodeGenerator(ctClass,
							classpathPool, configuration);

					break;

				case PersistableMetadata.JOINED_STRATEGY:
					codeGenerator = new JoinedStrategyCodeGenerator(ctClass,
							classpathPool, configuration);

					break;
				}

				codeGenerator.generateCode();

				if (configuration.isGenerateSource()) {
					byte[] source = codeGenerator.getSource().getBytes();
					String fileName = className.replace('.', File.separatorChar) + ".txt";
					outputPool.addResource(new ByteArrayInputStream(source), fileName);
				}

				this.outputPool.addClass(ctClass);

				if (LOG.isDebugEnabled())
					LOG.debug("Bytecode modified.");
			}

			addPersistableMetadataManagerClass();

			if (embeddedClassesOutputPool != outputPool) {
				embeddedClassesOutputPool.finish();
			}

			outputPool.finish();

			if (!invocationOfShutdownMethodFound) {
				LOG.warn(
					"-------------------------------------------------------------------------------------------------------------------");
				LOG.warn(
					"The PersistableManager.shutdown() method is not being called. Please call it from MIDlet.destroyApp(boolean) method");
				LOG.warn(
					"-------------------------------------------------------------------------------------------------------------------");
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new WeaverException(e.getMessage(), e);
		}

		time = System.currentTimeMillis() - time;
		LOG.info("Time elapsed: " + time + "ms");
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public XStream getXStream() {
		XStream stream = new XStream(new DomDriver());
		stream.alias("floggy", Configuration.class);
		stream.useAttributeFor(Configuration.class, "generateSource");
		stream.aliasAttribute("generate-source", "generateSource");
		stream.useAttributeFor(Configuration.class, "addDefaultConstructor");
		stream.aliasAttribute("add-default-constructor", "addDefaultConstructor");

		stream.aliasType("persistables", ArrayList.class);

		stream.alias("persistable", PersistableMetadata.class);
		stream.omitField(PersistableMetadata.class, "isAbstract");
		stream.omitField(PersistableMetadata.class, "fieldTypes");
		stream.omitField(PersistableMetadata.class, "fieldNames");
		stream.omitField(PersistableMetadata.class, "persistableImplementations");
		stream.omitField(PersistableMetadata.class, "persistableStrategy");
		stream.omitField(PersistableMetadata.class, "superClassName");
		stream.omitField(PersistableMetadata.class, "recordId");
		stream.aliasField("indexes", PersistableMetadata.class, "indexMetadatas");
		stream.aliasField("persistable-strategy", PersistableMetadata.class,
			"persistableStrategy");
		stream.registerLocalConverter(PersistableMetadata.class,
			"persistableStrategy", new PersistableStrategyConverter());
		stream.useAttributeFor(PersistableMetadata.class, "className");
		stream.aliasAttribute("class-name", "className");
		stream.useAttributeFor(PersistableMetadata.class, "recordStoreName");
		stream.aliasAttribute("record-store-name", "recordStoreName");
		stream.useAttributeFor(PersistableMetadata.class, "suiteName");
		stream.aliasAttribute("suite-name", "suiteName");
		stream.useAttributeFor(PersistableMetadata.class, "vendorName");
		stream.aliasAttribute("vendor-name", "vendorName");

		stream.aliasType("indexes", Vector.class);
		stream.alias("index", IndexMetadata.class);
		stream.alias("field", String.class);
		stream.useAttributeFor(IndexMetadata.class, "name");
		stream.aliasField("record-store-name", IndexMetadata.class,
			"recordStoreName");
		stream.addImplicitCollection(IndexMetadata.class, "fields");

		return stream;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param c1 DOCUMENT ME!
	* @param c2 DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	*/
	public void mergeConfigurations(Configuration c1, Configuration c2)
		throws FloggyException {
		c1.setAddDefaultConstructor(c2.isAddDefaultConstructor());
		c1.setGenerateSource(c2.isGenerateSource());

		Iterator iterator = c2.getPersistableMetadatas().iterator();

		while (iterator.hasNext()) {
			PersistableMetadata tempMetadata = (PersistableMetadata) iterator.next();
			String className = tempMetadata.getClassName();
			String suiteName = tempMetadata.getSuiteName();
			String vendorName = tempMetadata.getVendorName();
			PersistableMetadata currentMetadata =
				c1.getPersistableMetadata(className);

			if (((suiteName == null) && (vendorName != null))
				 || ((suiteName != null) && (vendorName == null))) {
				throw new FloggyException(
					"You must provide suite-name and vendor-name for persistable "
					+ className);
			}

			if (currentMetadata != null) {
				String recordStoreName = tempMetadata.getRecordStoreName();

				if (!Utils.isEmpty(recordStoreName)) {
					currentMetadata.setRecordStoreName(recordStoreName.trim());
				}

				int persistableStrategy = tempMetadata.getPersistableStrategy();

				if (persistableStrategy > 0) {
					currentMetadata.setPersistableStrategy(persistableStrategy);
				}

				Vector indexMetadatas = tempMetadata.getIndexMetadatas();

				if (indexMetadatas != null) {
					int size = indexMetadatas.size();

					for (int i = 0; i < size; i++) {
						IndexMetadata indexMetadata = (IndexMetadata) indexMetadatas.get(i);
						int hashCode = Math.abs(className.hashCode());
						recordStoreName = "Index" + hashCode + indexMetadata.getName();
						indexMetadata.setRecordStoreName(recordStoreName);

						Vector fields = indexMetadata.getFields();

						for (int j = 0; j < fields.size(); j++) {
							String fieldName = (String) fields.get(j);

							if (!containsField(className, fieldName)) {
								throw new FloggyException("The field " + fieldName
									+ " that compounds the index " + indexMetadata.getName()
									+ " does not exist on persistable " + className);
							} else if (!isIndexableField(className, fieldName)) {
								throw new FloggyException("The field " + fieldName
									+ " that compounds the index " + indexMetadata.getName()
									+ " on persistable " + className
									+ " it is not from a valid type");
							}
						}
					}

					currentMetadata.setIndexMetadatas(indexMetadatas);
				}
			}
		}
	}

	/**
	* Sets the classpath.
	*
	* @param classpath DOCUMENT ME!
	*/
	public void setClasspath(String[] classpath) {
		if ((classpath != null) && (classpath.length > 0)) {
			for (int i = classpath.length - 1; i >= 0; i--) {
				try {
					this.classpathPool.insertClassPath(classpath[i]);
				} catch (NotFoundException e) {
				}
			}
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param configuration DOCUMENT ME!
	*/
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
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
	* @param embeddedClassesOutputFile DOCUMENT ME!
	*
	* @throws WeaverException DOCUMENT ME!
	*/
	public void setEmbeddedClassesOutputPool(File embeddedClassesOutputFile)
		throws WeaverException {
		embeddedClassesOutputPool = PoolFactory.createOutputPool(embeddedClassesOutputFile);
	}

	/**
	* Sets the input file.
	*
	* @param inputFile DOCUMENT ME!
	*
	* @throws WeaverException DOCUMENT ME!
	*/
	public void setInputFile(File inputFile) throws WeaverException {
		this.inputPool = PoolFactory.createInputPool(inputFile);

		try {
			this.classpathPool.insertClassPath(inputFile.getCanonicalPath());
		} catch (NotFoundException e) {
		} catch (IOException e) {
		}
	}

	/**
	* Sets the output file.
	*
	* @param outputFile
	*
	* @throws WeaverException DOCUMENT ME!
	*/
	public void setOutputFile(File outputFile) throws WeaverException {
		outputPool = PoolFactory.createOutputPool(outputFile);

		if (embeddedClassesOutputPool == null) {
			embeddedClassesOutputPool = outputPool;
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	* @throws CannotCompileException DOCUMENT ME!
	* @throws NotFoundException DOCUMENT ME!
	*/
	protected void adaptFrameworkToTargetCLDC()
		throws IOException, CannotCompileException, NotFoundException {
		URL fileURL =
			getClass()
			 .getResource("/net/sourceforge/floggy/persistence/impl/SerializationManager.class");
		classpathPool.makeClass(fileURL.openStream());

		CtClass ctClass =
			this.classpathPool.get(
				"net.sourceforge.floggy.persistence.impl.SerializationManager");

		if (isCLDC10()) {
			CtMethod[] methods = ctClass.getMethods();

			for (int i = 0; i < methods.length; i++) {
				String methodName = methods[i].getName();

				if ((methodName.indexOf("Float") != -1)
					 || (methodName.indexOf("Double") != -1)
					 || "readObject".equals(methodName)
					 || "writeObject".equals(methodName)) {
					ctClass.removeMethod(methods[i]);
				}
			}

			for (int i = 0; i < methods.length; i++) {
				String methodName = methods[i].getName();

				if ("readObjectCLDC10".equals(methodName)) {
					methods[i].setName("readObject");
				}

				if ("writeObjectCLDC10".equals(methodName)) {
					methods[i].setName("writeObject");
				}
			}
		} else {
			CtMethod[] methods = ctClass.getMethods();

			for (int i = 0; i < methods.length; i++) {
				String methodName = methods[i].getName();

				if (methodName.indexOf("CLDC10") != -1) {
					ctClass.removeMethod(methods[i]);
				}
			}
		}

		embeddedClassesOutputPool.addClass(ctClass);

		fileURL = getClass()
			 .getResource("/net/sourceforge/floggy/persistence/impl/migration/AbstractEnumerationImpl.class");
		classpathPool.makeClass(fileURL.openStream());

		ctClass = this.classpathPool.get(
				"net.sourceforge.floggy.persistence.impl.migration.AbstractEnumerationImpl");

		if (isCLDC10()) {
			CtMethod[] methods = ctClass.getMethods();

			for (int i = 0; i < methods.length; i++) {
				String methodName = methods[i].getName();

				if ((methodName.indexOf("Float") != -1)
					 || (methodName.indexOf("Double") != -1)
					 || "createArray".equals(methodName)
					 || "readArray".equals(methodName) || "readObject".equals(methodName)
					 || "readPrimitive".equals(methodName)) {
					ctClass.removeMethod(methods[i]);
				}
			}

			for (int i = 0; i < methods.length; i++) {
				String methodName = methods[i].getName();

				if ("createArrayCLDC10".equals(methodName)) {
					methods[i].setName("createArray");
				}

				if ("readArrayCLDC10".equals(methodName)) {
					methods[i].setName("readArray");
				}

				if ("readObjectCLDC10".equals(methodName)) {
					methods[i].setName("readObject");
				}

				if ("readPrimitiveCLDC10".equals(methodName)) {
					methods[i].setName("readPrimitive");
				}
			}
		} else {
			CtMethod[] methods = ctClass.getMethods();

			for (int i = 0; i < methods.length; i++) {
				String methodName = methods[i].getName();

				if (methodName.indexOf("CLDC10") != -1) {
					ctClass.removeMethod(methods[i]);
				}
			}
		}

		embeddedClassesOutputPool.addClass(ctClass);
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws CannotCompileException DOCUMENT ME!
	* @throws IOException DOCUMENT ME!
	* @throws NotFoundException DOCUMENT ME!
	*/
	protected void addPersistableMetadataManagerClass()
		throws CannotCompileException, IOException, NotFoundException {
		alreadyProcessedMetadatas.addAll(configuration.getPersistableMetadatas());

		Set metadatas = alreadyProcessedMetadatas;
		StringBuffer buffer = new StringBuffer();

		buffer.append("public static void init() throws Exception {\n");
		buffer.append("rmsBasedMetadatas = new java.util.Hashtable();\n");
		buffer.append("classBasedMetadatas = new java.util.Hashtable();\n");
		buffer.append("java.util.Hashtable persistableImplementations = null;\n");
		buffer.append("java.util.Vector indexMetadatas = null;\n");
		buffer.append("java.util.Vector fields = null;\n");

		for (Iterator iterator = metadatas.iterator(); iterator.hasNext();) {
			PersistableMetadata metadata = (PersistableMetadata) iterator.next();
			boolean isAbstract = metadata.isAbstract();
			String className = metadata.getClassName();
			String superClassName = metadata.getSuperClassName();
			String[] fieldNames = metadata.getFieldNames();
			int[] fieldTypes = metadata.getFieldTypes();
			Hashtable persistableImplementations =
				metadata.getPersistableImplementations();
			String recordStoreName = metadata.getRecordStoreName();
			int persistableStrategy = metadata.getPersistableStrategy();
			Vector indexMetadatas = metadata.getIndexMetadatas();
			String[] descendents = metadata.getDescendents();

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

			if ((persistableImplementations != null)
				 && !persistableImplementations.isEmpty()) {
				buffer.append(
					"persistableImplementations = new java.util.Hashtable();\n");

				Enumeration enumeration = persistableImplementations.keys();

				while (enumeration.hasMoreElements()) {
					String fieldName = (String) enumeration.nextElement();
					String classNameOfField =
						(String) persistableImplementations.get(fieldName);
					buffer.append("persistableImplementations.put(\"");
					buffer.append(fieldName);
					buffer.append("\", \"");
					buffer.append(classNameOfField);
					buffer.append("\");\n");
				}
			} else {
				buffer.append("persistableImplementations = null;\n");
			}

			if ((indexMetadatas != null) && !indexMetadatas.isEmpty()) {
				buffer.append("indexMetadatas = new java.util.Vector();\n");

				Enumeration enumeration = indexMetadatas.elements();

				while (enumeration.hasMoreElements()) {
					IndexMetadata indexMetadata =
						(IndexMetadata) enumeration.nextElement();

					buffer.append("fields = new java.util.Vector();\n");

					Vector fields = indexMetadata.getFields();

					for (int j = 0; j < fields.size(); j++) {
						buffer.append("fields.addElement(\"");
						buffer.append(fields.elementAt(j));
						buffer.append("\");\n");
					}

					buffer.append(
						"indexMetadatas.addElement(new net.sourceforge.floggy.persistence.impl.IndexMetadata(\"");
					buffer.append(indexMetadata.getRecordStoreName());
					buffer.append("\", \"");
					buffer.append(indexMetadata.getName());
					buffer.append("\", fields));\n");
				}
			} else {
				buffer.append("indexMetadatas = null;\n");
			}

			StringBuffer descendentsBuffer = new StringBuffer("new String[");
			addHeader = true;

			if (descendents != null) {
				for (int i = 0; i < descendents.length; i++) {
					if (addHeader) {
						descendentsBuffer.append("]{");
						addHeader = false;
					}

					descendentsBuffer.append("\"");
					descendentsBuffer.append(descendents[i]);
					descendentsBuffer.append("\",");
				}
			}

			if (addHeader) {
				descendentsBuffer.append("0]");
			} else {
				descendentsBuffer.deleteCharAt(descendentsBuffer.length() - 1);
				descendentsBuffer.append("}");
			}

			buffer.append("classBasedMetadatas.put(\"" + className
				+ "\", new net.sourceforge.floggy.persistence.impl.PersistableMetadata("
				+ isAbstract + ", \"" + className + "\", "
				+ ((superClassName != null) ? ("\"" + superClassName + "\", ") : ("null, "))
				+ fieldNamesBuffer.toString() + ", " + fieldTypesBuffer.toString()
				+ ", persistableImplementations, indexMetadatas, " + "\""
				+ recordStoreName + "\", " + persistableStrategy + ", "
				+ descendentsBuffer.toString() + "));\n");
		}

		buffer.append("load();\n");
		buffer.append("}\n");

		CtClass ctClass =
			this.classpathPool.get(
				"net.sourceforge.floggy.persistence.impl.PersistableMetadataManager");
		CtMethod[] methods = ctClass.getMethods();

		for (int i = 0; i < methods.length; i++) {
			if (methods[i].getName().equals("init")) {
				ctClass.removeMethod(methods[i]);
			}
		}

		ctClass.addMethod(CtNewMethod.make(buffer.toString(), ctClass));
		embeddedClassesOutputPool.addClass(ctClass);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param ctClass DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	protected List buildClassTree(CtClass ctClass) throws NotFoundException {
		final CtClass persistable = classpathPool.get(Weaver.PERSISTABLE_CLASSNAME);
		List list = new ArrayList();
		CtClass superClass = ctClass;
		String superName = ctClass.getName();

		do {
			list.add(superName);
			superClass = superClass.getSuperclass();
			superName = superClass.getName();
		} while (!"java.lang.Object".equals(superName)
			 && superClass.subtypeOf(persistable));

		Collections.reverse(list);

		for (int i = 0; i < list.size(); i++) {
			String className = (String) list.get(i);

			PersistableMetadata metadata =
				configuration.getPersistableMetadata(className);

			if (metadata == null) {
				CtClass clazz = classpathPool.get(className);

				metadata = createPersistableMetadata(clazz);

				configuration.addPersistableMetadata(metadata);
			}

			String[] descendents = metadata.getDescendents();

			if (descendents == null) {
				descendents = new String[0];
			}

			Set temp = new HashSet(Arrays.asList(descendents));
			temp.addAll(list.subList(i, list.size()));

			metadata.setDescendents((String[]) temp.toArray(new String[temp.size()]));
		}

		return list;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param fieldType DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
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

		return Integer.valueOf(floggyFieldType);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param fieldType DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	protected int buildFloggyObjectFieldType(CtClass fieldType)
		throws NotFoundException {
		int floggyFieldType = 0;
		String typeName = fieldType.getName();

		if ("java.lang.Boolean".equals(typeName)) {
			floggyFieldType = PersistableMetadata.BOOLEAN;
		}

		if ("java.lang.Byte".equals(typeName)) {
			floggyFieldType = PersistableMetadata.BYTE;
		}

		if ("java.lang.Character".equals(typeName)) {
			floggyFieldType = PersistableMetadata.CHARACTER;
		}

		if ("java.lang.Double".equals(typeName)) {
			floggyFieldType = PersistableMetadata.DOUBLE;
		}

		if ("java.lang.Float".equals(typeName)) {
			floggyFieldType = PersistableMetadata.FLOAT;
		}

		if ("java.lang.Integer".equals(typeName)) {
			floggyFieldType = PersistableMetadata.INT;
		}

		if ("java.lang.Long".equals(typeName)) {
			floggyFieldType = PersistableMetadata.LONG;
		}

		if ("java.lang.Short".equals(typeName)) {
			floggyFieldType = PersistableMetadata.SHORT;
		}

		if ("java.lang.String".equals(typeName)) {
			floggyFieldType = PersistableMetadata.STRING;
		}

		if ("java.util.Calendar".equals(typeName)) {
			floggyFieldType = PersistableMetadata.CALENDAR;
		}

		if ("java.util.Date".equals(typeName)) {
			floggyFieldType = PersistableMetadata.DATE;
		}

		if ("java.util.Hashtable".equals(typeName)) {
			floggyFieldType = PersistableMetadata.HASHTABLE;
		}

		if (fieldType.subtypeOf(fieldType.getClassPool()
				 .get(Weaver.PERSISTABLE_CLASSNAME))) {
			floggyFieldType = PersistableMetadata.PERSISTABLE;
		}

		if ("java.lang.StringBuffer".equals(typeName)) {
			floggyFieldType = PersistableMetadata.STRINGBUFFER;
		}

		if ("java.util.Stack".equals(typeName)) {
			floggyFieldType = PersistableMetadata.STACK;
		}

		if ("java.util.TimeZone".equals(typeName)) {
			floggyFieldType = PersistableMetadata.TIMEZONE;
		}

		if ("java.util.Vector".equals(typeName)) {
			floggyFieldType = PersistableMetadata.VECTOR;
		}

		return floggyFieldType;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param fieldType DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
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

	/**
	 * DOCUMENT ME!
	*
	* @param className DOCUMENT ME!
	* @param fieldName DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected boolean containsField(String className, String fieldName) {
		try {
			CtClass ctClass = classpathPool.get(className);
			ctClass.getField(fieldName);

			return true;
		} catch (NotFoundException e) {
			return false;
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param fileName DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	protected void embeddedClass(String fileName) throws IOException {
		URL fileURL = getClass().getResource(fileName);

		if (fileURL != null) {
			fileName = fileName.replace('/', File.separatorChar);
			embeddedClassesOutputPool.addFile(fileURL, fileName);
			classpathPool.makeClass(fileURL.openStream());
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws IOException DOCUMENT ME!
	*/
	protected void embeddedUnderlineCoreClasses() throws IOException {
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/FloggyOutputStream.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/FloggyProperties$1.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/IndexMetadata.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/IndexManager.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/Index.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/IndexEntry.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/ObjectComparator.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/ObjectSetImpl.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/__Persistable.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/PersistableManagerImpl.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/PersistableMetadata.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/PolymorphicObjectSetImpl.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/PolymorphicObjectSetImpl$ObjectList.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/RecordStoreManager.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/RecordStoreManager$1.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/RecordStoreManager$RecordStoreReference.class");
		embeddedClass("/net/sourceforge/floggy/persistence/impl/Utils.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/migration/MigrationManagerImpl.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/migration/SingleStrategyEnumerationImpl.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/migration/PerClassStrategyEnumerationImpl.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/migration/JoinedStrategyEnumerationImpl.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/migration/HashtableValueNullable.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/strategy/JoinedStrategyObjectFilter.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/strategy/PerClassStrategyObjectFilter.class");
		embeddedClass(
			"/net/sourceforge/floggy/persistence/impl/strategy/SingleStrategyObjectFilter.class");
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	*/
	protected void excludeAbstractDescendents() throws NotFoundException {
		List persistables = configuration.getPersistableMetadatas();

		for (int i = 0; i < persistables.size(); i++) {
			PersistableMetadata metadata = (PersistableMetadata) persistables.get(i);

			String[] temp = metadata.getDescendents();

			if (temp != null) {
				Set descendents = new HashSet(Arrays.asList(temp));

				List toRemove = new LinkedList();

				Iterator iterator = descendents.iterator();

				while (iterator.hasNext()) {
					String className = (String) iterator.next();

					CtClass ctClass = classpathPool.get(className);

					if (Modifier.isAbstract(ctClass.getModifiers())) {
						toRemove.add(className);
					}
				}

				if (descendents.removeAll(toRemove)) {
					metadata.setDescendents((String[]) descendents.toArray(
							new String[descendents.size()]));
				}
			}
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param ctClass DOCUMENT ME!
	*/
	protected void findInvocationOfShutdownMethod(CtClass ctClass) {
		if (!ctClass.isInterface()) {
			try {
				ClassFile classFile = ctClass.getClassFile2();
				ConstPool constantPool = classFile.getConstPool();
				List methods = classFile.getMethods();

				for (Iterator iterator = methods.iterator(); iterator.hasNext();) {
					MethodInfo method = (MethodInfo) iterator.next();

					if ((method.getAccessFlags() & AccessFlag.ABSTRACT) != AccessFlag.ABSTRACT) {
						CodeAttribute codeAttribute = method.getCodeAttribute();

						if (codeAttribute != null) {
							byte[] code = codeAttribute.getCode();
							CodeIterator codeIterator = codeAttribute.iterator();

							while (codeIterator.hasNext()) {
								int index = codeIterator.next();
								int opcode = codeIterator.byteAt(index);

								if (opcode == CodeAttribute.INVOKEVIRTUAL) {
									int temp = (code[index + 1] << 8) | code[index + 2];
									String className = constantPool.getMethodrefClassName(temp);
									String methodName = constantPool.getMethodrefName(temp);

									if (PersistableManager.class.getName().equals(className)
										 && "shutdown".equals(methodName)) {
										invocationOfShutdownMethodFound = true;
									}
								}
							}
						}
					}
				}
			} catch (Exception ex) {
				LOG.warn(ex.getMessage(), ex);
			}
		}
	}

	/**
	* Returns the class name given a file name.
	*
	* @param fileName
	*
	* @return
	*/
	protected String getClassName(String fileName) {
		if (fileName.endsWith(".class")) {
			String className = fileName.replace(File.separatorChar, '.');

			return className.substring(0, className.length() - 6);
		}

		return null;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	* @throws IOException DOCUMENT ME!
	*/
	protected List getClassThatImplementsPersistable()
		throws NotFoundException, IOException {
		int classCount = this.inputPool.getFileCount();
		LOG.info("Look up for classes that implements Persistable!");

		List list = new LinkedList();
		final CtClass persistable = classpathPool.get(Weaver.PERSISTABLE_CLASSNAME);
		final CtClass __persistable =
			classpathPool.get(Weaver.__PERSISTABLE_CLASSNAME);

		for (int i = 0; i < classCount; i++) {
			String fileName = this.inputPool.getFileName(i);
			String className = getClassName(fileName);

			if (className == null) {
				this.outputPool.addFile(inputPool.getFileURL(i), fileName);

				continue;
			}

			CtClass ctClass = this.classpathPool.get(className);

			if (ctClass.subtypeOf(persistable) && !ctClass.subtypeOf(__persistable)
				 && !ctClass.isInterface()) {
				List tree = buildClassTree(ctClass);

				for (int j = 0; j < tree.size(); j++) {
					Object object = tree.get(j);

					if (!list.contains(object)) {
						list.add(object);
					}
				}
			} else {
				LOG.debug("Bytecode NOT modified.");

				if (ctClass.subtypeOf(__persistable) && !ctClass.equals(__persistable)) {
					List temp = buildClassTree(ctClass);
					Iterator iterator = temp.iterator();

					while (iterator.hasNext()) {
						className = (String) iterator.next();

						PersistableMetadata metadata =
							configuration.getPersistableMetadata(className);
						alreadyProcessedMetadatas.add(metadata);
					}
				}

				findInvocationOfShutdownMethod(ctClass);

				this.outputPool.addFile(inputPool.getFileURL(i), fileName);
			}
		}

		return list;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param field DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected boolean ignoreField(CtField field) {
		int modifier = field.getModifiers();

		return field.getName().equals("__id")
		 || field.getName().equals("__persistableMetadata")
		 || Modifier.isTransient(modifier) || Modifier.isStatic(modifier);
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected boolean isCLDC10() {
		try {
			CtClass ctClass = classpathPool.get("java.io.DataInput");
			ctClass.getMethod("readFloat", "()F");
		} catch (NotFoundException nfex) {
			return true;
		}

		return false;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param className DOCUMENT ME!
	* @param fieldName DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected boolean isIndexableField(String className, String fieldName) {
		try {
			CtClass ctClass = classpathPool.get(className);
			CtField field = ctClass.getField(fieldName);
			CtClass fieldType = field.getType();
			String fieldTypeClassName = fieldType.getName();

			if ("boolean".equals(fieldTypeClassName)
				 || "byte".equals(fieldTypeClassName)
				 || "char".equals(fieldTypeClassName)
				 || "double".equals(fieldTypeClassName)
				 || "float".equals(fieldTypeClassName)
				 || "int".equals(fieldTypeClassName)
				 || "long".equals(fieldTypeClassName)
				 || "short".equals(fieldTypeClassName)
				 || "java.lang.Boolean".equals(fieldTypeClassName)
				 || "java.lang.Byte".equals(fieldTypeClassName)
				 || "java.lang.Character".equals(fieldTypeClassName)
				 || "java.lang.Double".equals(fieldTypeClassName)
				 || "java.lang.Float".equals(fieldTypeClassName)
				 || "java.lang.Integer".equals(fieldTypeClassName)
				 || "java.lang.Long".equals(fieldTypeClassName)
				 || "java.lang.Short".equals(fieldTypeClassName)
				 || "java.lang.String".equals(fieldTypeClassName)
				 || "java.lang.StringBuffer".equals(fieldTypeClassName)
				 || "java.util.Date".equals(fieldTypeClassName)
				 || "java.util.TimeZone".equals(fieldTypeClassName)) {
				return true;
			} else {
				return false;
			}
		} catch (NotFoundException e) {
			return false;
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws FloggyException DOCUMENT ME!
	* @throws IOException DOCUMENT ME!
	*/
	protected void readConfiguration() throws FloggyException, IOException {
		if ((configurationFile != null) && configurationFile.exists()) {
			XStream stream = getXStream();
			Configuration temp =
				(Configuration) stream.fromXML(new FileReader(configurationFile));
			mergeConfigurations(configuration, temp);
		}
	}
}
