package com.necromine.editor;

import com.gadarts.necromine.Assets;

public abstract class MappingProcess extends MappingAction {
	private boolean processing;

	protected void begin() {
		processing = true;
	}

	protected void finish() {
		processing = false;
	}
}
