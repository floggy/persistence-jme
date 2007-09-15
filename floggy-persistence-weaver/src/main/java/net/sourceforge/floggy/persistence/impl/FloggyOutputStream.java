package net.sourceforge.floggy.persistence.impl;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class FloggyOutputStream extends DataOutputStream {
	
	public FloggyOutputStream() {
		super(new ByteArrayOutputStream());
	}
	
	public byte[] toByteArray() {
		return ((ByteArrayOutputStream)out).toByteArray();
	}

}
