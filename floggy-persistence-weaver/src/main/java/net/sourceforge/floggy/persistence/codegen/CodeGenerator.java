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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.floggy.persistence.Configuration;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.IDable;
import net.sourceforge.floggy.persistence.Weaver;
import net.sourceforge.floggy.persistence.formatter.CodeFormatter;
import net.sourceforge.floggy.persistence.impl.IndexMetadata;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public abstract class CodeGenerator {
	private static final Log LOG = LogFactory.getLog(CodeGenerator.class);

	/**
	 * DOCUMENT ME!
	 */
	protected ClassPool classPool;

	/**
	 * DOCUMENT ME!
	 */
	protected Configuration configuration;

	/** Class to be modified; */
	protected CtClass ctClass;

	/**
	 * DOCUMENT ME!
	 */
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

	/**
	* Generate all the necessary source code for this class.
	*
	* @throws NotFoundException
	* @throws CannotCompileException
	*/
	public final void generateCode()
		throws NotFoundException, CannotCompileException {
		if (configuration.isGenerateSource()) {
			source = new StringBuffer();
		}

		this.generateDefaultConstructor();

		this.generateNonFinalFields();

		this.generateSpecificMethods();

		this.generatePersistableInterface();

		this.generateGetIndexValueMethod();
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getSource() {
		return source.toString();
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
	public boolean isIDable(CtClass ctClass) throws NotFoundException {
		CtClass idableClass = classPool.get(IDable.class.getName());

		return ctClass.subtypeOf(idableClass);
	}

	/**
	 * DOCUMENT ME!
	*
	* @param buffer DOCUMENT ME!
	*
	* @throws CannotCompileException DOCUMENT ME!
	*/
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

	/**
	 * DOCUMENT ME!
	*
	* @param buffer DOCUMENT ME!
	*
	* @throws CannotCompileException DOCUMENT ME!
	*/
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
	 * DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	* @throws CannotCompileException DOCUMENT ME!
	*/
	protected void generateDefaultConstructor()
		throws NotFoundException, CannotCompileException {
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
	DOCUMENT ME!
	*
	* @throws CannotCompileException
	* @throws NotFoundException DOCUMENT ME!
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
				IndexMetadata indexMetadata =
					(IndexMetadata) indexMetadatas.elementAt(i);

				buffer.append("if (\"" + indexMetadata.getName()
					+ "\".equals(indexName)) {\n");

				Vector fields = indexMetadata.getFields();
				int fieldsSize = fields.size();

				for (int j = 0; j < fieldsSize; j++) {
					String fieldName = (String) fields.get(j);

					CtClass fieldType = ctClass.getField(fieldName).getType();

					if (fieldType.isPrimitive()) {
						buffer.append("return new "
							+ PrimitiveTypeGenerator.getWrapperNameClass(fieldType)
							+ "(this." + fieldName + ");\n");
					} else {
						buffer.append("return this." + fieldName + ";\n");
					}
				}

				buffer.append("}\n");
			}
		}

		buffer.append("return null;\n");
		buffer.append("}\n");

		addMethod(buffer);
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @throws CannotCompileException
	* @throws NotFoundException DOCUMENT ME!
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

			addMethod(buffer);
		}
	}

	/**
	 * DOCUMENT ME!
	*/
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
	* 
	DOCUMENT ME!
	*
	* @throws NotFoundException
	*/
	protected void generatePersistableInterface() throws NotFoundException {
		this.ctClass.addInterface(this.ctClass.getClassPool()
			 .get(Weaver.__PERSISTABLE_CLASSNAME));
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	* @throws CannotCompileException DOCUMENT ME!
	*/
	protected abstract void generateSpecificMethods()
		throws NotFoundException, CannotCompileException;

	/**
	 * DOCUMENT ME!
	*
	* @param field DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	protected boolean ignoreField(CtField field) {
		boolean ignore = false;

		if (field.getName().equals("__id")
			 || field.getName().equals("__persistableMetadata")) {
			ignore = true;
		}

		int modifier = field.getModifiers();

		if (Modifier.isTransient(modifier) || Modifier.isStatic(modifier)) {
			ignore = true;
		}

		return ignore;
	}
}
