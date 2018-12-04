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

package com.kilo52.icecrusher.ui;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.kilo52.common.struct.BooleanColumn;
import com.kilo52.common.struct.Column;
import com.kilo52.common.struct.DataFrame;
import com.kilo52.common.struct.NullableBooleanColumn;
import com.kilo52.common.struct.NullableColumn;
import com.kilo52.icecrusher.io.Files;
import com.kilo52.icecrusher.ui.view.BooleanTableCell;
import com.kilo52.icecrusher.ui.view.ConversionPack;
import com.kilo52.icecrusher.ui.view.DataFrameView;
import com.kilo52.icecrusher.util.EditorConfiguration;
import com.kilo52.icecrusher.util.EditorConfiguration.Section;
import com.kilo52.icecrusher.util.EditorFile;

import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.GridPane;

/**
 * Helper class to keep code for row additions out of the FrameController.
 *
 */
public class RowAdder {

	private DataFrameView view;
	private FrameController controller;

	public RowAdder(FrameController controller){
		this.controller = controller;
	}

	public void prepareRowAddition(){
		final FileTab tab = ((FileTab)controller.mainTabs.getSelectionModel().getSelectedItem());
		this.view = tab.getView();
		final DataFrame selected = tab.getDataFrame();
		final GridPane grid = new GridPane();
		final Node[] fields = new Node[selected.columns()];
		for(int i=0; i<selected.columns(); ++i){
			final Column col = selected.getColumnAt(i);
			Node node = null;
			if(col instanceof BooleanColumn || col instanceof NullableBooleanColumn){
				JFXComboBox<Object> cb = new JFXComboBox<>((col instanceof NullableColumn) 
						? BooleanTableCell.optionsNullable 
						: BooleanTableCell.optionsDefault);
				
				cb.getSelectionModel().select(Boolean.FALSE);
				cb.setTooltip(new Tooltip(selected.getColumnName(i)));
				node = cb;
			}else{
				final ConversionPack pack = ConversionPack.columnConversion(col);
				final JFXTextField txt = new JFXTextField();
				txt.setPromptText(selected.getColumnName(i));
				txt.setLabelFloat(true);
				txt.setTextFormatter(new TextFormatter<Object>(pack.getConverter(), null, pack.getFilter()));
				node = txt;
			}
			fields[i] = node;
			GridPane.setMargin(node, new Insets(20, 10, 0, 10));
		}
		grid.setAlignment(Pos.CENTER);
		grid.addRow(1, fields);
		controller.editSceneAnchor.getChildren().add(grid);
		final EditorFile file = tab.getFile();
		controller.editSceneFile.setText(file != null ? file.getName() : Files.DEFAULT_NEW_FILENAME);
	}

	public boolean addRow(){
		ObservableList<Node> nodes = ((GridPane)controller.editSceneAnchor.getChildren().get(0)).getChildren();
		final DataFrame df = this.view.getDataFrame();
		final boolean NULLABLE = this.view.getDataFrame().isNullable();
		final Object[] row = new Object[nodes.size()];
		for(final Node node : nodes){
			if(node instanceof JFXTextField){
				final JFXTextField field = ((JFXTextField)node);
				final String s = field.getText();
				if(!NULLABLE && (s == null || s.isEmpty())){
					OneShotSnackbar.showFor(controller.rootPane, "DefaultDataFrame cannot use null");
					return false;
				}
				row[df.getColumnIndex(field.getPromptText())] = field.getTextFormatter().getValue();
			}else{
				final JFXComboBox<?> field = (JFXComboBox<?>)node;
				Object value = field.getValue();
				if((value != null) && value.toString().equals("null")){
					value = null;
				}
				row[df.getColumnIndex(field.getTooltip().getText())] = value;
			}
		}
		df.addRow(row);
		this.view.reload();
		if(controller.config.booleanOf(Section.GLOBAL, EditorConfiguration.CONFIG_CLEAR_AFTER_ROW_ADD)){
			clearFields(nodes);
		}
		return true;
	}

	private void clearFields(final ObservableList<Node> nodes){
		for(final Node node : nodes){
			if(node instanceof JFXTextField){
				((JFXTextField)node).getTextFormatter().setValue(null);
			}
		}
	}

}
