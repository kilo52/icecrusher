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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.kilo52.common.struct.BooleanColumn;
import com.kilo52.common.struct.ByteColumn;
import com.kilo52.common.struct.CharColumn;
import com.kilo52.common.struct.Column;
import com.kilo52.common.struct.DataFrame;
import com.kilo52.common.struct.DefaultDataFrame;
import com.kilo52.common.struct.DoubleColumn;
import com.kilo52.common.struct.FloatColumn;
import com.kilo52.common.struct.IntColumn;
import com.kilo52.common.struct.LongColumn;
import com.kilo52.common.struct.ShortColumn;
import com.kilo52.common.struct.StringColumn;
import com.kilo52.common.struct.NullableBooleanColumn;
import com.kilo52.common.struct.NullableByteColumn;
import com.kilo52.common.struct.NullableCharColumn;
import com.kilo52.common.struct.NullableDataFrame;
import com.kilo52.common.struct.NullableDoubleColumn;
import com.kilo52.common.struct.NullableFloatColumn;
import com.kilo52.common.struct.NullableIntColumn;
import com.kilo52.common.struct.NullableLongColumn;
import com.kilo52.common.struct.NullableShortColumn;
import com.kilo52.common.struct.NullableStringColumn;
import com.kilo52.icecrusher.ui.OneShotSnackbar;
import com.kilo52.icecrusher.util.Layout;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

/**
 * Controller class for the {@link CreateDialog}.
 *
 */
public class CreateDialogController {
	
	/**
	 * Listener interface for the <code>CreateDialog</code>.
	 *
	 */
	public interface DialogListener {
		
		/**
		 * Called when the user confirms a DataFrame creation
		 * 
		 * @param df The <code>DataFrame</code> as specified by the user
		 */
		void onCreate(DataFrame df);
	}
	
	public static String[] options = new String[]{"Byte", "Short", "Int", "Long", "String", "Float", "Double", "Char", "Boolean"};
	
	@FXML
	private ScrollPane scrollPane;
	
	@FXML
	private JFXCheckBox cbIsNullable;
	
	@FXML
	private JFXButton btnCreate;
	
	@FXML
	private HBox hboxColumns;
	
	@FXML
	private JFXComboBox<String> cBoxColType;
	
	@FXML
	private JFXTextField txtColName;
	
	private Pane rootPane;
	
	private DialogListener delegate;
	
	private List<ColumnItemController> items;
	private List<Column> columns;
	private List<String> names;;

	@FXML
	public void initialize(){
		items = new LinkedList<>();
		cBoxColType.getItems().removeAll(cBoxColType.getItems());
		cBoxColType.getItems().addAll(options);
	}
	
	public void setRootContainer(final Pane pane){
		this.rootPane = pane;
	}
	
	public void setCreateListener(DialogListener listener){
		this.delegate = listener;
	}
	
	private Column constructColumn(final String type){
		if(type != null){
			final boolean useNull = cbIsNullable.isSelected();
			switch(type){
			case "Byte":
				return (useNull ? new NullableByteColumn() : new ByteColumn());
			case "Short":
				return (useNull ? new NullableShortColumn() : new ShortColumn());
			case "Int":
				return (useNull ? new NullableIntColumn() : new IntColumn());
			case "Long":
				return (useNull ? new NullableLongColumn() : new LongColumn());
			case "String":
				return (useNull ? new NullableStringColumn() : new StringColumn());
			case "Float":
				return (useNull ? new NullableFloatColumn() : new FloatColumn());
			case "Double":
				return (useNull ? new NullableDoubleColumn() : new DoubleColumn());
			case "Char":
				return (useNull ? new NullableCharColumn() : new CharColumn());
			case "Boolean":
				return (useNull ? new NullableBooleanColumn() : new BooleanColumn());
			}
		}
		return null;
	}
	
	private void showWarnMsg(final String msg){
		OneShotSnackbar.showFor(rootPane, msg);
	}
	
	@FXML
	private void onCreate(ActionEvent event){
		final Column col = constructColumn(cBoxColType.getValue());
		final String name = txtColName.getText();
		if(col == null){
			showWarnMsg("Please specify all column types");
			return;
		}
		this.columns = new ArrayList<>();
		this.names = new ArrayList<>();
		int i = 0;
		columns.add(col);
		names.add(((name == null || name.isEmpty()) ? String.valueOf(i) : name));
		++i;
		for(ColumnItemController item : items){
			final Column col2 = constructColumn(item.getSelectedType());
			final String name2 = item.getColumnName();;
			if(col2 == null){
				showWarnMsg("Please specify all column types");
				return;
			}
			columns.add(col2);
			names.add(((name2 == null || name2.isEmpty()) ? String.valueOf(i) : name2));
			++i;
		}
		if(hasDuplicate(names)){
			showWarnMsg("Duplicate column name");
			return;
		}
		DataFrame df = (cbIsNullable.isSelected() ? new NullableDataFrame() : new DefaultDataFrame());
		for(int j=0; j<i; ++j){
			df.addColumn(names.get(j), columns.get(j));
		}
		if(delegate != null){
			delegate.onCreate(df);
		}
	}
	
	@FXML
	private void onAddColumn(ActionEvent event){
		final Layout layout = new Layout(Layout.COLUMN_ITEM);
        final Parent parent = layout.load();
		final ColumnItemController item = layout.getController();
        items.add(item);
		hboxColumns.getChildren().add(parent);
		scrollPane.setContent(hboxColumns);
	}
	
	private boolean hasDuplicate(final List<String> names){
		final Set<String> set = new HashSet<>();
		set.add(names.get(0));
		if(names.size() >= 2){
			for(int i=1; i<names.size(); ++i){
				if(set.contains(names.get(i))){
					return true;
				}
				set.add(names.get(i));
			}
		}
		return false;
	}
}
