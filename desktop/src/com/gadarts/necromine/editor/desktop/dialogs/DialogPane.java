package com.gadarts.necromine.editor.desktop.dialogs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class DialogPane extends JPanel {
	private static final int PADDING = 10;
	private static final String BUTTON_LABEL_OK = "OK";
	private static final int SPINNER_WIDTH = 50;
	private static final Color TEXT_COLOR = Color.BLACK;

	protected DialogPane() {
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

	protected Button addOkButton(final GridBagConstraints c, final ActionListener actionListener) {
		c.gridy++;
		c.gridwidth = 2;
		Button ok = new Button(BUTTON_LABEL_OK);
		ok.addActionListener(actionListener);
		ok.setForeground(TEXT_COLOR);
		add(ok, c);
		return ok;
	}

	protected JSpinner addSpinner(double value,
								  int maximum,
								  float step,
								  GridBagConstraints c) {
		return addSpinner(value, maximum, step, c, true);
	}

	protected JSpinner addSpinner(double value,
								  int maximum,
								  float step,
								  GridBagConstraints c,
								  boolean allowNegative) {
		SpinnerModel model = new SpinnerNumberModel(value, -1, maximum, step);
		model.setValue(value);
		JSpinner jSpinner = new JSpinner(model);
		add(jSpinner, c);
		Dimension preferredSize = jSpinner.getPreferredSize();
		jSpinner.setPreferredSize(new Dimension(SPINNER_WIDTH, preferredSize.height));
		jSpinner.addChangeListener(e -> {
			if (!allowNegative && ((Double) jSpinner.getValue()) < 0) {
				jSpinner.setValue(0.0);
			}
		});
		return jSpinner;
	}

	public abstract String getDialogTitle();

	void closeDialog() {
		((Dialog) getRootPane().getParent()).dispose();
	}

	protected void addLabel(final GridBagConstraints c, final String label) {
		add(new JLabel(label), c);
	}
}
