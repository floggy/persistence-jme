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
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;

import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Formation;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FormationForm extends Form implements CommandListener {
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
	protected Formation formation;

	/**
	 * DOCUMENT ME!
	 */
	protected TextField txtFormation;

	/**
	 * Creates a new FormationForm object.
	 *
	 * @param formation DOCUMENT ME!
	 */
	public FormationForm(Formation formation) {
		super("Formation");

		this.formation = formation;

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
				formation.setFormation(this.txtFormation.getString());
				pm.save(formation);
			} catch (FloggyException e) {
				HospitalMIDlet.showException(e);
			}
		}

		HospitalMIDlet.setCurrent(new FormationList());
	}

	private void startComponents() {
		this.txtFormation = new TextField("Formation", formation.getFormation(),
				30, TextField.ANY);
		this.append(this.txtFormation);

		this.cmdOk = new Command("Ok", Command.OK, 0);
		this.addCommand(this.cmdOk);

		this.cmdCancel = new Command("Cancel", Command.CANCEL, 1);
		this.addCommand(this.cmdCancel);

		this.setCommandListener(this);
	}
}
