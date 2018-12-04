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

import com.jfoenix.controls.JFXButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller class for the {@link SaveDialog}.
 *
 */
public class SaveDialogController {

	/**
	 * Listener interface for the <code>SaveDialog</code>.
	 *
	 */
	public interface DialogListener {
		
		/**
		 * Called when the user confirms the action
		 * 
		 * @param save Indicating whether to save or dismiss 
		 *        changes made by the user
		 */
		void onConfirm(boolean save);
	}
	
	@FXML
	private Label labelTitle;
	
	@FXML
	private Label labelMessage;
	
	@FXML
	private JFXButton btnSave;
	
	@FXML
	private JFXButton btnDiscard;
	
	private DialogListener delegate;
	
	public void setConfirmListener(DialogListener delegate){
		this.delegate = delegate;
	}
	
	public void setTitle(final String title){
		this.labelTitle.setText(title);
	}
	
	public void setMessage(final String message){
		this.labelMessage.setText(message);
	}
	
	@FXML
	private void initialize(){ }
	
	@FXML
	private void onConfirm(ActionEvent event){
		final JFXButton btn = (JFXButton) event.getSource();
		if(delegate != null){
			delegate.onConfirm(btn.getId().equals("btnSave"));
		}
	}
	
	
	
}
