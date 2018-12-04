/* 
 * Copyright (C) 2018 Phil Gaiser
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

package com.kilo52.icecrusher.ui.view;

import com.kilo52.icecrusher.ui.view.Converters.Converter;

import javafx.beans.binding.Bindings;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TableCell;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Implements an editable table cell to be used in DataFrameViews.
 *
 */
public class EditingTableCell extends TableCell<Integer, Object> {

	private TextField textField;
	private TextFormatter<Object> textFormatter;

	/**
	 * Constructs a new <code>EditingTableCell</code> using the filter and string converter
	 * of the specified ConversionPack.<br>
	 * The filter of the ConversionPack is used to filter editing input passed to this cell.
	 * The converter is used to convert the string input to an object and vice versa
	 * 
	 * @param conversion The <code>ConversionPack</code> holding a filter and converter for
	 *                   this table cell. Must not be null
	 */
	public EditingTableCell(final ConversionPack conversion){
		this(conversion, null);
	}
	
	/**
	 * Constructs a new <code>EditingTableCell</code> using the filter and
	 * string converter of the specified ConversionPack and adding the specified tooltip.<br>
	 * The filter of the ConversionPack is used to filter editing input passed to this cell.
	 * The converter is used to convert the string input to an object and vice versa
	 * 
	 * @param conversion The <code>ConversionPack</code> holding a filter and converter for
	 *                   this table cell. Must not be null
	 * @param tooltip The tooltip to add to the cell. May be null
	 */
	public EditingTableCell(final ConversionPack conversion, final Tooltip tooltip){
		final Converter converter = conversion.getConverter();
		textField = new TextField();
		textFormatter = new TextFormatter<Object>(converter, "DEFAULT", conversion.getFilter());
		textField.setTextFormatter(textFormatter);

		textField.addEventFilter(KeyEvent.KEY_RELEASED, e -> {
			if (e.getCode() == KeyCode.ESCAPE) {
				cancelEdit();
			}
		});

		textField.setOnAction(e -> {
			commitEdit(converter.fromString(textField.getText()));
		});

		textProperty().bind(Bindings
				.when(emptyProperty())
				.then((String)null)
				.otherwise(itemProperty().asString()));

		setGraphic(textField);
		setContentDisplay(ContentDisplay.TEXT_ONLY);
		if(tooltip != null){
			setTooltip(tooltip);
		}
	}

	@Override
	protected void updateItem(Object value, boolean empty) {
		super.updateItem(value, empty);
		if (isEditing()) {
			textField.requestFocus();
			textField.selectAll();
			setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		} else {
			setContentDisplay(ContentDisplay.TEXT_ONLY);
		}
	}

	@Override
	public void startEdit() {
		super.startEdit();
		final Object value = getItem();
		textFormatter.setValue(value != null ? value.toString() : "");
		setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
		textField.requestFocus();
		textField.selectAll();
	}

	@Override
	public void commitEdit(Object newValue) {
		super.commitEdit(newValue);
		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

	@Override
	public void cancelEdit() {
		super.cancelEdit();
		setContentDisplay(ContentDisplay.TEXT_ONLY);
	}

}
