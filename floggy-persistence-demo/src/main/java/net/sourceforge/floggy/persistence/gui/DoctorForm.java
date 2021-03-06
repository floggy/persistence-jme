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

import java.util.Vector;

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import net.sourceforge.floggy.persistence.Comparator;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Doctor;
import net.sourceforge.floggy.persistence.model.Formation;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class DoctorForm extends Form implements CommandListener {
	/**
	 * DOCUMENT ME!
	 */
	protected ChoiceGroup cgFormation;

	/**
	 * DOCUMENT ME!
	 */
	protected Command cmdCancel;

	/**
	 * DOCUMENT ME!
	 */
	protected Command cmdOk;

	/**
	 * DOCUMENT ME!
	 */
	protected DateField dtBornDate;

	/**
	 * DOCUMENT ME!
	 */
	protected Doctor doctor;

	/**
	 * DOCUMENT ME!
	 */
	protected ObjectSet formations;

	/**
	 * DOCUMENT ME!
	 */
	protected TextField txtCRM;

	/**
	 * DOCUMENT ME!
	 */
	protected TextField txtName;

	/**
	 * DOCUMENT ME!
	 */
	protected TextField txtPassport;

	/**
	 * Creates a new DoctorForm object.
	 *
	 * @param doctor DOCUMENT ME!
	 */
	public DoctorForm(Doctor doctor) {
		super("Doctor");

		this.doctor = doctor;

		startComponents();

		startFormations();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param cmd DOCUMENT ME!
	* @param dsp DOCUMENT ME!
	*/
	public void commandAction(Command cmd, Displayable dsp) {
		if (cmd == this.cmdOk) {
			PersistableManager pm = PersistableManager.getInstance();

			try {
				this.doctor.setName(this.txtName.getString());
				this.doctor.setPassport(this.txtPassport.getString());
				this.doctor.setCrm(this.txtCRM.getString());
				this.doctor.setBornDate(this.dtBornDate.getDate());

				if (this.doctor.getFormations() != null) {
					this.doctor.getFormations().removeAllElements();
				} else {
					this.doctor.setFormations(new Vector());
				}

				for (int i = 0; i < this.cgFormation.size(); i++) {
					if (this.cgFormation.isSelected(i)) {
						this.doctor.getFormations().addElement(this.formations.get(i));
					}
				}

				pm.save(this.doctor);
			} catch (FloggyException e) {
				HospitalMIDlet.showException(e);
			}
		}

		HospitalMIDlet.setCurrent(new DoctorList());
	}

	/**
	 * DOCUMENT ME!
	*/
	public void startFormations() {
		PersistableManager pm = PersistableManager.getInstance();

		try {
			Class formationClass =
				Class.forName("net.sourceforge.floggy.persistence.model.Formation");
			formations =
				pm.find(formationClass, null,
					new Comparator() {
						public int compare(Persistable arg0, Persistable arg1) {
							Formation f1 = (Formation) arg0;
							Formation f2 = (Formation) arg1;

							return f1.getFormation().compareTo(f2.getFormation());
						}
					});

			for (int i = 0; i < formations.size(); i++) {
				Formation formation = (Formation) formations.get(i);
				int index = this.cgFormation.append(formation.getFormation(), null);

				if ((doctor.getFormations() != null)
					 && (this.doctor.getFormations().contains(formation))) {
					this.cgFormation.setSelectedIndex(index, true);
				}
			}
		} catch (Exception e) {
			HospitalMIDlet.showException(e);
		}
	}

	private void startComponents() {
		this.txtName = new TextField("Name", doctor.getName(), 30, TextField.ANY);
		this.append(this.txtName);

		this.txtPassport = new TextField("Passport", doctor.getPassport(), 30,
				TextField.ANY);
		this.append(this.txtPassport);

		this.dtBornDate = new DateField("Born date", DateField.DATE);
		this.dtBornDate.setDate(doctor.getBornDate());
		this.append(this.dtBornDate);

		this.txtCRM = new TextField("CRM", doctor.getCrm(), 30, TextField.ANY);
		this.append(this.txtCRM);

		this.cgFormation = new ChoiceGroup("Formation", ChoiceGroup.MULTIPLE);
		this.append(cgFormation);

		this.cmdOk = new Command("Ok", Command.OK, 0);
		this.addCommand(this.cmdOk);

		this.cmdCancel = new Command("Cancel", Command.CANCEL, 1);
		this.addCommand(this.cmdCancel);

		this.setCommandListener(this);
	}
}
