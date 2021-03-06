/**
 * Copyright (c) 2006-2011 Floggy Open Source Group. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sourceforge.floggy.persistence;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import net.sourceforge.floggy.persistence.gui.MainForm;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class HospitalMIDlet extends MIDlet {
	static Display display;
	static HospitalMIDlet midlet;
	static Exception exception;

	/**
	 * Creates a new HospitalMIDlet object.
	 */
	public HospitalMIDlet() {
		super();
		display = Display.getDisplay(this);
		midlet = this;
	}

	/**
	 * DOCUMENT ME!
	*/
	public static void exit() {
		midlet.destroyApp(true);
		midlet.notifyDestroyed();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param displayable DOCUMENT ME!
	*/
	public static void setCurrent(Displayable displayable) {
		if (exception != null) {
			Alert alert =
				new Alert("Error", exception.getMessage(), null, AlertType.ERROR);
			alert.setTimeout(Alert.FOREVER);
			display.setCurrent(alert, displayable);
			exception = null;
		} else {
			display.setCurrent(displayable);
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param exception DOCUMENT ME!
	*/
	public static void showException(Exception exception) {
		HospitalMIDlet.exception = exception;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param unconditional DOCUMENT ME!
	*/
	protected void destroyApp(boolean unconditional) {
		try {
			PersistableManager.getInstance().shutdown();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	protected void pauseApp() {
		try {
			PersistableManager.getInstance().shutdown();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @throws MIDletStateChangeException DOCUMENT ME!
	*/
	protected void startApp() throws MIDletStateChangeException {
		MainForm mainForm = new MainForm();
		HospitalMIDlet.setCurrent(mainForm);
	}
}
