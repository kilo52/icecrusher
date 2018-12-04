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

import java.util.List;

import com.kilo52.common.struct.DataFrame;

/**
 * Functional interface defining a callback for concurrently 
 * reading a List of files and representing the result of the
 * background operation as a List of DataFrames.
 *
 */
public interface ConcurrentBulkReader {

	/**
	 * Called when a concurrent reading operation has finished, 
	 * passing the result in form of a List of DataFrames as an 
	 * argument to this method
	 * 
	 * @param dataFrames The List holding all DataFrames read
	 */
	void onRead(List<DataFrame> dataFrames);
}
