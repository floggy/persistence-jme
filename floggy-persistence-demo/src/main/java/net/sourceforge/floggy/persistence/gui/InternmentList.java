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

import java.util.Date;

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

import net.sourceforge.floggy.persistence.Comparator;
import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Internment;

public class InternmentList extends List implements CommandListener {
	
    protected ObjectSet internments;

    protected Command cmdLeave;

    protected Command cmdBack;

    public InternmentList() {
        super("Internments", List.IMPLICIT);

        startData();
        startComponents();
    }

    private void startData() {
        PersistableManager pm = PersistableManager.getInstance();

        try {
            this.deleteAll();

            Class internmentClass = Class.forName("net.sourceforge.floggy.persistence.model.Internment");
            internments = pm.find(internmentClass, new Filter() {

                public boolean matches(Persistable arg0) {
                    return ((Internment) arg0).getExitDate() == null;
                }

            }, new Comparator() {
                public int compare(Persistable arg0, Persistable arg1) {
                    String s1 = ((Internment) arg0).getPatient().getName();
                    String s2 = ((Internment) arg1).getPatient().getName();

                    return s1.compareTo(s2);
                    
                }
            });

            for (int i = 0; i < internments.size(); i++) {
                Internment internment = (Internment) internments.get(i);
                this.append(internment.getPatient().getName() + " - "
                        + internment.getBed().getNumber(), null);
            }

        } catch (Exception e) {
        	HospitalMIDlet.showException(e);
        }
    }

    private void startComponents() {
        this.cmdBack = new Command("Back", Command.BACK, 0);
        this.addCommand(this.cmdBack);

        this.cmdLeave = new Command("Leave", Command.ITEM, 1);
        this.addCommand(this.cmdLeave);

        this.setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == this.cmdBack) {
            MainForm mainForm = new MainForm();
            HospitalMIDlet.setCurrent(mainForm);
        } else if (cmd == this.cmdLeave) {
            if (this.getSelectedIndex() != -1) {
                try {
                    Internment internment = (Internment) internments.get(this
                            .getSelectedIndex());
                    internment.setExitDate(new Date());
                    PersistableManager.getInstance().save(internment);
                    this.startData();
                } catch (FloggyException e) {
                	HospitalMIDlet.showException(e);
                }
            }
        }

    }
}