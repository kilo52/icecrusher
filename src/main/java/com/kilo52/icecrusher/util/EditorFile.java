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

import java.io.File;

/**
 * A {@link File} object as it is used by this editor.
 *
 */
public class EditorFile extends File {
	
	private static final long serialVersionUID = 1L;
	
	//DEFAULTS
	private boolean isImported = false;
	private boolean csvHeader = true;
	private char csvSeparator = ',';

	/**
	 * Constructs a new <code>EditorFile</code> from the specified path
	 * 
	 * @param pathname The path of the file
	 */
	public EditorFile(String pathname){
		super(pathname);
	}
	
	/**
	 * Indicates whether this file was imported
	 * 
	 * @return True if this file was imported. False otherwise
	 */
	public boolean isImported(){
		return this.isImported;
	}
	
	/**
	 * Sets this EditorFiles' imported member flag to the specified value
	 * 
	 * @param isImported Set to true if this EditorFile was imported
	 */
	public void setImported(final boolean isImported){
		this.isImported = isImported;
	}
	
	/**
	 * Gets the separator to be used when dealing with a CSV file
	 * 
	 * @return The separator to be used with this EditorFile
	 */
	public char getCSVSeparator(){
		return csvSeparator;
	}
	
	/**
	 * Sets the separator to use when dealing with a CSV file
	 * 
	 * @param csvSeparator The separator to be used
	 */
	public void setCSVSeparator(final char csvSeparator){
		this.csvSeparator = csvSeparator;
	}
	
	/**
	 * Indicates whether this (imported) file has a CSV header
	 * 
	 * @return True if it has a header. False otherwise
	 */
	public boolean hasCSVHeader(){
		return this.csvHeader;
	}
	
	/**
	 * Sets this EditorFiles' header member flag to the specified value
	 * 
	 * @param hasHeader Set to true if this EditorFile was imported and 
	 *                  has a header
	 */
	public void hasCSVHeader(final boolean hasHeader){
		this.csvHeader = hasHeader;
	}
	
	/**
	 * Creates a new <code>EditorFile</code> from the specified <code>File</>
	 * object
	 * 
	 * @param file The File from which to create the EditorFile
	 * @return A new EditorFile instance
	 */
	public static EditorFile fromFile(final File file){
		return (file != null ? new EditorFile(file.getAbsolutePath()) : null);
	}
}
