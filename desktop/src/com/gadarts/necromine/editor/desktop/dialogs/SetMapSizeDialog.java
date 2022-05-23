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
	void initializeView(final GridBagConstraints c) {
		JSpinner widthSpinner = addInputLine(c, LABEL_WIDTH, current.width);
		JSpinner heightSpinner = addInputLine(c, LABEL_DEPTH, current.height);
		addOkButton(c, (e -> {
			int width = ((Double) widthSpinner.getValue()).intValue();
			int depth = ((Double) heightSpinner.getValue()).intValue();
			mapRenderer.onMapSizeSet(width, depth);
			closeDialog();
		}));
	}

	private JSpinner addInputLine(final GridBagConstraints c, final String labelWidth, final float currentValue) {
		c.gridx = 0;
		addLabel(c, labelWidth);
		JSpinner spinner = addValueSpinner(c, currentValue);
		JFormattedTextField editor = ((JSpinner.NumberEditor) spinner.getEditor()).getTextField();
		((NumberFormatter) editor.getFormatter()).setAllowsInvalid(false);
		c.gridy++;
		return spinner;
	}

	private JSpinner addValueSpinner(final GridBagConstraints c, final float currentValue) {
		c.gridx++;
		return addSpinner(currentValue, MAXIMUM, STEP_SIZE, c, false);
	}

	@Override
	public String getDialogTitle() {
//		return ToolbarDefinition.MAP_SIZE.getButtonProperties().getToolTip();
		return "";
	}
}
