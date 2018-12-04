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

package com.kilo52.icecrusher.util;

import java.io.IOException;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.util.Duration;

/**
 * Class responsible for handling and loading layouts from resource FXML files.
 *
 */
public class Layout {

	/**
	 * Enum defining all layout transitions currently supported.
	 *
	 */
	public enum Transition {
		SLIDE_UP,
		SLIDE_DOWN,
		SLIDE_UP_BOTH,
		SLIDE_DOWN_BOTH;
		
		private static final int ANIM_DURATION = 500;//millis
	}

	public static final String MAIN_ACTIVITY = "Frame.fxml";
	public static final String PREFERENCES_ACTIVITY = "Preferences.fxml";
	public static final String ABOUT_ACTIVITY = "About.fxml";
	public static final String CREATE_DIALOG = "CreateDialog.fxml";
	public static final String COLUMN_ITEM = "ColumnItem.fxml";
	public static final String FILTER_DIALOG = "FilterDialog.fxml";
	public static final String SAVE_DIALOG = "SaveDialog.fxml";
	public static final String ADD_COLUMN_DIALOG = "AddColumnDialog.fxml";
	public static final String RENAME_COLUMN_DIALOG = "RenameColumnDialog.fxml";
	public static final String SORT_DIALOG = "SortDialog.fxml";
	
	private FXMLLoader fxmlLoader;
	private String layout;

	/**
	 * Constructs but does NOT load a new <code>Layout</code>
	 * 
	 * @param LAYOUT The layout to use
	 */
	public Layout(String LAYOUT){
		this.layout = LAYOUT;
	}

	/**
	 * Loads the layout of this <code>Layout</code> instance and returns the 
	 * loaded object hierarchy from the FXML file
	 * 
	 * @return The object hierarchy
	 */
	public Parent load(){
		this.fxmlLoader = new FXMLLoader(getClass().getResource(Const.DIR_LAYOUT+layout));
		Parent parent = null;
		try{
			parent = fxmlLoader.load();
		}catch(IOException ex){
			ex.printStackTrace();
		}
		return parent;
	}

	/**
	 * Returns the controller associated with the root object of the hierarchy
	 * 
	 * @return A reference of the controller loaded for this layout
	 */
	public <T> T getController(){
		return this.fxmlLoader.<T>getController();
	}

	/**
	 * Creates and returns a <code>Timeline</code> animation for a layout transition
	 * 
	 * @param transition The transition to be applied to the layouts
	 * @param metric The metric to be applied to the animation
	 * @param in The parent of the layout to transition in to the scene 
	 * @param out The parent of the layout to transition out of the scene 
	 * @return A <code>Timeline</code> being able to perform the specified transition. You
	 *         must call <code>play()</code> on that Timeline to start the animation
	 */
	public static Timeline getTimelineAnimationFor(Transition transition, double metric, Parent in, Parent out){
		switch(transition){
		case SLIDE_UP:
			return slideUpTransition(metric, out);
		case SLIDE_DOWN:
			return slideDownTransition(metric, in);
		case SLIDE_UP_BOTH:
			return slideUpBothTransition(metric, in, out);
		case SLIDE_DOWN_BOTH:
			return slideDownBothTransition(metric, in, out);
		default:
			return null;
		}
	}

	private static Timeline slideUpTransition(final double height, final Parent out){
		final KeyFrame start = new KeyFrame(Duration.ZERO,
				new KeyValue(out.translateYProperty(), 0, Interpolator.EASE_BOTH));
		final KeyFrame end = new KeyFrame(Duration.millis(Transition.ANIM_DURATION),
				new KeyValue(out.translateYProperty(), -height, Interpolator.EASE_BOTH));
		return new Timeline(start, end);
	}

	private static Timeline slideDownTransition(final double height, final Parent in){
		final KeyFrame start = new KeyFrame(Duration.ZERO,
				new KeyValue(in.translateYProperty(), -height, Interpolator.EASE_BOTH));
		final KeyFrame end = new KeyFrame(Duration.millis(Transition.ANIM_DURATION),
				new KeyValue(in.translateYProperty(), 0, Interpolator.EASE_BOTH));
		return new Timeline(start, end);
	}

	private static Timeline slideUpBothTransition(final double height, final Parent in, final Parent out){
		final KeyFrame start = new KeyFrame(Duration.ZERO,
				new KeyValue(in.translateYProperty(), height, Interpolator.EASE_BOTH),
				new KeyValue(out.translateYProperty(), 0, Interpolator.EASE_BOTH));
		final KeyFrame end = new KeyFrame(Duration.millis(Transition.ANIM_DURATION),
				new KeyValue(in.translateYProperty(), 0, Interpolator.EASE_BOTH),
				new KeyValue(out.translateYProperty(), -height, Interpolator.EASE_BOTH));
		return new Timeline(start, end);
	}

	private static Timeline slideDownBothTransition(final double height, final Parent in, final Parent out){
		final KeyFrame start = new KeyFrame(Duration.ZERO,
				new KeyValue(in.translateYProperty(), -height, Interpolator.EASE_BOTH),
				new KeyValue(out.translateYProperty(), 0, Interpolator.EASE_BOTH));
		final KeyFrame end = new KeyFrame(Duration.millis(Transition.ANIM_DURATION),
				new KeyValue(in.translateYProperty(), 0, Interpolator.EASE_BOTH),
				new KeyValue(out.translateYProperty(), height, Interpolator.EASE_BOTH));
		return new Timeline(start, end);
	}

}
