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

package net.sourceforge.floggy.barbecuecalculator.persistence;

import java.util.Hashtable;

import net.sourceforge.floggy.barbecuecalculator.core.Util;
import net.sourceforge.floggy.persistence.Persistable;

public class Barbecue implements Persistable {
	
	public static final int ALCHOOL_LEVEL_SUSSA = 1;
	public static final int ALCHOOL_LEVEL_RELAX = 2;
	public static final int ALCHOOL_LEVEL_NORMAL = 3;
	public static final int ALCHOOL_LEVEL_BUDDIES = 4;
	public static final int ALCHOOL_LEVEL_UNIVERSITY = 5;
	
	public static final int FOOD_QUANTITY_LUNCH = 1;
	public static final int FOOD_QUANTITY_LIGHT = 2;
	public static final int FOOD_QUANTITY_NORMAL = 3;
	public static final int FOOD_QUANTITY_BIG_FAMILY = 4;
	public static final int FOOD_QUANTITY_LONG_DURATION = 5;
	
	private static Hashtable DRINK_RATE = new Hashtable();
	private static Hashtable FOOD_RATE = new Hashtable();

	static {
		DRINK_RATE.put(new Integer(Barbecue.ALCHOOL_LEVEL_SUSSA), new Integer(3));
		DRINK_RATE.put(new Integer(Barbecue.ALCHOOL_LEVEL_RELAX), new Integer(4));
		DRINK_RATE.put(new Integer(Barbecue.ALCHOOL_LEVEL_NORMAL), new Integer(6));
		DRINK_RATE.put(new Integer(Barbecue.ALCHOOL_LEVEL_BUDDIES), new Integer(9));
		DRINK_RATE.put(new Integer(Barbecue.ALCHOOL_LEVEL_UNIVERSITY), new Integer(12));
		
		FOOD_RATE.put(new Integer(Barbecue.FOOD_QUANTITY_LUNCH), new Double(0.15));
		FOOD_RATE.put(new Integer(Barbecue.FOOD_QUANTITY_LIGHT), new Double(0.22));
		FOOD_RATE.put(new Integer(Barbecue.FOOD_QUANTITY_NORMAL), new Double(0.3));
		FOOD_RATE.put(new Integer(Barbecue.FOOD_QUANTITY_BIG_FAMILY), new Double(0.45));
		FOOD_RATE.put(new Integer(Barbecue.FOOD_QUANTITY_LONG_DURATION), new Double(0.6));
	}
	
	private int alchool;
	private int alchoolLevel;
	private int barbecueKnife;
	private int beer;
	private int bread;
	private int bulkedSalt;
	private int cachaca;
	private int coal;
	private String description;
	private int farofa;
	private int foodQuantity;
	private int fork;
	private int limon;
	private double meat;
	private int men;
	private int napkin;
	private int panoDePrato;
	private int plasticCup;
	private int plate;
	private double salsague;
	private int soda;
	private int sugar;
	private boolean withCaipirinha;
	private int women;
	
	public Barbecue() {
	}

	public Barbecue(int men, int women, int alchoolLevel, int foodQuantity, boolean withCaipirinha) {
		this.men = men;
		this.women = women;
		this.alchoolLevel = alchoolLevel;
		this.foodQuantity = foodQuantity;
		this.withCaipirinha = withCaipirinha;
	}

	public void calculateBarbecue() {
		double foodRate = ((Double)Barbecue.FOOD_RATE.get(new Integer(getFoodQuantity()))).doubleValue();
		int drinkRate = ((Integer)Barbecue.DRINK_RATE.get(new Integer(getAlchoolLevel()))).intValue();
		
		this.setSalsague(this.men*(foodRate-0.08)+this.women*(foodRate-0.1));
		this.setMeat(this.men*foodRate+this.women*(foodRate-0.07));
		this.setBeer(this.men*drinkRate+this.women*(drinkRate-2));
		this.setSoda(1+Util.round(((this.men*0.3+this.women*0.5)/2)));
		this.setBread((int)(this.men*2+this.women*1.5));
		this.setFarofa(Util.round(1+(this.men*0.03+this.women*0.01)));
		this.setCoal(Util.round(1+getMeat()/7));
		this.setNapkin(Util.round(1+(this.men+this.women)/14));
		this.setAlchool(1);
		this.setPanoDePrato(1+Util.round((this.men+this.women)/10));
		this.setBarbecueKnife(2+Util.round((this.men+this.women)/10));
		this.setPlate(3+Util.round(this.men/10+this.women/10));
		this.setFork(2+Util.round((this.men+this.women)/10));
		this.setPlasticCup(Util.round((((this.men*1.2+this.women*1.5))+1)/20));
		this.setBulkedSalt(Util.round(getMeat()/30));
		this.setLimon(3+Util.round(this.men/2+this.women/2));
		this.setCachaca(Util.round((this.men+this.women)/20));
		this.setSugar(Util.round((((this.men+this.women)/20+1)/5)));
	}

	public int getAlchool() {
		return alchool;
	}

	public int getAlchoolLevel() {
		return alchoolLevel;
	}

	public int getBarbecueKnife() {
		return barbecueKnife;
	}

	public int getBeer() {
		return beer;
	}

	public int getBread() {
		return bread;
	}

	public int getBulkedSalt() {
		return bulkedSalt;
	}

	public int getCachaca() {
		return cachaca;
	}

	public int getCoal() {
		return coal;
	}

	public String getDescription() {
		return description;
	}

	public int getFarofa() {
		return farofa;
	}

	public int getFoodQuantity() {
		return foodQuantity;
	}

	public int getFork() {
		return fork;
	}

	public int getLimon() {
		return limon;
	}

	public double getMeat() {
		return meat;
	}

	public int getMen() {
		return men;
	}

	public int getNapkin() {
		return napkin;
	}

	public int getPanoDePrato() {
		return panoDePrato;
	}

	public int getPlasticCup() {
		return plasticCup;
	}

	public int getPlate() {
		return plate;
	}

	public double getSalsague() {
		return salsague;
	}

	public int getSoda() {
		return soda;
	}

	public int getSugar() {
		return sugar;
	}

	public int getWomen() {
		return women;
	}

	public boolean isWithCaipirinha() {
		return withCaipirinha;
	}

	public void setAlchool(int alchool) {
		this.alchool = alchool;
	}

	public void setAlchoolLevel(int alchoolLevel) {
		this.alchoolLevel = alchoolLevel;
	}

	public void setBarbecueKnife(int barbecueKnife) {
		this.barbecueKnife = barbecueKnife;
	}

	public void setBeer(int beer) {
		this.beer = beer;
	}

	public void setBread(int bread) {
		this.bread = bread;
	}

	public void setBulkedSalt(int bulkedSalt) {
		this.bulkedSalt = bulkedSalt;
	}

	public void setCachaca(int cachaca) {
		this.cachaca = cachaca;
	}

	public void setCoal(int coal) {
		this.coal = coal;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setFarofa(int farofa) {
		this.farofa = farofa;
	}

	public void setFoodQuantity(int foodQuantity) {
		this.foodQuantity = foodQuantity;
	}

	public void setFork(int fork) {
		this.fork = fork;
	}

	public void setLimon(int limon) {
		this.limon = limon;
	}

	public void setMeat(double meat) {
		this.meat = meat;
	}

	public void setMen(int men) {
		this.men = men;
	}

	public void setNapkin(int napkin) {
		this.napkin = napkin;
	}

	public void setPanoDePrato(int panoDePrato) {
		this.panoDePrato = panoDePrato;
	}

	public void setPlasticCup(int plasticCup) {
		this.plasticCup = plasticCup;
	}

	public void setPlate(int plate) {
		this.plate = plate;
	}

	public void setSalsague(double salsague) {
		this.salsague = salsague;
	}

	public void setSoda(int soda) {
		this.soda = soda;
	}

	public void setSugar(int sugar) {
		this.sugar = sugar;
	}

	public void setWithCaipirinha(boolean withCaipirinha) {
		this.withCaipirinha = withCaipirinha;
	}
	
	public void setWomen(int women) {
		this.women = women;
	}

}