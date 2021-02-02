package com.gadarts.necromine.editor.desktop;

import com.necromine.editor.CameraModes;
import com.necromine.editor.EditModes;
import com.necromine.editor.EditorMode;
import lombok.Getter;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Optional;

@Getter
public class ModesHandler extends Component implements PropertyChangeListener {

	@Getter
	private static EditorMode mode = EditModes.TILES;

	public void setMode(final EditorMode mode) {
		if (ModesHandler.mode == mode) return;
		Class<? extends EditorMode> modeType = mode.getClass();
		String name = null;
		if (modeType.equals(CameraModes.class)) {
			name = Events.MODE_SET_CAMERA.name();
		} else if (modeType.equals(EditModes.class)) {
			name = Events.MODE_SET_EDIT.name();
		}
		Optional.ofNullable(name).ifPresent(n -> {
			ModesHandler.mode = mode;
			firePropertyChange(n, -1, mode.ordinal());
		});
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if (propertyName.equals(Events.REQUEST_TO_SET_EDIT_MODE.name())) {
			setMode(EditModes.values()[(int) evt.getNewValue()]);
		} else if (propertyName.equals(Events.REQUEST_TO_SET_CAMERA_MODE.name())) {
			setMode(CameraModes.values()[(int) evt.getNewValue()]);
		}
	}
}
