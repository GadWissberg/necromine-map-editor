package com.necromine.editor;


public abstract class MappingProcess extends MappingAction {
	private boolean processing;

	protected void begin() {
		processing = true;
	}

	protected void finish() {
		processing = false;
	}
}
