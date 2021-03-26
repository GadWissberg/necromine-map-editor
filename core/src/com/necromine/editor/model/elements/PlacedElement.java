package com.necromine.editor.model.elements;

import com.gadarts.necromine.model.ElementDefinition;
import com.gadarts.necromine.model.characters.Direction;
import com.necromine.editor.model.node.Node;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import static com.gadarts.necromine.model.characters.Direction.SOUTH;

@Getter
public class PlacedElement {

	private final ElementDefinition definition;
	private final Direction facingDirection;
	private final Node node;
	@Setter
	private float height;

	public PlacedElement(final PlacedElementParameters parameters) {
		this.definition = parameters.getDefinition();
		this.facingDirection = parameters.getFacingDirection();
		this.node = parameters.getNode();
		this.height = parameters.getHeight();
	}

	@Override
	public String toString() {
		return definition.getDisplayName();
	}

	@RequiredArgsConstructor
	@Getter
	public static class PlacedElementParameters {
		protected final ElementDefinition definition;
		private final Direction facingDirection;
		private final Node node;
		private final float height;

		public PlacedElementParameters(final ElementDefinition definition, final Node node, final float height) {
			this.definition = definition;
			this.node = node;
			this.height = height;
			this.facingDirection = SOUTH;
		}
	}
}
