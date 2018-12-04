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
import com.kilo52.icecrusher.ui.OneShotSnackbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Controller class for the {@link SortDialog}.
 *
 */
public class SortDialogController {

	/**
	 * Listener interface for the <code>SortDialog</code>.
	 *
	 */
	public interface DialogListener {
		
		/**
		 * Called when the user confirms the sort action
		 * 
		 * @param column The name of the column to sort
		 */
		void onSort(String column);
	}
	
	@FXML
	private JFXComboBox<String> cBoxColumns;
	
	private Pane rootPane;
	private DialogListener delegate;
	
	public void setSortListener(DialogListener delegate){
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
	private void onSort(ActionEvent event){
		final String column = cBoxColumns.getValue();
		if((column == null) || (column.isEmpty())){
			showWarnMsg("Please specify a column");
			return;
		}
		if(delegate != null){
			delegate.onSort(column);
		}
	}
	
	private void showWarnMsg(final String msg){
		OneShotSnackbar.showFor(rootPane, msg);
	}
}
