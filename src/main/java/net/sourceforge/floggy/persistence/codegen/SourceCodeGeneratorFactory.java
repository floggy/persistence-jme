/*
 * Criado em 05/09/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.codegen;

import javassist.CtClass;
import javassist.NotFoundException;

public class SourceCodeGeneratorFactory {

    private static char indexForIteration = 'a';

    public static SourceCodeGenerator getSourceCodeGenerator(String fieldName,
	    CtClass classType) throws NotFoundException {
	SourceCodeGenerator generator;

	// Primitive types
	generator = new PrimitiveTypeGenerator(fieldName, classType);
	if (generator.isInstanceOf()) {
	    return generator;
	}

	// Wrapper classes
	generator = new WrapperGenerator(fieldName, classType);
	if (generator.isInstanceOf()) {
	    return generator;
	}

	// String
	generator = new StringGenerator(fieldName, classType);
	if (generator.isInstanceOf()) {
	    return generator;
	}

	// Date
	generator = new DateGenerator(fieldName, classType);
	if (generator.isInstanceOf()) {
	    return generator;
	}

	// Persistable
	generator = new PersistableGenerator(fieldName, classType);
	if (generator.isInstanceOf()) {
	    return generator;
	}

	// Array
	generator = new ArrayGenerator(fieldName, classType);
	if (generator.isInstanceOf()) {
	    ((AttributeIterableGenerator) generator)
		    .setUpInterableVariable(getNextIndexForIteration());
	    return generator;
	}

	// Vector
	generator = new VectorGenerator(fieldName, classType);
	if (generator.isInstanceOf()) {
	    ((AttributeIterableGenerator) generator)
		    .setUpInterableVariable(getNextIndexForIteration());
	    return generator;
	}

	return null;
    }

    private static char getNextIndexForIteration() {
	indexForIteration++;
	if (indexForIteration == 'z') {
	    indexForIteration = 'a';
	}
	return indexForIteration;
    }

}
