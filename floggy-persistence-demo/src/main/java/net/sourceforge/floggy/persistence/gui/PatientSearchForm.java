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
package net.sourceforge.floggy.persistence.gui;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.IndexFilter;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Patient;

public class PatientSearchForm  extends Form implements CommandListener {

	protected TextField txtSearch;

	protected Command cmdOk;

	protected Command cmdCancel;
	
	public PatientSearchForm() {
		super("Search");
		startComponents();
	}

	private void startComponents() {
		this.txtSearch = new TextField("Name", null, 20, TextField.ANY);
		this.append(this.txtSearch);

		this.cmdOk = new Command("Ok", Command.OK, 0);
		this.addCommand(this.cmdOk);

		this.cmdCancel = new Command("Cancel", Command.CANCEL, 1);
		this.addCommand(this.cmdCancel);

		this.setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable arg1) {
		if (cmd == this.cmdOk) {
			PersistableManager pm = PersistableManager.getInstance();

			try {
				IndexFilter filter = new IndexFilter("byName", txtSearch.getString());

				Class patientClass = Class.forName("net.sourceforge.floggy.persistence.model.Patient");
				ObjectSet patients = pm.find(patientClass, filter, false);

				HospitalMIDlet.setCurrent(new PatientList(patients));
			} catch (Exception e) {
				HospitalMIDlet.showException(e);
			}
		}
		
	}
}
