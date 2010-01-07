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
package net.sourceforge.floggy.barbecuecalculator.ui;

import java.io.IOException;

import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
public class InitialScreenCanvas extends Canvas {
	private Image barbecueImage;

	/**
	 * Creates a new InitialScreenCanvas object.
	 */
	public InitialScreenCanvas() {
		try {
			this.barbecueImage = Image.createImage("/barbecue.png");
		} catch (IOException e) {
		}
	}

	/**
	 * DOCUMENT ME!
	*
	* @param g DOCUMENT ME!
	*/
	protected void paint(Graphics g) {
		g.drawImage(this.barbecueImage, this.getWidth() / 2, this.getHeight() / 2,
			Graphics.VCENTER | Graphics.HCENTER);
	}
}
