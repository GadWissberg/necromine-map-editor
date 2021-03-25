package com.gadarts.necromine.editor.desktop.dialogs;

import com.necromine.editor.actions.ActionAnswer;
import com.necromine.editor.model.elements.PlacedElement;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SelectObjectInNodeDialog extends DialogPane {
	private final ActionAnswer<PlacedElement> answer;
	private final List<? extends PlacedElement> elementsInTheNode;

	public SelectObjectInNodeDialog(final List<? extends PlacedElement> elementsInTheNode,
									final ActionAnswer<PlacedElement> answer) {
		this.elementsInTheNode = elementsInTheNode;
		this.answer = answer;
	}

	@Override
	void initializeView(final GridBagConstraints c) {
		JList<JLabel> list = new JList<>();
		ListSelectionModel listSelectionModel = list.getSelectionModel();
		listSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		Button ok = addOkButton(c, e -> answer.apply(elementsInTheNode.get(listSelectionModel.getSelectedIndices()[0])));
		listSelectionModel.addListSelectionListener(e -> ok.setEnabled(true));
		elementsInTheNode.forEach(element -> list.add(new JLabel(element.getDefinition().getDisplayName())));
		add(list);
		ok.setEnabled(false);
	}

	@Override
	public String getDialogTitle() {
		return null;
	}
}
