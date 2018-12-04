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

import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;
import javafx.scene.control.TextFormatter.Change;

/**
 * Utility class providing filters for DataFrameView table cells. <br>
 * This class cannot be instantiated. Use one of the <code>*Filter()</code> methods
 * instead.
 *
 */
public final class Filters {
	
	/**
	 * Interface all filters of DataFrameViews must imlement.
	 *
	 */
	public interface Filter extends UnaryOperator<TextFormatter.Change> { }
	
	private static Filter byteF = new ByteFilter();
	private static Filter shortF = new ShortFilter();
	private static Filter intF = new IntFilter();
	private static Filter longF = new LongFilter();
	private static Filter stringF = new StringFilter();
	private static Filter floatF = new FloatFilter();
	private static Filter doubleF = new DoubleFilter();
	private static Filter charF = new CharFilter();
	private static Filter booleanF = new BooleanFilter();
	
	private static Filter nullByteF = new NullByteFilter();
	private static Filter nullShortF = new NullShortFilter();
	private static Filter nullIntF = new NullIntFilter();
	private static Filter nullLongF = new NullLongFilter();
	private static Filter nullStringF = new NullStringFilter();
	private static Filter nullFloatF = new NullFloatFilter();
	private static Filter nullDoubleF = new NullDoubleFilter();
	private static Filter nullCharF = new NullCharFilter();
	private static Filter nullBooleanF = new NullBooleanFilter();

	private Filters(){ }
	
	/**
	 * Returns a reference to a Filter responsible for handling byte values
	 * 
	 * @param nullable Specifies whether the returned filter should accept null 
	 * 		  values as valid input
	 * @return A ByteFilter or NullByteFilter respectively
	 */
	public static Filter byteFilter(final boolean nullable){
		return (nullable ? nullByteF : byteF);
	}
	
	/**
	 * Returns a reference to a Filter responsible for handling short values
	 * 
	 * @param nullable Specifies whether the returned filter should accept null 
	 * 		  values as valid input
	 * @return A ShortFilter or NullShortFilter respectively
	 */
	public static Filter shortFilter(final boolean nullable){
		return (nullable ? nullShortF : shortF);
	}
	
	/**
	 * Returns a reference to a Filter responsible for handling int values
	 * 
	 * @param nullable Specifies whether the returned filter should accept null 
	 * 		  values as valid input
	 * @return An IntFilter or NullIntFilter respectively
	 */
	public static Filter intFilter(final boolean nullable){
		return (nullable ? nullIntF : intF);
	}
	
	/**
	 * Returns a reference to a Filter responsible for handling long values
	 * 
	 * @param nullable Specifies whether the returned filter should accept null 
	 * 		  values as valid input
	 * @return A LongFilter or NullLongFilter respectively
	 */
	public static Filter longFilter(final boolean nullable){
		return (nullable ? nullLongF : longF);
	}
	
	/**
	 * Returns a reference to a Filter responsible for handling string values.
	 * This is just an implementation for general compatibility. The returned filter
	 * will let through any string
	 * 
	 * @param nullable Specifies whether the returned filter should accept null 
	 * 		  values or empty strings as valid input
	 * @return A StringFilter or NullStringFilter respectively
	 */
	public static Filter stringFilter(final boolean nullable){
		return (nullable ? nullStringF : stringF);
	}
	
	/**
	 * Returns a reference to a Filter responsible for handling float values
	 * 
	 * @param nullable Specifies whether the returned filter should accept null 
	 * 		  values as valid input
	 * @return A FloatFilter or NullFloatFilter respectively
	 */
	public static Filter floatFilter(final boolean nullable){
		return (nullable ? nullFloatF : floatF);
	}
	
	/**
	 * Returns a reference to a Filter responsible for handling double values
	 * 
	 * @param nullable Specifies whether the returned filter should accept null 
	 * 		  values as valid input
	 * @return A DoubleFilter or NullDoubleFilter respectively
	 */
	public static Filter doubleFilter(final boolean nullable){
		return (nullable ? nullDoubleF : doubleF);
	}
	
	/**
	 * Returns a reference to a Filter responsible for handling char values
	 * 
	 * @param nullable Specifies whether the returned filter should accept null 
	 * 		  values as valid input
	 * @return A CharFilter or NullCharFilter respectively
	 */
	public static Filter charFilter(final boolean nullable){
		return (nullable ? nullCharF : charF);
	}
	
	/**
	 * Returns a reference to a Filter responsible for handling boolean values
	 * 
	 * @param nullable Specifies whether the returned filter should accept null 
	 * 		  values as valid input
	 * @return A BooleanFilter or NullBooleanFilter respectively
	 */
	public static Filter booleanFilter(final boolean nullable){
		return (nullable ? nullBooleanF : booleanF);
	}
	
	/*********************************************************
	 * Default filters prohibiting the use of null values    *
	 *********************************************************/
	
	private static class ByteFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Byte.valueOf(s);
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class ShortFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Short.valueOf(s);
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class IntFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Integer.valueOf(s);
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class LongFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Long.valueOf(s);
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class StringFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if(s.isEmpty()){
				return null;
			}else{
				return t;
			}
		}
	}
	
	private static class FloatFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Float.valueOf(s);
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class DoubleFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Double.valueOf(s);
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class CharFilter implements Filter {
		@Override
		public Change apply(Change t){
			return (t.getControlNewText().length() == 1 ? t : null);
		}
	}
	
	private static class BooleanFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if(s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")){
				return t;
			}else{
				return null;
			}
		}
	}
	
	/*******************************************
	 * Filters accepting null values           *
	 *******************************************/
	
	private static class NullByteFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if(s == null || s.isEmpty()){
				return t;
			}
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Byte.valueOf(t.getControlNewText());
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class NullShortFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if(s == null || s.isEmpty()){
				return t;
			}
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Short.valueOf(t.getControlNewText());
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class NullIntFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if(s == null || s.isEmpty()){
				return t;
			}
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Integer.valueOf(t.getControlNewText());
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class NullLongFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if(s == null || s.isEmpty()){
				return t;
			}
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Long.valueOf(t.getControlNewText());
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class NullStringFilter implements Filter {
		@Override
		public Change apply(Change t){
			return t;//accept anything
		}
	}
	
	private static class NullFloatFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if(s == null || s.isEmpty()){
				return t;
			}
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Float.valueOf(t.getControlNewText());
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class NullDoubleFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if(s == null || s.isEmpty()){
				return t;
			}
			if((s.length() == 1) && (s.startsWith("-") || s.startsWith("+"))){
				return t;
			}
			try{
				Double.valueOf(t.getControlNewText());
				return t;
			}catch(NumberFormatException ex){
				return null;
			}
		}
	}
	
	private static class NullCharFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if(s == null || s.isEmpty()){
				return t;
			}
			return (t.getControlNewText().length() == 1 ? t : null);
		}
	}
	
	private static class NullBooleanFilter implements Filter {
		@Override
		public Change apply(Change t){
			final String s = t.getControlNewText();
			if(s == null || s.isEmpty() || s.equalsIgnoreCase("true") || s.equalsIgnoreCase("false")){
				return t;
			}else{
				return null;
			}
		}
	}

}
