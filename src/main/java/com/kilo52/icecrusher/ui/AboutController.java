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

package com.kilo52.icecrusher.ui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.kilo52.icecrusher.util.Const;
import com.kilo52.icecrusher.net.Updater;
import com.kilo52.icecrusher.net.Updater.UpdateHandler;
import com.kilo52.icecrusher.net.Updater.Version;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

/**
 * Controller class for the about activity.
 *
 */
public class AboutController extends Controller implements UpdateHandler {
	
	@FXML
	private Label labelApp;
	
	@FXML
	private Label labelVersion;
	
	@FXML
	private Label labelDevel;
	
	@FXML
	private Label labelContact;
	
	@FXML
	private JFXButton btnUpdate;
	
	private Updater updater;

	public AboutController(){ }
	
	@FXML
	public void initialize(){
		labelApp.setText(Const.APPLICATION_NAME);
		labelVersion.setText(Const.APPLICATION_VERSION);
		labelDevel.setText(Const.APPLICATION_DEVELOPER);
		labelContact.setText(Const.APPLICATION_DEVELOPER_EMAIL);
		Platform.runLater(() -> btnUpdate.requestFocus());
	}
	
	@Override
	public boolean onExitRequested(){
		final ArgumentBundle bundle = new ArgumentBundle();
		bundle.addArgument(Const.BUNDLE_KEY_EXIT_REQUESTED, true);
		finishActivity(bundle);
		return false;
	}
	
	@FXML
	private void onClose(ActionEvent event){
		finishActivity();
	}
	
	@FXML
	private void onUpdate(ActionEvent event){
		if(updater == null){
			this.updater = new Updater();
			this.btnUpdate.setText("Checking for updates...");
			this.updater.checkForUpdates(this);
		}
	}

	@Override
	public void onResolve(Version version){
		if(version != null){
			final int i = Version.current().compareTo(version);
			if(i == 0){
				new JFXSnackbar(getRootNode()).show("This application is up to date", 6000);
				this.btnUpdate.setText("You're up to date");
			}else if(i < 0){
				final JFXSnackbar sb = new JFXSnackbar(getRootNode());
				sb.show("Version "+version+" is now available", "Show", 10000, (event) -> {
					sb.close();
					Updater.showInBrowser();
				});
				this.btnUpdate.setText("v"+version+" available");
			}
		}else{
			new JFXSnackbar(getRootNode()).show("Unable to check for updates", 6000);
			this.btnUpdate.setText("Disconnected");
		}
	}
	
}
