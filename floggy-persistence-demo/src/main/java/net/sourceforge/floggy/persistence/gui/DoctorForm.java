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
import net.sourceforge.floggy.persistence.model.Formation;
import net.sourceforge.floggy.persistence.model.Doctor;

public class DoctorForm extends Form implements CommandListener {

    protected Doctor doctor;

    protected ObjectSet formations;

    protected TextField txtName;

    protected TextField txtPassport;

    protected DateField dtBornDate;

    protected TextField txtCRM;

    protected ChoiceGroup cgFormation;

    protected Command cmdOk;

    protected Command cmdCancel;

    public DoctorForm(Doctor doctor) {
        super("Doctor");

        this.doctor = doctor;

        startComponents();

        startFormations();
    }

    private void startComponents() {
        this.txtName = new TextField("Name", doctor.getName(), 30,
                TextField.ANY);
        this.append(this.txtName);

        this.txtPassport = new TextField("Passport", doctor.getPassport(), 30, TextField.ANY);
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

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == this.cmdOk) {
            PersistableManager pm = PersistableManager.getInstance();

            try {
                this.doctor.setName(this.txtName.getString());
                this.doctor.setPassport(this.txtPassport.getString());
                this.doctor.setCrm(this.txtCRM.getString());
                this.doctor.setBornDate(this.dtBornDate.getDate());

                if(this.doctor.getFormations() != null) {
                    this.doctor.getFormations().removeAllElements();
                }
                else {
                    this.doctor.setFormations(new Vector());
                }
                for (int i = 0; i < this.cgFormation.size(); i++) {
                    if (this.cgFormation.isSelected(i) ) {
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

    public void startFormations() {
       
        PersistableManager pm = PersistableManager.getInstance();
        try {
            formations = pm.find(Formation.class, null, new Comparator() {

                public int compare(Persistable arg0, Persistable arg1) {
                    Formation f1 = (Formation) arg0;
                    Formation f2 = (Formation) arg1;

                    return f1.getFormation().compareTo(f2.getFormation());
                }

            });

            for (int i = 0; i < formations.size(); i++) {
                Formation formation = (Formation) formations.get(i);
                int index = this.cgFormation.append(formation.getFormation(), null);
                if ((doctor.getFormations() != null) && (this.doctor.getFormations().contains(formation))) {
                    this.cgFormation.setSelectedIndex(index, true);
                }

            }

        } catch (FloggyException e) {
        	HospitalMIDlet.showException(e);
        }

    }
}