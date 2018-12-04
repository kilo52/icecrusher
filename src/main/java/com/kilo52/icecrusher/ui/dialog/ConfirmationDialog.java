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

import java.util.LinkedList;
import java.util.List;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;

import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

/**
 * Standard confirmation dialog. This class is reusable by supplying a 
 * different title, body message text and confirm-button label when 
 * constructing a new ConfirmationDialog.
 *
 */
public class ConfirmationDialog extends EditorDialog {
	
	/**
	 * Listener interface for the <code>ConfirmationDialog</code>.
	 *
	 */
	public interface DialogListener {
		
		/**
		 * Called when the user confirms the action.<br>
		 * This function will not be called when the dialog gets dismissed
		 */
		void onConfirm();
	}
	
	private DialogListener delegate;

	public ConfirmationDialog(StackPane root, String title, String message, String buttonText){
		super(root, null);
		JFXDialogLayout layout = new JFXDialogLayout();
		final JFXButton button = new JFXButton(buttonText);
		button.getStyleClass().add("dialog-button");
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			if(delegate != null){ 
				delegate.onConfirm();
			}
		});
		List<JFXButton> actions = new LinkedList<>();
		actions.add(button);
		layout.setHeading(new Label(title));
		layout.setBody(new Label(message));
		layout.setActions(actions);
		setContent(layout);
	}
	
	public void setOnConfirm(final DialogListener delegate){
		this.delegate = delegate;
	}

}
