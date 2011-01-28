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
package net.sourceforge.floggy.barbecuecalculator.persistence;

import java.util.Hashtable;

import net.sourceforge.floggy.barbecuecalculator.core.Util;
import net.sourceforge.floggy.persistence.Persistable;

/**
 * DOCUMENT ME!
 *
 * @author <a href="mailto:thiago.moreira@floggy.org">Thiago Moreira</a>
 * @version $Revision$
  */
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
		DRINK_RATE.put(new Integer(Barbecue.ALCHOOL_LEVEL_UNIVERSITY),
			new Integer(12));

		FOOD_RATE.put(new Integer(Barbecue.FOOD_QUANTITY_LUNCH), new Double(0.15));
		FOOD_RATE.put(new Integer(Barbecue.FOOD_QUANTITY_LIGHT), new Double(0.22));
		FOOD_RATE.put(new Integer(Barbecue.FOOD_QUANTITY_NORMAL), new Double(0.3));
		FOOD_RATE.put(new Integer(Barbecue.FOOD_QUANTITY_BIG_FAMILY),
			new Double(0.45));
		FOOD_RATE.put(new Integer(Barbecue.FOOD_QUANTITY_LONG_DURATION),
			new Double(0.6));
	}

	private String description;
	private boolean withCaipirinha;
	private double meat;
	private double salsague;
	private int alchool;
	private int alchoolLevel;
	private int barbecueKnife;
	private int beer;
	private int bread;
	private int bulkedSalt;
	private int cachaca;
	private int coal;
	private int farofa;
	private int foodQuantity;
	private int fork;
	private int limon;
	private int men;
	private int napkin;
	private int panoDePrato;
	private int plasticCup;
	private int plate;
	private int soda;
	private int sugar;
	private int women;

	/**
	 * Creates a new Barbecue object.
	 */
	public Barbecue() {
	}

	/**
	 * Creates a new Barbecue object.
	 *
	 * @param men DOCUMENT ME!
	 * @param women DOCUMENT ME!
	 * @param alchoolLevel DOCUMENT ME!
	 * @param foodQuantity DOCUMENT ME!
	 * @param withCaipirinha DOCUMENT ME!
	 */
	public Barbecue(int men, int women, int alchoolLevel, int foodQuantity,
		boolean withCaipirinha) {
		this.men = men;
		this.women = women;
		this.alchoolLevel = alchoolLevel;
		this.foodQuantity = foodQuantity;
		this.withCaipirinha = withCaipirinha;
	}

	/**
	 * DOCUMENT ME!
	*/
	public void calculateBarbecue() {
		double foodRate =
			((Double) Barbecue.FOOD_RATE.get(new Integer(getFoodQuantity())))
			 .doubleValue();
		int drinkRate =
			((Integer) Barbecue.DRINK_RATE.get(new Integer(getAlchoolLevel())))
			 .intValue();

		this.setSalsague((this.men * (foodRate - 0.08))
			+ (this.women * (foodRate - 0.1)));
		this.setMeat((this.men * foodRate) + (this.women * (foodRate - 0.07)));
		this.setBeer((this.men * drinkRate) + (this.women * (drinkRate - 2)));
		this.setSoda(1 + Util.round((((this.men * 0.3) + (this.women * 0.5)) / 2)));
		this.setBread((int) ((this.men * 2) + (this.women * 1.5)));
		this.setFarofa(Util.round(1 + ((this.men * 0.03) + (this.women * 0.01))));
		this.setCoal(Util.round(1 + (getMeat() / 7)));
		this.setNapkin(Util.round(1 + ((this.men + this.women) / 14)));
		this.setAlchool(1);
		this.setPanoDePrato(1 + Util.round((this.men + this.women) / 10));
		this.setBarbecueKnife(2 + Util.round((this.men + this.women) / 10));
		this.setPlate(3 + Util.round((this.men / 10) + (this.women / 10)));
		this.setFork(2 + Util.round((this.men + this.women) / 10));
		this.setPlasticCup(Util.round(
				(((this.men * 1.2) + (this.women * 1.5)) + 1) / 20));
		this.setBulkedSalt(Util.round(getMeat() / 30));
		this.setLimon(3 + Util.round((this.men / 2) + (this.women / 2)));
		this.setCachaca(Util.round((this.men + this.women) / 20));
		this.setSugar(Util.round(((((this.men + this.women) / 20) + 1) / 5)));
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getAlchool() {
		return alchool;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getAlchoolLevel() {
		return alchoolLevel;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getBarbecueKnife() {
		return barbecueKnife;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getBeer() {
		return beer;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getBread() {
		return bread;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getBulkedSalt() {
		return bulkedSalt;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getCachaca() {
		return cachaca;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getCoal() {
		return coal;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public String getDescription() {
		return description;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getFarofa() {
		return farofa;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getFoodQuantity() {
		return foodQuantity;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getFork() {
		return fork;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getLimon() {
		return limon;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public double getMeat() {
		return meat;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getMen() {
		return men;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getNapkin() {
		return napkin;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getPanoDePrato() {
		return panoDePrato;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getPlasticCup() {
		return plasticCup;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getPlate() {
		return plate;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public double getSalsague() {
		return salsague;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getSoda() {
		return soda;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getSugar() {
		return sugar;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public int getWomen() {
		return women;
	}

	/**
	 * DOCUMENT ME!
	*
	* @return DOCUMENT ME!
	*/
	public boolean isWithCaipirinha() {
		return withCaipirinha;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param alchool DOCUMENT ME!
	*/
	public void setAlchool(int alchool) {
		this.alchool = alchool;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param alchoolLevel DOCUMENT ME!
	*/
	public void setAlchoolLevel(int alchoolLevel) {
		this.alchoolLevel = alchoolLevel;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param barbecueKnife DOCUMENT ME!
	*/
	public void setBarbecueKnife(int barbecueKnife) {
		this.barbecueKnife = barbecueKnife;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param beer DOCUMENT ME!
	*/
	public void setBeer(int beer) {
		this.beer = beer;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param bread DOCUMENT ME!
	*/
	public void setBread(int bread) {
		this.bread = bread;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param bulkedSalt DOCUMENT ME!
	*/
	public void setBulkedSalt(int bulkedSalt) {
		this.bulkedSalt = bulkedSalt;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param cachaca DOCUMENT ME!
	*/
	public void setCachaca(int cachaca) {
		this.cachaca = cachaca;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param coal DOCUMENT ME!
	*/
	public void setCoal(int coal) {
		this.coal = coal;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param description DOCUMENT ME!
	*/
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param farofa DOCUMENT ME!
	*/
	public void setFarofa(int farofa) {
		this.farofa = farofa;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param foodQuantity DOCUMENT ME!
	*/
	public void setFoodQuantity(int foodQuantity) {
		this.foodQuantity = foodQuantity;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param fork DOCUMENT ME!
	*/
	public void setFork(int fork) {
		this.fork = fork;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param limon DOCUMENT ME!
	*/
	public void setLimon(int limon) {
		this.limon = limon;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param meat DOCUMENT ME!
	*/
	public void setMeat(double meat) {
		this.meat = meat;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param men DOCUMENT ME!
	*/
	public void setMen(int men) {
		this.men = men;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param napkin DOCUMENT ME!
	*/
	public void setNapkin(int napkin) {
		this.napkin = napkin;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param panoDePrato DOCUMENT ME!
	*/
	public void setPanoDePrato(int panoDePrato) {
		this.panoDePrato = panoDePrato;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param plasticCup DOCUMENT ME!
	*/
	public void setPlasticCup(int plasticCup) {
		this.plasticCup = plasticCup;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param plate DOCUMENT ME!
	*/
	public void setPlate(int plate) {
		this.plate = plate;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param salsague DOCUMENT ME!
	*/
	public void setSalsague(double salsague) {
		this.salsague = salsague;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param soda DOCUMENT ME!
	*/
	public void setSoda(int soda) {
		this.soda = soda;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param sugar DOCUMENT ME!
	*/
	public void setSugar(int sugar) {
		this.sugar = sugar;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param withCaipirinha DOCUMENT ME!
	*/
	public void setWithCaipirinha(boolean withCaipirinha) {
		this.withCaipirinha = withCaipirinha;
	}

	/**
	 * DOCUMENT ME!
	*
	* @param women DOCUMENT ME!
	*/
	public void setWomen(int women) {
		this.women = women;
	}
}
