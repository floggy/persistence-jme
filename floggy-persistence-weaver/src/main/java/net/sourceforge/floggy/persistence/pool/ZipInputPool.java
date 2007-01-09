package net.sourceforge.floggy.persistence.pool;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZipInputPool implements InputPool {

    protected File file;

    protected Vector files;

    public ZipInputPool(File file) throws IOException {
	this.files = new Vector();
	this.initFiles(file);
    }

    public int getFileCount() {
	return this.files.size();
    }

    public String getFileName(int index) {
	return this.files.get(index).toString();
    }

    private void initFiles(File file) throws IOException {
	ZipInputStream in = new ZipInputStream(new FileInputStream(file));
	for (ZipEntry entry = in.getNextEntry(); entry != null; entry = in
		.getNextEntry()) {
	    String name = entry.getName();
	    name = name.replace('/', File.separatorChar);
	    this.files.add(name);
	}
    }

    public URL getFileURL(int index) throws IOException {
	String name = getFileName(index);
	// jar:http://www.foo.com/bar/baz.jar!/COM/foo/Quux.class
	System.out.println("jar:" + file.toURL() + "!" + name);
	return null;
    }

}
