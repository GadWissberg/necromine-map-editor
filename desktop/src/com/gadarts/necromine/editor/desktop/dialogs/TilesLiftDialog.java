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
		initializeView();
	}

	private void initializeView() {
		GridBagConstraints c = createGridBagConstraints();
		addLabel(c, LABEL_HEIGHT);
		SpinnerModel model = addSpinner(c);
		addOkButton(c, e -> {
			float value = ((Double) model.getValue()).floatValue();
			if (value > 0) {
				guiEventsSubscriber.onTilesLift(src, dst, value);
			}
			closeDialog();
		});
	}

	private SpinnerModel addSpinner(final GridBagConstraints c) {
		c.gridy--;
		c.gridx++;
		SpinnerModel model = new SpinnerNumberModel(0, 0, MAX_HEIGHT, STEP);
		add(new JSpinner(model));
		c.gridx--;
		c.gridy++;
		return model;
	}

	@Override
	public String getDialogTitle() {
		return "Lift Tiles";
	}
}
