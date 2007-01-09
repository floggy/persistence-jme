package net.sourceforge.floggy.persistence.pool;

import java.io.File;
import java.io.IOException;

import net.sourceforge.floggy.persistence.WeaverException;

public class PoolFactory {

    public static InputPool createInputPool(File file) throws WeaverException {
	if (!file.exists()) {
	    throw new WeaverException(file.getName()
		    + " does not exists. (Full path: " + file.getPath() + ")");
	}

	if (file.isFile() && file.getName().endsWith(".jar")) {
	    try {
		return new JarInputPool(file);
	    } catch (IOException e) {
		throw new WeaverException("Invalid input file type.");
	    }
	} else if (file.isFile() && file.getName().endsWith(".zip")) {
	    try {
		return new ZipInputPool(file);
	    } catch (IOException e) {
		throw new WeaverException("Invalid input file type.");
	    }
	} else if (file.isDirectory()) {
	    try {
		return new DirectoryInputPool(file);
	    } catch (IOException e) {
		throw new WeaverException(e.getMessage());
	    }
	}

	throw new WeaverException(
		file.getName()
			+ " is a invalid file type. Valid types are: .jar, .zip and directories.");
    }

    public static OutputPool createOutputPool(File file) throws WeaverException {
	if (file.isFile() && file.getName().endsWith(".jar")) {
	    // try {
	    // return new JarInputPool(new JarInputStream(new
	    // FileInputStream(
	    // file)));
	    // } catch (IOException e) {
	    // throw new CompilerException("Invalid input file type.");
	    // }
	    // } else if (file.isFile() && file.getName().endsWith(".zip"))
	    // {
	    // try {
	    // return new ZipInputPool(new ZipInputStream(new
	    // FileInputStream(
	    // file)));
	    // } catch (IOException e) {
	    // throw new CompilerException("Invalid input file type.");
	    // }
	} else {
	    return new DirectoryOutputPool(file);
	}

	throw new WeaverException(
		file.getName()
			+ " is a invalid file type for output file. Valid types are: .jar, .zip and directories.");
    }

}
