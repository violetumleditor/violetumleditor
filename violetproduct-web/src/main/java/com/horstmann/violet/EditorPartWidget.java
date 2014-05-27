package com.horstmann.violet;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.IOException;

import com.horstmann.violet.workspace.editorpart.EditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IGrid;

import eu.webtoolkit.jwt.KeyboardModifier;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WMouseEvent;
import eu.webtoolkit.jwt.WMouseEvent.Button;
import eu.webtoolkit.jwt.WPaintDevice;
import eu.webtoolkit.jwt.WPaintedWidget;
import eu.webtoolkit.jwt.WPainter;

public class EditorPartWidget extends WPaintedWidget {

	private IEditorPart editorPart;
	
	private int mouseDragGapX = 0;
	private int mouseDragGapY = 0;
	
	private MouseEvent lastMouseEvent;

	public EditorPartWidget(IEditorPart editorPart) throws IOException {
	    this.editorPart = editorPart;
		final IGrid grid = editorPart.getGrid();
		grid.setVisible(false);
		this.editorPart.getGraph().setGridSticker(grid.getGridSticker());
		final IEditorPartBehaviorManager behaviorManager = editorPart.getBehaviorManager();
		
		
		mouseMoved().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				MouseEvent mouseEvent = convertMouseEvent(event, MouseEvent.MOUSE_MOVED, EditorPartWidget.this.editorPart.getSwingComponent());
				behaviorManager.fireOnMouseMoved(mouseEvent);
			}
		});
		mouseDragged().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				MouseEvent mouseEvent = convertMouseEvent(event, MouseEvent.MOUSE_DRAGGED, EditorPartWidget.this.editorPart.getSwingComponent());
				behaviorManager.fireOnMouseDragged(mouseEvent);
				int deltaX = Math.abs(event.getDragDelta().x);
				int deltaY = Math.abs(event.getDragDelta().y);
				if (Math.abs(deltaX - mouseDragGapX) >= grid.getSnappingWidth() || Math.abs(deltaY - mouseDragGapY) >= grid.getSnappingHeight()) {
					update();
					mouseDragGapX = deltaX;
					mouseDragGapY = deltaY;
				}
			}
		});
		mouseWentDown().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				MouseEvent mouseEvent = convertMouseEvent(event, MouseEvent.MOUSE_PRESSED, EditorPartWidget.this.editorPart.getSwingComponent());
				behaviorManager.fireOnMousePressed(mouseEvent);
				update();
			}
		});
		mouseWentUp().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				MouseEvent mouseEvent = convertMouseEvent(event, MouseEvent.MOUSE_RELEASED, EditorPartWidget.this.editorPart.getSwingComponent());
				behaviorManager.fireOnMouseReleased(mouseEvent);
				update();
			}
		});
		clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				MouseEvent mouseEvent = convertMouseEvent(event, MouseEvent.MOUSE_CLICKED, EditorPartWidget.this.editorPart.getSwingComponent());
				behaviorManager.fireOnMouseClicked(mouseEvent);
				update();
			}
		});
//		mouseWheel().addListener(this, new Signal1.Listener<WMouseEvent>() {
//			@Override
//			public void trigger(WMouseEvent event) {
//				MouseWheelEvent mouseWheelEvent = convertMouseWheelEvent(event, MouseEvent.MOUSE_WHEEL, editorPart.getSwingComponent());
//				behaviorManager.fireOnMouseWheelMoved(mouseWheelEvent);
//				update();
//			}
//		});
		
	}

	private MouseEvent convertMouseEvent(WMouseEvent event, int type, Component c) {
		int modifiers = 0;

		if (event.getModifiers().contains(KeyboardModifier.AltModifier))
			modifiers |= MouseEvent.ALT_DOWN_MASK;
		if (event.getModifiers().contains(KeyboardModifier.ShiftModifier))
			modifiers |= MouseEvent.SHIFT_DOWN_MASK;
		if (event.getModifiers().contains(KeyboardModifier.ControlModifier))
			modifiers |= MouseEvent.CTRL_DOWN_MASK;
		if (event.getModifiers().contains(KeyboardModifier.MetaModifier))
			modifiers |= MouseEvent.META_DOWN_MASK;

		int button = 0;

		if (event.getButton() == Button.LeftButton) {
			modifiers |= MouseEvent.BUTTON1_DOWN_MASK;
			button = MouseEvent.BUTTON1;
		} else if (event.getButton() == Button.MiddleButton) {
			modifiers |= MouseEvent.BUTTON2_DOWN_MASK;
			button = MouseEvent.BUTTON2;
		} else if (event.getButton() == Button.RightButton) {
			modifiers |= MouseEvent.BUTTON3_DOWN_MASK;
			button = MouseEvent.BUTTON3;
		}
		
		MouseEvent newMouseEvent = new MouseEvent(c, type, System.currentTimeMillis(), modifiers, event.getWidget().x, event.getWidget().y, 1, event.getButton() == Button.RightButton, button);
		if (event.getButton() == Button.LeftButton && isDoubleClickDetected(this.lastMouseEvent, newMouseEvent)) {
			newMouseEvent = new MouseEvent(c, type, System.currentTimeMillis(), modifiers, event.getWidget().x, event.getWidget().y, 2, event.getButton() == Button.RightButton, button);
		}
		this.lastMouseEvent = newMouseEvent;
		
		return newMouseEvent;
	}
	
	
	private MouseWheelEvent convertMouseWheelEvent(WMouseEvent event, int type, Component c) {
		int modifiers = 0;

		if (event.getModifiers().contains(KeyboardModifier.AltModifier))
			modifiers |= MouseEvent.ALT_DOWN_MASK;
		if (event.getModifiers().contains(KeyboardModifier.ShiftModifier))
			modifiers |= MouseEvent.SHIFT_DOWN_MASK;
		if (event.getModifiers().contains(KeyboardModifier.ControlModifier))
			modifiers |= MouseEvent.CTRL_DOWN_MASK;
		if (event.getModifiers().contains(KeyboardModifier.MetaModifier))
			modifiers |= MouseEvent.META_DOWN_MASK;

		int wheelDelta = event.getWheelDelta();
		
		return new MouseWheelEvent(c, type, System.currentTimeMillis(), modifiers, event.getWidget().x, event.getWidget().y, 1, false, MouseWheelEvent.WHEEL_UNIT_SCROLL, 1, -wheelDelta);
	}

	@Override
	protected void paintEvent(WPaintDevice paintDevice) {
		WPainter painter = new WPainter(paintDevice);
		painter.setClipping(true);
		paintDevice.init();
		Graphics2D graphics = new CustomWebGraphics2D(painter);
		this.editorPart.getSwingComponent().paint(graphics);
		paintDevice.done();
	}

	
	@Override
	protected void layoutSizeChanged(int width, int height) {
		super.layoutSizeChanged(width, height);
		this.editorPart.getSwingComponent().setSize(width, height);
	}
	
	@Override
	public void resize(WLength width, WLength height) {
		super.resize(width, height);
		this.editorPart.getSwingComponent().setSize((int) width.toPixels() , (int) height.toPixels());
	}
	
	private boolean isDoubleClickDetected(MouseEvent firstMouseEvent, MouseEvent secondMouseEvent) {
		if (firstMouseEvent == null || secondMouseEvent == null) {
			return false;
		}
		boolean isSameButton = (firstMouseEvent.getButton() == secondMouseEvent.getButton());
		boolean isButton1 = (firstMouseEvent.getButton() == MouseEvent.BUTTON1);
		boolean isSameLocation = firstMouseEvent.getPoint().equals(secondMouseEvent.getPoint());
		return isSameButton && isButton1 && isSameLocation;
	}

}
