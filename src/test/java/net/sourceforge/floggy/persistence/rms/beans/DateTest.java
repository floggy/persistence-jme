package net.sourceforge.floggy.persistence.rms.beans;

import java.util.Date;

import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.beans.FloggyDate;
import net.sourceforge.floggy.persistence.rms.AbstractTest;

public class DateTest extends AbstractTest {

    public final static Date date = new Date();

    public Persistable newInstance() {
	return new FloggyDate();
    }

    public Object getValueForSetMethod() {
	return date;
    }

}
