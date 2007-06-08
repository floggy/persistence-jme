/**
 *  Copyright 2006 Floggy Open Source Group
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
    	
    	//melhorar isso pode haver uma cadeia de interações com mais de 26 iteradores!!!
	indexForIteration++;
	if (indexForIteration == 'z') {
	    indexForIteration = 'a';
	}
	return indexForIteration;
    }

}
