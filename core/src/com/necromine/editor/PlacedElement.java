package com.necromine.editor;

import com.gadarts.necromine.model.ElementDefinition;
import lombok.Getter;

@Getter
public class PlacedElement  {
	protected final int row;
	protected final int col;
	protected final ElementDefinition definition;

	public PlacedElement(final int row, final int col, final ElementDefinition definition) {
		this.row = row;
		this.col = col;
		this.definition = definition;
	}
}
