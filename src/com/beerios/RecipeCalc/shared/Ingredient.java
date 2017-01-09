package com.beerios.RecipeCalc.shared;

import java.io.Serializable;

public abstract class Ingredient implements Serializable {
	
	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = -9143645345916048872L;

	public static enum Type{MALT, YEAST, HOPS, ADJUNCT};
	
	public abstract Type getType();
	
	public abstract String getUnits();

	/**
	 * @return the name
	 */
	public abstract String getName();
	
}
