package com.necromine.editor.model.elements;

import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.model.node.Node;
import lombok.Getter;

@Getter
public class PlacedElement {
	protected final ElementDefinition definition;
	private final Direction facingDirection;
	private final Node node;

	public PlacedElement(final Node node,
						 final ElementDefinition definition,
						 final Direction selectedDirection) {
		this.node = node;
		this.definition = definition;
		this.facingDirection = selectedDirection;
	}

}
