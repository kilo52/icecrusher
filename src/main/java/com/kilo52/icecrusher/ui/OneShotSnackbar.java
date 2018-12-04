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

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSnackbar;
import com.jfoenix.controls.JFXButton.ButtonType;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.geometry.Insets;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

/**
 * Custom implementation of a <code>JFXSnackbar</code>. This class merely adds a listener
 * to the standard implementation to notify callers once the snackbar has closed.<br>
 * Most of the code in this class was just copied from <code>JFXSnackbar</code> to resemble
 * the standard behaviour.
 * 
 * <p><b>NOTE:</b> This class may get replaced by a more solid solution in the future
 *
 */
public class OneShotSnackbar extends JFXSnackbar {

	public interface CloseListener {
		
		void onFinished();
	}

	private static final String DEFAULT_STYLE_CLASS = "jfx-snackbar";
	private static final long DEFAULT_TIMEOUT = 4000;

	private Label toast;
	private JFXButton action;
	private StackPane actionContainer;
	private Timeline openAnimation = null;
	private ConcurrentLinkedQueue<SnackbarEvent> eventQueue = new ConcurrentLinkedQueue<>();
	private Interpolator easeInterpolator = Interpolator.SPLINE(0.250, 0.100, 0.250, 1.000);
	private AtomicBoolean processingQueue = new AtomicBoolean(false);
	private BorderPane content;

	private CloseListener delegate;
	private static OneShotSnackbar sbInfo;

	public OneShotSnackbar(Pane snackbarContainer){
		initialize();
		toast = new Label();
		toast.setMinWidth(Control.USE_PREF_SIZE);
		toast.getStyleClass().add("jfx-snackbar-toast");
		toast.setWrapText(true);
		final StackPane toastContainer = new StackPane(toast);
		toastContainer.setPadding(new Insets(20));

		action = new JFXButton();
		action.setMinWidth(Control.USE_PREF_SIZE);
		action.setButtonType(ButtonType.FLAT);
		action.getStyleClass().add("jfx-snackbar-action");
		actionContainer = new StackPane(action);
		actionContainer.setPadding(new Insets(0, 10, 0, 0));

		content = new BorderPane();
		content.setLeft(toastContainer);
		content.setRight(actionContainer);

		toast.prefWidthProperty().bind(Bindings.createDoubleBinding(() -> {
			if(content.getPrefWidth() == -1){
				return content.getPrefWidth();
			}
			double actionWidth = (actionContainer.isVisible() ? actionContainer.getWidth() : 0.0);
			return content.prefWidthProperty().get() - actionWidth;
		}, content.prefWidthProperty(), actionContainer.widthProperty(), actionContainer.visibleProperty()));
		content.getStyleClass().add("jfx-snackbar-content");
		getChildren().add(content);
		setManaged(false);
		setVisible(false);
		registerSnackbarContainer(snackbarContainer);
		layoutBoundsProperty().addListener((o, oldVal, newVal) -> refreshPopup());
		addEventHandler(SnackbarEvent.SNACKBAR, e -> enqueue(e));
	}

	private void initialize() {
		this.getStyleClass().add(DEFAULT_STYLE_CLASS);
	}

	public void setOnFinished(final CloseListener delegate){
		this.delegate = delegate;
	}

	@Override
	public void show(String message, long timeout){
		toast.setText(message);
		actionContainer.setVisible(false);
		actionContainer.setManaged(false);
		action.setVisible(false);
		openAnimation = getTimeline(timeout);
		openAnimation.play();
	}

	public static void showFor(final Pane snackbarContainer, final String msg){
		showFor(snackbarContainer, msg, DEFAULT_TIMEOUT);
	}

	public static void showFor(final Pane snackbarContainer, final String msg, final long timeout){
		if(sbInfo == null){
			sbInfo = new OneShotSnackbar(snackbarContainer);
			sbInfo.setOnFinished(() -> {
				sbInfo = null;
			});
			sbInfo.show(msg, timeout);
		}
	}

	private Timeline getTimeline(long timeout){
		Timeline animation;
		if(timeout <= 0){
			animation = new Timeline(
					new KeyFrame(
							Duration.ZERO,
							e -> this.toBack(),
							new KeyValue(this.visibleProperty(), false, Interpolator.EASE_BOTH),
							new KeyValue(this.translateYProperty(), this.getLayoutBounds().getHeight(), easeInterpolator),
							new KeyValue(this.opacityProperty(), 0, easeInterpolator)
							),
					new KeyFrame(
							Duration.millis(10),
							e -> this.toFront(),
							new KeyValue(this.visibleProperty(), true, Interpolator.EASE_BOTH)
							),
					new KeyFrame(Duration.millis(300),
							new KeyValue(this.opacityProperty(), 1, easeInterpolator),
							new KeyValue(this.translateYProperty(), 0, easeInterpolator)
							)
					);
			animation.setCycleCount(1);
		}else{
			animation = new Timeline(
					new KeyFrame(
							Duration.ZERO,
							(e) -> this.toBack(),
							new KeyValue(this.visibleProperty(), false, Interpolator.EASE_BOTH),
							new KeyValue(this.translateYProperty(), this.getLayoutBounds().getHeight(), easeInterpolator),
							new KeyValue(this.opacityProperty(), 0, easeInterpolator)
							),
					new KeyFrame(
							Duration.millis(10),
							(e) -> this.toFront(),
							new KeyValue(this.visibleProperty(), true, Interpolator.EASE_BOTH)
							),
					new KeyFrame(Duration.millis(300),
							new KeyValue(this.opacityProperty(), 1, easeInterpolator),
							new KeyValue(this.translateYProperty(), 0, easeInterpolator)
							),
					new KeyFrame(Duration.millis(timeout / 2))
					);
			animation.setAutoReverse(true);
			animation.setCycleCount(2);
			animation.setOnFinished((e) -> {
				processSnackbars();
				if(delegate != null){
					delegate.onFinished();
				}
			});
		}
		return animation;
	}

	private void processSnackbars(){
		final SnackbarEvent qevent = eventQueue.poll();
		if(qevent != null){
			if(qevent.isPersistent()){
				show(qevent.getMessage(), qevent.getpseudoClass(), qevent.getActionText(), qevent.getActionHandler());
			}else{
				show(qevent.getMessage(), qevent.getpseudoClass(), qevent.getActionText(), qevent.getTimeout(), qevent.getActionHandler());
			}
		}else{
			processingQueue.getAndSet(false);
		}
	}

}
