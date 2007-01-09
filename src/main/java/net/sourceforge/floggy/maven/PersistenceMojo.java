package net.sourceforge.floggy.maven;

import java.io.File;
import java.util.List;

import net.sourceforge.floggy.persistence.Weaver;

import org.apache.commons.io.FileUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.project.MavenProject;

/**
 * Goal which runs the Floggy compiler.
 * 
 * @goal weaver
 * @phase process-classes
 */
public class PersistenceMojo extends AbstractMojo {
    /**
         * Location of the file.
         * 
         * @parameter expression="${project.build.outputDirectory}"
         * @required
         */
    private File input;

    /**
         * Location of the file.
         * 
         * @parameter expression="${project.build.outputDirectory}"
         * @required
         */
    private File output;

    /**
         * Location of the file.
         * 
         * @parameter expression="${project.build.outputDirectory}"
         * @required
         */
    private boolean generateSource = false;

    /**
         * Location of the file.
         * 
         * @parameter expression="${project}"
         * @required
         */
    private MavenProject project;

    public void execute() throws MojoExecutionException {
	Weaver weaver = new Weaver();
	try {
	    List list = project.getCompileClasspathElements();
	    File temp = new File(project.getBuild().getDirectory(), String
		    .valueOf(System.currentTimeMillis()));
	    FileUtils.forceMkdir(temp);
	    weaver.setOutputFile(temp);
	    weaver.setInputFile(input);
	    weaver.setClasspath((String[]) list.toArray(new String[0]));
	    weaver.setGenerateSource(generateSource);
	    weaver.execute();
	    FileUtils.copyDirectory(temp, output);
	    FileUtils.forceDelete(temp);

	} catch (Exception e) {
	    throw new MojoExecutionException(e.getMessage(), e);
	}
    }

}
