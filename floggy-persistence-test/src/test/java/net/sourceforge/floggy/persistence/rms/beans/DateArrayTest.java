package net.sourceforge.floggy.persistence.rms.beans;

import java.util.Date;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyDateArray;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class DateArrayTest extends AbstractTest {

    public Persistable newInstance() {
	return new FloggyDateArray();
    }

    public Object getValueForSetMethod() {
	return new Date[] { DateTest.date, null, new Date(1000253) };
    }

}
