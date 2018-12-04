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

package com.kilo52.icecrusher.io;

import com.kilo52.common.struct.CharColumn;
import com.kilo52.common.struct.Column;
import com.kilo52.common.struct.DataFrame;
import com.kilo52.common.struct.NullableCharColumn;

/**
 * Utility class which provides methods to manipulate DataFrames
 *
 */
public class DataFrames {

	private DataFrames() { }
	
	/**
	 * Sets all column names of the provided <code>DataFrame</code> to their
	 * default value, which is the index of that column as a string
	 * 
	 * @param df The DataFrame to change
	 */
	public static void setDefaultColumnNames(final DataFrame df){
		final String[] names = new String[df.columns()];
		for(int i=0; i<names.length; ++i){
			names[i] = String.valueOf(i);
		}
		df.setColumnNames(names);
	}

	/**
	 * Changes all entries in all columns of the provided <code>DataFrame</code> to
	 * a default value, if and only if, that entry cannot be handled by this implementation
	 * of this application or <code>DataFrameView</code>.<br><br>
	 * <b><u>IMPLEMENTATION NOTE:</u></b><br>
	 * Currently, all DataFrames must have column names set. If a deserialized DataFrame has
	 * not set any column names, default values will be added to it.<br>
	 * NUL characters in any CharColumn instance cannot be handled and must be replaced.
	 * As of this implementation, a NUL character will be replaced by a dash
	 * 
	 * @param df The DataFrame to modify
	 * @return The sanitized DataFrame instance
	 */
	public static DataFrame sanitize(final DataFrame df){
		if((!df.hasColumnNames()) && (df.columns() != 0)){
			setDefaultColumnNames(df);
		}
		for(final Column col : df){
			if(col instanceof CharColumn){
				final CharColumn chars = (CharColumn)col;
				for(int i=0; i<df.rows(); ++i){
					if(chars.get(i) == '\u0000'){
						chars.set(i, '-');
					}
				}
			}else if(col instanceof NullableCharColumn){
				final NullableCharColumn chars = (NullableCharColumn)col;
				for(int i=0; i<df.rows(); ++i){
					if(chars.get(i) == '\u0000'){
						chars.set(i, '-');
					}
				}
			}
		}
		return df;
	}

}
