package com.necromine.editor;

import com.gadarts.necromine.model.characters.Direction;
import lombok.Getter;

@Getter
public class PlacedElement  {
	protected final int row;
	protected final int col;
	protected final Direction facingDirection;

	public PlacedElement(final int row, final int col, final Direction facingDirection) {
		this.row = row;
		this.col = col;
		this.facingDirection = facingDirection;
	}
}
