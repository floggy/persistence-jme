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
import javax.microedition.lcdui.List;

import net.sourceforge.floggy.persistence.Comparator;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.HospitalMIDlet;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.Persistable;
import net.sourceforge.floggy.persistence.PersistableManager;
import net.sourceforge.floggy.persistence.model.Formation;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FormationList extends List implements CommandListener, Comparator {
	/**
	 * DOCUMENT ME!
	 */
	protected Command cmdBack;

	/**
	 * DOCUMENT ME!
	 */
	protected Command cmdCreate;

	/**
	 * DOCUMENT ME!
	 */
	protected Command cmdDelete;

	/**
	 * DOCUMENT ME!
	 */
	protected Command cmdEdit;

	/**
	 * DOCUMENT ME!
	 */
	protected ObjectSet formations;

	/**
	 * Creates a new FormationList object.
	 */
	public FormationList() {
		super("Formation list", List.IMPLICIT);

		startData();
		startComponents();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param cmd DOCUMENT ME!
	* @param dsp DOCUMENT ME!
	*/
	public void commandAction(Command cmd, Displayable dsp) {
		if (cmd == this.cmdBack) {
			MainForm mainForm = new MainForm();
			HospitalMIDlet.setCurrent(mainForm);
		} else if (cmd == this.cmdCreate) {
			Formation formation = new Formation();
			HospitalMIDlet.setCurrent(new FormationForm(formation));
		} else if (cmd == this.cmdEdit) {
			if (this.getSelectedIndex() != -1) {
				Formation formation = null;

				try {
					formation = (Formation) formations.get(this.getSelectedIndex());
					HospitalMIDlet.setCurrent(new FormationForm(formation));
				} catch (FloggyException e) {
					HospitalMIDlet.showException(e);
				}
			}
		} else if (cmd == this.cmdDelete) {
			if (this.getSelectedIndex() != -1) {
				try {
					Formation formation =
						(Formation) formations.get(this.getSelectedIndex());
					PersistableManager.getInstance().delete(formation);
					this.startData();
				} catch (FloggyException e) {
					HospitalMIDlet.showException(e);
				}
			}
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param p1 DOCUMENT ME!
	* @param p2 DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int compare(Persistable p1, Persistable p2) {
		Formation f1 = (Formation) p1;
		Formation f2 = (Formation) p2;

		return f1.getFormation().compareTo(f2.getFormation());
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

	private void startData() {
		PersistableManager pm = PersistableManager.getInstance();

		try {
			this.deleteAll();

			formations = pm.find(Formation.class, null, this);

			for (int i = 0; i < formations.size(); i++) {
				Formation element = (Formation) formations.get(i);
				this.append(element.getFormation(), null);
			}
		} catch (FloggyException e) {
			HospitalMIDlet.showException(e);
		}
	}
}
