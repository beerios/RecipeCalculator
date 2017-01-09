/**
 * Copyright Beerios 2010
 *
 * @author Chris Harley
 *
 * File: Yeast.java
 * 
 * Description:
 *
 */
package com.beerios.RecipeCalc.shared;


/**
 * @author Chris Harley
 *
 */
public class Yeast extends Ingredient {

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = 7543351760502706516L;

	public static final String UNITS = "pack";
	public static enum YeastType{SMACKPACK, VIAL, DRY};

	private String m_name;
	private double m_attenuation;
	private double m_flocculation;
	private String m_tempRange;
	
	/**
	 * Default Constructor
	 */
	public Yeast() {
	}
	
	/**
	 * Convenience Constructor
	 * 
	 * @param name
	 * @param attenuation
	 * @param flocculation
	 * @param temp_range
	 */
	public Yeast(String name, double attenuation, double flocculation, String temp_range) {
		m_name = name;
		m_attenuation = attenuation;
		m_flocculation = flocculation;
		m_tempRange = temp_range;
	}

	@Override
	public Type getType() {
		return Type.YEAST;
	}

	@Override
	public String getUnits() {
		return UNITS;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return m_name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.m_name = name;
	}

	/**
	 * @return the flocculation
	 */
	public double getFlocculation() {
		return m_flocculation;
	}

	/**
	 * @param flocculation the flocculation to set
	 */
	public void setFlocculation(double flocculation) {
		this.m_flocculation = flocculation;
	}

	/**
	 * @return the attenuation
	 */
	public double getAttenuation() {
		return m_attenuation;
	}

	/**
	 * @param attenuation the attenuation to set
	 */
	public void setAttenuation(double attenuation) {
		this.m_attenuation = attenuation;
	}

	/**
	 * @return the tempRange
	 */
	public String getTempRange() {
		return m_tempRange;
	}

	/**
	 * @param tempRange the tempRange to set
	 */
	public void setTempRange(String tempRange) {
		this.m_tempRange = tempRange;
	}

}
