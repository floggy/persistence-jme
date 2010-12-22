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

import java.util.Date;

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
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.SingleObjectSet;
import net.sourceforge.floggy.persistence.model.Bed;
import net.sourceforge.floggy.persistence.model.BedComparator;
import net.sourceforge.floggy.persistence.model.Doctor;
import net.sourceforge.floggy.persistence.model.Internment;
import net.sourceforge.floggy.persistence.model.Patient;

public class InternmentForm extends Form implements CommandListener {

    protected Internment internment;

    protected SingleObjectSet patients;

    protected SingleObjectSet doctors;

    protected SingleObjectSet beds;

    protected DateField dtEnter;

    protected TextField txtReason;

    protected ChoiceGroup cgPatients;

    protected ChoiceGroup cgDoctors;

    protected ChoiceGroup cgBeds;

    protected Command cmdOk;

    protected Command cmdCancel;

    public InternmentForm() {
        super("Internment");

        this.internment = new Internment();
        
        startComponents();

        startPatients();

        startDoctors();

        startBeds();

    }

    private void startComponents() {
        this.dtEnter = new DateField("Enter date", DateField.DATE);
        this.dtEnter.setDate(new Date());
        this.append(this.dtEnter);

        this.txtReason = new TextField("Reason", internment.getReason(), 100,
                TextField.ANY);
        this.append(this.txtReason);

        this.cgPatients = new ChoiceGroup("Patient", ChoiceGroup.EXCLUSIVE);
        this.append(cgPatients);

        this.cgDoctors = new ChoiceGroup("Doctor", ChoiceGroup.EXCLUSIVE);
        this.append(cgDoctors);

        this.cgBeds = new ChoiceGroup("Beds", ChoiceGroup.EXCLUSIVE);
        this.append(cgBeds);

        this.cmdOk = new Command("Ok", Command.OK, 0);
        this.addCommand(this.cmdOk);

        this.cmdCancel = new Command("Cancel", Command.CANCEL, 1);
        this.addCommand(this.cmdCancel);

        this.setCommandListener(this);
    }

    public void startPatients() {
        PersistableManager pm = PersistableManager.getInstance();
        try {
			Class patientClass = Class.forName("net.sourceforge.floggy.persistence.model.Patient");
            patients = pm.find(patientClass, null, new Comparator() {

                public int compare(Persistable arg0, Persistable arg1) {
                    Patient p1 = (Patient) arg0;
                    Patient p2 = (Patient) arg1;

                    return p1.getName().compareTo(p2.getName());
                }

            });
            Patient patient = new Patient();
            for (int i = 0; i < patients.size(); i++) {
            	patients.get(i, patient);
                this.cgPatients.append(patient.getName(), null);
            }

        } catch (Exception e) {
        	HospitalMIDlet.showException(e);
        }

    }

    public void startDoctors() {
        PersistableManager pm = PersistableManager.getInstance();
        try {
            Class doctorClass = Class.forName("net.sourceforge.floggy.persistence.model.Doctor");
            doctors = pm.find(doctorClass, null, new Comparator() {

                public int compare(Persistable arg0, Persistable arg1) {
                    Doctor p1 = (Doctor) arg0;
                    Doctor p2 = (Doctor) arg1;

                    return p1.getName().compareTo(p2.getName());
                }

            });

            Doctor doctor= new Doctor();
            for (int i = 0; i < doctors.size(); i++) {
                doctors.get(i, doctor);
                this.cgDoctors.append(doctor.getName(), null);
            }

        } catch (Exception e) {
        	HospitalMIDlet.showException(e);
        }

    }

    public void startBeds() {
        PersistableManager pm = PersistableManager.getInstance();
        try {
            Class bedClass = Class.forName("net.sourceforge.floggy.persistence.model.Bed");
            beds = pm.find(bedClass, null, new BedComparator());
            Bed bed= new Bed();
            for (int i = 0; i < beds.size(); i++) {
            	beds.get(i, bed);
                this.cgBeds.append(String.valueOf(bed.getNumber()), null);
            }
        } catch (Exception e) {
        	HospitalMIDlet.showException(e);
        }

    }

    public Patient getSelectedPatient() {
        for (int i = 0; i < this.cgPatients.size(); i++) {
            if (this.cgPatients.isSelected(i)) {
                try {
                    return (Patient) this.patients.get(i);
                } catch (Exception e) {
                    // 
                }

            }

        }
        return null;
    }
    
    public Doctor getSelectedDoctor() {
        for (int i = 0; i < this.cgDoctors.size(); i++) {
            if (this.cgDoctors.isSelected(i)) {
                try {
                    return (Doctor) this.doctors.get(i);
                } catch (Exception e) {
                    // 
                }

            }

        }
        return null;
    }


    public Bed getSelectedBed() {
        for (int i = 0; i < this.cgBeds.size(); i++) {
            if (this.cgBeds.isSelected(i)) {
                try {
                    return (Bed) this.beds.get(i);
                } catch (Exception e) {
                    // 
                }

            }

        }
        return null;
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == this.cmdOk) {
            PersistableManager pm = PersistableManager.getInstance();

            try {
                this.internment.setEnterDate(this.dtEnter.getDate());
                this.internment.setReason(this.txtReason.getString());
                this.internment.setPatient(getSelectedPatient());
                this.internment.setDoctor(getSelectedDoctor());
                this.internment.setBed(getSelectedBed());
                pm.save(this.internment);
            } catch (FloggyException e) {
            	HospitalMIDlet.showException(e);
            }
        }
        HospitalMIDlet.setCurrent(new MainForm());
    }

}
