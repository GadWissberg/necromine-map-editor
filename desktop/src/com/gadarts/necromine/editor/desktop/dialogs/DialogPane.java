package com.gadarts.necromine.editor.desktop.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class DialogPane extends JPanel {
	private static final int PADDING = 10;
	private static final String BUTTON_LABEL_OK = "OK";

	protected GridBagConstraints createGridBagConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		return c;
	}

	protected void addOkButton(final GridBagConstraints c, final ActionListener actionListener) {
		c.gridwidth = 2;
		Button ok = new Button(BUTTON_LABEL_OK);
		ok.addActionListener(actionListener);
		add(ok, c);
	}

	public abstract String getDialogTitle();

	public DialogPane() {
		setLayout(new GridBagLayout());
	}

	void closeDialog() {
		((Dialog) getRootPane().getParent()).dispose();
	}

	protected void addLabel(final GridBagConstraints c, final String label) {
		add(new JLabel(label), c);
		c.gridy += 1;
	}
}
