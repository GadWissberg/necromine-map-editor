package com.necromine.editor.mode.tools;

public enum ElementTools implements EditorTool {
	BRUSH, DEFINE;

	@Override
	public boolean isForceCursorDisplay( ) {
		return false;
	}
}
