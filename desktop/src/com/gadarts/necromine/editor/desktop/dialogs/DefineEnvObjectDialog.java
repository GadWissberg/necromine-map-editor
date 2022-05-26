package com.gadarts.necromine.editor.desktop.dialogs;

import com.necromine.editor.MapRenderer;
import com.necromine.editor.model.elements.PlacedEnvObject;
import com.necromine.editor.model.node.FlatNode;

import javax.swing.*;

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
	void initializeView( ) {
		JSpinner spinner = createSpinner(
				element.getHeight(),
				FlatNode.MAX_HEIGHT,
				TilesLiftDialog.STEP,
				0,
				false);
		addSpinnerWithLabel(LABEL_HEIGHT, spinner);
		addGeneralButtons(e -> {
			mapRenderer.onEnvObjectDefined(element, ((Double) spinner.getModel().getValue()).floatValue());
			closeDialog();
		});
	}

	@Override
	public String getDialogTitle( ) {
		return "Define Environment Object";
	}
}
