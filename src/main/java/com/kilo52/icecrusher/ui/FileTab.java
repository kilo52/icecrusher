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

import com.kilo52.common.struct.DataFrame;
import com.kilo52.icecrusher.ui.view.DataFrameView;
import com.kilo52.icecrusher.util.EditorFile;

import javafx.scene.control.Tab;
import javafx.scene.control.Tooltip;

import static com.kilo52.icecrusher.util.EditorConfiguration.Section.*;
import static com.kilo52.icecrusher.util.EditorConfiguration.*;

/**
 * A <code>Tab</code> containing a <code>DataFrameView</code> and an <code>EditorFile</code>.<br>
 *
 */
public class FileTab extends Tab {
	
	private EditorFile file;
	private DataFrame df;
	private DataFrameView view;
	private boolean isSave = true;

	/**
	 * Constructs a new <code>FileTab</code> from the specified EditorFile and DataFrame
	 * 
	 * @param file The <code>EditorFile</code> of this Tab
	 * @param df The <code>DataFrame</code> used to fill the DataFrameView inside the created Tab
	 */
	public FileTab(EditorFile file, DataFrame df){
		this.file = file;
		this.df = df;
		if(file != null){
			setTooltip(new Tooltip(file.getAbsolutePath()));
		}
		this.view = new DataFrameView(df, getConfiguration().booleanOf(GLOBAL, CONFIG_SHOW_INDEX_COL));
		setContent(this.view);
	}
	
	/**
	 * Gets the DataFrameView of this Tab
	 * 
	 * @return The <code>DataFrameView</code> inside this Tab
	 */
	public DataFrameView getView(){
		return this.view;
	}

	/**
	 * Gets the file of this Tab
	 * 
	 * @return The <code>EditorFile</code> used in this Tab
	 */
	public EditorFile getFile(){
		return this.file;
	}
	
	/**
	 * Sets the file to be associated by this Tab
	 * 
	 * @param file The <code>EditorFile</code> to be used by this Tab
	 */
	public void setFile(final EditorFile file){
		this.file = file;
		this.setText(file.getName());
		if(file != null){
			setTooltip(new Tooltip(file.getAbsolutePath()));
		}
	}

	/**
	 * Gets the DataFrame of this Tab
	 * 
	 * @return The <code>DataFrame</code> inside this Tab
	 */
	public DataFrame getDataFrame(){
		return this.df;
	}
	
	/**
	 * Replaces the DataFrame of this Tab
	 * 
	 * @param df The <code>DataFrame</code> to be used by this Tab
	 */
	public void replaceWith(final DataFrame df){
		this.df = df;
		this.view.setDataFrame(df);
	}

	/**
	 * Indicates whether this tab has unsaved changes
	 * 
	 * @return True if this tab has no unsaved changes. 
	 *         False if it has unsaved changes
	 */
	public boolean isSaved(){
		return this.isSave;
	}

	/**
	 * Specifies whether this Tab has unsaved changes
	 * 
	 * @param isSaved Value indicating whether this Tab has unsaved changes
	 */
	public void setSaved(final boolean isSaved) {
		this.isSave = isSaved;
	}

}
