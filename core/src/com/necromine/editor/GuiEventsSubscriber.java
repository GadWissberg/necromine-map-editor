package com.necromine.editor;

import com.gadarts.necromine.Assets;

public interface GuiEventsSubscriber {
	void onTileSelected(Assets.FloorsTextures texture);

	void onModeChanged(EditorModes mode);
}
