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

package com.kilo52.icecrusher.net;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import javax.net.ssl.HttpsURLConnection;
import javax.swing.SwingUtilities;

import com.kilo52.icecrusher.util.Const;
import com.kilo52.icecrusher.util.EditorConfiguration;

import javafx.application.Platform;
import javafx.concurrent.Task;

import static com.kilo52.icecrusher.util.EditorConfiguration.Section.*;
import static com.kilo52.icecrusher.util.EditorConfiguration.*;

/**
 * This class is responsible to check for updates and show new versions of this
 * application in the user's default web browser.
 *
 */
public class Updater {
	
	public interface UpdateHandler {
		
		void onResolve(Version version);
	}
	
	private Version version;
	private UpdateHandler handler;

	public Updater(){ }

	public void checkForUpdates(final UpdateHandler handler){
		this.handler = handler;
		final Task<Void> task = new Task<Void>(){
			@Override
			protected Void call() throws Exception{
				HttpURLConnection connection = null;
				try{
					final String url = getConfiguration().valueOf(UPDATER, CONFIG_RELEASE_URL);
					if(url.startsWith("https")){
						connection = (HttpsURLConnection) new URL(url).openConnection();
					}else{
						connection = (HttpURLConnection) new URL(url).openConnection();
					}
					connection.setRequestMethod("GET");
					connection.setRequestProperty("Accept-Language", "en-US,en,q=0.5");
					connection.setRequestProperty("Accept-Encoding", "UTF-8");
					connection.setConnectTimeout(10000);
					final StringBuilder sb = new StringBuilder();
					if(connection.getResponseCode() == HttpURLConnection.HTTP_OK){
						final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String line;
						while((line = reader.readLine()) != null){
							sb.append(line);
						}
						reader.close();
					}
					final String[] s = sb.toString().split("\\.", 3);
					int major = 0;
					int minor = 0;
					int patch = 0;
					if(s.length >= 3){
						major = Integer.valueOf(s[0]);
						minor = Integer.valueOf(s[1]);
						patch = Integer.valueOf(s[2].replaceAll("[^\\d.]", ""));
					}
					version = new Version(major, minor, patch);
					push(version);
					getConfiguration().set(UPDATER, CONFIG_LAST_UPDATE_CHECK, Const.DATE_FORMAT_ENCODED.format(new Date()));
				}catch(Exception ex){
					ex.printStackTrace();
					push(null);
				}finally{
					if(connection != null){
						connection.disconnect();
					}
				}
				return null;
			}
		};
		new Thread(task).start();
	}
	
	public boolean dataIsStale(){
		final EditorConfiguration config = getConfiguration();
		final String last = config.valueOf(UPDATER, CONFIG_LAST_UPDATE_CHECK);
		if(last.matches("\\d+")){
			final int nowEncoded = Integer.valueOf(Const.DATE_FORMAT_ENCODED.format(new Date()));
			final int lastEncoded = Integer.valueOf(last);
			final int threshold = Integer.valueOf(config.valueOf(UPDATER, CONFIG_DATA_STALE_THRESHOLD));
			return ((nowEncoded - lastEncoded) >= threshold);
		}
		return true;
	}
	
	public static void showInBrowser(){
		//using swing utilities instead of javafx.application.HostServices here
		//because former has had some issues
		SwingUtilities.invokeLater(() -> {
			try{
				if(Desktop.isDesktopSupported()){
					Desktop.getDesktop().browse(new URI(getConfiguration().valueOf(UPDATER, CONFIG_REDIRECT_BROWSER_URL)));
				}
			}catch(IOException | URISyntaxException ex){
				ex.printStackTrace();
			}
		});
	}
	
	private void push(final Version version){
		if(handler != null){
			Platform.runLater(() -> handler.onResolve(version));
		}
	}
	
	/**
	 * Defining a version as it is used by an application.<br>
	 * A version is comprised of the following components:<br>
	 * <pre>
	 * {major}.{minor}.{patch}
	 * </pre>
	 *
	 */
	public static class Version implements Comparable<Version> {

		private int major;
		private int minor;
		private int patch;
		
		public Version(final String version){
			final String[] v = version.split("\\.", 3);
			if(v.length >= 3){
				this.major = Integer.valueOf(v[0]);
				this.minor = Integer.valueOf(v[1]);
				this.patch = Integer.valueOf(v[2].replaceAll("[^\\d.]", ""));
			}
		}

		public Version(final int major, final int minor, final int patch){
			this.major = major;
			this.minor = minor;
			this.patch = patch;
		}

		public int getMajor(){
			return this.major;
		}

		public int getMinor(){
			return this.minor;
		}

		public int getPatch(){
			return this.patch;
		}

		@Override
		public int compareTo(final Version o) {
			if(this.major != o.getMajor()){
				return (this.major - o.getMajor());
			}
			if(this.minor != o.getMinor()){
				return (this.minor - o.getMinor());
			}
			if(this.patch != o.getPatch()){
				return (this.patch - o.getPatch());
			}
			return 0;
		}
		
		@Override
		public String toString(){
			return String.valueOf(this.major)+"."+String.valueOf(this.minor)+"."+String.valueOf(this.patch);
		}
		
		public static Version current(){
			return new Version(Const.APPLICATION_VERSION);
		}
	}

}
