/*
 * Criado em 24/08/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.pool;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;

public class DirectoryInputPool implements InputPool {

    protected File rootDirectory;

    protected Vector files;

    public DirectoryInputPool(File directory) throws IOException {
	this.rootDirectory = directory;
	this.files = new Vector();
	this.initFiles(directory);
    }

    public int getFileCount() {
	return this.files.size();
    }

    public String getFileName(int index) {
	return this.files.get(index).toString();
    }

    private void initFiles(File directory) throws IOException {
	File[] files = directory.listFiles();

	if (files != null) {
	    String rootPath = this.rootDirectory.getCanonicalPath();

	    for (int i = 0; i < files.length; i++) {
		if (files[i].isDirectory()) {
		    this.initFiles(files[i]);
		} else if (files[i].isFile()) {
		    String filePath = files[i].getCanonicalPath();

		    String className = filePath.substring(rootPath.length());
		    if (className.startsWith(File.separator)) {
			className = className.substring(1);
		    }

		    this.files.add(className);
		}
	    }
	}
    }

    public URL getFileURL(int index) throws IOException {
	File file = new File(rootDirectory, getFileName(index));
	return file.toURL();
    }
}
