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

import com.kilo52.icecrusher.ui.dialog.FilterDialogController.DialogListener;
import com.kilo52.icecrusher.util.Layout;

import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;

/**
 * A Dialog which lets the user filter a <code>DataFrame</code>.
 *
 */
public class FilterDialog extends EditorDialog {
	
	private FilterDialogController controller;

	public FilterDialog(StackPane root){
		super(root, null);
        final Layout layout = new Layout(Layout.FILTER_DIALOG);
        final Parent parent = layout.load();
        controller = layout.getController();
        controller.setRootContainer(root);
		setContent((Region)parent);
	}
	
	public void setOnFilter(DialogListener listener){
		controller.setFilterListener(listener);
	}
	
	public void setColumns(final String[] columns){
		controller.setColumns(columns);
	}

}
