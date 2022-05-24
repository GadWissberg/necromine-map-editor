package com.gadarts.necromine.editor.desktop.dialogs;

import com.necromine.editor.MapRenderer;

import javax.swing.*;

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
	void initializeView( ) {
		addLabel(LABEL_AMBIENT);
		JSpinner spinner = addValueSpinner();
		addGeneralButtons((e -> {
			mapRenderer.onAmbientLightValueSet(((Double) spinner.getValue()).floatValue());
			closeDialog();
		}));
	}

	private JSpinner addValueSpinner( ) {
		return addSpinner(current, MAXIMUM, STEP_SIZE, false);
	}

	@Override
	public String getDialogTitle( ) {
		return "Set Ambient Light";
	}
}
