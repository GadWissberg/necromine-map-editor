package com.gadarts.necromine.editor.desktop.dialogs;

import com.gadarts.necromine.model.map.MapNodeData;
import com.necromine.editor.MapRenderer;
import com.necromine.editor.model.node.FlatNode;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.necromine.editor.model.node.FlatNode.MAX_HEIGHT;

public class TilesLiftDialog extends DialogPane {
	private static final String LABEL_HEIGHT = "Height: ";
	static final float STEP = 0.1f;
	private final FlatNode src;
	private final FlatNode dst;
	private final MapRenderer mapRenderer;

	public TilesLiftDialog(final FlatNode src, final FlatNode dst, final MapRenderer mapRenderer) {
		this.src = src;
		this.dst = dst;
		this.mapRenderer = mapRenderer;
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
			if (value >= 0) {
				mapRenderer.onTilesLift(src, dst, value);
			}
			closeDialog();
		});
	}

	private JSpinner addHeightSpinner(final GridBagConstraints c) {
		c.gridx++;
		List<MapNodeData> nodes = mapRenderer.getRegion(src, dst);
		float initialValue = -1;
		if (nodes.stream().allMatch(n -> n.getHeight() == nodes.get(0).getHeight())) {
			initialValue = nodes.get(0).getHeight();
		}
		return addSpinner(initialValue, MAX_HEIGHT, STEP, c, false);
	}

	@Override
	public String getDialogTitle() {
		return "Lift Tiles";
	}
}
