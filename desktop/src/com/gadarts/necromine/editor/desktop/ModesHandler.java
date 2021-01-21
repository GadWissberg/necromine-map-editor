package com.gadarts.necromine.editor.desktop;

import com.necromine.editor.EditorModes;
import lombok.Getter;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

@Getter
public class ModesHandler extends Component implements PropertyChangeListener {

	@Getter
	private static EditorModes mode = EditorModes.TILES;

	public void setMode(final EditorModes mode) {
		if (ModesHandler.mode == mode) return;
		firePropertyChange(Events.MODE_CHANGED.name(), ModesHandler.mode.ordinal(), mode.ordinal());
		ModesHandler.mode = mode;
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals(Events.REQUEST_TO_CHANGE_MODE.name())) {
			setMode(EditorModes.values()[(int) evt.getNewValue()]);
		}
	}
}
