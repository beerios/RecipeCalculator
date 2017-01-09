package com.beerios.RecipeCalc.client;

import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Element;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;

public class IngredientTable extends FlexTable {

	int numCellClicks = 0;
	
	public IngredientTable() {
		super();
		this.setCellPadding(5);
		this.insertRow(0);
		this.insertCells(0, 0, 4);
		//TODO add formatting to table
		//this.getRowFormatter().setStyleName(0, "malt-header");
		
		this.setText(0, 0, "Ingredient");
		this.setText(0, 1, "Amount");
		//this.setText(0, 2, "Color (srm)");				

		this.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent click) {
				numCellClicks++;
				Element e = getEventTargetCell(Event.as(click.getNativeEvent()));
				e.setTitle("NumClicks" + numCellClicks);
			}
		});
		
		//Add Row Button
		this.insertRow(1);
		this.insertCell(1, 0);
		Button addMalt = new Button();
		addMalt.setText("+");
		AddMaltButtonClickHandler handler = new AddMaltButtonClickHandler(this);
		addMalt.addClickHandler(handler);			
		this.setWidget(1, 0, addMalt);

	}

	public void addRow(String malt, double amt) {
		int row = this.getRowCount() - 1; //insert before the add button

		this.insertRow(row);
		this.insertCells(row, 0, 4);
		//TODO create row styles.
		//this.getRowFormatter().setStyleName(0, "malt-ingredient");
		
		this.setWidget(row, 0, new CellBox(malt));
		this.setWidget(row, 1, new CellBox(amt));
		//this.setWidget(row, 2, new CellBox(srm));
		this.setWidget(row, 2, new DelButton(this));

	}

	class CellBoxHandler implements ClickHandler, BlurHandler {

		@Override
		public void onClick(ClickEvent event) {
			TextBox tb = (TextBox) event.getSource();
			tb.setReadOnly(false);
			tb.setFocus(true);
		}

		@Override
		public void onBlur(BlurEvent event) {
			TextBox tb = (TextBox) event.getSource();
			tb.setReadOnly(true);
		}
		
	}
	
	class CellBox extends TextBox {
		
		CellBoxHandler handler = new CellBoxHandler();
		
		CellBox() {
			super();			
			
			this.setReadOnly(true);			
			this.addClickHandler(handler);
			this.addBlurHandler(handler);
		}
		
		CellBox(String text) {
			this();
			setText(text);
		}
		
		CellBox(Double val) {
			this(Double.toString(val));
		}		
	}
	
	class DelButton extends Button {
		DelButton(IngredientTable table) {
			super();
			this.setText("X");
			this.addClickHandler(new DelButtonClickHandler(table));
		}
	}
	
	class DelButtonClickHandler implements ClickHandler {
		IngredientTable m_table;
		
		DelButtonClickHandler(IngredientTable table) {
			super();
			m_table = table;
		}

		public void onClick(ClickEvent click) {
			Cell c = m_table.getCellForEvent(click);
			m_table.removeRow(c.getRowIndex());
		}
	}
	
	class AddMaltButtonClickHandler implements ClickHandler {

		IngredientTable m_table;
		
		AddMaltButtonClickHandler(IngredientTable table) {
			super();
			m_table = table;
		}
		
		public void onClick(ClickEvent click) {
			m_table.addRow("", m_table.getRowCount());
		}
	}

}
