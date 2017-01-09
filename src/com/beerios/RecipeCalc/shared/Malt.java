package com.beerios.RecipeCalc.shared;


public class Malt extends Ingredient {

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = 3626476932770905923L;
	
	
	
	public static final String UNITS = "lbs";
	public static enum MaltType{EXTRACT, GRAIN, SUGAR};
	
	private String m_name;
	private double m_gravity;
	private double m_srm;
	
	public Malt() {
	}

	public Malt(String name, double gravity, double color) {
		m_name = name;
		m_gravity = gravity;
		m_srm = color;
	}
	
	public Type getType() {
		return Type.MALT;
	}
	
	public String getUnits(){
		return UNITS;
	}

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
	 * @return the srm
	 */
	public double getSrm() {
		return m_srm;
	}

	/**
	 * @param srm the srm to set
	 */
	public void setSrm(double srm) {
		this.m_srm = srm;
	}

	/**
	 * @return the gravity
	 */
	public double getGravity() {
		return m_gravity;
	}

	/**
	 * @param gravity the gravity to set
	 */
	public void setGravity(double gravity) {
		this.m_gravity = gravity;
	}
	
}
