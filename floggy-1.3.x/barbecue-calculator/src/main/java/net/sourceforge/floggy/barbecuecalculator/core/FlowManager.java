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
package net.sourceforge.floggy.barbecuecalculator.core;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.AlertType;
import javax.microedition.lcdui.Choice;
import javax.microedition.lcdui.ChoiceGroup;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.Gauge;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.List;
import javax.microedition.lcdui.StringItem;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.MIDlet;

import net.sourceforge.floggy.barbecuecalculator.persistence.Barbecue;
import net.sourceforge.floggy.persistence.FloggyException;
import net.sourceforge.floggy.persistence.ObjectSet;
import net.sourceforge.floggy.persistence.PersistableManager;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class FlowManager {
	private static FlowManager instance;
	private Barbecue barbecue;
	private MIDlet midlet;
	private Barbecue[] savedBarbecues;

	private FlowManager(MIDlet midlet) {
		this.midlet = midlet;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws FlowManagerNotStartedException DOCUMENT ME!
	*/
	public static FlowManager getInstance() throws FlowManagerNotStartedException {
		if (instance == null)
			throw new FlowManagerNotStartedException();

		return instance;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param midlet DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*
	* @throws FlowManagerAlreadyStartedException DOCUMENT ME!
	*/
	public static FlowManager startFlowManager(MIDlet midlet)
		throws FlowManagerAlreadyStartedException {
		if (instance != null)
			throw new FlowManagerAlreadyStartedException(
				"FlowManager already exists at " + instance);

		instance = new FlowManager(midlet);

		return instance;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param selectedIndex DOCUMENT ME!
	*/
	public void apagarChurrasSalvo(int selectedIndex) {
		try {
			PersistableManager.getInstance().delete(savedBarbecues[selectedIndex]);
			DisplayManager.getInstance().removeFromToShowStack();
			this.exibirListaChurrasSalvos();
		} catch (FloggyException e) {
		} catch (DisplayManagerException e) {
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void calculateBarbecue() {
		Form barbecueForm = null;

		try {
			barbecueForm = (Form) DisplayManager.getInstance().getCurrent();
		} catch (DisplayManagerNotStartedException e) {
		}

		int men = 0;
		int women = 0;

		try {
			String value = ((TextField) barbecueForm.get(0)).getString();
			men = Integer.parseInt(value);

			value = ((TextField) barbecueForm.get(1)).getString();
			women = Integer.parseInt(value);
		} catch (NumberFormatException nfe) {
		}

		boolean withCaipirinha = ((ChoiceGroup) barbecueForm.get(2)).isSelected(0);
		int alchoolLevel = ((Gauge) barbecueForm.get(3)).getValue();
		int foodQuantity = ((Gauge) barbecueForm.get(4)).getValue();

		if ((men == 0) && (women == 0)) {
			try {
				DisplayManager.getInstance()
				 .show(new Alert("Warn!", "# of men and woman required!", null,
						 AlertType.ERROR));
			} catch (DisplayManagerException e) {
			}
		} else if ((alchoolLevel <= 0) || (foodQuantity <= 0)) {
			try {
				DisplayManager.getInstance()
				 .show(new Alert("Warn!",
						 "Every barbecue has to have drink and food!!!", null,
						 AlertType.ERROR));
			} catch (DisplayManagerException e) {
			}
		} else {
			barbecue = new Barbecue(men, women, alchoolLevel, foodQuantity,
					withCaipirinha);
			barbecue.calculateBarbecue();
			showBarbecue();
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void endApplication() {
		this.midlet.notifyDestroyed();
	}

	/**
	 * DOCUMENT ME!
	*
	* @param selectedIndex DOCUMENT ME!
	*/
	public void exibirChurrasSalvo(int selectedIndex) {
		this.barbecue = this.savedBarbecues[selectedIndex];
		this.showBarbecue();
	}

	/**
	 * DOCUMENT ME!
	*/
	public void exibirDadosChurrasForm() {
		Form barbecueForm = new Form("Churras Calculator");
		barbecueForm.append(new TextField("# men", "", 3, TextField.NUMERIC));
		barbecueForm.append(new TextField("# woman", "", 3, TextField.NUMERIC));
		barbecueForm.append(new ChoiceGroup("", Choice.MULTIPLE,
				new String[] { " With Caipirinha" }, new Image[] { null }));
		barbecueForm.append(new Gauge("Alchool level", true, 5, 3));
		barbecueForm.append(new Gauge("Food level", true, 5, 3));
		barbecueForm.addCommand(CommandHandler.CALCULATE_CMD);
		barbecueForm.addCommand(CommandHandler.BACK_CMD);
		barbecueForm.setCommandListener(CommandHandler.getInstance());

		try {
			DisplayManager.getInstance().show(barbecueForm, true);
		} catch (DisplayManagerNotStartedException e) {
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void exibirListaChurrasSalvos() {
		List churrasSalvosList =
			new List("Saved barbecues", List.EXCLUSIVE | List.IMPLICIT);
		churrasSalvosList.setFitPolicy(Choice.TEXT_WRAP_OFF);

		try {
			Barbecue temp = new Barbecue();
			ObjectSet os =
				PersistableManager.getInstance().find(temp.getClass(), null, null);
			int size = os.size();
			savedBarbecues = new Barbecue[size];

			for (int i = 0; i < size; i++) {
				savedBarbecues[i] = (Barbecue) os.get(i);
			}
		} catch (FloggyException e) {
			e.printStackTrace();
		}

		if (savedBarbecues.length == 0) {
			try {
				DisplayManager.getInstance()
				 .show(new Alert("Warn", "There isn't barbecues saved.", null,
						 AlertType.INFO));
			} catch (DisplayManagerException e) {
			}
		} else {
			for (int i = 0; i < savedBarbecues.length; i++)
				churrasSalvosList.append(savedBarbecues[i].getDescription(), null);

			churrasSalvosList.addCommand(CommandHandler.SHOW_BARBECUE_CMD);
			churrasSalvosList.addCommand(CommandHandler.DELETE_BARBECUE_CMD);
			churrasSalvosList.addCommand(CommandHandler.BACK_CMD);
			churrasSalvosList.setCommandListener(CommandHandler.getInstance());

			try {
				DisplayManager.getInstance().show(churrasSalvosList, true);
			} catch (DisplayManagerNotStartedException e) {
			}
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void finalizarPersistenciaChurras() {
		Form dadosChurras = null;

		try {
			dadosChurras = (Form) DisplayManager.getInstance().getCurrent();
		} catch (DisplayManagerNotStartedException e) {
		}

		barbecue.setDescription(((TextField) dadosChurras.get(0)).getString());

		try {
			PersistableManager.getInstance().save(barbecue);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			DisplayManager.getInstance().backToTheFirst();
			DisplayManager.getInstance()
			 .show(new Alert("Success", "Barbecue saved!", null,
					 AlertType.CONFIRMATION));
		} catch (DisplayManagerException e) {
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void salvarChurrasco() {
		Form salvarChurrasForm = new Form("Save barbecue");
		salvarChurrasForm.append(new TextField(
				"Give a name to the barbecue to save: ", "", 30, TextField.ANY));
		salvarChurrasForm.addCommand(CommandHandler.SAVE_BARBECUE_CMD);
		salvarChurrasForm.addCommand(CommandHandler.BACK_CMD);
		salvarChurrasForm.setCommandListener(CommandHandler.getInstance());

		try {
			DisplayManager.getInstance().show(salvarChurrasForm, true);
		} catch (DisplayManagerNotStartedException e) {
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void showAboutForm() {
		Form aboutForm = new Form("About");
		aboutForm.append(new StringItem("Barbecue Calculator Floggynized",
				"Calculator of itens to do a barbecue."));
		aboutForm.append(new StringItem("Author: ", "Neto Marin"));
		aboutForm.append(new StringItem("Contatc: ", "netomarin@gmail.com"));
		aboutForm.append(new StringItem("Floggynizer: ", "Thiago Moreira"));
		aboutForm.append(new StringItem("Contatc: ", "thiago.moreira@floggy.org"));

		aboutForm.addCommand(CommandHandler.BACK_CMD);
		aboutForm.setCommandListener(CommandHandler.getInstance());

		try {
			DisplayManager.getInstance().show(aboutForm, true);
		} catch (DisplayManagerNotStartedException e) {
		}
	}

	/**
	 * DOCUMENT ME!
	*/
	public void showBarbecue() {
		Form listaChurras = new Form("Show barbecue");
		listaChurras.append("\nMens: " + barbecue.getMen());
		listaChurras.append("\nWomen: " + barbecue.getWomen());
		listaChurras.append("\nSalsague: " + barbecue.getSalsague() + " kg");
		listaChurras.append("\nMeat: " + barbecue.getMeat() + " kg");
		listaChurras.append("\nBeer: " + barbecue.getBeer() + " cans");
		listaChurras.append("\nSoda: " + barbecue.getSoda() + " 2 litters bottle");
		listaChurras.append("\nBread: " + barbecue.getBread() + " un");
		listaChurras.append("\nFarofa: " + barbecue.getFarofa() + " un");
		listaChurras.append("\nCoal: " + barbecue.getCoal() + " bags");
		listaChurras.append("\nNapkins: " + barbecue.getNapkin() + " emb. c/50");
		listaChurras.append("\nAlchool: " + barbecue.getAlchool() + " L");
		listaChurras.append("\nPano de Prato: " + barbecue.getPanoDePrato() + " un");
		listaChurras.append("\nBarbecue knife: " + barbecue.getBarbecueKnife()
			+ " un");
		listaChurras.append("\nPlates: " + barbecue.getPlate() + " un");
		listaChurras.append("\nForks: " + barbecue.getFork() + " un");
		listaChurras.append("\nPlastic cups: " + barbecue.getPlasticCup()
			+ " emb. c/20");
		listaChurras.append("\nSalt: " + barbecue.getBulkedSalt() + " kg");

		if (barbecue.isWithCaipirinha()) {
			listaChurras.append("\nLimon: " + barbecue.getLimon() + " un");
			listaChurras.append("\nCachaça: " + barbecue.getCachaca() + " L");
			listaChurras.append("\nSugar: " + barbecue.getSugar() + " kg");
		}

		listaChurras.addCommand(CommandHandler.SET_BARBECUE_NAME_CMD);
		listaChurras.addCommand(CommandHandler.BACK_CMD);
		listaChurras.addCommand(CommandHandler.EXIT_CMD);
		listaChurras.setCommandListener(CommandHandler.getInstance());

		try {
			DisplayManager.getInstance().show(listaChurras, true);
		} catch (DisplayManagerNotStartedException e) {
		}
	}
}
