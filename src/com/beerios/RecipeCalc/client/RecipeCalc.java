package com.beerios.RecipeCalc.client;

import java.util.LinkedList;

import com.beerios.RecipeCalc.shared.*;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.widgetideas.client.SliderBar;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public final class RecipeCalc implements EntryPoint {
	
	final NumberFormat nf_decimal = NumberFormat.getDecimalFormat();	

	/**
	 * The list of recipes from the server
	 */
	private LinkedList<Recipe> m_styleList = new LinkedList<Recipe>();
	
	/**
	 * All of the ui components as member variables
	 */
	private ListBox m_styleListBox;
	private TextBox m_batchSizeBox;
	private SliderBar m_abvSlider;
	private SliderBar m_ibuSlider;
	private SliderBar m_srmSlider;
	private IngredientTable m_ingredientTable;
	
	/**
	 * A recipe object to represent the current state of the UI.
	 */
	private Recipe m_Recipe;
	
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	/**
	 * Create a remote service proxy to talk to the server-side Greeting service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {

		//retrieve the style list from the server
		getDefaultStylesFromServer();
		
		//Add the Style and Batch Size elements
		createStyleListBox();
		createBatchSizeBox();
		RootPanel.get("styleListContainer").add(m_styleListBox);
		RootPanel.get("batchSizeTextContainer").add(m_batchSizeBox);

		m_Recipe = m_styleList.get(m_styleListBox.getSelectedIndex());		
		
		//add the sliders
		createSliders();
		updateSilders();  //Set the values to the current recipe
		RootPanel.get("abvSliderContainer").add(m_abvSlider);
		RootPanel.get("ibuSliderContainer").add(m_ibuSlider);
		RootPanel.get("srmSliderContainer").add(m_srmSlider);
				
		//Create a vertical panel to put the ingredient table in
		createIngredientTable();
		updateIngredientTable();  //Set the contents to the current recipe
		final VerticalPanel vp = new VerticalPanel();
		vp.add(m_ingredientTable);
		RootPanel.get("ingredientTableContainer").add(vp);
		
		
	}
	
	
	private void createSliders() {
		
		//ABV Slider Bar
		m_abvSlider = new SliderBar(0, 12.5);
		m_abvSlider.setStepSize(0.1);
		m_abvSlider.setNumTicks(18);
		m_abvSlider.setNumLabels(9);
		m_abvSlider.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				m_abvSlider.setTitle(nf_decimal.format(m_abvSlider.getCurrentValue()));
				m_Recipe.updateMaltForABV(m_abvSlider.getCurrentValue());
				updateIngredientTable();
				updateSilders();
			}
		});

		//IBU Slider Bar
		m_ibuSlider = new SliderBar(0, 100);
		m_ibuSlider.setStepSize(0.5);
		m_ibuSlider.setNumTicks(20);
		m_ibuSlider.setNumLabels(10);
		m_ibuSlider.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				m_ibuSlider.setTitle(nf_decimal.format(m_ibuSlider.getCurrentValue()));
				m_Recipe.updateHopsForIBU(m_ibuSlider.getCurrentValue());
				updateIngredientTable();
				updateSilders();
			}
		});
		
		//SRM Slider Bar
		m_srmSlider = new SliderBar(0, 50);
		m_srmSlider.setStepSize(1);
		m_srmSlider.setNumTicks(20);
		m_srmSlider.setNumLabels(10);
		m_srmSlider.addMouseUpHandler(new MouseUpHandler() {
			public void onMouseUp(MouseUpEvent event) {
				m_srmSlider.setTitle(nf_decimal.format(m_srmSlider.getCurrentValue()));
				m_Recipe.updateMaltForColor(m_srmSlider.getCurrentValue());
				updateIngredientTable();
				updateSilders();
			}
		});

	}

	private void updateSilders() {
		m_abvSlider.setCurrentValue(m_Recipe.getABV());
		m_abvSlider.setTitle(nf_decimal.format(m_abvSlider.getCurrentValue()));		

		m_ibuSlider.setCurrentValue(m_Recipe.getIBU());
		m_ibuSlider.setTitle(nf_decimal.format(m_ibuSlider.getCurrentValue()));		
		
		m_srmSlider.setCurrentValue(m_Recipe.getColor());
		m_srmSlider.setTitle(nf_decimal.format(m_srmSlider.getCurrentValue()));		
	}
	
	private void createStyleListBox() {
		
		if (m_styleListBox != null) {
			return;
		}
		
		class BeerStyleBoxHandler implements ChangeHandler {

			@Override
			public void onChange(ChangeEvent event) {
				m_Recipe = m_styleList.get(m_styleListBox.getSelectedIndex());
				updateIngredientTable();
				updateSilders();
				
			}
			
		}
		
		m_styleListBox = new ListBox();
		//TODO build list from XML recipe/style database
		for (Recipe recipe : m_styleList) {
			m_styleListBox.addItem(recipe.getStyle());
		}
		m_styleListBox.setTitle("Selecting a new style will reset the enire page.");
		m_styleListBox.addChangeHandler(new BeerStyleBoxHandler());
		//return m_StyleListBox;
		
	}

	private void createBatchSizeBox() {

		if (m_batchSizeBox != null) {
			return;
		}
		
		m_batchSizeBox = new TextBox();
		m_batchSizeBox.setTitle("Enter the volume of wort that will be fermented.");
		m_batchSizeBox.setMaxLength(5);
		m_batchSizeBox.setText("5.0");
		
		//return m_BatchSizeBox;
	}
	
	private void createIngredientTable() {
		if (m_ingredientTable != null){
			return;
		}
		
		m_ingredientTable = new IngredientTable();
	}

	private void updateIngredientTable() {
		//Remove all rows except for the add button
		while (m_ingredientTable.getRowCount() > 1) {
			m_ingredientTable.removeRow(0);
		}
		
		for (Recipe.RecipeIngredient ingredient : m_Recipe.getIngredientList()) {
			m_ingredientTable.addRow(ingredient.getIngredient().getName(), ingredient.getAmount());				
		}
	}
	
	/**
	 * Retrieve the default list of recipes from the server.
	 */
	private void getDefaultStylesFromServer() {
		//TODO - Retrieve Recipe List from Server
		Malt PaleMalt = new Malt("Pale Malt Extract", 1.036, 2.5);
		Malt AmberMalt = new Malt("Amber Malt Extract", 1.034, 10.0);
		Malt DarkMalt = new Malt("Dark Malt Extract", 1.032, 275.0);
		
		Hops Cascade = new Hops("Cascade Hops", 5.5);
		
		Yeast London = new Yeast("London Ale Yeast", 85.0, 75.0, "68 - 75");
		
		Recipe IPA = new Recipe("IPA");
		IPA.addIngredient(PaleMalt, 8.0);
		IPA.addIngredient(Cascade, 5.25);
		IPA.addIngredient(London, 1);
		
		Recipe Red = new Recipe("Red");
		Red.addIngredient(PaleMalt, 5.0);
		Red.addIngredient(AmberMalt, 1.0);
		Red.addIngredient(Cascade, 1.75);
		Red.addIngredient(London, 1);
		
		Recipe Stout = new Recipe("Stout");
		Stout.addIngredient(PaleMalt, 5.0);
		Stout.addIngredient(AmberMalt, 0.5);
		Stout.addIngredient(DarkMalt, 0.5);
		Stout.addIngredient(Cascade, 1.5);
		Stout.addIngredient(London, 1);
		
		m_styleList.add(IPA);
		m_styleList.add(Red);
		m_styleList.add(Stout);
	}
	
}
