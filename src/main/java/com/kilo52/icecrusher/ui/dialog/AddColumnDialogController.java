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

package com.kilo52.icecrusher.ui.dialog;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.kilo52.common.struct.BooleanColumn;
import com.kilo52.common.struct.ByteColumn;
import com.kilo52.common.struct.CharColumn;
import com.kilo52.common.struct.Column;
import com.kilo52.common.struct.DataFrame;
import com.kilo52.common.struct.DoubleColumn;
import com.kilo52.common.struct.FloatColumn;
import com.kilo52.common.struct.IntColumn;
import com.kilo52.common.struct.LongColumn;
import com.kilo52.common.struct.NullableBooleanColumn;
import com.kilo52.common.struct.NullableByteColumn;
import com.kilo52.common.struct.NullableCharColumn;
import com.kilo52.common.struct.NullableDoubleColumn;
import com.kilo52.common.struct.NullableFloatColumn;
import com.kilo52.common.struct.NullableIntColumn;
import com.kilo52.common.struct.NullableLongColumn;
import com.kilo52.common.struct.NullableShortColumn;
import com.kilo52.common.struct.NullableStringColumn;
import com.kilo52.common.struct.ShortColumn;
import com.kilo52.common.struct.StringColumn;
import com.kilo52.icecrusher.ui.OneShotSnackbar;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

/**
 * Controller class for the {@link AddColumnDialog}.
 *
 */
public class AddColumnDialogController {
	
	/**
	 * Listener interface for the <code>AddColumnDialog</code>.
	 *
	 */
	public interface DialogListener {
		
		/**
		 * Called when the user confirms a column creation
		 * 
		 * @param name The name of the <code>Column</code> to add
		 * @param col The <code>Column</code> to add to the DataFrame
		 */
		void onCreate(String name, Column col);
	}
	
	public static String[] options = new String[]{"Byte", "Short", "Int", "Long", "String", "Float", "Double", "Char", "Boolean"};
	
	@FXML
	private JFXComboBox<String> cBoxColType;
	
	@FXML
	private JFXTextField txtColName;
	
	private Pane rootPane;
	private DialogListener delegate;
	private DataFrame df;
	private boolean useNullable;
	private int size;
	
	@FXML
	public void initialize(){
		cBoxColType.getItems().removeAll(cBoxColType.getItems());
		cBoxColType.getItems().addAll(options);
	}

	public void setRootContainer(Pane pane){
		this.rootPane = pane;
	}
	
	public void useDataFrame(final DataFrame df){
		this.df = df;
		this.useNullable = df.isNullable();
		this.size = df.rows();
	}
	
	public void setAddListener(DialogListener listener){
		this.delegate = listener;
	}
	
	@FXML
	private void onAdd(ActionEvent event){
		final Column col = constructColumn(cBoxColType.getValue());
		if(col == null){
			showWarnMsg("Please specify the column type");
			return;
		}
		final String name = txtColName.getText();
		if(name == null || name.isEmpty()){
			showWarnMsg("Please specify a valid column name");
			return;
		}
		if(nameIsDuplicate(name)){
			showWarnMsg("Duplicate column name");
			return;
		}
		if(delegate != null){
			delegate.onCreate(name, col);
		}
	}
	
	private Column constructColumn(final String type){
		if(type != null){
			switch(type){
			case "Byte":
				return (useNullable ? new NullableByteColumn(new Byte[size]) : new ByteColumn(new byte[size]));
			case "Short":
				return (useNullable ? new NullableShortColumn(new Short[size]) : new ShortColumn(new short[size]));
			case "Int":
				return (useNullable ? new NullableIntColumn(new Integer[size]) : new IntColumn(new int[size]));
			case "Long":
				return (useNullable ? new NullableLongColumn(new Long[size]) : new LongColumn(new long[size]));
			case "String":
				return (useNullable ? new NullableStringColumn(new String[size]) : new StringColumn(new String[size]));
			case "Float":
				return (useNullable ? new NullableFloatColumn(new Float[size]) : new FloatColumn(new float[size]));
			case "Double":
				return (useNullable ? new NullableDoubleColumn(new Double[size]) : new DoubleColumn(new double[size]));
			case "Char":
				Column col = null;
				if(useNullable){
					col = new NullableCharColumn(new Character[size]);
				}else{
					final char[] chars = new char[size];
					for(int i=0; i<chars.length; ++i){
						chars[i] = '-';
					}
					col = new CharColumn(chars);
				}
				return col;
			case "Boolean":
				return (useNullable ? new NullableBooleanColumn(new Boolean[size]) : new BooleanColumn(new boolean[size]));
			}
		}
		return null;
	}
	
	private void showWarnMsg(final String msg){
		OneShotSnackbar.showFor(rootPane, msg);
	}
	
	private boolean nameIsDuplicate(final String name){
		final String[] names = this.df.getColumnNames();
		for(int i=0; i<names.length; ++i){
			if(names[i].equals(name)){ return true; }
		}
		return false;
	}
}
