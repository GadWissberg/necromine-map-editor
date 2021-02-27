package com.gadarts.necromine.editor.desktop;

import javax.swing.*;
import java.awt.*;

public abstract class DialogPane extends JPanel {
	abstract String getDialogTitle();

	void closeDialog() {
		((Dialog) getRootPane().getParent()).dispose();
	}
}
