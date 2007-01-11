package net.sourceforge.floggy.persistence;

import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import net.sourceforge.floggy.persistence.gui.MainForm;

public class HospitalMIDlet extends MIDlet {

    static Display display;
    
    static MIDlet midlet;

    public HospitalMIDlet() {
        super();        
        display = Display.getDisplay(this);
        midlet = this; 
    }

    protected void startApp() throws MIDletStateChangeException {
        MainForm mainForm = new MainForm();
        HospitalMIDlet.setCurrent(mainForm);
    }

    protected void pauseApp() {
    }

    protected void destroyApp(boolean arg0) throws MIDletStateChangeException {
    }
    
    public static void setCurrent(Displayable displayable) {
        display.setCurrent(displayable);
    }
    
    public static void exit(){
        midlet.notifyDestroyed();
    }
}
