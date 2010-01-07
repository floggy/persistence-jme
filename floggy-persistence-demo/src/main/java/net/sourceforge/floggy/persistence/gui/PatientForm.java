/**
 * Copyright (c) 2006-2010 Floggy Open Source Group. All rights reserved.
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

import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.DateField;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Patient;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class PatientForm extends Form implements CommandListener {
	/**
	 * DOCUMENT ME!
	 */
	protected ChoiceGroup cgInsuredByGoverment;

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
	protected ObjectSet formations;

	/**
	 * DOCUMENT ME!
	 */
	protected Patient patient;

	/**
	 * DOCUMENT ME!
	 */
	protected TextField txtAddress;

	/**
	 * DOCUMENT ME!
	 */
	protected TextField txtCellPhone;

	/**
	 * DOCUMENT ME!
	 */
	protected TextField txtHomePhone;

	/**
	 * DOCUMENT ME!
	 */
	protected TextField txtName;

	/**
	 * DOCUMENT ME!
	 */
	protected TextField txtPassport;

	/**
	 * DOCUMENT ME!
	 */
	protected TextField txtWorkPhone;

	/**
	 * Creates a new PatientForm object.
	 *
	 * @param paciente DOCUMENT ME!
	 */
	public PatientForm(Patient paciente) {
		super("Patient");

		this.patient = paciente;

		startComponents();
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
				this.patient.setName(this.txtName.getString());
				this.patient.setPassport(this.txtPassport.getString());
				this.patient.setBornDate(this.dtBornDate.getDate());
				this.patient.setInsuredByGoverment(this.cgInsuredByGoverment.isSelected(
						0));
				this.patient.setHomePhone(this.txtHomePhone.getString());
				this.patient.setCellPhone(this.txtCellPhone.getString());
				this.patient.setWorkPhone(this.txtWorkPhone.getString());
				pm.save(this.patient);
			} catch (FloggyException e) {
				HospitalMIDlet.showException(e);
			}
		}

		HospitalMIDlet.setCurrent(new PatientList());
	}

	private void startComponents() {
		this.txtName = new TextField("Name", patient.getName(), 30, TextField.ANY);
		this.append(this.txtName);

		this.txtPassport = new TextField("Passport", patient.getPassport(), 30,
				TextField.ANY);
		this.append(this.txtPassport);

		this.dtBornDate = new DateField("Born date", DateField.DATE);
		this.dtBornDate.setDate(patient.getBornDate());
		this.append(this.dtBornDate);

		this.txtAddress = new TextField("Address", patient.getAddress(), 100,
				TextField.ANY);
		this.append(txtAddress);

		this.append("Phones");
		this.txtHomePhone = new TextField("Home", patient.getHomePhone(), 20,
				TextField.PHONENUMBER);
		this.append(this.txtHomePhone);
		this.txtCellPhone = new TextField("Cell", patient.getCellPhone(), 20,
				TextField.PHONENUMBER);
		this.append(this.txtCellPhone);
		this.txtWorkPhone = new TextField("Work", patient.getCellPhone(), 20,
				TextField.PHONENUMBER);
		this.append(this.txtWorkPhone);

		this.cgInsuredByGoverment = new ChoiceGroup("Type:", ChoiceGroup.EXCLUSIVE);
		this.cgInsuredByGoverment.append("Private", null);
		this.cgInsuredByGoverment.append("Goverment", null);
		this.cgInsuredByGoverment.setSelectedIndex(0, patient.isInsuredByGoverment());
		this.cgInsuredByGoverment.setSelectedIndex(1,
			!patient.isInsuredByGoverment());
		this.append(cgInsuredByGoverment);

		this.cmdOk = new Command("Ok", Command.OK, 0);
		this.addCommand(this.cmdOk);

		this.cmdCancel = new Command("Cancel", Command.CANCEL, 1);
		this.addCommand(this.cmdCancel);

		this.setCommandListener(this);
	}
}
