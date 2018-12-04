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

package com.kilo52.icecrusher.ui.view;

import com.kilo52.common.struct.Column;
import com.kilo52.common.struct.NullableColumn;

import javafx.scene.control.Tooltip;


/**
 * Utility class providing tooltips for DataFrameView table cells. <br>
 * This class cannot be instantiated. Use one of the <code>*Tooltip()</code> methods
 * instead.
 *
 */
public class Tooltips {
	
	private static Tooltip byteTooltip = new Tooltip("byte");
	private static Tooltip shortTooltip = new Tooltip("short");
	private static Tooltip intTooltip = new Tooltip("int");
	private static Tooltip longTooltip = new Tooltip("long");
	private static Tooltip stringTooltip = new Tooltip("string");
	private static Tooltip floatTooltip = new Tooltip("float");
	private static Tooltip doubleTooltip = new Tooltip("double");
	private static Tooltip charTooltip = new Tooltip("character");
	private static Tooltip booleanTooltip = new Tooltip("boolean");

	private Tooltips(){ }

	/**
	 * Returns a reference to a Tooltip responsible for byte columns
	 * 
	 * @return A Tooltip
	 */
	public static Tooltip byteTooltip() {
		return byteTooltip;
	}

	/**
	 * Returns a reference to a Tooltip responsible for short columns
	 * 
	 * @return A Tooltip
	 */
	public static Tooltip shortTooltip() {
		return shortTooltip;
	}

	/**
	 * Returns a reference to a Tooltip responsible for int columns
	 * 
	 * @return A Tooltip
	 */
	public static Tooltip intTooltip() {
		return intTooltip;
	}

	/**
	 * Returns a reference to a Tooltip responsible for long columns
	 * 
	 * @return A Tooltip
	 */
	public static Tooltip longTooltip() {
		return longTooltip;
	}

	/**
	 * Returns a reference to a Tooltip responsible for string columns
	 * 
	 * @return A Tooltip
	 */
	public static Tooltip stringTooltip() {
		return stringTooltip;
	}

	/**
	 * Returns a reference to a Tooltip responsible for float columns
	 * 
	 * @return A Tooltip
	 */
	public static Tooltip floatTooltip() {
		return floatTooltip;
	}

	/**
	 * Returns a reference to a Tooltip responsible for double columns
	 * 
	 * @return A Tooltip
	 */
	public static Tooltip doubleTooltip() {
		return doubleTooltip;
	}

	/**
	 * Returns a reference to a Tooltip responsible for character columns
	 * 
	 * @return A Tooltip
	 */
	public static Tooltip charTooltip() {
		return charTooltip;
	}

	/**
	 * Returns a reference to a Tooltip responsible for boolean columns
	 * 
	 * @return A Tooltip
	 */
	public static Tooltip booleanTooltip() {
		return booleanTooltip;
	}
	
	/**
	 * Returns the appropriate <code>Tooltip</code> object for the specified column
	 * 
	 * @param col The <code>Column</code> object to get a Tooltip for
	 * @return The <code>Tooltip</code> to be used with the specified column
	 */
	public static Tooltip columnTooltip(final Column col){
		if(col instanceof NullableColumn){
			switch(col.getClass().getSimpleName()){
			case "NullableStringColumn":
				return stringTooltip();
			case "NullableByteColumn":
				return byteTooltip();
			case "NullableShortColumn":
				return shortTooltip();
			case "NullableIntColumn":
				return intTooltip();
			case "NullableLongColumn":
				return longTooltip();
			case "NullableFloatColumn":
				return floatTooltip();
			case "NullableDoubleColumn":
				return doubleTooltip();
			case "NullableBooleanColumn":
				return booleanTooltip();
			case "NullableCharColumn":
				return charTooltip();
			}
		}else{
			switch(col.getClass().getSimpleName()){
			case "StringColumn":
				return stringTooltip();
			case "ByteColumn":
				return byteTooltip();
			case "ShortColumn":
				return shortTooltip();
			case "IntColumn":
				return intTooltip();
			case "LongColumn":
				return longTooltip();
			case "FloatColumn":
				return floatTooltip();
			case "DoubleColumn":
				return doubleTooltip();
			case "BooleanColumn":
				return booleanTooltip();
			case "CharColumn":
				return charTooltip();
			}
		}
		return null;
	}

}
