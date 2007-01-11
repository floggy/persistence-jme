/*
 * Criado em 13/09/2005.
 * 
 * Todos os direiros reservados aos autores.
 */
package net.sourceforge.floggy.persistence.model;

import net.sourceforge.floggy.persistence.Persistable;

public class Bed implements Persistable {

	private int floor;

	private int number;

	public Bed() {
	}

	public Bed(int floor, int number) {
		this.floor = floor;
		this.number = number;
	}

	public int getFloor() {
		return floor;
	}

	public void setFloor(int floor) {
		this.floor = floor;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}
