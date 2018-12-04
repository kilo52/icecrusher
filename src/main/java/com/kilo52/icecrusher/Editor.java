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

package com.kilo52.icecrusher;

import com.kilo52.icecrusher.ui.Controller.ArgumentBundle;
import com.kilo52.icecrusher.ui.StackedApplication;
import com.kilo52.icecrusher.util.Const;
import com.kilo52.icecrusher.util.EditorConfiguration;
import com.kilo52.icecrusher.util.Layout;
import com.kilo52.icecrusher.util.Resources;

import javafx.stage.Stage;

import static com.kilo52.icecrusher.util.EditorConfiguration.Section.*;
import static com.kilo52.icecrusher.util.EditorConfiguration.*;

/**
 * Main class of the Icecrusher application
 *
 */
public class Editor extends StackedApplication {
	
	private static EditorConfiguration config;
	
	@Override
	public void onStart(Stage stage) throws Exception{
		stage.setTitle(Const.APPLICATION_NAME);
		stage.getIcons().add(Resources.image(Resources.IC_ICECRUSHER));
		
		final ArgumentBundle bundle = new ArgumentBundle();
		bundle.addArgument(BUNDLE_KEY_SCENE_WIDTH, Double.valueOf(config.valueOf(WINDOW, CONFIG_WINDOW_WIDTH)));
		bundle.addArgument(BUNDLE_KEY_SCENE_HEIGHT, Double.valueOf(config.valueOf(WINDOW, CONFIG_WINDOW_HEIGHT)));
		startActivity(new Layout(Layout.MAIN_ACTIVITY), bundle);
	}
	
	@Override
	public void onStop(){
		config.persistConfiguration();
		if(config.booleanOf(GLOBAL, CONFIG_RECALL_TABS)){
			config.persistHistory();
		}
	}
	
	@Override
	public void onWindowResized(final double width, final double height){
		if(config != null){
			config.set(WINDOW, CONFIG_WINDOW_WIDTH, String.valueOf(getMainStage().getWidth()));
			config.set(WINDOW, CONFIG_WINDOW_HEIGHT, String.valueOf(getMainStage().getHeight()));
		}
	}

	public static void main(String[] args){
		config = getConfiguration();
		launch(args);
	}

}