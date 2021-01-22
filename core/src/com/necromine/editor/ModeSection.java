package com.necromine.editor;

import com.gadarts.necromine.model.ElementDefinition;
import lombok.Getter;

@Getter
public class ModeSection {
	private final String header;
	private final ElementDefinition[] definitions;

	public ModeSection(final String header, final ElementDefinition[] definitions) {
		this.header = header;
		this.definitions = definitions;
	}
}
