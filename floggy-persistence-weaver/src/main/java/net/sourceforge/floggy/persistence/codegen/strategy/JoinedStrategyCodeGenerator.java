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
package net.sourceforge.floggy.persistence.codegen.strategy;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.NotFoundException;
import net.sourceforge.floggy.persistence.ClassVerifier;
import net.sourceforge.floggy.persistence.Configuration;
import net.sourceforge.floggy.persistence.codegen.CodeGenerator;
import net.sourceforge.floggy.persistence.codegen.SourceCodeGenerator;
import net.sourceforge.floggy.persistence.codegen.SourceCodeGeneratorFactory;
import net.sourceforge.floggy.persistence.codegen.SuperClassGenerator;

/**
 * Class JoinedStrategyCodeGenerator
 * 
 * @author Thiago Rossato <thiago.rossato@floggy.org>
 * @author Thiago Leão Moreira <thiago.moreira@floggy.org>
 */
public class JoinedStrategyCodeGenerator extends CodeGenerator {

	/**
	 * Creates a new code generator for the class.
	 * 
	 * @param ctClass
	 *            Class to be modified.
	 * @param configuration
	 *            the configuration object
	 */
	public JoinedStrategyCodeGenerator(CtClass ctClass, ClassPool classPool,
			Configuration configuration) {
		super(ctClass, classPool, configuration);
	}

	/**
	 * Generate all the necessary source code for this class.
	 * 
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 */
	public void generateSpecificMethods() throws NotFoundException, CannotCompileException {
		// Attributes
		this.generateIdField();

		// Methods
		this.generateGetIdMethod();
		this.generateSetIdMethod();
		this.generateGetRecordStoreNameMethod();
		this.generateDeserializeMethod();
		this.generateSerializeMethod();
		this.generateDeleteMethod();
	}
	
	/**
	 * 
	 * @throws CannotCompileException
	 */
	protected void generateDeleteMethod() throws CannotCompileException,
			NotFoundException {
		StringBuffer buffer = new StringBuffer();

		buffer.append("public void __delete() throws java.lang.Exception {\n");

		// Save the superclass if it is persistable.
		CtClass superClass = this.ctClass.getSuperclass();
		ClassVerifier verifier = new ClassVerifier(superClass, classPool);
		if (verifier.isPersistable()) {
			buffer.append("super.__delete();\n");
			buffer.append("javax.microedition.rms.RecordStore superRS = net.sourceforge.floggy.persistence.impl.RecordStoreManager.getRecordStore(super.getRecordStoreName(), net.sourceforge.floggy.persistence.impl.MetadataManagerUtil.getClassBasedMetadata(\"" + superClass.getName() + "\"));\n");
			buffer.append("try {\n");
			buffer.append("superRS.deleteRecord(super.__getId());\n");
			buffer.append("super.__setId(0);\n");
			buffer.append("} finally {\n");
			buffer
					.append("net.sourceforge.floggy.persistence.impl.RecordStoreManager.closeRecordStore(superRS);\n");
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
	protected void generateDeserializeMethod() throws CannotCompileException,
			NotFoundException {
		StringBuffer buffer = new StringBuffer();
		// Header
		buffer.append("public void __deserialize(byte[] buffer, boolean lazy) throws java.lang.Exception {\n");

		StringBuffer tempBuffer = new StringBuffer();
		// Streams
		tempBuffer.append("java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.ByteArrayInputStream(buffer));\n");
		
		int tempBufferSize = tempBuffer.length();

		// Save the superclass if it is persistable.
		CtClass superClass = ctClass.getSuperclass();
		ClassVerifier verifier = new ClassVerifier(superClass, classPool);
		if (verifier.isPersistable()) {
			tempBuffer.append(SuperClassGenerator.generateLoadSource(superClass));
		}

		CtField[] fields = ctClass.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			SourceCodeGenerator generator;
			CtField field;
			for (int i = 0; i < fields.length; i++) {
				field = fields[i];

				if (ignoreField(field)) {
					continue;
				}

				generator = SourceCodeGeneratorFactory.getSourceCodeGenerator(
						ctClass, field.getName(), field.getType());
				if (generator != null) {
					tempBuffer.append(generator.getLoadCode());
				}
			}
		}
	

		if (tempBuffer.length() != tempBufferSize) {
			// Close the streams
			tempBuffer.append("dis.close();\n");

			buffer.append(tempBuffer);
		}
		
		buffer.append("}\n");

		// adding the method
		addMethod(buffer);
	}

	/**
	 * 
	 * @throws CannotCompileException
	 */
	protected void generateGetIdMethod() throws CannotCompileException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("public int __getId() {\n");
		buffer.append("return __id;\n");
		buffer.append("}\n");

		// adicionando a classe
		addMethod(buffer);
	}

	/**
	 * 
	 * @throws CannotCompileException
	 */
	protected void generateIdField() throws CannotCompileException {
		String buffer = "private int __id = -1;";

		addField(new StringBuffer(buffer));
	}

	/**
	 * 
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	protected void generateSerializeMethod() throws CannotCompileException,
			NotFoundException {
		StringBuffer buffer = new StringBuffer();
		// Header
		buffer.append("public byte[] __serialize() throws java.lang.Exception {\n");

		StringBuffer tempBuffer = new StringBuffer();
		// Streams
		tempBuffer.append("net.sourceforge.floggy.persistence.impl.FloggyOutputStream fos= new net.sourceforge.floggy.persistence.impl.FloggyOutputStream();\n");

		int tempBufferSize = tempBuffer.length();

		// Save the superclass if it is persistable.
		CtClass superClass = ctClass.getSuperclass();
		ClassVerifier verifier = new ClassVerifier(superClass, classPool);
		if (verifier.isPersistable()) {
			tempBuffer.append(SuperClassGenerator.generateSaveSource(superClass));
		}

		CtField[] fields = ctClass.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			SourceCodeGenerator generator;
			CtField field;
			for (int i = 0; i < fields.length; i++) {
				field = fields[i];

				if (ignoreField(field)) {
					continue;
				}

				generator = SourceCodeGeneratorFactory.getSourceCodeGenerator(
						ctClass, field.getName(), field.getType());
				if (generator != null) {
					tempBuffer.append(generator.getSaveCode());
				}
			}
		}
		
		if (tempBuffer.length() != tempBufferSize) {
			// Close the streams
			tempBuffer.append("fos.flush();\n");
			tempBuffer.append("return fos.toByteArray();\n");

			buffer.append(tempBuffer);
		} else {
			buffer.append("return new byte[0];\n");
		}

		buffer.append("}");

		// adding the method
		addMethod(buffer);
	}

	/**
	 * 
	 * @throws CannotCompileException
	 */
	protected void generateSetIdMethod() throws CannotCompileException,
			NotFoundException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("public void __setId(int id) {\n");
		buffer.append("this.__id= id;\n");
		if (isIDable(ctClass)) {
			buffer.append("this.setId(id);\n");
		}
		buffer.append("}\n");

		// adicionando a classe
		addMethod(buffer);
	}
}