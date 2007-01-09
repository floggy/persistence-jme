/*
 * Criado em 24/08/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.pool;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.NotFoundException;

public interface OutputPool {

    public abstract void addClass(CtClass ctClass) throws NotFoundException,
	    IOException, CannotCompileException;

    public abstract void addFile(URL fileURL, String fileName)
	    throws IOException;

    public abstract void addResource(InputStream resourceStream, String fileName)
	    throws IOException;

    public abstract void finish();

}
