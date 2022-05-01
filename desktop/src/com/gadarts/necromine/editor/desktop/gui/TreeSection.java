package com.necromine.editor;

import com.gadarts.necromine.model.ElementDefinition;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TreeSection {
	private final String header;
	private final ElementDefinition[] definitions;
	private final String entryIcon;

}
