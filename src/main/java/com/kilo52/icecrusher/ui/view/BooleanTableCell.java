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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.ComboBoxTableCell;

/**
 * Implements an editable table cell for boolean values. These values are displayed
 * as text and editable by a combo box to restrict user input.
 *
 */
public class BooleanTableCell extends ComboBoxTableCell<Integer, Object> {
	
	public static ObservableList<Object> optionsDefault = FXCollections.observableArrayList(Boolean.TRUE, Boolean.FALSE);
	public static ObservableList<Object> optionsNullable = FXCollections.observableArrayList(Boolean.TRUE, Boolean.FALSE, "null");
	
	/**
	 * Constructs a new <code>BooleanTableCell</code>
	 */
	public BooleanTableCell(final boolean nullable){
		super(Converters.booleanConverter(), allOptions(nullable));
	}
	
	public BooleanTableCell(final boolean nullable, final Tooltip tooltip){
		super(Converters.booleanConverter(), allOptions(nullable));
		setTooltip(tooltip);
	}

	public BooleanTableCell(ObservableList<Object> items){
		super(Converters.booleanConverter(), items);
	}
	
	public static ObservableList<Object> allOptions(final boolean nullable){
		return (nullable ? optionsNullable : optionsDefault);
	}
	
	@Override
	public void startEdit(){
		//prevents the ComboBox from showing an empty selection when the user has cancelled 
		//a modification once. This has only an effect with null values in NullableBooleanColumns
		final Object value = getItem();
		if(value == null){
			setItem("null");
		}
		super.startEdit();
	}
}
