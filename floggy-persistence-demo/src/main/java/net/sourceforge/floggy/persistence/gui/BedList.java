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

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Bed;
import net.sourceforge.floggy.persistence.model.BedComparator;

public class BedList extends List implements CommandListener {

    protected ObjectSet beds;

    protected Command cmdCreate;

    protected Command cmdEdit;

    protected Command cmdDelete;

    protected Command cmdBack;

    public BedList() {
        super("Beds list", List.IMPLICIT);

        startData();
        startComponents();
    }

    private void startData() {
        PersistableManager pm = PersistableManager.getInstance();

        try {
            this.deleteAll();

            Class bedClass = Class.forName("net.sourceforge.floggy.persistence.model.Bed");
            beds = pm.find(bedClass, null, new BedComparator());

            for (int i = 0; i < beds.size(); i++) {
                Bed bed = (Bed) beds.get(i);
                this.append(bed.getNumber() + " - Floor " + bed.getFloor(), null);
            }

        } catch (Exception e) {
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
            Bed bed = new Bed();
            HospitalMIDlet.setCurrent(new BedForm(bed));
        } else if (cmd == this.cmdEdit) {
            if (this.getSelectedIndex() != -1) {
                Bed bed = null;

                try {
                    bed = (Bed) beds.get(this.getSelectedIndex());
                    HospitalMIDlet.setCurrent(new BedForm(bed));
                } catch (FloggyException e) {
                	HospitalMIDlet.showException(e);
                }
            }
        } else if (cmd == this.cmdDelete) {
            if (this.getSelectedIndex() != -1) {

                try {
                    Bed bed = (Bed) beds.get(this.getSelectedIndex());
                    PersistableManager.getInstance().delete(bed);
                    this.startData();
                } catch (FloggyException e) {
                	HospitalMIDlet.showException(e);
                }
            }
        }
    }
}