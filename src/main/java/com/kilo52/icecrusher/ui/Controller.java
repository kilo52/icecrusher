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

import java.util.HashMap;
import java.util.Map;

import com.kilo52.icecrusher.util.Layout;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;

/**
 * Base class for all controllers. Subclasses can, but don't have to, override methods of the
 * activity lifecycle. The order in which an activity is initialized is:<br>
 *  - The activity's constructor<br>
 *  - The <code>initialize()</code> method as used by the <code>FXMLLoader</code> class<br>
 *  - The <code>onStart()</code> method<br>
 * <p>Activities can hand around data to each other by passing <code>ArgumentBundle</code>
 * objects to the <code>startActivity()</code> and <code>finishActivity()</code> methods
 * when appropriate. 
 *
 */
public abstract class Controller {
	
	/**
	 * Called when the activity starts. If the parent activity passed any arguments
	 * to the invoked activity, the latter can access them via the 
	 * <code>ArgumentBundle</code> passed to this method
	 * 
	 * @param bundle The arguments passed to this activity at startup
	 */
	public void onStart(ArgumentBundle bundle){ }
	
	/**
	 * Called when the activity stops, causing it to be removed from the top of 
	 * the activity-stack
	 */
	public void onStop(){ }
	
	/**
	 * Called when all other activities on the activity-stack finished, causing this activity
	 * to resume its operation while being brought to the foreground again. If the stopped 
	 * activity passed any arguments to this one, you may access them via the 
	 * <code>ArgumentBundle</code> passed to this method
	 * 
	 * @param bundle The arguments passed to this activity when resuming
	 */
	public void onResume(ArgumentBundle bundle){ }
	
	/**
	 * Called when another activity starts while this activity is still in the foreground,
	 * causing it to pause while the started activity is still running
	 */
	public void onPause(){ }
	
	/**
	 * Called when exit, i.e. the shutdown of this application, has been requested by a 
	 * component or user. This will cause the activity which is at that point in the 
	 * foreground (on top of the activity-stack) to receive the request via this method. <br>
	 * A boolean is returned by this method to indicate whether to <b>continue</b> the exit
	 * command or to abort. The default behaviour simply returns <code>true</code>, causing
	 * this application to terminate.<br>
	 * Subclasses of <code>Controller</code> may override this method to implement a custom
	 * exit behaviour
	 * 
	 * @return true if this activity wants to signal that the application can continue to
	 *         shut itself down
	 */
	public boolean onExitRequested(){ return true; }
	
	/**
	 * Gets the main stage of this application
	 * 
	 * @return A reference to the main <code>Stage</code> of this application
	 */
	protected final Stage getStage(){
		return StackedApplication.getMainStage();
	}
	
	/**
	 * Gets the root node of the scene graph of this application
	 * 
	 * @return A reference to the root <code>Pane</code> of this application
	 */
	protected final Pane getRootNode(){
		return StackedApplication.getRootPane();
	}
	
	/**
	 * Starts the specified activity and puts it on top of the activity-stack.<br>
	 * This call will cause this activity's <code>onPause()</code> method to be called, followed by
	 * the <code>onStart()</code> method of the target activity
	 * 
	 * @param activity The activity to start
	 */
	protected final void startActivity(Layout activity){
		StackedApplication.startActivity(activity);
	}
	
	/**
	 * Starts the specified activity and puts it on top of the activity-stack and passes it the 
	 * specified arguments.<br>
	 * This call will cause this activity's <code>onPause()</code> method to be called, followed by
	 * the <code>onStart()</code> method of the target activity
	 * 
	 * @param activity The activity to start
	 * @param args The arguments to be passed over to the target activity
	 */
	protected final void startActivity(Layout activity, ArgumentBundle args){
		StackedApplication.startActivity(activity, args);
	}
	
	/**
	 * Finishes this activity and removes it from the top of the activity-stack.<br>
	 * This call will cause this activity's <code>onStop()</code> method to be called, followed by
	 * the <code>onResume()</code> method of the activity that is residing under the removed entry 
	 * in the activity-stack
	 * 
	 */
	protected final void finishActivity(){
		StackedApplication.finishActivity();
	}
	
	/**
	 * Finishes this activity and removes it from the top of the activity-stack.<br>
	 * This call will cause this activity's <code>onStop()</code> method to be called, followed by
	 * the <code>onResume()</code> method of the activity that is residing under the removed entry 
	 * in the activity-stack.<br>
	 * The arguments specified will be passed to the resumed activity
	 * 
	 * @param args The arguments to be passed over to the resumed activity
	 */
	protected final void finishActivity(ArgumentBundle args){
		StackedApplication.finishActivity(args);
	}

	/**
	 * A bundle holding arguments passed between activities.<br>
	 * Each argument is represented by a key-value-pair and must be set and accessed that way.
	 *
	 */
	public static class ArgumentBundle {

		private Map<String, Object> map;
		private String finishedController;

		/**
		 * Constructs a new <code>ArgumentBundle</code> without any arguments set
		 */
		public ArgumentBundle(){
			this.map = new HashMap<>();
		}

		/**
		 * Adds and possibly replaces an argument in this bundle
		 * 
		 * @param key The key of the argument to add to this bundle
		 * @param value The object to add to this argument key
		 */
		public void addArgument(final String key, final Object value){
			this.map.put(key, value);
		}

		/**
		 * Gets the argument with the specified key from this bundle
		 * 
		 * @param key The key of the argument to get
		 * @return The argument value, as an Object, or null if no value was set for 
		 *         the specified key
		 */
		public Object getArgument(final String key){
			return this.map.get(key);
		}

		public void setFinishedController(final String previous){
			this.finishedController = previous;
		}

		public String getFinishedController(){
			return this.finishedController;
		}

	}
}
