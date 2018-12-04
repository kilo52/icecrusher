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

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import com.kilo52.icecrusher.util.Const;
import com.kilo52.icecrusher.util.EditorConfiguration;
import com.kilo52.icecrusher.util.EditorFile;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.effect.BoxBlur;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import static com.kilo52.icecrusher.util.EditorConfiguration.Section.*;
import static com.kilo52.icecrusher.util.EditorConfiguration.*;
import static com.kilo52.icecrusher.util.Const.KEY_USER_HOME;

/**
 * Utility class for showing file open/save dialogs (FileChooser) and providing 
 * additional features for all dialog implementations in this package.
 *
 */
public class Dialogs {
	
	private static EditorConfiguration config = getConfiguration();
	private static BoxBlur blur = new BoxBlur(3, 3, 3);

	private Dialogs(){ }
	
	/**
	 * Shows a native open file dialog where the user can choose a file to open
	 * 
	 * @param stage The owner stage of the displayed file dialog
	 * @return An <code>EditorFile</code> object representing the file chosen by the user
	 */
	public static EditorFile showFileOpenDialog(final Stage stage){
		final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a DataFrame file to open");
        fileChooser.setInitialDirectory(initDir());
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("DataFrame file (.df)", "*.df")
            );
        
        final EditorFile file = EditorFile.fromFile(fileChooser.showOpenDialog(stage));
		if((file != null) && !config.booleanOf(GLOBAL, CONFIG_DIALOG_ALWAYS_HOME)){
			final String parent = file.getParent();
			config.set(WINDOW, CONFIG_WINDOW_DIALOG_DIR, ((parent != null) ? parent : KEY_USER_HOME));
		}
        return file;
	}
	
	/**
	 * Shows a native save file dialog where the user can choose the file to save data to
	 * 
	 * @param stage The owner stage of the displayed file dialog
	 * @return An <code>EditorFile</code> object representing the file chosen by the user
	 */
	public static EditorFile showFileSaveDialog(final Stage stage){
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save DataFrame to a file");
        fileChooser.setInitialDirectory(initDir());
        
        final EditorFile file = EditorFile.fromFile(fileChooser.showSaveDialog(stage));
		if((file != null) && !config.booleanOf(GLOBAL, CONFIG_DIALOG_ALWAYS_HOME)){
			final String parent = file.getParent();
			config.set(WINDOW, CONFIG_WINDOW_DIALOG_DIR, ((parent != null) ? parent : KEY_USER_HOME));
		}
        return file;
	}
	
	/**
	 * Shows a native import file dialog (for CSV files) where the user can choose a file to import
	 * 
	 * @param stage The owner stage of the displayed file dialog
	 * @return An <code>EditorFile</code> object representing the file chosen by the user
	 */
	public static EditorFile showFileImportDialog(final Stage stage){
		final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a CSV file to import");
        fileChooser.setInitialDirectory(initDir());
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV file (.csv)", "*.csv"),
                new FileChooser.ExtensionFilter("All files", "*")
            );
        
        final EditorFile file = EditorFile.fromFile(fileChooser.showOpenDialog(stage));
		if((file != null) && !config.booleanOf(GLOBAL, CONFIG_DIALOG_ALWAYS_HOME)){
			final String parent = file.getParent();
			config.set(WINDOW, CONFIG_WINDOW_DIALOG_DIR, ((parent != null) ? parent : KEY_USER_HOME));
		}
        return file;
	}
	
	/**
	 * Shows a native export file dialog (for CSV files) where the user can choose a file to export
	 * 
	 * @param stage The owner stage of the displayed file dialog
	 * @return An <code>EditorFile</code> object representing the file chosen by the user
	 */
	public static EditorFile showFileExportDialog(final Stage stage){
		FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export DataFrame to a CSV file");
        fileChooser.setInitialDirectory(initDir());
        final EditorFile file = EditorFile.fromFile(fileChooser.showSaveDialog(stage));
		if((file != null) && !config.booleanOf(GLOBAL, CONFIG_DIALOG_ALWAYS_HOME)){
			final String parent = file.getParent();
			config.set(WINDOW, CONFIG_WINDOW_DIALOG_DIR, ((parent != null) ? parent : KEY_USER_HOME));
		}
        return file;
	}
	
	/**
	 * Shows a simple informational dialog displaying text from an <code>Exception</code>.<br>
	 * This method can be called to give a response to the user in the case of an Exception occurring during
	 * some operation
	 * 
	 * @param stage The owner stage of the displayed file dialog
	 * @param ex The Exception that occured
	 */
	public static void showExceptionDialog(final Stage stage, final Exception ex){
		final Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Error");
		alert.setHeaderText("An error occurred for "+Const.APPLICATION_NAME+" - "+Const.APPLICATION_VERSION);
		alert.setContentText("An Exception was thrown with the message: \""+ex.getMessage()+"\"");
		final StringWriter sw = new StringWriter();
		ex.printStackTrace(new PrintWriter(sw));
		String exceptionText = sw.toString();
		final Label label = new Label("Stacktrace:");
		final TextArea txt = new TextArea(exceptionText);
		txt.setEditable(false);
		txt.setWrapText(true);
		txt.setMaxWidth(Double.MAX_VALUE);
		txt.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(txt, Priority.ALWAYS);
		GridPane.setHgrow(txt, Priority.ALWAYS);
		final GridPane grid = new GridPane();
		grid.setMaxWidth(Double.MAX_VALUE);
		grid.add(label, 0, 0);
		grid.add(txt, 0, 1);
		alert.getDialogPane().setExpandableContent(grid);
		alert.initOwner(stage);
		alert.initModality(Modality.APPLICATION_MODAL);
		alert.showAndWait();
	}
	
	/**
	 * Gets a blur effect which can be applied to the background nodes of a shown dialog
	 * 
	 * @return A <code>BoxBlur</code> object to be applied to a Node
	 */
	public static BoxBlur getBackgroundBlur(){
		return blur;
	}
	
	private static File initDir(){
		File file = null;
		final String dir = config.valueOf(WINDOW, CONFIG_WINDOW_DIALOG_DIR);
		if(dir.equals(KEY_USER_HOME)){
			file = new File(System.getProperty(KEY_USER_HOME));
		}else{
			file = new File(dir);
			if(!file.exists() || !file.isDirectory()){
				file = new File(System.getProperty(KEY_USER_HOME));
				config.set(WINDOW, CONFIG_WINDOW_DIALOG_DIR, KEY_USER_HOME);
			}
		}
		return file;
	}

}
