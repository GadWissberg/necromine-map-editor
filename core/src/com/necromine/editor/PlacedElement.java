package com.necromine.editor;

import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;

@Getter
public class PlacedElement  {
	protected final int row;
	protected final int col;
	protected final ElementDefinition definition;
	private final Direction facingDirection;

	public PlacedElement(final int row,
						 final int col,
						 final ElementDefinition definition,
						 final Direction selectedDirection) {
		this.row = row;
		this.col = col;
		this.definition = definition;
		this.facingDirection =selectedDirection;
	}
}
