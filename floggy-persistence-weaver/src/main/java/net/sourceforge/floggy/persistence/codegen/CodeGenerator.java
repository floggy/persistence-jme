package net.sourceforge.floggy.persistence.codegen;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtNewMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import net.sourceforge.floggy.persistence.ClassVerifier;
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

	private boolean generateSource;

	private StringBuffer source;

	/**
	 * Creates a new code generator for the class.
	 * 
	 * @param ctClass
	 *            Class to be modified.
	 */
	public CodeGenerator(CtClass ctClass) {
		this.ctClass = ctClass;
	}

	/**
	 * Creates a new code generator for the class.
	 * 
	 * @param ctClass
	 *            Class to be modified.
	 * @param generateSource
	 *            indicate the generation or not os source code.
	 */
	public CodeGenerator(CtClass ctClass, boolean generateSource) {
		this.ctClass = ctClass;
		this.generateSource = generateSource;
	}

	/**
	 * Generate all the necessary source code for this class.
	 * 
	 * @throws NotFoundException
	 * @throws CannotCompileException
	 */
	public void generateCode() throws NotFoundException, CannotCompileException {
		if (generateSource) {
			source = new StringBuffer();
		}
		// Implements interface
		this.generatePersistableInterface();

		// Attributes
		this.generateIdField();
		this.generatePersistableMetadataField();

		// Methods
		this.generateGetPersistableMetadata();
		this.generateLoadFromBufferMethod();
		this.generateLoadFromIdMethod();
		this.generateSaveMethod();
		this.generateDeleteMethod();
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
		String buffer = "private int __id = -1;";

		if (generateSource) {
			source.append(CodeFormatter.format(buffer.toString()));
		}
		this.ctClass.addField(CtField.make(buffer, ctClass));
	}

	/**
	 * 
	 * @throws CannotCompileException
	 */
	private void generatePersistableMetadataField() throws CannotCompileException {
		String recordStoreName= ctClass.getSimpleName()+ctClass.getName().hashCode();
		String buffer = "private final static net.sourceforge.floggy.persistence.PersistableMetadata __persistableMetadata = new net.sourceforge.floggy.persistence.PersistableMetadata(\""+recordStoreName+"\");";

		if (generateSource) {
			source.append(CodeFormatter.format(buffer.toString()));
		}
		this.ctClass.addField(CtField.make(buffer, ctClass));
	}

	/**
	 * 
	 * @throws CannotCompileException
	 */
	private void generateGetPersistableMetadata() throws CannotCompileException {
		StringBuffer buffer = new StringBuffer();
		buffer.append("public net.sourceforge.floggy.persistence.PersistableMetadata __getPersistableMetadata() {\n");
		//TODO podemos adicionar a quantidade de registros nesse persistable
		buffer.append("return __persistableMetadata;\n");
		buffer.append("}\n");

		if (generateSource) {
			source.append(CodeFormatter.format(buffer.toString()));
		}
		this.ctClass.addMethod(CtNewMethod.make(buffer.toString(), ctClass));
	}

	/**
	 * 
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	private void generateLoadFromBufferMethod() throws CannotCompileException,
			NotFoundException {
		StringBuffer buffer = new StringBuffer();
		// Header
		buffer
				.append("public void __load(byte[] buffer) throws java.lang.Exception {\n");

		// Streams
		buffer
				.append("java.io.ByteArrayInputStream bais = new java.io.ByteArrayInputStream(buffer);\n");
		buffer
				.append("java.io.DataInputStream dis = new java.io.DataInputStream(bais);\n");

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
						|| field.getName().equals("__metadata")) {
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
						field.getName(), field.getType());
				if (generator != null) {
					buffer.append(generator.getLoadCode());
				}
			}
		}

		// Close the streams
		buffer.append("dis.close();\n");

		buffer.append("}\n");

		if (generateSource) {
			source.append(CodeFormatter.format(buffer.toString()));
		}
		// if (ctClass.getName().contains("primitive.array.TestShort")) {
		// System.out.println(CodeFormatter.format(functionLoad_Buffer.toString()));
		// }

		// SyntheticAttribute attribute= new SyntheticAttribute();
		this.ctClass.addMethod(CtNewMethod
				.make(buffer.toString(), this.ctClass));
	}

	/**
	 * 
	 * @throws CannotCompileException
	 */
	private void generateLoadFromIdMethod() throws CannotCompileException {
		StringBuffer buffer = new StringBuffer();

		buffer
				.append("public void __load(int id) throws java.lang.Exception {\n");

		// RecordStore
		buffer
				.append("javax.microedition.rms.RecordStore rs = net.sourceforge.floggy.persistence.PersistableManager.getRecordStore(__persistableMetadata);\n");
		buffer.append("byte[] buffer = rs.getRecord(id);\n");
		buffer.append("rs.closeRecordStore();\n");

		// Load
		buffer.append("this.__load(buffer);\n");
		buffer.append("this.__id = id;\n");
		buffer.append("}");

		if (generateSource) {
			source.append(CodeFormatter.format(buffer.toString()));
		}

		this.ctClass.addMethod(CtNewMethod
				.make(buffer.toString(), this.ctClass));
	}

	/**
	 * 
	 * @throws CannotCompileException
	 */
	private void generateDeleteMethod() throws CannotCompileException {
		StringBuffer buffer = new StringBuffer();

		buffer.append("public void __delete() throws java.lang.Exception {\n");

		// verifing if the class has the void beforeDelete() method
		boolean containsBeforeDeteleMethod = false;
		try {
			ctClass.getMethod("beforeDelete", "()V");
			containsBeforeDeteleMethod = true;
		} catch (NotFoundException e) {
		}
		if (containsBeforeDeteleMethod) {
			buffer.append("this.beforeDelete();\n");
		}

		// RecordStore
		buffer.append("if (this.__id != -1) {\n");
		buffer
				.append("javax.microedition.rms.RecordStore rs = net.sourceforge.floggy.persistence.PersistableManager.getRecordStore(__persistableMetadata);\n");
		buffer.append("rs.deleteRecord(this.__id);\n");
		buffer.append("rs.closeRecordStore();\n");

		buffer.append("}\n");
		buffer.append("}");

		if (generateSource) {
			source.append(CodeFormatter.format(buffer.toString()));
		}

		this.ctClass.addMethod(CtNewMethod
				.make(buffer.toString(), this.ctClass));
	}

	/**
	 * 
	 * @throws CannotCompileException
	 * @throws NotFoundException
	 */
	private void generateSaveMethod() throws CannotCompileException,
			NotFoundException {
		StringBuffer buffer = new StringBuffer();
		// Header
		buffer.append("public int __save() throws java.lang.Exception {\n");

		// Streams
		buffer
				.append("java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();\n");
		buffer
				.append("java.io.DataOutputStream dos = new java.io.DataOutputStream(baos);\n");

		// Save the superclass if it is persistable.
		CtClass superClass = this.ctClass.getSuperclass();
		ClassVerifier verifier = new ClassVerifier(superClass);
		if (verifier.isPersistable()) {
			buffer.append(SuperClassGenerator.generateSaveSource(superClass));
		}

		// RecordStore (open)
		buffer
				.append("javax.microedition.rms.RecordStore rs = net.sourceforge.floggy.persistence.PersistableManager.getRecordStore(__persistableMetadata);\n");

		CtField[] fields = ctClass.getDeclaredFields();
		if (fields != null && fields.length > 0) {
			SourceCodeGenerator generator;
			CtField field;
			for (int i = 0; i < fields.length; i++) {
				field = fields[i];

				// Ignores compiler fields.
				if (field.getName().equals("__id")
						|| field.getName().equals("__metadata")) {
					continue;
				}
				// Ignores transient and static fields.
				int modifier = field.getModifiers();
				if (Modifier.isTransient(modifier)
						|| Modifier.isStatic(modifier)) {
					continue;
				}

				generator = SourceCodeGeneratorFactory.getSourceCodeGenerator(
						field.getName(), field.getType());
				if (generator != null) {
					buffer.append(generator.getSaveCode());
				}
			}
		}

		// RecordStore (save and close)
		buffer.append("if(this.__id == -1) {\n");
		buffer
				.append("this.__id = rs.addRecord(baos.toByteArray(), 0, baos.size());\n");
		buffer.append("}\n");
		buffer.append("else {\n");
		buffer
				.append("rs.setRecord(this.__id, baos.toByteArray(), 0, baos.size());\n");
		buffer.append("}\n");
		buffer.append("rs.closeRecordStore();\n");

		// Close the streams
		buffer.append("dos.close();\n");

		buffer.append("return this.__id;\n");
		buffer.append("}");

		if (generateSource) {
			source.append(CodeFormatter.format(buffer.toString()));
		}

		// if (ctClass.getName().contains("VectorArray")) {
		// System.out.println(CodeFormatter.format(functionSave.toString()));
		// }

		ctClass.addMethod(CtNewMethod.make(buffer.toString(), ctClass));

	}

	/**
	 * @return the generateSource
	 */
	public boolean isGenerateSource() {
		return generateSource;
	}

	/**
	 * @param generateSource
	 *            the generateSource to set
	 */
	public void setGenerateSource(boolean generateSource) {
		this.generateSource = generateSource;
		if (generateSource) {
			source = new StringBuffer();
		}
	}

	public String getSource() {
		return source.toString();
	}

}
