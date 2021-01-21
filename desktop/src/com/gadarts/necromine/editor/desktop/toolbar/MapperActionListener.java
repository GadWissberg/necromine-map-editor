package com.gadarts.necromine.editor.desktop.toolbar;

import com.gadarts.necromine.editor.desktop.ModesHandler;
import lombok.Setter;

import java.awt.event.ActionListener;
@Setter
public abstract class MapperActionListener implements ActionListener {
	protected ModesHandler modesHandler;
}
