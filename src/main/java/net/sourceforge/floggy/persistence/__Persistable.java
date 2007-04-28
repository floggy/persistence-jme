package net.sourceforge.floggy.persistence;

/*
 * Internal interface to identify a persistable class.
 * All classes that implement Persistable will implement automatically this class. This work is done by the Compiler!
 */
public interface __Persistable {
	
    public int __getId();

    public void __load(int id) throws Exception;

    public void __load(byte[] buffer) throws Exception;

    public int __save() throws Exception;

    public void __delete() throws Exception;
    
    public PersistableMetadata __getPersistableMetadata();

}
