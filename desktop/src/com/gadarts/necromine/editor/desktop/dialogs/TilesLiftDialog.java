package com.gadarts.necromine.editor.desktop.dialogs;

import com.necromine.editor.GuiEventsSubscriber;
import com.necromine.editor.model.node.Node;

import javax.swing.*;
import java.awt.*;

public class TilesLiftDialog extends DialogPane {
	private static final String LABEL_HEIGHT = "Height: ";
	private static final int MAX_HEIGHT = 10;
	private static final float STEP = 0.1f;
	private final Node src;
	private final Node dst;
	private final GuiEventsSubscriber guiEventsSubscriber;

	public TilesLiftDialog(final Node src, final Node dst, final GuiEventsSubscriber guiEventsSubscriber) {
		this.src = src;
		this.dst = dst;
		this.guiEventsSubscriber = guiEventsSubscriber;
		init();
	}

	@Override
	void initializeView(final GridBagConstraints c) {
		c.gridx = 0;
		addLabel(c, LABEL_HEIGHT);
		JSpinner model = addHeightSpinner(c);
		c.gridx = 0;
		c.gridy++;
		addOkButton(c, e -> {
			float value = ((Double) model.getValue()).floatValue();
			if (value > 0) {
				guiEventsSubscriber.onTilesLift(src, dst, value);
			}
			closeDialog();
		});
	}

	private JSpinner addHeightSpinner(final GridBagConstraints c) {
		c.gridy--;
		c.gridx++;
		return addSpinner(0, MAX_HEIGHT, STEP, c);
	}

	@Override
	public String getDialogTitle() {
		return "Lift Tiles";
	}
}
