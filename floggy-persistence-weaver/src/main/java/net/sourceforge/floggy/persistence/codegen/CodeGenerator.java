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

package net.sourceforge.floggy.persistence.codegen;

import java.util.Vector;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import net.sourceforge.floggy.persistence.Configuration;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.IDable;
import net.sourceforge.floggy.persistence.Weaver;
import net.sourceforge.floggy.persistence.formatter.CodeFormatter;
import net.sourceforge.floggy.persistence.impl.IndexMetadata;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class CodeGenerator {

	private static final Log LOG = LogFactory.getLog(CodeGenerator.class);

	/**
	 * Class to be modified;
	 */
	protected CtClass ctClass;
	protected ClassPool classPool;
	protected Configuration configuration;
	protected StringBuffer source;

	/**
	 * Creates a new code generator for the class.
	 * 
	 * @param ctClass
	 *            Class to be modified.
	 * @param configuration
	 *            the configuration object
	 */
	public CodeGenerator(CtClass ctClass, ClassPool classPool,
			Configuration configuration) throws FloggyException, NotFoundException {
		this.ctClass = ctClass;
		this.classPool = classPool;
		this.configuration = configuration;
	}

	protected final void addField(StringBuffer buffer)
			throws CannotCompileException {
		String temp = buffer.toString();
		if (configuration.isGenerateSource()) {
			source.append(CodeFormatter.format(temp));
		}

		try {
			ctClass.addField(CtField.make(temp, ctClass));
		} catch (CannotCompileException ccex) {
			LOG.error("Adding field: \n" + CodeFormatter.format(temp));
			throw ccex;
		}
	}

	protected final void addMethod(StringBuffer buffer)
			throws CannotCompileException {
		String temp = buffer.toString();
		if (configuration.isGenerateSource()) {
			source.append(CodeFormatter.format(temp));
		}

		try {
			ctClass.addMethod(CtNewMethod.make(temp, ctClass));
		} catch (CannotCompileException ccex) {
			LOG.error("Adding method: \n" + CodeFormatter.format(temp));
			throw ccex;
		}
	}

	/**
	 * Generate all the necessary source code for this class.
	 * 
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 */
	public final void generateCode() throws NotFoundException,
			CannotCompileException {
		if (configuration.isGenerateSource()) {
			source = new StringBuffer();
		}

		// Constructor
		this.generateDefaultConstructor();

		// Remove final
		this.generateNonFinalFields();

		this.generateSpecificMethods();

		// Implements interface
		this.generatePersistableInterface();

		// index method
		this.generateGetIndexValueMethod();
	}

	protected void generateDefaultConstructor() throws NotFoundException,
			CannotCompileException {
		CtConstructor constructor = null;
		try {
			constructor = ctClass.getConstructor("()V");
			if (AccessFlag.PUBLIC != constructor.getModifiers()) {
				throw new CannotCompileException(
						"You must provide a public default constructor to class: "
								+ ctClass.getName());
			}
		} catch (NotFoundException e) {
			if (configuration.isAddDefaultConstructor()) {
				constructor = CtNewConstructor.defaultConstructor(ctClass);
				ctClass.addConstructor(constructor);
			} else {
				throw new CannotCompileException(
						"You must provide a public default constructor to class: "
								+ ctClass.getName());
			}
		}
	}

	/**
	 * 
	 * @throws CannotCompileException
	 */
	protected void generateGetIndexValueMethod()
			throws CannotCompileException, NotFoundException {
		PersistableMetadata metadata = 
			configuration.getPersistableMetadata(ctClass.getName());
		StringBuffer buffer = new StringBuffer();
		buffer.append("public Object __getIndexValue(String indexName) {\n");

		Vector indexMetadatas = metadata.getIndexMetadatas();
		
		if (indexMetadatas != null) {
			int indexMetadatasSize = indexMetadatas.size();
			for (int i = 0; i < indexMetadatasSize; i++) {
				IndexMetadata indexMetadata = (IndexMetadata) indexMetadatas.elementAt(i);
				
				buffer.append("if (\"" + indexMetadata.getName() + "\".equals(indexName)) {\n");

				Vector fields = indexMetadata.getFields();
				int fieldsSize = fields.size();
				for (int j = 0; j < fieldsSize; j++) {
					String fieldName = (String) fields.get(j);
					
					CtClass fieldType = ctClass.getField(fieldName).getType(); 
					if (fieldType.isPrimitive()) {
						buffer.append("return new " + PrimitiveTypeGenerator.getWrapperNameClass(fieldType) + "(this." + fieldName + ");\n");
					}
					else {
						buffer.append("return this." + fieldName + ";\n");
					}
				}
				buffer.append("}\n");
			}
		}
		buffer.append("return null;\n");
		buffer.append("}\n");

		// adicionando a classe
		addMethod(buffer);
	}
	
	/**
	 * 
	 * @throws CannotCompileException
	 */
	protected void generateGetRecordStoreNameMethod()
			throws CannotCompileException, NotFoundException {
		try {
			ctClass.getDeclaredMethod("getRecordStoreName");
		} catch (NotFoundException nfex) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("public String getRecordStoreName() {\n");
			buffer.append("return \""
					+ configuration.getPersistableMetadata(ctClass.getName())
							.getRecordStoreName() + "\";\n");
			buffer.append("}\n");

			// adicionando a classe
			addMethod(buffer);
		}
	}
	
	protected void generateNonFinalFields() {
		CtField[] fields = null;

		fields = ctClass.getDeclaredFields();
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				CtField field = fields[i];
				if (Modifier.isFinal(field.getModifiers())) {
					field.setModifiers(field.getModifiers() & ~Modifier.FINAL);
				}
			}
		}
	}

	/**
	 * @throws NotFoundException
	 */
	protected void generatePersistableInterface() throws NotFoundException {
		this.ctClass.addInterface(this.ctClass.getClassPool().get(
				Weaver.__PERSISTABLE_CLASSNAME));
	}

	protected abstract void generateSpecificMethods() throws NotFoundException,
			CannotCompileException;

	public String getSource() {
		return source.toString();
	}

	protected boolean ignoreField(CtField field) {
		boolean ignore = false;

		// Ignores compiler fields.
		if (field.getName().equals("__id")
				|| field.getName().equals("__persistableMetadata")) {
			ignore = true;
		}

		// Ignores transient and static fields.
		int modifier = field.getModifiers();
		if (Modifier.isTransient(modifier) || Modifier.isStatic(modifier)) {
			ignore = true;
		}

		return ignore;
	}

	public boolean isIDable(CtClass ctClass) throws NotFoundException {
		CtClass idableClass = classPool.get(IDable.class.getName());
		return ctClass.subtypeOf(idableClass);
	}

}
