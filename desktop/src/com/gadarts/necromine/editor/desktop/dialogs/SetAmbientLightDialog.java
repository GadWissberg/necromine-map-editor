package com.gadarts.necromine.editor.desktop.dialogs;

import com.necromine.editor.GuiEventsSubscriber;

import javax.swing.*;
import java.awt.*;

public class SetAmbientLightDialog extends DialogPane {

	public static final int MAXIMUM = 1;
	private static final String LABEL_AMBIENT = "Ambient: ";
	private static final float STEP_SIZE = 0.1f;
	private static final int SPINNER_WIDTH = 50;
	private final float current;
	private final GuiEventsSubscriber guiEventsSubscriber;

	public SetAmbientLightDialog(final float current, final GuiEventsSubscriber guiEventsSubscriber) {
		this.current = current;
		this.guiEventsSubscriber = guiEventsSubscriber;
		init();
	}

	@Override
	void initializeView(final GridBagConstraints c) {
		c.gridx = 0;
		addLabel(c, LABEL_AMBIENT);
		c.gridy--;
		JSpinner spinner = addValueSpinner(c);
		c.gridx = 0;
		c.gridy++;
		addOkButton(c, (e -> {
			guiEventsSubscriber.onAmbientLightValueSet(((Double) spinner.getValue()).floatValue());
			closeDialog();
		}));
	}

	private JSpinner addValueSpinner(final GridBagConstraints c) {
		c.gridx++;
		JSpinner spinner = addSpinner(current, MAXIMUM, STEP_SIZE, c);
		Dimension preferredSize = spinner.getPreferredSize();
		spinner.setPreferredSize(new Dimension(SPINNER_WIDTH, preferredSize.height));
		return spinner;
	}

	@Override
	public String getDialogTitle() {
		return "Set Ambient Light";
	}
}
