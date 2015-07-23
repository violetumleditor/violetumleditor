package com.horstmann.violet.web.workspace.editorpart;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Rectangle2D;

import com.horstmann.violet.web.util.jwt.CustomWebGraphics2D;
import com.horstmann.violet.workspace.editorpart.IEditorPart;
import com.horstmann.violet.workspace.editorpart.IEditorPartBehaviorManager;
import com.horstmann.violet.workspace.editorpart.IGrid;

import eu.webtoolkit.jwt.Coordinates;
import eu.webtoolkit.jwt.KeyboardModifier;
import eu.webtoolkit.jwt.Signal1;
import eu.webtoolkit.jwt.WFont;
import eu.webtoolkit.jwt.WLength;
import eu.webtoolkit.jwt.WLength.Unit;
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
	private boolean isDragDetected = false; // Used to avoid 'clicked' event after 'dragged" event

	public EditorPartWidget(IEditorPart editorPart) {
		setWidth(new WLength(100, Unit.Percentage));
		setHeight(new WLength(100, Unit.Percentage));
		this.editorPart = editorPart;
		final IGrid grid = editorPart.getGrid();
		grid.setVisible(false);
		this.editorPart.getGraph().setGridSticker(grid.getGridSticker());
		final IEditorPartBehaviorManager behaviorManager = editorPart.getBehaviorManager();
		
		mouseDragged().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				MouseEvent mouseEvent = convertMouseEvent(event, MouseEvent.MOUSE_DRAGGED, 0, EditorPartWidget.this.editorPart.getSwingComponent());
				if (lastMouseEvent != null && isSameEvent(lastMouseEvent, mouseEvent)) {
					return;
				}
				int deltaX = Math.abs(event.getDragDelta().x);
				int deltaY = Math.abs(event.getDragDelta().y);
				if (Math.abs(deltaX - mouseDragGapX) >= grid.getSnappingWidth() || Math.abs(deltaY - mouseDragGapY) >= grid.getSnappingHeight()) {
					behaviorManager.fireOnMouseDragged(mouseEvent);
					fixEditorSize(event);
					update();
					mouseDragGapX = deltaX;
					mouseDragGapY = deltaY;
				}
				lastMouseEvent = mouseEvent;
				isDragDetected = true;
			}
		});
		mouseWentDown().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				MouseEvent mouseEvent = convertMouseEvent(event, MouseEvent.MOUSE_PRESSED, 0, EditorPartWidget.this.editorPart.getSwingComponent());
				if (lastMouseEvent != null && isSameEvent(lastMouseEvent, mouseEvent)) {
					return;
				}
				behaviorManager.fireOnMousePressed(mouseEvent);
				// No need to call update() that will be done on drag on button
				// release;
				lastMouseEvent = mouseEvent;
			}
		});

		mouseWentUp().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				MouseEvent mouseEvent = convertMouseEvent(event, MouseEvent.MOUSE_RELEASED, 0, EditorPartWidget.this.editorPart.getSwingComponent());
				if (lastMouseEvent != null && isSameEvent(lastMouseEvent, mouseEvent)) {
					return;
				}
				behaviorManager.fireOnMouseReleased(mouseEvent);
				update();
				lastMouseEvent = mouseEvent;
				fixEditorSize(event);
			}
		});
		clicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				if (isDragDetected) {
					isDragDetected = false;
					return;
				}
				MouseEvent mouseEvent = convertMouseEvent(event, MouseEvent.MOUSE_CLICKED, 1, EditorPartWidget.this.editorPart.getSwingComponent());
				if (lastMouseEvent != null && isSameEvent(lastMouseEvent, mouseEvent)) {
					return;
				}
				behaviorManager.fireOnMouseClicked(mouseEvent);
				lastMouseEvent = mouseEvent;
				fixEditorSize(event);
				update();
			}
		});
		doubleClicked().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				MouseEvent mouseEvent = convertMouseEvent(event, MouseEvent.MOUSE_CLICKED, 2, EditorPartWidget.this.editorPart.getSwingComponent());
				if (lastMouseEvent != null && isSameEvent(lastMouseEvent, mouseEvent)) {
					return;
				}
				behaviorManager.fireOnMouseClicked(mouseEvent);
				lastMouseEvent = mouseEvent;
				fixEditorSize(event);
				update();
			}
		});
		mouseWheel().addListener(this, new Signal1.Listener<WMouseEvent>() {
			@Override
			public void trigger(WMouseEvent event) {
				MouseWheelEvent wheelEvent = convertMouseWheelEvent(event, MouseEvent.MOUSE_WHEEL, EditorPartWidget.this.editorPart.getSwingComponent());
				behaviorManager.fireOnMouseWheelMoved(wheelEvent);
				update();
			}
		});
	}

	private MouseEvent convertMouseEvent(WMouseEvent event, int type, int clickCount, Component c) {
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

		MouseEvent newMouseEvent = new MouseEvent(c, type, System.currentTimeMillis(), modifiers, event.getWidget().x, event.getWidget().y, clickCount, event.getButton() == Button.RightButton, button);
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
		WPainter painter = new WPainter(paintDevice) {
			@Override
			public WFont getFont() {
				WFont font = super.getFont();
				font.setSize(new WLength(12, Unit.Pixel));
				return font;
			}
		};
		painter.setClipping(false);
		paintDevice.init();
		Graphics2D graphics = new CustomWebGraphics2D(painter);
		this.editorPart.getSwingComponent().paint(graphics);
		paintDevice.done();
	}

	
	private boolean isSameEvent(MouseEvent firstMouseEvent, MouseEvent secondMouseEvent) {
		if (firstMouseEvent == null || secondMouseEvent == null) {
			return false;
		}
		boolean isSameButton = (firstMouseEvent.getButton() == secondMouseEvent.getButton());
		boolean isSameLocation = firstMouseEvent.getPoint().equals(secondMouseEvent.getPoint());
		boolean isSameModifiers = (firstMouseEvent.getModifiers() == secondMouseEvent.getModifiers());
		boolean isSameClickCount = (firstMouseEvent.getClickCount() == secondMouseEvent.getClickCount());
		boolean isSameType = (firstMouseEvent.getID() == secondMouseEvent.getID());
		boolean isSameEvent = isSameButton && isSameLocation && isSameModifiers && isSameClickCount && isSameType;
		return isSameEvent;
	}
	
	public IEditorPart getEditorPart() {
		return this.editorPart;
	}
	
	
	@Override
	public void resize(int widthPixels, int heightPixels) {
		this.editorPart.getSwingComponent().setSize(widthPixels, heightPixels);
		super.resize(widthPixels, heightPixels);
	}

	private void fixEditorSize(WMouseEvent event) {
		Dimension preferredSize = this.editorPart.getSwingComponent().getPreferredSize();
		int gap = 10;
		Coordinates mouseLocationRelativeToEditorPart = event.getWidget();
		double graphWidth = Math.max(preferredSize.getWidth() + gap, mouseLocationRelativeToEditorPart.x + gap);
		double graphHeight = Math.max(preferredSize.getHeight() + gap, mouseLocationRelativeToEditorPart.y + gap);
		double editorPartWidth = getWidth().toPixels();
		double editorPartHeight = getHeight().toPixels();
		if (editorPartWidth != graphWidth || editorPartHeight != graphHeight) {
			setMinimumSize(new WLength(100, Unit.Percentage), new WLength(100, Unit.Percentage));
			resize((int) graphWidth, (int) graphHeight);
		}
	}

}
