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

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.kilo52.icecrusher.ui.OneShotSnackbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Controller class for the {@link FilterDialog}.
 *
 */
public class FilterDialogController {
	
	/**
	 * Listener interface for the <code>FilterDialog</code>.
	 *
	 */
	public interface DialogListener {
		
		/**
		 * Called when the user confirms a filter action
		 * 
		 * @param column The name of the column to filter
		 * @param regex The regex to apply to the filter
		 * @param openTab Indicating whether to show the results in a new tab
		 */
		void onFilter(String column, String regex, boolean openTab);
	}
	
	@FXML
	private JFXComboBox<String> cBoxColumns;
	
	@FXML
	private JFXTextField txtRegex;
	
	@FXML
	private JFXCheckBox checkBoxTab;
	
	private Pane rootPane;
	private DialogListener delegate;
	
	public void setFilterListener(DialogListener delegate){
		this.delegate = delegate;
	}
	
	public void setRootContainer(Pane pane) {
		this.rootPane = pane;
	}
	
	public void setColumns(final String[] columns){
		cBoxColumns.getItems().removeAll(cBoxColumns.getItems());
		cBoxColumns.getItems().addAll(columns);
	}
	
	@FXML
	private void initialize(){ }
	
	@FXML
	private void onFilter(ActionEvent event){
		final String column = cBoxColumns.getValue();
		if((column == null) || (column.isEmpty())){
			showWarnMsg("Please specify a column");
			return;
		}
		final String regex = txtRegex.getText();
		if((regex == null) || (regex.isEmpty())){
			showWarnMsg("Please specify a regex");
			return;
		}
		try{
			Pattern.compile(regex);
		}catch(PatternSyntaxException ex){
			showWarnMsg("The entered regular expression is invalid");
			return;
		}
		if(delegate != null){
			delegate.onFilter(column, regex, checkBoxTab.isSelected());
		}
	}
	
	private void showWarnMsg(final String msg){
		OneShotSnackbar.showFor(rootPane, msg);
	}

}
