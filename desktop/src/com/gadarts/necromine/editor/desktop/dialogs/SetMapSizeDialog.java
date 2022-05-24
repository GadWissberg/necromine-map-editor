package com.gadarts.necromine.editor.desktop.dialogs;

import com.necromine.editor.MapRenderer;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;

public class SetMapSizeDialog extends DialogPane {

	public static final int MAXIMUM = 160;
	private static final String LABEL_WIDTH = "Width: ";
	private static final String LABEL_DEPTH = "Depth: ";
	private static final int STEP_SIZE = 1;
	private final Dimension current;
	private final MapRenderer mapRenderer;

	public SetMapSizeDialog(final Dimension current, final MapRenderer mapRenderer) {
		this.current = current;
		this.mapRenderer = mapRenderer;
		init();
	}

	@Override
	void initializeView( ) {
		JSpinner widthSpinner = addInputLine(LABEL_WIDTH, current.width);
		JSpinner heightSpinner = addInputLine(LABEL_DEPTH, current.height);
		addGeneralButtons((e -> {
			int width = ((Double) widthSpinner.getValue()).intValue();
			int depth = ((Double) heightSpinner.getValue()).intValue();
			mapRenderer.onMapSizeSet(width, depth);
			closeDialog();
		}));
	}

	private JSpinner addInputLine(final String labelWidth, final float currentValue) {
		addLabel(labelWidth);
		JSpinner spinner = addValueSpinner(currentValue);
		JFormattedTextField editor = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
		((NumberFormatter) editor.getFormatter()).setAllowsInvalid(false);
		return spinner;
	}

	private JSpinner addValueSpinner(final float currentValue) {
		return addSpinner(currentValue, MAXIMUM, STEP_SIZE, false);
	}

	@Override
	public String getDialogTitle( ) {
//		return ToolbarDefinition.MAP_SIZE.getButtonProperties().getToolTip();
		return "";
	}
}
