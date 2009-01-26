/**
 * Copyright (c) 2006-2009 Floggy Open Source Group. All rights reserved.
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

import net.sourceforge.floggy.persistence.HospitalMIDlet;

public class MainForm extends List implements CommandListener {

	protected Command cmdExit;

    public MainForm() {
        super("Hospital", List.IMPLICIT);

        startComponents();
    }

    protected void startComponents() {
        this.append("Formations", null);
        this.append("Beds", null);
        this.append("Doctors", null);
        this.append("Patients", null);
        this.append("-----", null);
        this.append("Add internment", null);
        this.append("Leave patient", null);        
        this.append("-----", null);
        this.append("Free beds report", null);
        
        this.cmdExit = new Command("Exit", Command.ITEM, 3);
        this.addCommand(this.cmdExit);

        this.setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if (cmd == this.cmdExit) {
            HospitalMIDlet.exit();   
        } else if (cmd == List.SELECT_COMMAND) {
            switch (this.getSelectedIndex()) {
            case 0:
                HospitalMIDlet.setCurrent(new FormationList());
                break;
            case 1:
                HospitalMIDlet.setCurrent(new BedList());
                break;
            case 2:
                HospitalMIDlet.setCurrent(new DoctorList());
                break;
            case 3:
        		HospitalMIDlet.setCurrent(new PatientList());
        		break;
        	case 5:
                HospitalMIDlet.setCurrent(new InternmentForm());
                break;
            case 6:
                HospitalMIDlet.setCurrent(new InternmentList());
                break;
            case 8:
                HospitalMIDlet.setCurrent(new FreeBedsReport());
                break;                
            }            
        }
    }
}
