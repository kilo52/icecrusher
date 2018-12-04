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

import com.jfoenix.controls.JFXTextField;
import com.kilo52.common.struct.DataFrame;
import com.kilo52.icecrusher.ui.OneShotSnackbar;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Controller class for the {@link RenameColumnDialog}.
 *
 */
public class RenameColumnDialogController {
	
	/**
	 * Listener interface for the <code>RenameColumnDialog</code>.
	 *
	 */
	public interface DialogListener {
		
		/**
		 * Called when the user confirms a column rename action
		 * 
		 * @param newName The new name of the column
		 */
		void onRename(String newName);
	}
	
	@FXML
	private JFXTextField txtColName;
	
	private Pane rootPane;
	private DialogListener delegate;
	private DataFrame df;
	private String name;
	
	public void setRenameListener(DialogListener delegate){
		this.delegate = delegate;
	}
	
	public void setRootContainer(Pane pane) {
		this.rootPane = pane;
	}
	
	public void setCurrent(final DataFrame df, final String name){
		this.df = df;
		this.name = name;
//		txtColName.requestFocus();  //is not working. BUG: JDK-8087950
		txtColName.setText(name);
		txtColName.focusedProperty().addListener((ov, t, t1) -> {
			Platform.runLater(() -> {
				if(txtColName.isFocused() && !txtColName.getText().isEmpty()){
					txtColName.selectAll();
				}
			});
		});
	}

	@FXML
	private void initialize(){ }
	
	@FXML
	private void onRename(ActionEvent event){
		final String newName = txtColName.getText();
		if(newName == null || newName.isEmpty()){
			showWarnMsg("Column name cannot be empty");
			return;
		}
		if(nameIsDuplicate(newName)){
			showWarnMsg("Duplicate column name");
			return;
		}
		if(delegate != null){
			delegate.onRename(newName);
		}
	}
	
	private void showWarnMsg(final String msg){
		OneShotSnackbar.showFor(rootPane, msg);
	}
	
	private boolean nameIsDuplicate(final String newName){
		if(this.name.equals(newName)){ return false; }
		for(final String s : df.getColumnNames()){
			if(newName.equals(s)){
				return true;
			}
		}
		return false;
	}

}
