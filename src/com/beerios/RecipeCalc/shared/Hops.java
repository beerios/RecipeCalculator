package com.beerios.RecipeCalc.shared;


public class Hops extends Ingredient {

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = 4621691441524931366L;
	
	public static final String UNITS = "oz";
	public enum HopsType{LEAF, PELLET, PLUG, SHOT};
	
	private String m_name;
	private double m_alphaAcid;
	private double m_boilTime = 60.0;
	
	public Hops() {
	}
	
	public Hops(String name, double alpha_acid) {
		m_name = name;
		m_alphaAcid = alpha_acid;
	}

	@Override
	public Type getType() {
		return Type.HOPS;
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
	 * @return the alphaAcid
	 */
	public double getAlphaAcid() {
		return m_alphaAcid;
	}

	/**
	 * @param alphaAcid the alphaAcid to set
	 */
	public void setAlphaAcid(double alphaAcid) {
		this.m_alphaAcid = alphaAcid;
	}

	/**
	 * @return the boilTime
	 */
	public double getBoilTime() {
		return m_boilTime;
	}

	/**
	 * @param boilTime the boilTime to set
	 */
	public void setBoilTime(double boilTime) {
		this.m_boilTime = boilTime;
	}

}
