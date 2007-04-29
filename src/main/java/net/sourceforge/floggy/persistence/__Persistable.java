package net.sourceforge.floggy.persistence;

/**
 * An internal <code>interface</code> that holds all methods used by the
 * persistence module. All classes identified as <b>persistable</b> ({@link Persistable})
 * will be modified to implement all methods of this <code>interface</code>.
 * 
 * @since 1.0
 * @see Persistable
 */
public interface __Persistable {

	public int __getId();

	public void __load(int id) throws Exception;

	public void __load(byte[] buffer) throws Exception;

	public int __save() throws Exception;

	public void __delete() throws Exception;

	public PersistableMetadata __getPersistableMetadata();

}
