package com.gadarts.necromine.editor.desktop;

import com.necromine.editor.mode.CameraModes;
import com.necromine.editor.mode.EditModes;
import com.necromine.editor.mode.EditorMode;
import com.necromine.editor.mode.tools.EditorTool;
import com.necromine.editor.mode.tools.TilesTools;
import lombok.Getter;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Optional;

@Getter
public class ModesHandler extends Component implements PropertyChangeListener {

	@Getter
	private static EditorMode mode = EditModes.TILES;

	@Getter
	private static EditorTool tool = TilesTools.BRUSH;

	public void applyMode(final EditorMode mode) {
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

	public void setTool(final TilesTools tool) {
		if (ModesHandler.tool == tool) return;
		Class<? extends EditorTool> toolType = tool.getClass();
		String name = null;
		if (toolType.equals(TilesTools.class)) {
			name = Events.TILE_TOOL_SET.name();
		}
		Optional.ofNullable(name).ifPresent(n -> {
			ModesHandler.tool = tool;
			firePropertyChange(n, -1, tool.ordinal());
		});
	}

	@Override
	public void propertyChange(final PropertyChangeEvent evt) {
		String propertyName = evt.getPropertyName();
		if (propertyName.equals(Events.REQUEST_TO_SET_EDIT_MODE.name())) {
			applyMode(EditModes.values()[(int) evt.getNewValue()]);
		} else if (propertyName.equals(Events.REQUEST_TO_SET_CAMERA_MODE.name())) {
			applyMode(CameraModes.values()[(int) evt.getNewValue()]);
		} else if (propertyName.equals(Events.REQUEST_TO_SET_TILE_TOOL.name())) {
			setTool(TilesTools.values()[(int) evt.getNewValue()]);
		}
	}
}
