package com.gadarts.necromine.editor.desktop.dialogs;

import com.necromine.editor.GuiEventsSubscriber;
import com.necromine.editor.model.node.FlatNode;

import javax.swing.*;
import java.awt.*;

import static com.necromine.editor.model.node.FlatNode.MAX_HEIGHT;

public class TilesLiftDialog extends DialogPane {
	private static final String LABEL_HEIGHT = "Height: ";
	static final float STEP = 0.1f;
	private final FlatNode src;
	private final FlatNode dst;
	private final GuiEventsSubscriber guiEventsSubscriber;

	public TilesLiftDialog(final FlatNode src, final FlatNode dst, final GuiEventsSubscriber guiEventsSubscriber) {
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
		c.gridx++;
		return addSpinner(0, MAX_HEIGHT, STEP, c);
	}

	@Override
	public String getDialogTitle() {
		return "Lift Tiles";
	}
}
