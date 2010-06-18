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

import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Bed;

public class BedForm extends Form implements CommandListener {
    
    protected Bed bed;
    
    protected TextField txtBedNumber;
    
    protected TextField txtBedFloor;
    
    protected Command cmdOk;
    
    protected Command cmdCancel;
    
    public BedForm(Bed bed) {
        super("Bed");
        
        this.bed = bed;
        
        startComponents();
    }
    
    private void startComponents() {
        this.txtBedNumber = new TextField("Bed number", String.valueOf(bed.getNumber()), 3, TextField.NUMERIC);
        this.append(this.txtBedNumber);

        this.txtBedFloor  = new TextField("Bed floor", String.valueOf(bed.getFloor()), 15, TextField.NUMERIC);
        this.append(this.txtBedFloor);        
               
        this.cmdOk = new Command("Ok", Command.OK, 0);
        this.addCommand(this.cmdOk);
        
        this.cmdCancel = new Command("Cancel", Command.CANCEL, 1);
        this.addCommand(this.cmdCancel);
        
        this.setCommandListener(this);
    }

    public void commandAction(Command cmd, Displayable dsp) {
        if(cmd == this.cmdOk) {
            PersistableManager pm = PersistableManager.getInstance();
            
            try {
                bed.setNumber(Integer.parseInt(this.txtBedNumber.getString()));
                bed.setFloor(Integer.parseInt(this.txtBedFloor.getString()));
                pm.save(bed);         
            
            } catch (FloggyException e) {
            	HospitalMIDlet.showException(e);
            } 
        }
        
        HospitalMIDlet.setCurrent(new BedList());
    }
}
