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

import com.jfoenix.controls.JFXToggleButton;
import com.kilo52.icecrusher.util.Const;
import com.kilo52.icecrusher.util.EditorConfiguration;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import static com.kilo52.icecrusher.util.EditorConfiguration.Section.*;
import static com.kilo52.icecrusher.util.EditorConfiguration.*;

/**
 * Controller class for the preferences activity.
 *
 */
public class PreferencesController extends Controller {
	
	@FXML
	private JFXToggleButton prefRememberTabs;
	
	@FXML
	private JFXToggleButton prefShowIndex;
	
	@FXML
	private JFXToggleButton prefClearAfterRowAdd;
	
	@FXML
	private JFXToggleButton prefConfirmRowDeletion;
	
	@FXML
	private JFXToggleButton prefDialogAlwaysHome;
	
	private EditorConfiguration config;
	private boolean isDirty;
	private boolean reloadRequired;

	public PreferencesController(){
		this.config = getConfiguration();
	}
	
	@Override
	public boolean onExitRequested(){
		if(isDirty){
			this.config.persistConfiguration();
		}
		ArgumentBundle bundle = new ArgumentBundle();
		bundle.addArgument(Const.BUNDLE_KEY_RELOAD_REQUIRED, reloadRequired);
		bundle.addArgument(Const.BUNDLE_KEY_EXIT_REQUESTED, true);
		finishActivity(bundle);
		return false;
	}
	
	@FXML
	public void initialize(){
		prefRememberTabs.setSelected(config.booleanOf(GLOBAL, CONFIG_RECALL_TABS));
		prefShowIndex.setSelected(config.booleanOf(GLOBAL, CONFIG_SHOW_INDEX_COL));
		prefClearAfterRowAdd.setSelected(config.booleanOf(GLOBAL, CONFIG_CLEAR_AFTER_ROW_ADD));
		prefConfirmRowDeletion.setSelected(config.booleanOf(GLOBAL, CONFIG_CONFIRM_ROW_DELETION));
		prefDialogAlwaysHome.setSelected(config.booleanOf(GLOBAL, CONFIG_DIALOG_ALWAYS_HOME));
	}
	
	@FXML
	private void onClose(ActionEvent event){
		if(isDirty){
			this.config.persistConfiguration();
		}
		ArgumentBundle bundle = new ArgumentBundle();
		bundle.addArgument(Const.BUNDLE_KEY_RELOAD_REQUIRED, reloadRequired);
		finishActivity(bundle);
	}
	
	@FXML
	private void onPreferenceChanged(ActionEvent event){
		final JFXToggleButton btn = (JFXToggleButton) event.getSource();
		switch(btn.getId()){
		case "prefRememberTabs":
			config.set(GLOBAL, CONFIG_RECALL_TABS, btn.isSelected());
			break;
		case "prefShowIndex":
			config.set(GLOBAL, CONFIG_SHOW_INDEX_COL, btn.isSelected());
			reloadRequired = true;
			break;
		case "prefClearAfterRowAdd":
			config.set(GLOBAL, CONFIG_CLEAR_AFTER_ROW_ADD, btn.isSelected());
			break;
		case "prefConfirmRowDeletion":
			config.set(GLOBAL, CONFIG_CONFIRM_ROW_DELETION, btn.isSelected());
			break;
		case "prefDialogAlwaysHome":
			final boolean isSelected = btn.isSelected();
			config.set(GLOBAL, CONFIG_DIALOG_ALWAYS_HOME, isSelected);
			if(isSelected){
				config.set(WINDOW, CONFIG_WINDOW_DIALOG_DIR, Const.KEY_USER_HOME);
			}
			break;
		}
		isDirty = true;
	}

}
