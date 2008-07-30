/**
 *  Copyright 2006 Floggy Open Source Group
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
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
import net.sourceforge.floggy.persistence.model.Patient;

public class PatientList extends List implements CommandListener {

	protected ObjectSet patients;

	protected Command cmdCreate;

	protected Command cmdEdit;

	protected Command cmdDelete;

	protected Command cmdBack;

    public PatientList() {
        super("Patients list", List.IMPLICIT);

        startData();
        startComponents();
        
    }

    private void startData() {
        PersistableManager pm = PersistableManager.getInstance();

        try {
            this.deleteAll();
            
            patients = pm.find(Patient.class, null, new Comparator() {
                public int compare(Persistable arg0, Persistable arg1) {
                    String s1 = arg0 == null ? "" : ((Patient) arg0).getName();
                    String s2 = arg1 == null ? "" : ((Patient) arg1).getName();

                    return s1.compareTo(s2);
                }
            });

            for (int i = 0; i < patients.size(); i++) {
                Patient patient = (Patient) patients.get(i);
                String type;
                if (patient.isInsuredByGoverment()) {
                    type = "Goverment";
                } else {
                    type = "Private";
                }
                this.append(patient.getName() + " - " + type, null);
            }

        } catch (FloggyException e) {
        	HospitalMIDlet.showException(e);
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

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == this.cmdBack) {
            MainForm mainForm = new MainForm();
            HospitalMIDlet.setCurrent(mainForm);
        } else if (cmd == this.cmdCreate) {
            Patient patient = new Patient();
            HospitalMIDlet.setCurrent(new PatientForm(patient));
        } else if (cmd == this.cmdEdit) {
            if (this.getSelectedIndex() != -1) {
                Patient patient = null;

                try {
                    patient = (Patient) patients
                            .get(this.getSelectedIndex());
                    HospitalMIDlet.setCurrent(new PatientForm(patient));
                } catch (FloggyException e) {
                	HospitalMIDlet.showException(e);
                }
            }
        } else if (cmd == this.cmdDelete) {
            if (this.getSelectedIndex() != -1) {

                try {
                    Patient patient = (Patient) patients.get(this
                            .getSelectedIndex());
                    PersistableManager.getInstance().delete(patient);
                    this.startData();
                } catch (FloggyException e) {
                	HospitalMIDlet.showException(e);
                }
            }
        }
    }
}