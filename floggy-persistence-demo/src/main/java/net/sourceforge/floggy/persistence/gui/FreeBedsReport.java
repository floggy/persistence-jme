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

import net.sourceforge.floggy.persistence.Filter;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Bed;
import net.sourceforge.floggy.persistence.model.BedComparator;
import net.sourceforge.floggy.persistence.model.Internment;

public class FreeBedsReport extends List implements CommandListener {

	protected Command cmdBack;

	public FreeBedsReport() {
		super("Free beds", List.IMPLICIT);

		startData();
		startComponents();
	}

	private void startData() {
		PersistableManager pm = PersistableManager.getInstance();

		try {
			this.deleteAll();

			final ObjectSet internments = pm.find(Internment.class,
					new Filter() {
						public boolean matches(Persistable arg0) {
							return ((Internment) arg0).getExitDate() == null;
						}
					}, null);

			ObjectSet freeBeds = pm.find(Bed.class, new Filter() {

				public boolean matches(Persistable arg0) {
					Bed bed = (Bed) arg0;

					for (int i = 0; i < internments.size(); i++) {

						try {
							if (((Internment) internments.get(i)).getBed()
									.getNumber()== bed.getNumber()) {
								return false;
							}
						} catch (FloggyException e) {
				        	HospitalMIDlet.showException(e);
						}
					}

					return true;
				}

			}, new BedComparator());

        	            for (int i = 0; i < freeBeds.size(); i++) {
        	                Bed bed = (Bed) freeBeds.get(i);
        			this.append(bed.getNumber() + " - "
        					+ bed.getFloor(), null);
			}

		} catch (FloggyException e) {
        	HospitalMIDlet.showException(e);
		}
	}

	private void startComponents() {
		this.cmdBack = new Command("Back", Command.BACK, 0);
		this.addCommand(this.cmdBack);

		this.setCommandListener(this);
	}

	public void commandAction(Command cmd, Displayable dsp) {
		if (cmd == this.cmdBack) {
			MainForm mainForm = new MainForm();
			HospitalMIDlet.setCurrent(mainForm);
		}
	}
}
