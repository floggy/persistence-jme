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
package net.sourceforge.floggy.persistence.codegen.strategy;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;

import net.sourceforge.floggy.persistence.Configuration;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.codegen.CodeGenerator;
import net.sourceforge.floggy.persistence.codegen.SourceCodeGenerator;
import net.sourceforge.floggy.persistence.codegen.SourceCodeGeneratorFactory;
import net.sourceforge.floggy.persistence.impl.PersistableMetadata;
import net.sourceforge.floggy.persistence.strategy.JoinedStrategy;
import net.sourceforge.floggy.persistence.strategy.SingleStrategy;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class PerClassStrategyCodeGenerator extends CodeGenerator {
	/**
	 * Creates a new PerClassStrategyCodeGenerator object.
	 *
	 * @param ctClass DOCUMENT ME!
	 * @param classPool DOCUMENT ME!
	 * @param configuration DOCUMENT ME!
	 *
	 * @throws FloggyException DOCUMENT ME!
	 * @throws NotFoundException DOCUMENT ME!
	 */
	public PerClassStrategyCodeGenerator(CtClass ctClass, ClassPool classPool,
		Configuration configuration) throws FloggyException, NotFoundException {
		super(ctClass, classPool, configuration);

		CtClass joinedStrategy = classPool.get(JoinedStrategy.class.getName());
		CtClass singleStrategy = classPool.get(SingleStrategy.class.getName());

		if (ctClass.subtypeOf(joinedStrategy) || ctClass.subtypeOf(singleStrategy)) {
			throw new FloggyException(
				"You cannot use two persistence strategies on the same object hierarchy!");
		}
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @throws CannotCompileException
	* @throws NotFoundException DOCUMENT ME!
	*/
	protected void generateDeleteMethod()
		throws CannotCompileException, NotFoundException {
		if (!Modifier.isAbstract(ctClass.getModifiers())) {
			StringBuffer buffer = new StringBuffer();

			buffer.append("public void __delete() throws java.lang.Exception {\n");

			try {
				ctClass.getDeclaredMethod("delete");
				buffer.append("this.delete();\n");
			} catch (NotFoundException nfex) {
			}

			buffer.append("}");

			addMethod(buffer);
		}
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @throws CannotCompileException
	* @throws NotFoundException
	*/
	protected void generateDeserializeMethod()
		throws CannotCompileException, NotFoundException {
		if (!Modifier.isAbstract(ctClass.getModifiers())) {
			StringBuffer buffer = new StringBuffer();

			buffer.append(
				"public void __deserialize(byte[] buffer, boolean lazy) throws java.lang.Exception {\n");

			buffer.append(
				"java.io.DataInputStream dis = new java.io.DataInputStream(new java.io.ByteArrayInputStream(buffer));\n");

			CtField[] fields = ctClass.getFields();

			if ((fields != null) && (fields.length > 0)) {
				SourceCodeGenerator generator;
				CtField field;

				for (int i = 0; i < fields.length; i++) {
					field = fields[i];

					if (ignoreField(field)) {
						continue;
					}

					generator = SourceCodeGeneratorFactory.getSourceCodeGenerator(ctClass,
							field.getName(), field.getType());

					if (generator != null) {
						buffer.append(generator.getLoadCode());
					}
				}
			}

			buffer.append("dis.close();\n");

			buffer.append("}\n");

			addMethod(buffer);
		}
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @throws CannotCompileException
	* @throws NotFoundException DOCUMENT ME!
	*/
	protected void generateGetIdMethod()
		throws CannotCompileException, NotFoundException {
		if (isRootPersistableClass(ctClass)) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("public int __getId() {\n");
			buffer.append("return __id;\n");
			buffer.append("}\n");

			addMethod(buffer);
		}
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
			if (!Modifier.isAbstract(ctClass.getModifiers())) {
				StringBuffer buffer = new StringBuffer();
				buffer.append("public String getRecordStoreName() {\n");
				buffer.append("return \""
					+ configuration.getPersistableMetadata(ctClass.getName())
					 .getRecordStoreName() + "\";\n");
				buffer.append("}\n");

				addMethod(buffer);
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
	protected void generateIdField()
		throws CannotCompileException, NotFoundException {
		if (isRootPersistableClass(ctClass)) {
			String buffer = "private int __id = -1;";

			addField(new StringBuffer(buffer));
		}
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @throws CannotCompileException
	* @throws NotFoundException
	*/
	protected void generateSerializeMethod()
		throws CannotCompileException, NotFoundException {
		if (!Modifier.isAbstract(ctClass.getModifiers())) {
			StringBuffer buffer = new StringBuffer();

			buffer.append(
				"public byte[] __serialize(boolean isRealObject) throws java.lang.Exception {\n");

			buffer.append(
				"net.sourceforge.floggy.persistence.impl.FloggyOutputStream fos= new net.sourceforge.floggy.persistence.impl.FloggyOutputStream();\n");

			CtField[] fields = ctClass.getFields();

			if ((fields != null) && (fields.length > 0)) {
				SourceCodeGenerator generator;
				CtField field;

				for (int i = 0; i < fields.length; i++) {
					field = fields[i];

					if (ignoreField(field)) {
						continue;
					}

					generator = SourceCodeGeneratorFactory.getSourceCodeGenerator(ctClass,
							field.getName(), field.getType());

					if (generator != null) {
						buffer.append(generator.getSaveCode());
					}
				}
			}

			buffer.append("fos.flush();\n");
			buffer.append("return fos.toByteArray();\n");
			buffer.append("}");

			addMethod(buffer);
		}
	}

	/**
	* 
	DOCUMENT ME!
	*
	* @throws CannotCompileException
	* @throws NotFoundException DOCUMENT ME!
	*/
	protected void generateSetIdMethod()
		throws CannotCompileException, NotFoundException {
		if (isRootPersistableClass(ctClass)) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("public void __setId(int id) {\n");
			buffer.append("this.__id= id;\n");

			if (isIDable(ctClass)) {
				buffer.append("this.setId(id);\n");
			}

			buffer.append("}\n");

			addMethod(buffer);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws NotFoundException DOCUMENT ME!
	* @throws CannotCompileException DOCUMENT ME!
	*/
	protected void generateSpecificMethods()
		throws NotFoundException, CannotCompileException {
		this.generateIdField();

		this.generateGetIdMethod();
		this.generateSetIdMethod();
		this.generateGetRecordStoreNameMethod();
		this.generateDeserializeMethod();
		this.generateSerializeMethod();
		this.generateDeleteMethod();
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
	protected boolean isRootPersistableClass(CtClass ctClass)
		throws NotFoundException {
		PersistableMetadata metadata =
			configuration.getPersistableMetadata(ctClass.getName());
		String superClasssName = metadata.getSuperClassName();

		return superClasssName == null;
	}
}
