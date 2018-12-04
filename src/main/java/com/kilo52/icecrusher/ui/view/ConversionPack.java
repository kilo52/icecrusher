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
import com.kilo52.icecrusher.ui.view.Converters.Converter;
import com.kilo52.icecrusher.ui.view.Filters.Filter;

/**
 * Wrapper for holding filters and converters to be used in DataFrameView table cells.
 *
 */
public class ConversionPack {

	private Filter filter;
	private Converter converter;

	public ConversionPack(Filter filter, Converter converter){
		this.filter = filter;
		this.converter = converter;
	}
	
	/**
	 * Gets the filter of this pack
	 * 
	 * @return A reference to a Filter
	 */
	public Filter getFilter() {
		return this.filter;
	}

	/**
	 * Gets the converter of this pack
	 * 
	 * @return A reference to a StringConverter
	 */
	public Converter getConverter() {
		return this.converter;
	}
	
	/**
	 * Creates a <code>ConversionPack</code> holding the appropriate filters and converters for a 
	 * cell used in DataFrameViews. The choice which filters and converters to pick is made according
	 * to the column's type
	 * 
	 * @param col The <code>Column</code> for which to get a ConversionPack
	 * @return The <code>ConversionPack</code> to be used for this Column
	 */
	public static ConversionPack columnConversion(final Column col){
		if(col instanceof NullableColumn){
			switch(col.getClass().getSimpleName()){
			case "NullableStringColumn":
				return new ConversionPack(Filters.stringFilter(true), Converters.stringConverter());
			case "NullableByteColumn":
				return new ConversionPack(Filters.byteFilter(true), Converters.byteConverter());
			case "NullableShortColumn":
				return new ConversionPack(Filters.shortFilter(true), Converters.shortConverter());
			case "NullableIntColumn":
				return new ConversionPack(Filters.intFilter(true), Converters.intConverter());
			case "NullableLongColumn":
				return new ConversionPack(Filters.longFilter(true), Converters.longConverter());
			case "NullableFloatColumn":
				return new ConversionPack(Filters.floatFilter(true), Converters.floatConverter());
			case "NullableDoubleColumn":
				return new ConversionPack(Filters.doubleFilter(true), Converters.doubleConverter());
			case "NullableBooleanColumn":
				return new ConversionPack(Filters.booleanFilter(true), Converters.booleanConverter());
			case "NullableCharColumn":
				return new ConversionPack(Filters.charFilter(true), Converters.charConverter());
			}
		}else{
			switch(col.getClass().getSimpleName()){
			case "StringColumn":
				return new ConversionPack(Filters.stringFilter(false), Converters.stringConverter());
			case "ByteColumn":
				return new ConversionPack(Filters.byteFilter(false), Converters.byteConverter());
			case "ShortColumn":
				return new ConversionPack(Filters.shortFilter(false), Converters.shortConverter());
			case "IntColumn":
				return new ConversionPack(Filters.intFilter(false), Converters.intConverter());
			case "LongColumn":
				return new ConversionPack(Filters.longFilter(false), Converters.longConverter());
			case "FloatColumn":
				return new ConversionPack(Filters.floatFilter(false), Converters.floatConverter());
			case "DoubleColumn":
				return new ConversionPack(Filters.doubleFilter(false), Converters.doubleConverter());
			case "BooleanColumn":
				return new ConversionPack(Filters.booleanFilter(false), Converters.booleanConverter());
			case "CharColumn":
				return new ConversionPack(Filters.charFilter(false), Converters.charConverter());
			}
		}
		return null;
	}
}
