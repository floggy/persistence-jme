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
package net.sourceforge.floggy.persistence.codegen;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtNewConstructor;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import net.sourceforge.floggy.persistence.ClassVerifier;
import net.sourceforge.floggy.persistence.Configuration;
import net.sourceforge.floggy.persistence.PersistableConfiguration;
import net.sourceforge.floggy.persistence.Weaver;
import net.sourceforge.floggy.persistence.formatter.CodeFormatter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class CodeGenerator
 * 
 * @author Thiago Rossato <thiagorossato@sourceforge.net>
 * @author Thiago Leão Moreira <thiagolm@sourceforge.net>
 */
public class CodeGenerator {

	private static final Log LOG = LogFactory.getLog(CodeGenerator.class);

	/**
	 * Class to be modified;
	 */
	private CtClass ctClass;

	private Configuration configuration;
	
	private StringBuffer source;

	/**
	 * Creates a new code generator for the class.
	 * 
	 * @param ctClass
	 *            Class to be modified.
	 * @param configuration
	 *            the configuration object
	 */
	public CodeGenerator(CtClass ctClass, Configuration configuration) {
		this.ctClass = ctClass;
		this.configuration= configuration;
	}

	private void generateDefaultConstructor() throws NotFoundException,
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

	private void generateNonFinalFields() {
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
	 * Generate all the necessary source code for this class.
	 * 
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 */
	public void generateCode() throws NotFoundException, CannotCompileException {
		if (configuration.isGenerateSource()) {
			source = new StringBuffer();
		}
		

		// Constructor
		this.generateDefaultConstructor();

		// Remove final
		this.generateNonFinalFields();

		// Attributes
		this.generateIdField();
		this.generatePersistableMetadataField();

		// Methods
		this.generateGetIdMethod();
		this.generateSetIdMethod();
		this.generateGetPersistableMetadata();
		this.generateGetRecordStoreNameMethod();
		this.generateDeserializeMethod();
		this.generateSerializeMethod();
		this.generateDeleteMethod();
		
		// Implements interface
		this.generatePersistableInterface();
	}

	/**
	 * @throws NotFoundException
	 */
	private void generatePersistableInterface() throws NotFoundException {
		this.ctClass.addInterface(this.ctClass.getClassPool().get(
				Weaver.__PERSISTABLE_CLASSNAME));
	}

	/**
	 * 
	 * @throws CannotCompileException
	 */
	private void generateIdField() throws CannotCompileException {
		String buffer = "public int __id = -1;";

		addField(buffer);
	}
	
	/**
	 * 
	 * @throws CannotCompileException
	 */
	private void generatePersistableMetadataField() throws CannotCompileException {
		PersistableConfiguration pConfig= configuration.getPersistableConfig(ctClass.getName());
		String buffer = "private final static net.sourceforge.floggy.persistence.impl.PersistableMetadata __persistableMetadata = new net.sourceforge.floggy.persistence.impl.PersistableMetadata(\""+pConfig.getRecordStoreName()+"\");";
		addField(buffer);
	}
	
	/**
	 * 
	 * @throws CannotCompileException
	 */
	private void generateGetIdMethod() throws CannotCompileException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("public int __getId() {\n");
		buffer.append("return __id;\n");
		buffer.append("}\n");

	    //adicionando a classe
	    addMethod(buffer);
	}

	/**
	 * 
	 * @throws CannotCompileException
	 */
	private void generateGetRecordStoreNameMethod() throws CannotCompileException {
		try {
			ctClass.getDeclaredMethod("getRecordStoreName");
		} catch (NotFoundException nfex) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("public String getRecordStoreName() {\n");
			buffer.append("return __persistableMetadata.getRecordStoreName();\n");
			buffer.append("}\n");

		    //adicionando a classe
		    addMethod(buffer);
		}
	}

	/**
	 *
	 * @throws CannotCompileException
	 */
	private void generateSetIdMethod() throws CannotCompileException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("public void __setId(int id) {\n");
		buffer.append("this.__id= id;\n");
		buffer.append("}\n");

	    //adicionando a classe
	    addMethod(buffer);
	}

	/**
	 *
	 * @throws CannotCompileException
	 */
	private void generateGetPersistableMetadata() throws CannotCompileException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("public net.sourceforge.floggy.persistence.impl.PersistableMetadata __getPersistableMetadata() {\n");
		// TODO podemos adicionar a quantidade de registros nesse persistable
		buffer.append("return __persistableMetadata;\n");
		buffer.append("}\n");

		// adicionando a classe
		addMethod(buffer);
	}

	/**
	 * 
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	private void generateDeserializeMethod() throws CannotCompileException,
			NotFoundException {
		StringBuffer buffer = new StringBuffer();
		// Header
		buffer.append("public void __deserialize(byte[] buffer, boolean lazy) throws java.lang.Exception {\n");

		// Streams
		buffer.append("java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.ByteArrayInputStream(buffer));\n");

		// Save the superclass if it is persistable.
		CtClass superClass = this.ctClass.getSuperclass();
		ClassVerifier verifier = new ClassVerifier(superClass);
		if (verifier.isPersistable()) {
			buffer.append(SuperClassGenerator.generateLoadSource(superClass));
		}

		CtField[] fields = ctClass.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			SourceCodeGenerator generator;
			CtField field;
			for (int i = 0; i < fields.length; i++) {
				field = fields[i];

				// Ignores compiler fields.
				if (field.getName().equals("__id")
						|| field.getName().equals("__persistableMetadata")) {
					continue;
				}
				// Ignores transient and static fields.
				int modifier = field.getModifiers();
				if (Modifier.isTransient(modifier)
						|| Modifier.isStatic(modifier)) {
					LOG.info("Ignoring field:" + field.getName());
					continue;
				}

				generator = SourceCodeGeneratorFactory.getSourceCodeGenerator(
						ctClass, field.getName(), field.getType());
				if (generator != null) {
					buffer.append(generator.getLoadCode());
				}
			}
		}

		// Close the streams
		buffer.append("dis.close();\n");

		buffer.append("}\n");

		// adicionando a classe
		addMethod(buffer);
	}

	/**
	 * 
	 * @throws CannotCompileException
	 */
	private void generateDeleteMethod() throws CannotCompileException, NotFoundException {
		StringBuffer buffer = new StringBuffer();

		buffer.append("public void __delete() throws java.lang.Exception {\n");

		// Save the superclass if it is persistable.
		CtClass superClass = this.ctClass.getSuperclass();
		ClassVerifier verifier = new ClassVerifier(superClass);
		if (verifier.isPersistable()) {
			buffer.append("super.__delete();\n");
			buffer.append("javax.microedition.rms.RecordStore superRS = net.sourceforge.floggy.persistence.impl.PersistableManagerImpl.getRecordStore(super.getRecordStoreName());\n");
			buffer.append("try {\n");
			buffer.append("superRS.deleteRecord(super.__getId());\n");
			buffer.append("super.__setId(-1);\n");
			buffer.append("} finally {\n");
			buffer.append("net.sourceforge.floggy.persistence.impl.PersistableManagerImpl.closeRecordStore(superRS);\n");
			buffer.append("}\n");
		}

		try {
			ctClass.getDeclaredMethod("delete");
			buffer.append("this.delete();\n");
		} catch (NotFoundException nfex) {
		}

		buffer.append("}");

		// adicionando a classe
		addMethod(buffer);
	}

	/**
	 * 
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	private void generateSerializeMethod() throws CannotCompileException,
			NotFoundException {
		StringBuffer buffer = new StringBuffer();
		// Header
		buffer.append("public byte[] __serialize() throws java.lang.Exception {\n");

		// Streams
		buffer.append("net.sourceforge.floggy.persistence.impl.FloggyOutputStream fos= new net.sourceforge.floggy.persistence.impl.FloggyOutputStream();\n");

		// Save the superclass if it is persistable.
		CtClass superClass = this.ctClass.getSuperclass();
		ClassVerifier verifier = new ClassVerifier(superClass);
		if (verifier.isPersistable()) {
			buffer.append(SuperClassGenerator.generateSaveSource(superClass));
		}

		CtField[] fields = ctClass.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			SourceCodeGenerator generator;
			CtField field;
			for (int i = 0; i < fields.length; i++) {
				field = fields[i];

				// Ignores compiler fields.
				if (field.getName().equals("__id")
						|| field.getName().equals("__persistableMetadata")) {
					continue;
				}
				// Ignores transient and static fields.
				int modifier = field.getModifiers();
				if (Modifier.isTransient(modifier)
						|| Modifier.isStatic(modifier)) {
					continue;
				}

				generator = SourceCodeGeneratorFactory.getSourceCodeGenerator(
						ctClass, field.getName(), field.getType());
				if (generator != null) {
					buffer.append(generator.getSaveCode());
				}
			}
		}

		buffer.append("fos.flush();\n");
		buffer.append("return fos.toByteArray();\n");
		buffer.append("}");

		// adicionando a classe
		addMethod(buffer);
	}

	private void addMethod(StringBuffer buffer) throws CannotCompileException {
		if (configuration.isGenerateSource()) {
			source.append(CodeFormatter.format(buffer.toString()));
			//System.out.println(source);
		}
		ctClass.addMethod(CtNewMethod.make(buffer.toString(), ctClass));
	}

	private void addField(String buffer) throws CannotCompileException {
		if (configuration.isGenerateSource()) {
			source.append(CodeFormatter.format(buffer));
		}
		ctClass.addField(CtField.make(buffer, ctClass));
	}

	public String getSource() {
		return source.toString();
	}

}
