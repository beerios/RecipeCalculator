/**
 * Copyright Beerios 2010
 *
 * @author 
 *
 * File: 
 * 
 * Description:
 *
 */
package com.beerios.RecipeCalc.shared;

/**
 * @author Chris
 *
 * Class to eventually contain calculations and formula needed by the brewing software.
 *
 */
public class Utils {

	/**
	 * Private constructor to prevent static class from being instantiated.
	 */
	private Utils(){}
	
	public static double calculateABV(double sg, double fg) {
		return (1000*(sg-fg))/7.46 - 0.5;
	}
	
	public static double calculateABV(Recipe recipe) {
		return 5.5;
	}
	
	public static double calculateIBU() {
		return 48.7;
	}
	
	public static double calculateSRM() {
		return 22.0;
	}
	
}
