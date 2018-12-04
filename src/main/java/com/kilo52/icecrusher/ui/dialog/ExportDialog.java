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
import com.kilo52.icecrusher.ui.view.Converters;
import com.kilo52.icecrusher.ui.view.Filters;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * A Dialog which lets the user specify attributes when trying to 
 * export a <code>DataFrame</code> to a CSV file.
 *
 */
public class ExportDialog extends EditorDialog {

	/**
	 * Listener interface for the <code>ExportDialog</code>.
	 *
	 */
	public interface DialogListener {
		
		/**
		 * Called when the user confirms the intended action
		 * 
		 * @param separator The separator to use when exporting to a CSV file
		 */
		void onExport(char separator);
	}
	
	private DialogListener delegate;

	public ExportDialog(StackPane root){
		super(root, null);
		final JFXDialogLayout layout = new JFXDialogLayout();
		final VBox vbox = new VBox();
		final Label label = new Label("Use separator:");
		final TextField txtField = new TextField(",");
		txtField.setTextFormatter(new TextFormatter<Object>(Converters.charConverter(), ",", Filters.charFilter(true)));
		txtField.setMaxWidth(40.0);
		vbox.getChildren().addAll(label, txtField);
		final JFXButton button = new JFXButton("Export");
		button.getStyleClass().add("dialog-button");
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			final String separator = txtField.getText();
			if((separator == null) || (separator.isEmpty())){
				showWarning("Please specify the separator to use");
				return;
			}
			if(delegate != null){ 
				delegate.onExport(separator.charAt(0));
			}
		});
		List<JFXButton> actions = new LinkedList<>();
		actions.add(button);
		layout.setHeading(new Label("Export to CSV file"));
		layout.setBody(vbox);
		layout.setActions(actions);
		setContent(layout);
	}
	
	public void setOnExport(final DialogListener delegate){
		this.delegate = delegate;
	}

}
