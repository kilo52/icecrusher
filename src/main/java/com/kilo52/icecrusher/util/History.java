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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.kilo52.common.io.ConfigurationFile;
import com.kilo52.common.io.ConfigurationFile.Section;

/**
 * Class responsible for handling recall files and to construct a usable
 * history to recover persistent states.
 *
 */
public class History {
	
	private static final String HISTORY_SECTION_META = "/-RECALL-/";
	private static final String HISTORY_KEY_TIMESTAMP = "since";
	private static final String HISTORY_KEY_FOCUS = "focus";
	private static final String HISTORY_KEY_PATH = "path";
	private static final String HISTORY_KEY_IMPORTED = "imported";
	private static final String HISTORY_KEY_HEADER = "header";
	private static final String HISTORY_KEY_SEPARATOR = "separator";
	
	private ConfigurationFile recallFile;
	private List<EditorFile> historyList;

	/**
	 * Constructs a new <code>History</code> object with the standard header section.<br>
	 * The History object is otherwise empty
	 * 
	 */
	public History(){
		this.recallFile = new ConfigurationFile();
		final Section meta = new Section(HISTORY_SECTION_META);
		meta.set(HISTORY_KEY_TIMESTAMP, Const.DATE_FORMAT.format(new Date()));
		this.recallFile.addSection(meta);
	}
	
	protected History(final ConfigurationFile file){
		this.recallFile = file;
	}

	/**
	 * Gets the list of previously used <code>EditorFile</code> objects
	 * 
	 * @return A <code>List</code> holding the EditorFile objects of this history
	 */
	public List<EditorFile> getHistoryList(){
		if(historyList == null){
			final Iterator<Section> iter = recallFile.iterator();
			if(iter.hasNext()){
				iter.next();//unused. Ignore meta section
			}
			final List<EditorFile> list = new ArrayList<>();
			while(iter.hasNext()){
				final Section section = iter.next();
				final EditorFile file = new EditorFile(section.valueOf(HISTORY_KEY_PATH));
				final boolean wasImported = Boolean.valueOf(section.valueOf(HISTORY_KEY_IMPORTED));
				file.setImported(wasImported);
				if(wasImported){
					file.hasCSVHeader(Boolean.valueOf(section.valueOf(HISTORY_KEY_HEADER)));
					file.setCSVSeparator(section.valueOf(HISTORY_KEY_SEPARATOR).charAt(0));
				}
				list.add(file);
			}
			this.historyList = list;
		}
		return this.historyList;
	}

	/**
	 * Sets the list of EditorFiles previously used. They will be set as the history
	 * 
	 * @param historyList A <code>List</code> holding all EditorFile objects to be used
	 *                    by this History
	 */
	public void setHistoryList(final List<EditorFile> historyList){
		this.historyList = historyList;
		final ConfigurationFile newRecall = new ConfigurationFile();
		final Section meta = new Section(HISTORY_SECTION_META);
		meta.set(HISTORY_KEY_TIMESTAMP, Const.DATE_FORMAT.format(new Date()));
		//copy focus index value
		meta.set(HISTORY_KEY_FOCUS, this.recallFile.getSection(HISTORY_SECTION_META).valueOf(HISTORY_KEY_FOCUS));
		newRecall.addSection(meta);
		for(final EditorFile file : historyList){
			final Section section = new Section(file.getName());
			section.set(HISTORY_KEY_PATH, file.getAbsolutePath());
			final boolean wasImported = file.isImported();
			section.set(HISTORY_KEY_IMPORTED, String.valueOf(wasImported));
			if(wasImported){
				section.set(HISTORY_KEY_HEADER, String.valueOf(file.hasCSVHeader()));
				section.set(HISTORY_KEY_SEPARATOR, String.valueOf(file.getCSVSeparator()));
			}
			newRecall.addSection(section);
		}
		this.recallFile = newRecall;
	}

	/**
	 * Gets the timestamp of this Histsory 
	 * 
	 * @return A string representing the timestamp of this History
	 */
	public String getTimestamp(){
		return this.recallFile.getSection(HISTORY_SECTION_META).valueOf(HISTORY_KEY_TIMESTAMP);
	}

	/**
	 * Sets the timestamp of this Histsory 
	 * 
	 * @param timestamp The timestamp of this History
	 */
	public void setTimestamp(final String timestamp){
		this.recallFile.getSection(HISTORY_SECTION_META).set(HISTORY_KEY_TIMESTAMP, timestamp);
	}

	/**
	 * Gets the index of the focus from the point when the history was created
	 * 
	 * @return The index of the focus of this history
	 */
	public int getFocusIndex(){
		return Integer.valueOf(recallFile.getSection(HISTORY_SECTION_META).valueOf(HISTORY_KEY_FOCUS));
	}

	/**
	 * Sets the index of the focus of this history
	 * 
	 * @param focusIndex The index of the focus to be used in this history
	 */
	public void setFocusIndex(final int focusIndex){
		this.recallFile.getSection(HISTORY_SECTION_META).set(HISTORY_KEY_FOCUS, String.valueOf(focusIndex));
	}
	
	protected ConfigurationFile getRecallFile(){
		return this.recallFile;
	}
	
	protected static History fromFile(final ConfigurationFile file){
		return new History(file);
	}

}
