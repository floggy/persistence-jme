package net.sourceforge.floggy.persistence.pool;

import java.io.File;
import java.io.IOException;

public class JarInputPool extends ZipInputPool {

    public JarInputPool(File file) throws IOException {
	super(file);
    }

}
