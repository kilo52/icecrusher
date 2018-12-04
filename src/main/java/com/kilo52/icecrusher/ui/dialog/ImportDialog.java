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
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXDialogLayout;
import com.kilo52.icecrusher.ui.view.Converters;
import com.kilo52.icecrusher.ui.view.Filters;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;

/**
 * A Dialog which lets the user specify attributes when trying to 
 * import a CSV file.
 *
 */
public class ImportDialog extends EditorDialog {

	/**
	 * Listener interface for the <code>ImportDialog</code>.
	 *
	 */
	public interface DialogListener {
		
		/**
		 * Called when the user confirms the intended action
		 * 
		 * @param hasHeader Indicating whether the CSV file has a header
		 * @param separator The separator to use when importing the CSV file
		 */
		void onImport(boolean hasHeader, char separator);
	}
	
	private DialogListener delegate;

	public ImportDialog(StackPane root){
		super(root, null);
		final JFXDialogLayout layout = new JFXDialogLayout();
		final VBox vbox = new VBox();
		final JFXCheckBox checkBox = new JFXCheckBox("Treat first line as a header");
		checkBox.setCheckedColor(Paint.valueOf("#1822ba"));//fixes visual bug
		checkBox.setSelected(true);
		final VBox sepBox =  new VBox();
		final Label label = new Label("Use separator:");
		final TextField txtField = new TextField(",");
		txtField.setTextFormatter(new TextFormatter<Object>(Converters.charConverter(), ",", Filters.charFilter(true)));
		txtField.setMaxWidth(40.0);
		sepBox.getChildren().addAll(label, txtField);
		vbox.setSpacing(20.0);
		vbox.getChildren().addAll(checkBox, sepBox);
		final JFXButton button = new JFXButton("Import");
		button.getStyleClass().add("dialog-button");
		button.addEventHandler(MouseEvent.MOUSE_CLICKED, (MouseEvent mouseEvent) -> {
			final String separator = txtField.getText();
			if((separator == null) || (separator.isEmpty())){
				showWarning("Please specify the separator to use");
				return;
			}
			if(delegate != null){ 
				delegate.onImport(checkBox.isSelected(), separator.charAt(0));
			}
		});
		final List<JFXButton> actions = new LinkedList<>();
		actions.add(button);
		layout.setHeading(new Label("Import CSV file"));
		layout.setBody(vbox);
		VBox.setMargin(checkBox, new Insets(20.0, 0.0, 0.0, 0.0));
		layout.setActions(actions);
		setContent(layout);
	}
	
	public void setOnImport(final DialogListener delegate){
		this.delegate = delegate;
	}

}
