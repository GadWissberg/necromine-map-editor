package com.gadarts.necromine.editor.desktop.dialogs;

import com.necromine.editor.MapRenderer;

import javax.swing.*;
import java.awt.*;

public class SetAmbientLightDialog extends DialogPane {

	public static final int MAXIMUM = 1;
	private static final String LABEL_AMBIENT = "Ambient: ";
	private static final float STEP_SIZE = 0.1f;
	private final float current;
	private final MapRenderer mapRenderer;

	public SetAmbientLightDialog(final float current, final MapRenderer mapRenderer) {
		this.current = current;
		this.mapRenderer = mapRenderer;
		init();
	}

	@Override
	void initializeView(final GridBagConstraints c) {
		c.gridx = 0;
		addLabel(c, LABEL_AMBIENT);
		JSpinner spinner = addValueSpinner(c);
		c.gridx = 0;
		c.gridy++;
		addOkButton(c, (e -> {
			mapRenderer.onAmbientLightValueSet(((Double) spinner.getValue()).floatValue());
			closeDialog();
		}));
	}

	private JSpinner addValueSpinner(final GridBagConstraints c) {
		c.gridx++;
		return addSpinner(current, MAXIMUM, STEP_SIZE, c, false);
	}

	@Override
	public String getDialogTitle() {
		return "Set Ambient Light";
	}
}
