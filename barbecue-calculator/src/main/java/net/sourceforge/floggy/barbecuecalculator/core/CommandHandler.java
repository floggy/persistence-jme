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

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.List;

public class CommandHandler implements CommandListener {

	private static CommandHandler instance;

	public static Command CALCULATE_CMD = new Command("Calculate",
			Command.SCREEN, 1);
	public static Command SAVED_CMD = new Command("Saved barbecues",
			Command.SCREEN, 2);
	public static Command SHOW_BARBECUE_CMD = new Command("Show barbecue",
			Command.OK, 1);
	public static Command DELETE_BARBECUE_CMD = new Command("Delete barbecue",
			Command.SCREEN, 2);
	public static Command ABOUT_CMD = new Command("About", Command.HELP, 4);
	public static Command SET_BARBECUE_NAME_CMD = new Command("Save barbecue",
			Command.SCREEN, 1);
	public static Command SAVE_BARBECUE_CMD = new Command("Save",
			Command.SCREEN, 1);
	public static Command BACK_CMD = new Command("Back", Command.BACK, 2);
	public static Command EXIT_CMD = new Command("Exit", Command.EXIT, 5);

	private CommandHandler() {
	}

	public static CommandHandler getInstance() {
		if (instance == null)
			instance = new CommandHandler();

		return instance;
	}

	public void commandAction(Command command, Displayable displayable) {
		if (command == CommandHandler.BACK_CMD) {
			try {
				DisplayManager.getInstance().backAndRemove();
			} catch (DisplayManagerException e1) {
			}
		} else if (command == CommandHandler.EXIT_CMD) {
			try {
				FlowManager.getInstance().endApplication();
			} catch (FlowManagerNotStartedException e) {
			}
		} else if (command.equals(CommandHandler.CALCULATE_CMD)) {
			if (displayable instanceof Canvas) {
				try {
					FlowManager.getInstance().exibirDadosChurrasForm();
				} catch (FlowManagerNotStartedException e) {
				}
			} else {
				try {
					FlowManager.getInstance().calculateBarbecue();
				} catch (FlowManagerNotStartedException e) {
				}
			}
		} else if (command == CommandHandler.SET_BARBECUE_NAME_CMD) {
			try {
				FlowManager.getInstance().salvarChurrasco();
			} catch (FlowManagerNotStartedException e) {
			}
		} else if (command == CommandHandler.SAVE_BARBECUE_CMD) {
			try {
				FlowManager.getInstance().finalizarPersistenciaChurras();
			} catch (FlowManagerNotStartedException e) {
			}
		} else if (command == CommandHandler.SAVED_CMD) {
			try {
				FlowManager.getInstance().exibirListaChurrasSalvos();
			} catch (FlowManagerNotStartedException e) {
			}
		} else if (command == CommandHandler.SHOW_BARBECUE_CMD) {
			try {
				FlowManager.getInstance().exibirChurrasSalvo(
						((List) displayable).getSelectedIndex());
			} catch (FlowManagerNotStartedException e) {
			}
		} else if (command == CommandHandler.DELETE_BARBECUE_CMD) {
			try {
				FlowManager.getInstance().apagarChurrasSalvo(
						((List) displayable).getSelectedIndex());
			} catch (FlowManagerNotStartedException e) {
			}
		} else if (command == CommandHandler.ABOUT_CMD) {
			try {
				FlowManager.getInstance().showAboutForm();
			} catch (FlowManagerNotStartedException e) {
			}
		}
	}
}