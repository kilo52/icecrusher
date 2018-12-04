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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.kilo52.common.struct.BooleanColumn;
import com.kilo52.common.struct.Column;
import com.kilo52.common.struct.DataFrame;
import com.kilo52.common.struct.NullableBooleanColumn;
import com.kilo52.common.struct.NullableColumn;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;

/**
 * The DataFrameView control is designed to visualize an instance of a {@link DataFrame}.<br>
 * Behind the scenes, the <code>javafx.scene.control.TableView</code> API is used to do most
 * of the heavy lifting.<br>
 * <p>Every cell inside a DataFrameView is configured to filter user input while in editing mode
 * so that only valid data can be entered and used in each column. For example, the user will not
 * be able to enter text in a cell that belongs to a <code>FloatColumn</code>.<br>
 * It is possible to disable editing and therefore to use a DataFrameView only as a read only view.
 * Use <code>setEditable(false)</code> to disable editing.<br>
 * 
 * <p>You may set a <code>ViewListener</code> to get notified of various DataFrameView related
 * changes induced by the user.
 * 
 * <p>The content of the underlying DataFrame used by a DataFrameView will get updated immediately
 * after the user commits a change. If you hold a reference of that DataFrame you can observe the
 * change of the content, i.e. values of specific cells changed, right away.<br>
 * If you have registered a <code>ViewListener</code> then you get notified right after the change got
 * committed. Please note that by default every column of a DataFrameView exposed a context menu, 
 * allowing the user to perform various column related tasks. The listener, however, will merely 
 * notify you of a context menu event but no other actions will be performed directly.
 *
 */
public class DataFrameView extends TableView<Integer> {
	
	/**
	 * Listener interface defining callbacks for events caused by DataFrameViews.
	 *
	 */
	public interface ViewListener {
		
		/**
		 * Called when an edit event occurred. An <code>EditEvent</code> is passed to
		 * the callback holding information about the event
		 * 
		 * @param event The <code>EditEvent</code> object of the event fired
		 */
		void onEdit(EditEvent event);
		
		/**
		 * Called when the user selected a menu item from the context menu of a 
		 * DataFrameView TableColumn.<br>
		 * A <code>ContextMenuEvent</code> is passed to the callback holding information 
		 * about the event
		 * 
		 * @param event The <code>ContextMenuEvent</code> object of the event fired
		 */
		void onMenuAction(ContextMenuEvent event);
	}
	
	private DataFrame df;
	private List<ViewListener> listeners;
	private ColumnChangeListener columnListener;
	private boolean showIndexColumn;

	/**
	 * Constructs a new <code>DataFrameView</code> to visualise the content of 
	 * a <code>DataFrame</code> object. An index column is displayed on the 
	 * left-most side of this view
	 * 
	 * @param df The <code>DataFrame</code> to display in this view
	 */
	public DataFrameView(DataFrame df){
		this(df, true);
	}
	
	/**
	 * Constructs a new <code>DataFrameView</code> to visualise the content of 
	 * a <code>DataFrame</code> object. You can specify whether an index column 
	 * should be displayed on the left-most side of this view
	 * 
	 * @param df The <code>DataFrame</code> to display in this view
	 * @param showIndexColumn A boolean flag specifying whether to show an index column
	 */
	public DataFrameView(DataFrame df, boolean showIndexColumn){
		this.df = df;
		this.showIndexColumn = showIndexColumn;
		load();
	}

	/**
	 * Adds a <code>ViewListener</code> to this DataFrameView that will notify the caller
	 * about events concerning this view. You should remove your listener by calling 
	 * <code>removeEditListenr()</code> when you no longer need to get notified
	 * 
	 * @param listener The <code>ViewListener</code> to add
	 */
	public void addEditListener(final ViewListener listener){
		if(listeners == null){
			this.listeners = new LinkedList<ViewListener>();
		}
		this.listeners.add(listener);
	}
	
	/**
	 * Removes the specified <code>ViewListener</code> from this DataFrameView. It will no
	 * longer receive events about this DataFrameView
	 * 
	 * @param listener The <code>ViewListener</code> to remove
	 */
	public void removeEditListener(final ViewListener listener){
		this.listeners.remove(listener);
		if(listeners.isEmpty()){
			this.listeners = null;
		}
	}
	
	/**
	 * Reloads this DataFrameView and refreshes the entire content. This method can be called when
	 * the internal structure of the DataFrame used by this view has changed
	 * 
	 */
	public void reload(){
		//keep track of the current width of each table column
		//the width will be restored if possible
		final Map<String, Double> map = backupTableColumnWidths();
		reloadInternal();
		restoreTableColumnWidths(map);
	}
	
	/**
	 * Gets the <code>DataFrame</code> used by this view
	 * 
	 * @return The <code>DataFrame</code> of this view
	 */
	public DataFrame getDataFrame(){
		return this.df;
	}
	
	/**
	 * Sets and replaces the DataFrame used by this view and reloads the content
	 * 
	 * @param df The <code>DataFrame</code> to set this view to
	 */
	public void setDataFrame(final DataFrame df){
		this.df = df;
		reload();
	}
	
	/**
	 * Sets whether this DataFrameView should show an index column inside its view.<br>
	 * If this call changes the setting, the view will reload itself to reflect the
	 * changes performed
	 * 
	 * @param value A boolean flag indicating whether to show an index column
	 */
	public void showIndexColumn(final boolean value){
		//only reload if necessary
		if(this.showIndexColumn != value){
			final Map<String, Double> map = backupTableColumnWidths();
			this.showIndexColumn = value;
			reloadInternal();
			restoreTableColumnWidths(map);
		}
	}
	
	/**
	 * Indicates whether this DataFrameView currently shows an index column
	 * 
	 * @return True if this view shows an index column. False otherwise
	 */
	public boolean hasIndexColumn(){
		return this.showIndexColumn;
	}
	
	private void load(){
		if(showIndexColumn){
			createIndexColumn();
		}
		for(int i=0; i<df.rows(); ++i){
			this.getItems().add(i);
		}
		populateColumns();
		this.setEditable(true);
		final Label label = new Label("This DataFrame is empty");
		label.setStyle("-fx-font-weight: bold");
		this.setPlaceholder(label);
		this.getSelectionModel().cellSelectionEnabledProperty().set(true);
		setKeyListener();
		this.columnListener = new ColumnChangeListener();
		getColumns().addListener(this.columnListener);
	}
	
	private void createIndexColumn(){
		TableColumn<Integer, Void> indexCol = new TableColumn<>("");
		final int length = String.valueOf((df.rows()-1)).length();
		indexCol.setPrefWidth((length == 1) ? length*18 : length*12.5);
		indexCol.setResizable(false);
		indexCol.setSortable(false);
		indexCol.setEditable(false);
		indexCol.setCellFactory(col -> {
			final TableCell<Integer, Void> cell = new TableCell<>();
			cell.textProperty().bind(Bindings.createStringBinding(() -> {
				if(cell.isEmpty()){
					return null ;
				}else{
					return Integer.toString(cell.getIndex());
				}
			}, cell.emptyProperty(), cell.indexProperty()));

			return cell ;
		});
		this.getColumns().add(indexCol);
	}
	
	private void populateColumns(){
		for(int i=0; i<df.columns(); ++i){
			final Column col = df.getColumnAt(i);
			final TableColumn<Integer, Object> tableCol = new TableColumn<>(df.getColumnName(i));
			tableCol.setId(df.getColumnName(i));
			tableCol.setStyle( "-fx-alignment: CENTER;");
			tableCol.setSortable(false);
			tableCol.setEditable(true);
			addContextMenu(tableCol);
			final Tooltip tooltip = Tooltips.columnTooltip(col);
			if(col instanceof BooleanColumn || col instanceof NullableBooleanColumn){
				tableCol.setCellFactory((column) -> new BooleanTableCell(col instanceof NullableColumn, tooltip));
			}else{
				ConversionPack pack = ConversionPack.columnConversion(col);
				tableCol.setCellFactory((column) -> new EditingTableCell(pack, tooltip));
			}
			tableCol.setOnEditCommit((event) -> {
				final TablePosition<Integer, Object> pos = event.getTablePosition();
				int iCol = pos.getColumn()-1;
				int iRow = pos.getRow();
				Object oldVal = event.getOldValue();
				Object newVal = event.getNewValue();
				if(listeners != null){
					notifyListeners(new EditEvent(DataFrameView.this, iRow, iCol, oldVal, newVal));
				}
				//convert a "null" string to actual null value
				if(newVal != null && newVal.toString().equals("null")){
					newVal = null;
				}
				col.setValueAt(iRow, newVal);
			});
			tableCol.setCellValueFactory((cellData) -> {
				final int rowIndex = cellData.getValue();
				return new SimpleObjectProperty<>(col.getValueAt(rowIndex));
			});
			this.getColumns().add(tableCol);
		}
	}

	private void setKeyListener(){
		setOnKeyPressed((t) -> {
			if(t.getCode() == KeyCode.DELETE){
				final int row = getSelectionModel().getSelectedIndex();
				if((row >= 0) && (row < df.rows())){
					if(listeners != null){
						notifyListeners(new EditEvent(this, row, -1, null, null));
					}
				}
			}
		});
	}

	private void notifyListeners(final EditEvent event){
		for(final ViewListener listener : listeners){
			listener.onEdit(event);
		}
	}
	
	private void notifyListeners(final ContextMenuEvent event){
		for(final ViewListener listener : listeners){
			listener.onMenuAction(event);
		}
	}
	
	private void reloadInternal(){
		//remove all table columns
		this.getItems().clear();
		this.getColumns().clear();
		getColumns().removeListener(this.columnListener);
		load();
	}
	
	private void addContextMenu(final TableColumn<Integer, Object> column){
		final MenuItem item1 = new MenuItem("Rename");
		item1.setOnAction((event) -> { if(listeners != null){ 
			notifyListeners(new ContextMenuEvent(DataFrameView.this, ContextMenuEvent.Action.RENAME, column.getText())); } 
		});
		final MenuItem item2 = new MenuItem("Sort");
		item2.setOnAction((event) -> { if(listeners != null){
			notifyListeners(new ContextMenuEvent(DataFrameView.this, ContextMenuEvent.Action.SORT, column.getText())); } 
		});
		final MenuItem item3 = new MenuItem("Delete");
		item3.setOnAction((event) -> { if(listeners != null){ 
			notifyListeners(new ContextMenuEvent(DataFrameView.this, ContextMenuEvent.Action.DELETE, column.getText())); } 
		});
		final ContextMenu menu = new ContextMenu(item1, item2, item3);
		column.setContextMenu(menu);
	}
	
	private Map<String, Double> backupTableColumnWidths(){
		final Map<String, Double> map = new HashMap<>();
		List<TableColumn<Integer, ?>> cols = getColumns();
		for(int i=0; i<cols.size(); ++i){
			final String id = cols.get(i).getId();
			if(id != null){
				map.put(id, cols.get(i).getWidth());
			}
		}
		cols = null;
		return map;
	}
	
	private void restoreTableColumnWidths(final Map<String, Double> map){
		final List<TableColumn<Integer, ?>> cols = getColumns();
		for(int i=0; i<cols.size(); ++i){
			final String id = cols.get(i).getId();
			if(id != null){
				final Double width = map.get(id);
				if(width != null){
					cols.get(i).setPrefWidth(width);
				}
			}
		}
	}
	
	/**
	 * An Event used for editing events.
	 *
	 */
	public static class EditEvent {
		
		private DataFrameView view;
		private int row;
		private int column;
		private Object oldVal;
		private Object newVal;
		private boolean isRowDeletion;
		
		private EditEvent(DataFrameView view, int row, int column, Object oldVal, Object newVal){
			this.view = view;
			this.row = row;
			this.column = column;
			this.oldVal = oldVal;
			this.newVal = newVal;
			if(column < 0){
				isRowDeletion = true;
			}
		}
		
		public DataFrameView getView(){
			return view;
		}

		public int getRow(){
			return row;
		}

		public int getColumn(){
			return column;
		}

		public Object getOldVal(){
			return oldVal;
		}

		public Object getNewVal(){
			return newVal;
		}
		
		public boolean isRowDeletion(){
			return isRowDeletion;
		}

	}
	
	/**
	 * An Event used for context menu events. You may query the specific action of the 
	 * context menu by calling <code>getAction()</code>.
	 *
	 */
	public static class ContextMenuEvent {
		
		/**
		 * The actions available to the context menu.
		 *
		 */
		public enum Action {
			RENAME,
			SORT,
			DELETE;
		}
		
		private DataFrameView view;
		private Action action;
		private String columnName;
		
		private ContextMenuEvent(DataFrameView view, Action action, String columnName){
			this.view = view;
			this.action = action;
			this.columnName = columnName;
		}
		
		public DataFrameView getView(){
			return view;
		}

		public Action getAction() {
			return action;
		}

		public String getColumnName() {
			return columnName;
		}

	}

	private class ColumnChangeListener implements ListChangeListener<TableColumn<Integer, ?>> {
		@Override
		public void onChanged(javafx.collections.ListChangeListener.Change<? extends TableColumn<Integer, ?>> change){
			change.next();
			if(change.getAddedSize() == change.getRemovedSize()){
				final ObservableList<? extends TableColumn<Integer,?>> listChange = change.getList();
				final String[] list = new String[df.columns()];
				for(int i=0, j=0; i<listChange.size(); ++i){//copy content
					if(!listChange.get(i).getText().isEmpty()){//ignore index column
						list[j++] = listChange.get(i).getText();
					}
				}
				df.flush();
				final Column[] cols = new Column[df.columns()];
				for(int i=0; i<list.length; ++i){
					cols[i] = df.getColumn(list[i]);
				}
				for(int i=0; i<cols.length; ++i){
					df.setColumnAt(i, cols[i]);
				}
				df.setColumnNames(list);
			}
		}
	}

}
