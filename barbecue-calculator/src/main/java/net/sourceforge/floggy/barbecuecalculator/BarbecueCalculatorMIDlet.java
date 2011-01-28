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
package net.sourceforge.floggy.barbecuecalculator;

import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import net.sourceforge.floggy.barbecuecalculator.core.CommandHandler;
import net.sourceforge.floggy.barbecuecalculator.core.DisplayManager;
import net.sourceforge.floggy.barbecuecalculator.core.FlowManager;
import net.sourceforge.floggy.barbecuecalculator.ui.InitialScreenCanvas;


public class BarbecueCalculatorMIDlet extends MIDlet {

	private InitialScreenCanvas initialScreen;

	public BarbecueCalculatorMIDlet() {

		try {
			DisplayManager.startDisplayManager(this);
			FlowManager.startFlowManager(this);

			this.initialScreen = new InitialScreenCanvas();
			this.initialScreen.addCommand(CommandHandler.CALCULATE_CMD);
			this.initialScreen.addCommand(CommandHandler.SAVED_CMD);
			this.initialScreen.addCommand(CommandHandler.ABOUT_CMD);
			this.initialScreen.addCommand(CommandHandler.EXIT_CMD);
			this.initialScreen.setCommandListener(CommandHandler.getInstance());

			DisplayManager.getInstance().pushToShowStack(this.initialScreen);
		} catch (Exception e) {
		}
	}

	protected void destroyApp(boolean unconditional)
			throws MIDletStateChangeException {
		notifyDestroyed();
	}

	protected void pauseApp() {
		notifyPaused();
	}

	protected void startApp() throws MIDletStateChangeException {
		try {
			DisplayManager.getInstance().show();
		} catch (Exception e) {
		}
	}
}