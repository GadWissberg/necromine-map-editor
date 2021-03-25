package com.gadarts.necromine.editor.desktop.dialogs;

import com.necromine.editor.actions.ActionAnswer;
import com.necromine.editor.model.elements.PlacedElement;
import com.necromine.editor.model.node.Node;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SelectEnvInNodeDialog extends DialogPane {
	private final Node node;
	private final ActionAnswer answer;

	public SelectEnvInNodeDialog(Node node, List<? extends PlacedElement> elementsInTheNode, ActionAnswer answer) {
		this.node = node;
		this.answer = answer;
	}

	@Override
	void initializeView(GridBagConstraints c) {
		JList list = new JList();
	}

	@Override
	public String getDialogTitle() {
		return null;
	}
}
