package net.sourceforge.floggy.persistence.fr2422928;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import junit.framework.TestCase;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.RMSMemoryMicroEmulator;
import net.sourceforge.floggy.persistence.impl.MetadataManagerUtil;
import net.sourceforge.floggy.persistence.impl.PersistableManagerImpl;
import net.sourceforge.floggy.persistence.migration.Enumeration;
import net.sourceforge.floggy.persistence.migration.FieldPersistableInfo;
import net.sourceforge.floggy.persistence.migration.MigrationManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.microemu.MIDletBridge;
import org.microemu.MicroEmulator;

public abstract class FR2422928AbstractVersionTest extends TestCase {

	protected MicroEmulator emulator;

	public abstract String getVersion();
	
	protected void setUp() throws Exception {
		emulator = MIDletBridge.getMicroEmulator();
		FileUtils.forceMkdir(new File("target/fr2422928/rms/" + getVersion()));
		IOUtils.copy(new FileInputStream("src/test/rms/" + getVersion() + "/FloggyProperties.rms"), new FileOutputStream("target/fr2422928/rms/" + getVersion() + "/FloggyProperties.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/" + getVersion() + "/FR2422928-284317062.rms"), new FileOutputStream("target/fr2422928/rms/" + getVersion() + "/FR2422928-284317062.rms"));
		IOUtils.copy(new FileInputStream("src/test/rms/" + getVersion() + "/Freezed-1739440490.rms"), new FileOutputStream("target/fr2422928/rms/" + getVersion() + "/Freezed-1739440490.rms"));
		MIDletBridge.setMicroEmulator(new RMSMemoryMicroEmulator("target/fr2422928/rms/" + getVersion()));
		MetadataManagerUtil.init();
		PersistableManagerImpl.reset();
	}

	protected void tearDown() throws Exception {
		MIDletBridge.setMicroEmulator(emulator);
		MetadataManagerUtil.init();
	}

	public void testFreezedClassIteration() throws Exception {
		PersistableManager manager = PersistableManager.getInstance();
		Hashtable properties = new Hashtable();
		properties.put(MigrationManager.MIGRATE_FROM_PREVIOUS_1_3_0_VERSION, Boolean.TRUE);
		properties.put(MigrationManager.ITERATION_MODE, Boolean.TRUE);
		
		Enumeration enumeration = MigrationManager.getInstance().start(Freezed.class, properties);
	
		assertEquals(2, enumeration.getSize());
	
		if (enumeration.hasMoreElements()) {
			Hashtable data = enumeration.nextElement();
			assertNull(data.get("uuid"));
			assertNull(data.get("deadline"));
			assertNull(data.get("description"));
			assertNull(data.get("nested"));
			assertEquals(0, ((Short)data.get("code")).shortValue());
		}
	
		if (enumeration.hasMoreElements()) {
			Hashtable data = enumeration.nextElement();
			assertEquals(Freezed.UUID, data.get("uuid"));
			assertEquals(Freezed.DEADLINE, data.get("deadline"));
			assertEquals(Freezed.DESCRIPTION, data.get("description"));
	
			FieldPersistableInfo freezedInfo = (FieldPersistableInfo) data.get("nested");
			Freezed nested = new Freezed();
			manager.load(nested, freezedInfo.getId());
			
			assertEquals(Freezed.NESTED, nested);
			assertEquals(Freezed.CODE, ((Short)data.get("code")).shortValue());
		}
	}

	public void testGetRMSBasedMetadata() {
		assertNull(MetadataManagerUtil.getRMSBasedMetadata(FR2422928.class.getName()));
		assertNull(MetadataManagerUtil.getRMSBasedMetadata(Freezed.class.getName()));
	}

	public void testGetRMSVersion() {
		assertEquals(getVersion(), MetadataManagerUtil.getRMSVersion());
	}

	public void testNoMigrationExecution() {
		PersistableManager manager = PersistableManager.getInstance();
		try {
			manager.find(Freezed.class, null, null);
			fail("Should throw a exception because no migration was made!");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), FloggyException.class);
		}
		
	}

	public void testNotThrowExceptionWhenMigratingFromPreviousVersion() {
		Hashtable properties = new Hashtable();
		properties.put(MigrationManager.MIGRATE_FROM_PREVIOUS_1_3_0_VERSION, Boolean.TRUE);
		
		try {
			Enumeration enumeration = MigrationManager.getInstance().start(FR2422928.class, properties);
			assertNotNull(enumeration);
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}
	
	public void testQuickMigration() {
		MigrationManager manager = MigrationManager.getInstance();
		
		List notMigratedClasses = Arrays.asList(manager.getNotMigratedClasses());
		assertTrue(notMigratedClasses.contains(Freezed.class.getName()));
		try {
			manager.quickMigration(Freezed.class);

			notMigratedClasses = Arrays.asList(manager.getNotMigratedClasses());
			assertFalse(notMigratedClasses.contains(Freezed.class.getName()));
		} catch (Exception ex) {
			fail(ex.getMessage());
		}
	}

	public void testQuickMigrationOfAll() throws Exception {
		MigrationManager manager = MigrationManager.getInstance();
		String[] notMigratedClasses = manager.getNotMigratedClasses();

		for (int i = 0; i < notMigratedClasses.length; i++) {
			String className = notMigratedClasses[i];
			manager.quickMigration(Class.forName(className));
		}

		notMigratedClasses = manager.getNotMigratedClasses();
		assertNotNull(notMigratedClasses);
		assertEquals(0, notMigratedClasses.length);
	}
	
	public void testThrowExceptionWhenMigratingFromPreviousVersion() {
		try {
			MigrationManager.getInstance().start(FR2422928.class, null);
			fail("Should throw a exception because the property MigrationManager.MIGRATE_FROM_PREVIOUS_FLOGGY_VERSION is not set to true!");
		} catch (Exception ex) {
			assertEquals(ex.getClass(), FloggyException.class);
		}
	}

}