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

package com.kilo52.icecrusher.ui.dialog;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;

import javafx.fxml.FXML;

/**
 * Controller class of a column item in the <code>CreateDialog</code>.
 *
 */
public class ColumnItemController {
	
	@FXML
	private JFXComboBox<String> cBoxColType;
	
	@FXML
	private JFXTextField txtColName;

	@FXML
	public void initialize(){
		cBoxColType.getItems().removeAll(cBoxColType.getItems());
		cBoxColType.getItems().addAll(CreateDialogController.options);
	}
	
	public String getSelectedType(){
		return cBoxColType.getValue();
	}
	
	public String getColumnName(){
		return txtColName.getText();
	}

}
