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
import java.io.IOException;
import java.util.Properties;

import javafx.scene.image.Image;

/**
 * Resources of this project.
 *
 */
public class Resources {
	
	public static final String IC_ICECRUSHER = "icecrusher.png";
	
	private static Properties res;
	
	static {
		res = new Properties();
		try{
			res.load(new BufferedInputStream(Resources.class.getClass().getResourceAsStream(Const.DIR_CONFIGS+"project.properties")));
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
	
	/**
	 * Loads and returns a resource image
	 * 
	 * @param IMAGE The image to load. Should be one of the Resources.IC_*-constants
	 * @return The image loaded
	 * @throws Exception If the image could not be found or loaded
	 */
	public static Image image(final String IMAGE) throws Exception{
		return new Image(Resources.class.getClass().getResourceAsStream(Const.DIR_ICON+IMAGE));
	}
	
	/**
	 * Returns a project property value
	 * 
	 * @param key The key of the property to get
	 * @return The property value associated with the specified key, or null 
	 *         if the specified property is not set
	 */
	public static String property(final String key){
		return res.getProperty(key);
	}

}
