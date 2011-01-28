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
package net.sourceforge.floggy.barbecuecalculator.core;

import java.util.Stack;

import javax.microedition.lcdui.Alert;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.midlet.MIDlet;


public class DisplayManager {
	
	/**
	 * Back action for the DisplayManager
	 */
	public static int BACK = 1;
	
	/**
	 * Forward action for the DisplayManager
	 */
	public static int FORWARD = 2;
	
	private static DisplayManager instance;
	private Display display;
	private MIDlet midlet;
	
	private Stack toShow;
	private Stack back;
	
	private DisplayManager (MIDlet midlet) {
		this.midlet = midlet;
		this.display = Display.getDisplay(this.midlet);
		this.toShow = new Stack();
		this.back = new Stack();
	}
	
	public static DisplayManager startDisplayManager(MIDlet midlet) throws DisplayManagerAlreadyStartedException {
		if ( instance != null ) {
			throw new DisplayManagerAlreadyStartedException("DisplayManager already exists at: "+instance);
		}
		
		instance = new DisplayManager(midlet);		
		return instance;
	}
	
	public static DisplayManager getInstance() throws DisplayManagerNotStartedException {
		if  ( instance == null ) {
			throw new DisplayManagerNotStartedException("DisplayManager not started.");
		}
		
		return instance;
	}
	
	public void show() throws DisplayManagerException {
		if ( this.toShow.empty() )
			throw new DisplayManagerException("Not enough displayables at stack.");
		
		display.setCurrent((Displayable)this.toShow.peek());
	}
	
	public void show(Displayable toShowDisplayable, boolean toSave) {
		this.display.setCurrent(toShowDisplayable);
		
		if ( toSave )
			this.toShow.push(toShowDisplayable);
	}
	
	public void show(Alert alertToShow) throws DisplayManagerException {
		if ( this.toShow.empty() )
			throw new DisplayManagerException("Not enough displayables at stack.");
		
		this.display.setCurrent(alertToShow, (Displayable)this.toShow.peek());
	}
	
	public void show(Alert alertToShow, Displayable nextDisplayableToShow, boolean toSave) {
		this.display.setCurrent(alertToShow, nextDisplayableToShow);
		
		if ( toSave )
			this.toShow.push(nextDisplayableToShow);
	}
	
	public void show(int displayManagerAction, boolean save) throws IllegalArgumentException, DisplayManagerException {
		Displayable retrived = null;
		
		if ( displayManagerAction == DisplayManager.BACK ) {
			if ( this.toShow.empty() )
				throw new DisplayManagerException("Not enough displayables at stack.");
			
			retrived = (Displayable)this.toShow.pop();
			this.show();
			
			if ( save )
				this.back.push(retrived);
		} else if ( displayManagerAction == DisplayManager.FORWARD ){
			if ( this.back.empty() )
				throw new DisplayManagerException("Not enough displayables at stack.");
			
			retrived = (Displayable)this.back.pop();
			this.show(retrived, save);
		} else
			throw new IllegalArgumentException();
	}
	
	public Displayable getCurrent() {
		return this.display.getCurrent();
	}
	
	public void go(int displayManagerAction) throws IllegalArgumentException, DisplayManagerException {
		this.show(displayManagerAction, true);
	}
	
	public void go(int displayManagerAction, int deep) throws DisplayManagerException, IllegalArgumentException {
		if ( deep <= 0 )
			throw new IllegalArgumentException();
		
		if ( displayManagerAction == DisplayManager.BACK ) {
			if ( this.toShow.size() > deep ) {
				for ( int i = 0; i < deep; i++ )
					this.back.push(this.toShow.pop());
			} else {
				throw new DisplayManagerException("Not enough displayables at stack.");
			}
		} else if ( displayManagerAction == DisplayManager.FORWARD ) {
			if ( this.back.size() >= deep ) {
				for ( int i = 0; i < deep; i++)
					this.toShow.push(this.back.pop());
			} else {
				throw new DisplayManagerException("Not enough displayables at stack.");
			}
		} else
			throw new IllegalArgumentException();
		
		this.show();
	}
	
	public void backToTheFirst() throws DisplayManagerException {
		
		while ( this.toShow.size() > 1 )
			this.toShow.pop();		
	}
	
	public void backAndRemove() throws IllegalArgumentException, DisplayManagerException {
		this.show(DisplayManager.BACK, false);
	}
	
	public void clearBackStack() {
		this.back.removeAllElements();
	}
	
	public void clearToShowStack() {
		this.toShow.removeAllElements();
	}
	
	public void removeFromBackStack() throws DisplayManagerException {
		if ( this.back.empty() )
			throw new DisplayManagerException("Not enough displayables at stack.");
		
		this.back.pop();
	}
	
	public void toShowBackStack() {
		this.toShow.removeAllElements();
	}
	
	public void removeFromToShowStack() throws DisplayManagerException {
		if ( this.toShow.empty() )
			throw new DisplayManagerException("Not enough displayables at stack.");
		
		this.toShow.pop();
	}
	
	public void pushToShowStack(Displayable toPushDisplayable) {
		this.toShow.push(toPushDisplayable);
	}
}
