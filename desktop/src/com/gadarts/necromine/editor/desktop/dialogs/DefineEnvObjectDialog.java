package com.gadarts.necromine.editor.desktop.dialogs;

import com.necromine.editor.MapRenderer;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.node.FlatNode;

import javax.swing.*;
import java.awt.*;

public class DefineEnvObjectDialog extends DialogPane {

	private static final String LABEL_HEIGHT = "Height: ";
	private final PlacedEnvObject element;
	private final MapRenderer mapRenderer;

	public DefineEnvObjectDialog(final PlacedEnvObject data, final MapRenderer mapRenderer) {
		this.element = data;
		this.mapRenderer = mapRenderer;
		init();
	}

	@Override
	void initializeView(final GridBagConstraints c) {
		c.gridx = 0;
		addLabel(c, LABEL_HEIGHT);
		c.gridx++;
		JSpinner spinner = addSpinner(element.getHeight(), FlatNode.MAX_HEIGHT, TilesLiftDialog.STEP, c);
		c.gridx = 0;
		c.gridy++;
		addOkButton(c, e -> {
			mapRenderer.onEnvObjectDefined(element, ((Double) spinner.getModel().getValue()).floatValue());
			closeDialog();
		});
	}

	@Override
	public String getDialogTitle() {
		return "Define Environment Object";
	}
}
