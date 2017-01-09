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

import java.io.Serializable;
import java.util.LinkedList;

/**
 * @author Chris Harley
 *
 */
public class Recipe implements Serializable {

	/**
	 * Serial Version ID
	 */
	private static final long serialVersionUID = -8930104112110732154L;
	
	private String m_title;
	private String m_style;
	private double m_batchSize = 5.0;
	private String m_instructions = "";
	private String m_notes = "";
	
	/**
	 * List of all ingredeints in this recipe.
	 */
	//TODO - consider splitting into multiple list to make calculations and display ordering easier
	private LinkedList<RecipeIngredient> m_ingredientList = new LinkedList<RecipeIngredient>();
	
	
	/**
	 * Default Constructor
	 */
	public Recipe(String style){
		m_style = style;
	}

	public void addIngredient(Ingredient ingred, double amt) {
		m_ingredientList.add(new RecipeIngredient(ingred, amt));
	}
	
	public LinkedList<RecipeIngredient> getIngredientList() {
		return new LinkedList<RecipeIngredient>(m_ingredientList);
	}
	
	public String getStyle() {
		return m_style;
	}
	
//	public void delIngredient(Ingredient ingred) {
//		m_ingredientList.remove(ingred);
//	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return m_instructions;
	}

	/**
	 * @param instructions the instructions to set
	 */
	public void setInstructions(String instructions) {
		this.m_instructions = instructions;
	}
	
	public double getOriginalGravity() {
		//TODO update to account for mashing vs extract vs steeping, etc
		double gravity = 0;
		for (RecipeIngredient ri : m_ingredientList) {
			if (ri.getIngredient().getType() == Ingredient.Type.MALT) {
				Malt m = (Malt) ri.getIngredient();
				gravity += 1000 * (m.getGravity() - 1) * ri.getAmount();
			}
		}
		return 1 + (gravity / m_batchSize) / 1000;
	}
	
	public double getFinalGravity() {
		return 1.015;  //TODO replace with some calculation
	}
	
	public double getABV() {
		return Utils.calculateABV(getOriginalGravity(), getFinalGravity());
	}
	
	public double getIBU() {
		//TODO update to account for boil times
		double bitterness = 0;
		for (RecipeIngredient ri : m_ingredientList) {
			if (ri.getIngredient().getType() == Ingredient.Type.HOPS) {
				Hops h = (Hops) ri.getIngredient();
				bitterness += 0.30 * (h.getBoilTime()/60) * h.getAlphaAcid() * ri.getAmount(); 
			}
		}
		return (bitterness / m_batchSize) / 0.01335; 
	}
	
	public double getColor() {
		double color = 0;
		for (RecipeIngredient ri : m_ingredientList) {
			if (ri.getIngredient().getType() == Ingredient.Type.MALT) {
				Malt m = (Malt) ri.getIngredient();
				color += m.getSrm() * ri.getAmount();
			}
		}
		return color / m_batchSize;
	}

	/**
	 * @return the batchSize
	 */
	public double getBatchSize() {
		return m_batchSize;
	}

	/**
	 * @param batchSize the batchSize to set
	 */
	public void setBatchSize(double batchSize) {
		this.m_batchSize = batchSize;
	}
	
	public class RecipeIngredient {
		private Ingredient m_ingredient;
		private double m_amount;
		
		RecipeIngredient() {
			
		}
		
		RecipeIngredient(Ingredient i, double amt) {
			m_ingredient = i;
			m_amount = amt;
		}

		/**
		 * @return the ingredient
		 */
		public Ingredient getIngredient() {
			return m_ingredient;
		}

		/**
		 * @param ingredient the ingredient to set
		 */
		public void setIngredient(Ingredient ingredient) {
			this.m_ingredient = ingredient;
		}

		/**
		 * @return the amount
		 */
		public double getAmount() {
			return m_amount;
		}

		/**
		 * @param amount the amount to set
		 */
		public void setAmount(double amount) {
			this.m_amount = amount;
		}
	}

	/**
	 * Update the malt amount in the recipe to create the passed in abv
	 * @param abv
	 */
	public void updateMaltForABV(double abv) {
		//TODO update to handle added complexities after updating elsewhere to handle different malt types
		RecipeIngredient ri = getBaseMalt();
		Malt m = (Malt) ri.getIngredient();
		double bs = m_batchSize;
		double og = 1000 * (getOriginalGravity() - 1) ;
		double ng = 7.46 * (abv + 0.5) + (1000 * (getFinalGravity() - 1));
		double maltGravity = 1000 * (m.getGravity() - 1);
		
		//TODO explain crazy algebra
		double newAmt = (ng * bs - (og * bs - (maltGravity * ri.getAmount()))) / maltGravity;
		//TODO loop through malts or update slider underneath user.  Could use bool return value for updating sliders.
		if (newAmt < 0) {newAmt = 0;} //No negative amounts.
		ri.setAmount(newAmt);
		
	}

	public void updateHopsForIBU(double ibu) {
		//TODO update to handle complexities of different boil times for each hop.
		RecipeIngredient ri = getBitteringHops();
		Hops h = (Hops) ri.getIngredient();
		double ob = (0.01335 * getIBU() * m_batchSize) / 0.30;  // ob ~ Old Bitterness
		double nb = (0.01335 * ibu * m_batchSize) / 0.30;  // nb ~ New Bitterness
		
		//TODO explain crazy algebra
		double newAmt = (nb - (ob - (ri.getAmount() * (h.getAlphaAcid() * (h.getBoilTime()/60) )))) / (h.getAlphaAcid() * (h.getBoilTime()/60));
		//TODO loop through hops or update slider underneath user.  Could use bool return value for updating sliders.
		if (newAmt < 0) {newAmt = 0;} //No negative amounts.
		ri.setAmount(newAmt);
	}

	public void updateMaltForColor(double srm) {
		RecipeIngredient ri = getDarkMalt();
		Malt m = (Malt) ri.getIngredient();
		double ocb = getColor() * m_batchSize; //ocb ~ Old Color * Batch
		double ncb = srm * m_batchSize; //ncb ~ New Color * Batch
		
		//TODO explain crazy algebra
		double newAmt = ((ncb - (ocb - m.getSrm() * ri.getAmount())) / m.getSrm()); 
		//TODO loop through malts or update slider underneath user.  Could use bool return value for updating sliders.
		if (newAmt < 0) {newAmt = 0;} //No negative amounts.
		ri.setAmount(newAmt);
	}

	/**
	 * Find the base malt, the malt that provide the most sugar (least color) in this recipe.
	 * 
	 * @return
	 */
	private RecipeIngredient getBaseMalt() {
		RecipeIngredient baseMalt = null;
		for (RecipeIngredient ri : m_ingredientList) {
			if (ri.getIngredient().getType() == Ingredient.Type.MALT) {
				if (baseMalt == null) { baseMalt = ri; }
				else if ( ((Malt) ri.getIngredient()).getSrm() < ((Malt) baseMalt.getIngredient()).getSrm() ){
					baseMalt = ri;
				}
			}
		}
		return baseMalt;
	}
	
	/**
	 * Find the malt that provides the most color in this recipe.
	 * 
	 * @return
	 */
	private RecipeIngredient getDarkMalt() {
		RecipeIngredient darkMalt = null;
		for (RecipeIngredient ri : m_ingredientList) {
			if (ri.getIngredient().getType() == Ingredient.Type.MALT) {
				if (darkMalt == null) { darkMalt = ri; }
				else if (((Malt) ri.getIngredient()).getSrm() > ((Malt) darkMalt.getIngredient()).getSrm()){
					darkMalt = ri;
				}
			}
		}
		return darkMalt;		
	}

	/**
	 * Find the Hops that provide the bitterness in this recipe
	 * 
	 */
	private RecipeIngredient getBitteringHops() {
		//TODO verify the boil time is the longest, not just return first hops
		for (RecipeIngredient ri : m_ingredientList) {
			if (ri.getIngredient().getType() == Ingredient.Type.HOPS) {
				return ri;
			}
		}
		return null;				
	}
	
}
