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
import javax.microedition.lcdui.List;

import net.sourceforge.floggy.persistence.Comparator;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Doctor;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class DoctorList extends List implements CommandListener {
	/**
	 * DOCUMENT ME!
	 */
	protected Command cmdBack;

	/**
	 * DOCUMENT ME!
	 */
	protected Command cmdCreate;

	/**
	 * DOCUMENT ME!
	 */
	protected Command cmdDelete;

	/**
	 * DOCUMENT ME!
	 */
	protected Command cmdEdit;

	/**
	 * DOCUMENT ME!
	 */
	protected ObjectSet doctors;

	/**
	 * Creates a new DoctorList object.
	 */
	public DoctorList() {
		super("Doctors list", List.IMPLICIT);

		startData();
		startComponents();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param cmd DOCUMENT ME!
	* @param dsp DOCUMENT ME!
	*/
	public void commandAction(Command cmd, Displayable dsp) {
		if (cmd == this.cmdBack) {
			MainForm mainForm = new MainForm();
			HospitalMIDlet.setCurrent(mainForm);
		} else if (cmd == this.cmdCreate) {
			Doctor doctor = new Doctor();
			HospitalMIDlet.setCurrent(new DoctorForm(doctor));
		} else if (cmd == this.cmdEdit) {
			if (this.getSelectedIndex() != -1) {
				Doctor doctor = null;

				try {
					doctor = (Doctor) doctors.get(this.getSelectedIndex());
					HospitalMIDlet.setCurrent(new DoctorForm(doctor));
				} catch (FloggyException e) {
					HospitalMIDlet.showException(e);
				}
			}
		} else if (cmd == this.cmdDelete) {
			if (this.getSelectedIndex() != -1) {
				try {
					Doctor doctor = (Doctor) doctors.get(this.getSelectedIndex());
					PersistableManager.getInstance().delete(doctor);
					this.startData();
				} catch (FloggyException e) {
					HospitalMIDlet.showException(e);
				}
			}
		}
	}

	private void startComponents() {
		this.cmdBack = new Command("Back", Command.BACK, 0);
		this.addCommand(this.cmdBack);

		this.cmdCreate = new Command("Create", Command.ITEM, 1);
		this.addCommand(this.cmdCreate);

		this.cmdEdit = new Command("Edit", Command.ITEM, 2);
		this.addCommand(this.cmdEdit);

		this.cmdDelete = new Command("Delete", Command.ITEM, 3);
		this.addCommand(this.cmdDelete);

		this.setCommandListener(this);
	}

	private void startData() {
		PersistableManager pm = PersistableManager.getInstance();

		try {
			this.deleteAll();

			Class doctorClass =
				Class.forName("net.sourceforge.floggy.persistence.model.Doctor");
			doctors =
				pm.find(doctorClass, null,
					new Comparator() {
						public int compare(Persistable arg0, Persistable arg1) {
							String s1 = (arg0 == null) ? "" : ((Doctor) arg0).getName();
							String s2 = (arg1 == null) ? "" : ((Doctor) arg1).getName();

							return s1.compareTo(s2);
						}
					});

			for (int i = 0; i < doctors.size(); i++) {
				Doctor doctor = (Doctor) doctors.get(i);
				this.append(doctor.getName() + " - CRM " + doctor.getCrm(), null);
			}
		} catch (Exception e) {
			HospitalMIDlet.showException(e);
		}
	}
}
