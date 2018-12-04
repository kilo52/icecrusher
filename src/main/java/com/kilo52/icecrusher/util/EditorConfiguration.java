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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import com.kilo52.common.io.ConfigurationFile;
import com.kilo52.common.io.ConfigurationFileHandler;

/**
 * Handles the entire configuration and all configurable properties, as
 * well as the persisting of those configurations to the local filesystem.<br>
 * This class cannot be instantiated directly. Use <code>getInstance()</code> to
 * obtain a reference to an instance of this class.
 *
 */
public class EditorConfiguration {
	
	/**
	 * Enum listing all sections used in the configuration file.
	 *
	 */
	public enum Section {
		
		GLOBAL("Global"),
		WINDOW("Window"),
		UPDATER("Updater");
		
		private String key;
		
		Section(String key){
			this.key = key;
		}
	}
	
	/** Directory for user specific configuration files **/
	private static final String CONFIG_DIR = System.getProperty(Const.KEY_USER_HOME)
			+"/.config/"+Const.APPLICATION_NAME.toLowerCase()+"/";
	
	public static final String CONFIG_RECALL_TABS = "recall.tabs";
	public static final String CONFIG_SHOW_INDEX_COL = "show.index_col";
	public static final String CONFIG_CLEAR_AFTER_ROW_ADD = "clear.after.row_add";
	public static final String CONFIG_CONFIRM_ROW_DELETION = "confirm.row.deletion";
	public static final String CONFIG_DIALOG_ALWAYS_HOME = "dialog.always.at_home";
	
	public static final String CONFIG_WINDOW_WIDTH = "width";
	public static final String CONFIG_WINDOW_HEIGHT = "height";
	public static final String CONFIG_WINDOW_DIALOG_DIR = "dialog.io.dir";
	
	public static final String CONFIG_AUTO_UPDATE_CHECK = "check.auto";
	public static final String CONFIG_LAST_UPDATE_CHECK = "check.last";
	public static final String CONFIG_USE_HTTPS = "use.HTTPS";
	public static final String CONFIG_RELEASE_URL = "release.version.URL";
	public static final String CONFIG_REDIRECT_BROWSER_URL = "redirect.browser.URL";
	public static final String CONFIG_DATA_STALE_THRESHOLD = "data.stale.threshold.days";
	
	private static final String TEMPLATE_FILE = Const.DIR_CONFIGS+"global_template.config";
	private static final String CONFIG_FILE = "editor.config";
	private static final String HISTORY_FILE = "recall";
	
	private static EditorConfiguration instance;
	
	private ConfigurationFileHandler fileHandler;
	private ConfigurationFile config;
	private ConfigurationFile recall;
	private History history;
	private boolean configChanged;

	private EditorConfiguration(){
		final File file = new File(CONFIG_DIR+CONFIG_FILE);
		this.fileHandler = new ConfigurationFileHandler(CONFIG_DIR+CONFIG_FILE);
		try{
			if(!file.exists()){
				this.config = copyTemplate();
			}else{
				this.config = fileHandler.read();
			}
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Gets a reference to an <code>EditorConfiguration</code> instance
	 * 
	 * @return An <code>EditorConfiguration</code> object that can be used to query or 
	 *         change configurations
	 */
	public static EditorConfiguration getConfiguration(){
		if(instance == null){
			instance = new EditorConfiguration();
		}
		return instance;
	}
	
	/**
	 * Returns the value of the configuration in the specified Section with the specified key
	 * 
	 * @param SECTION The <code>Section</code> of the configuration to access
	 * @param CONFIGURATION The key of the configuration to access
	 * @return The value of the configuration with the specified key, or null if the specified 
	 *         section does not hold a configuration with the specified key
	 */
	public String valueOf(final Section SECTION, final String CONFIGURATION){
		return this.config.getSection(SECTION.key).valueOf(CONFIGURATION);
	}
	
	/**
	 * Returns the value of the configuration in the specified Section with the specified key 
	 * directly converted to a boolean
	 * 
	 * @param SECTION The <code>Section</code> of the configuration to access
	 * @param CONFIGURATION The key of the configuration to access
	 * @return The value of the configuration with the specified key. Might return false if the
	 *         configuration accessed is not a boolean or was not found
	 */
	public boolean booleanOf(final Section SECTION, final String CONFIGURATION){
		return Boolean.valueOf(config.getSection(SECTION.key).valueOf(CONFIGURATION));
	}
	
	/**
	 * Sets the value of the configuration in the specified Section with the specified key to the 
	 * specified value
	 * 
	 * @param SECTION The <code>Section</code> of the configuration to access
	 * @param CONFIGURATION The key of the configuration to set
	 * @param value The new value of the configuration to set
	 */
	public void set(final Section SECTION, final String CONFIGURATION, final String value){
		configChanged = true;
		this.config.getSection(SECTION.key).set(CONFIGURATION, value);
	}
	
	/**
	 * Sets the value of the configuration in the specified Section with the specified key to the 
	 * specified boolean value
	 * 
	 * @param SECTION The <code>Section</code> of the configuration to access
	 * @param CONFIGURATION The key of the configuration to set
	 * @param value The new value of the configuration to set
	 */
	public void set(final Section SECTION, final String CONFIGURATION, final boolean value){
		configChanged = true;
		this.config.getSection(SECTION.key).set(CONFIGURATION, String.valueOf(value));
	}
	
	/**
	 * Gets the history object associated with the recall file from the filesystem
	 * 
	 * @return The <code>History</code> object of the recall file, or null if the file
	 *         does not exist or an error occurred
	 */
	public History getHistory(){
		if(recall == null){
			try{
				final File file = new File(CONFIG_DIR+HISTORY_FILE);
				if(file.exists()){
					this.recall = readHistoryFile();
				}else{
					return null;
				}
			}catch(IOException ex){
				ex.printStackTrace();
				return null;
			}
		}
		if(history == null){
			this.history = History.fromFile(recall);
		}
		return this.history;
	}
	
	/**
	 * Sets the history of the EditorConfiguration to the specified <code>History</code>
	 * 
	 * @param history The <code>History</code> object to use
	 */
	public void setHistory(final History history){
		this.history = history;
		this.recall = history.getRecallFile();
	}

	/**
	 * Persists the configuration as is to the local filesystem
	 */
	public void persistConfiguration(){
		if(configChanged){
			try{
				this.fileHandler.write(config);
				configChanged = false;
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Persists the recall file (if set) to the local filesystem
	 */
	public void persistHistory(){
		if(recall != null){
			try{
				new ConfigurationFileHandler(CONFIG_DIR+HISTORY_FILE).write(recall);
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Deletes the recall file from the local filesystem
	 * 
	 * @return True if and only if the recall file was successfully deleted. False otherwise
	 */
	public boolean deleteRecallFile(){
		final File recallFile = new File(CONFIG_DIR+HISTORY_FILE);
		if(recallFile.exists()){
			return recallFile.delete();
		}
		return false;
	}

	private ConfigurationFile copyTemplate() throws IOException{
		ConfigurationFile template = null;
		BufferedInputStream is = null;
		BufferedOutputStream os = null;
		try{
			final File configDir = new File(CONFIG_DIR);
			if(!configDir.exists()){
				if(!configDir.mkdirs()){
					throw new IOException("Failed to create config directory");
				}
			}
			is = new BufferedInputStream(getClass().getResourceAsStream(TEMPLATE_FILE));
			os = new BufferedOutputStream(new FileOutputStream(new File(CONFIG_DIR+CONFIG_FILE)));
			final ByteArrayOutputStream baos = new ByteArrayOutputStream(2048);
			int i = 0;
			while((i = is.read()) != -1){
				baos.write(i);
			}
			os.write(baos.toByteArray());
			os.close();
			os = null;
			template = new ConfigurationFileHandler(CONFIG_DIR+CONFIG_FILE).read();
		}catch(IOException ex){
			throw ex;
		}finally{
			if(is != null){ is.close(); }
			if(os != null){ os.close(); }
		}
		return template;
	}
	
	private ConfigurationFile readHistoryFile() throws IOException{
		return new ConfigurationFileHandler(CONFIG_DIR+HISTORY_FILE).read();
	}

}
