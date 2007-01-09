package net.sourceforge.floggy.persistence;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.apache.tools.ant.types.Reference;

public class WeaverTask extends Task {

    protected Path classpath;

    protected File input;

    protected File output;

    protected boolean generateSource;

    public void execute() throws BuildException {
	Weaver compiler = new Weaver();

	try {
	    System.out.println(classpath);
	    compiler.setClasspath(classpath.list());
	    compiler.setInputFile(input);
	    compiler.setOutputFile(output);
	    compiler.setGenerateSource(generateSource);
	    compiler.execute();
	} catch (WeaverException e) {
	    e.printStackTrace();
	    throw new BuildException(e);
	}
    }

    /**
         * Adds a reference to a classpath defined elsewhere.
         * 
         * @param r
         *                a reference to a classpath
         */
    public void setBootClasspathRef(Reference r) {
	if (classpath == null) {
	    classpath = new Path(getProject());
	}
	classpath.append((Path) r.getReferencedObject());
    }

    /**
         * Adds a reference to a classpath defined elsewhere.
         * 
         * @param r
         *                a reference to a classpath
         */
    public void setClasspathRef(Reference r) {
	if (classpath == null) {
	    classpath = new Path(getProject());
	}
	classpath.append((Path) r.getReferencedObject());
    }

    public void setGenerateSource(boolean generateSource) {
	this.generateSource = generateSource;
    }

    public void setInput(File input) {
	this.input = input;
    }

    public void setOutput(File output) {
	this.output = output;
    }

}
