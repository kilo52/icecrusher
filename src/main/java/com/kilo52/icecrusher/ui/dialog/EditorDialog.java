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

import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.events.JFXDialogEvent;
import com.kilo52.icecrusher.ui.OneShotSnackbar;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.effect.Effect;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * Base class for all dialogs.<br>
 * 
 * Note: the root node <b>MUST</b> be of type {@link StackPane}
 *
 */
public class EditorDialog extends JFXDialog {
	
	private Pane rootPane;
	
	/**
	 * Creates new dialog with the default transition animation
	 * 
	 * @param dialogContainer The parent of the dialog to create
	 * @param content The content of the dialog
	 */
	public EditorDialog(StackPane dialogContainer, Region content){
		this(dialogContainer, content, JFXDialog.DialogTransition.CENTER);
	}
	
	/**
	 * Creates new dialog with the specified transition animation
	 * 
	 * @param dialogContainer The parent of the dialog to create
	 * @param content The content of the dialog
	 * @param transitionType The transition animation to apply to the dialog
	 */
	public EditorDialog(StackPane dialogContainer, Region content, DialogTransition transitionType){
		super(dialogContainer, content, transitionType);
		this.rootPane = dialogContainer;
	}
	
	/**
	 * Sets an effect on a node of your choosing which will get removed when the dialog closes.<br>
	 * Use this method to apply a background effect when showing a dialog
	 * 
	 * @param node The <code>Node</code> object to set an effect to
	 * @param effect The <code>Effect</code> object to apply to the specified Node
	 */
	public void setBackgroundEffect(final Node node, final Effect effect){
		//adding an Eventhandler here so that the setOnDialogClosed() method 
		//can be used for other things by the API user
		addEventHandler(JFXDialogEvent.CLOSED, new EventHandler<Event>(){
			@Override
			public void handle(Event event){
				node.setEffect(null);
			}
		});
		node.setEffect(effect);
	}

	/**
	 * Shows a Snackbar warning visible when the dialog is showing
	 * 
	 * @param msg The message of the warning
	 */
	protected void showWarning(final String msg){
		OneShotSnackbar.showFor(rootPane, msg);
	}

}
