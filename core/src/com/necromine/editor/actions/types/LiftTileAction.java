package com.necromine.editor.actions.types;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Attribute;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Vector3;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapNode;
import com.necromine.editor.Node;
import com.necromine.editor.actions.MappingAction;

import java.util.Optional;

public class LiftTileAction extends MappingAction {
	public static final float STEP = 0.1F;
	private final static Vector3 auxVector1 = new Vector3();
	private final static Vector3 auxVector2 = new Vector3();
	private final static Vector3 auxVector3 = new Vector3();
	private final static Vector3 auxVector4 = new Vector3();
	private final Node node;
	private final int direction;
	private final ModelBuilder modelBuilder;

	public LiftTileAction(final GameMap map, final Node node, final int direction, final ModelBuilder builder) {
		super(map);
		this.node = node;
		this.direction = direction;
		this.modelBuilder = builder;
	}

	@Override
	protected void execute() {
		int row = node.getRow();
		int col = node.getCol();
		MapNode[][] tiles = map.getTiles();
		Optional.ofNullable(tiles[row][col]).ifPresent(n -> {
			n.lift(direction * STEP);
			MapNode northMapNode = tiles[row - 1][col];
			if (northMapNode != null) {
				if (northMapNode.getSouthWall() == null) {
					float northMapNodeHeight = northMapNode.getHeight();
					float height = n.getHeight();
					ModelInstance northWall = new ModelInstance(createRectModel(
							auxVector1.set(col, northMapNodeHeight, row),
							auxVector2.set(col + 1, northMapNodeHeight, row),
							auxVector3.set(col + 1, height, row),
							auxVector4.set(col, height, row)
					));
					n.setNorthWall(northWall);
				}
			}
		});
	}
	//No need to remodel.
	private Model createRectModel(Vector3 northWest, Vector3 northEast, Vector3 southEast, Vector3 southWest) {
		Material material = new Material(ColorAttribute.createDiffuse(Color.RED));
		return modelBuilder.createRect(
				1, 0, 0,
				0, 0, 0,
				0, 0, 1,
				1, 0, 1,
				0, 1, 0,
				material,
				VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates
		);
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
