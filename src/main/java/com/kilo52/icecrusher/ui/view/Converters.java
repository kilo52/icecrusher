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

import javafx.util.StringConverter;

/**
 * Utility class providing string converters for DataFrameView table cells. <br>
 * A converter defines conversion behaviour between strings and objects.
 * This class cannot be instantiated. Use one of the <code>*Converter()</code> methods
 * instead.
 *
 */
public final class Converters {
	
	private static Converter byteC = new ByteConverter();
	private static Converter shortC = new ShortConverter();
	private static Converter intC = new IntConverter();
	private static Converter longC = new LongConverter();
	private static Converter stringC = new SimpleStringConverter();
	private static Converter floatC = new FloatConverter();
	private static Converter doubleC = new DoubleConverter();
	private static Converter charC = new CharConverter();
	private static Converter booleanC = new BooleanConverter();

	private Converters() { }
	
	/**
	 * Returns a reference to a converter responsible for handling byte values
	 * 
	 * @return A ByteConverter
	 */
	public static Converter byteConverter(){
		return byteC;
	}

	/**
	 * Returns a reference to a converter responsible for handling short values
	 * 
	 * @return A ShortConverter
	 */
	public static Converter shortConverter(){
		return shortC;
	}

	/**
	 * Returns a reference to a converter responsible for handling int values
	 * 
	 * @return An IntConverter
	 */
	public static Converter intConverter(){
		return intC;
	}

	/**
	 * Returns a reference to a converter responsible for handling long values
	 * 
	 * @return A LongConverter
	 */
	public static Converter longConverter(){
		return longC;
	}

	/**
	 * Returns a reference to a converter responsible for handling string values
	 * 
	 * @return A SimpleStringConverter
	 */
	public static Converter stringConverter(){
		return stringC;
	}

	/**
	 * Returns a reference to a converter responsible for handling float values
	 * 
	 * @return A FloatConverter
	 */
	public static Converter floatConverter(){
		return floatC;
	}

	/**
	 * Returns a reference to a converter responsible for handling double values
	 * 
	 * @return A DoubleConverter
	 */
	public static Converter doubleConverter(){
		return doubleC;
	}

	/**
	 * Returns a reference to a converter responsible for handling char values
	 * 
	 * @return A CharConverter
	 */
	public static Converter charConverter(){
		return charC;
	}

	/**
	 * Returns a reference to a converter responsible for handling boolean values
	 * 
	 * @return A BooleanConverter
	 */
	public static Converter booleanConverter(){
		return booleanC;
	}
	
	/**
	 * Abstract class for all converters. A converter defines conversion behaviour between 
	 * strings and objects. Concrete implementations must override 
	 * <code>toString()</code> and <code>fromString()</code><br>
	 *
	 */
	public static abstract class Converter extends StringConverter<Object> {
		@Override
		public String toString(final Object obj){
			return (obj != null ? obj.toString() : "");
		}
	}
	
	private static class ByteConverter extends Converter {
		@Override
		public Object fromString(final String string){
			if(!string.isEmpty()){
				if((string.length() == 1) && (string.startsWith("-") || string.startsWith("+"))){
					return (byte)0;
				}
				return Byte.valueOf(string);
			}
			return null;
		}
	}
	
	private static class ShortConverter extends Converter {
		@Override
		public Object fromString(final String string){
			if(!string.isEmpty()){
				if((string.length() == 1) && (string.startsWith("-") || string.startsWith("+"))){
					return (short)0;
				}
				return Short.valueOf(string);
			}
			return null;
		}
	}
	
	private static class IntConverter extends Converter {
		@Override
		public Object fromString(final String string){
			if(!string.isEmpty()){
				if((string.length() == 1) && (string.startsWith("-") || string.startsWith("+"))){
					return 0;
				}
				return Integer.valueOf(string);
			}
			return null;
		}
	}
	
	private static class LongConverter extends Converter {
		@Override
		public Object fromString(final String string){
			if(!string.isEmpty()){
				if((string.length() == 1) && (string.startsWith("-") || string.startsWith("+"))){
					return (long)0;
				}
				return Long.valueOf(string);
			}
			return null;
		}
	}
	
	private static class SimpleStringConverter extends Converter {
		@Override
		public Object fromString(final String string){
			return (!string.isEmpty() ? string : null);
		}
	}
	
	private static class FloatConverter extends Converter {
		@Override
		public Object fromString(final String string){
			if(!string.isEmpty()){
				if((string.length() == 1) && (string.startsWith("-") || string.startsWith("+"))){
					return (float)0;
				}
				return Float.valueOf(string);
			}
			return null;
		}
	}
	
	private static class DoubleConverter extends Converter {
		@Override
		public Object fromString(final String string){
			if(!string.isEmpty()){
				if((string.length() == 1) && (string.startsWith("-") || string.startsWith("+"))){
					return (double)0;
				}
				return Double.valueOf(string);
			}
			return null;
		}
	}
	
	private static class CharConverter extends Converter {
		@Override
		public Object fromString(final String string){
			return (!string.isEmpty() ? Character.valueOf(string.charAt(0)) : null);
		}
	}
	
	private static class BooleanConverter extends Converter {
		@Override
		public String toString(final Object obj){
			return (obj != null ? obj.toString() : "null");
		}
		@Override
		public Object fromString(final String string){
			return (!string.isEmpty() && !string.equalsIgnoreCase("null") 
								? Boolean.valueOf(string) 
								: null);
		}
	}
}
