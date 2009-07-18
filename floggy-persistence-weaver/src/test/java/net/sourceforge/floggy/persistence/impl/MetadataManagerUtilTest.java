package net.sourceforge.floggy.persistence.impl;

import junit.framework.TestCase;

public class MetadataManagerUtilTest extends TestCase {
	
	public void testGetRMSVersion() throws Exception {
		MetadataManagerUtil.load();
		assertEquals(MetadataManagerUtil.getBytecodeVersion(), MetadataManagerUtil.getRMSVersion());
	}

	public void testGetBytecodeVersion() throws Exception {
		assertEquals(MetadataManagerUtil.CURRENT_VERSION, MetadataManagerUtil.getBytecodeVersion());
	}
}
