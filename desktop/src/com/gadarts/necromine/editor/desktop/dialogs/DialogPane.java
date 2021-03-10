package com.gadarts.necromine.editor.desktop.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class DialogPane extends JPanel {
	private static final int PADDING = 10;
	private static final String BUTTON_LABEL_OK = "OK";

	public DialogPane() {
		setLayout(new GridBagLayout());
	}

	void init() {
		GridBagConstraints c = createGridBagConstraints();
		initializeView(c);
	}

	protected GridBagConstraints createGridBagConstraints() {
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(PADDING, PADDING, PADDING, PADDING);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridy = 0;
		return c;
	}

	abstract void initializeView(GridBagConstraints c);

	protected void addOkButton(final GridBagConstraints c, final ActionListener actionListener) {
		c.gridwidth = 2;
		Button ok = new Button(BUTTON_LABEL_OK);
		ok.addActionListener(actionListener);
		add(ok, c);
	}

	protected JSpinner addSpinner(final float value, final int maximum, final float step, final GridBagConstraints c) {
		SpinnerModel model = new SpinnerNumberModel(value, 0, maximum, step);
		JSpinner jSpinner = new JSpinner(model);
		add(jSpinner, c);
		return jSpinner;
	}

	public abstract String getDialogTitle();

	void closeDialog() {
		((Dialog) getRootPane().getParent()).dispose();
	}

	protected void addLabel(final GridBagConstraints c, final String label) {
		add(new JLabel(label), c);
		c.gridy += 1;
	}
}
