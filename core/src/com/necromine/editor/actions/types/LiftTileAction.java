package com.necromine.editor.actions.types;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.necromine.editor.GameMap;
import com.necromine.editor.model.node.MapNode;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.node.Wall;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.Optional;

@Setter(AccessLevel.PACKAGE)
public class LiftTileAction extends MappingAction {
	public static final float STEP = 0.1F;
	private final static Vector3 auxVector = new Vector3();

	private Node node;
	private int direction;
	private Model wallModel;
	private GameAssetsManager assetsManager;

	LiftTileAction(final GameMap map) {
		super(map);
	}

	@Override
	protected void execute() {
		int row = node.getRow();
		int col = node.getCol();
		MapNode[][] tiles = map.getTiles();
		Optional.ofNullable(tiles[row][col]).ifPresent(selectedNode -> {
			selectedNode.lift(direction * STEP);
			Optional.ofNullable(tiles[row - 1][col]).ifPresent(north -> adjustNorthWall(selectedNode, north));
			Optional.ofNullable(tiles[row][col + 1]).ifPresent(east -> adjustEastWall(selectedNode, east));
			Optional.ofNullable(tiles[row + 1][col]).ifPresent(south -> adjustSouthWall(selectedNode, south));
			Optional.ofNullable(tiles[row][col - 1]).ifPresent(west -> adjustWestWall(selectedNode, west));
		});
	}

	private void adjustNorthWall(final MapNode southernNode,
								 final MapNode northernNode) {
		if (northernNode.getSouthWall() == null && southernNode.getNorthWall() == null) {
			createNorthWall(southernNode.getRow(), southernNode.getCol(), southernNode);
		}
		adjustWallBetweenNorthAndSouth(southernNode, northernNode);
	}

	private void adjustWallBetweenNorthAndSouth(final MapNode southernNode,
												final MapNode northernNode) {
		Wall wallBetween = Optional.ofNullable(southernNode.getNorthWall())
				.orElse(northernNode.getSouthWall());
		ModelInstance modelInstance = wallBetween.getModelInstance();
		TextureAttribute textureAttribute = (TextureAttribute) modelInstance.materials.get(0).get(TextureAttribute.Diffuse);
		textureAttribute.scaleV = adjustWallBetweenTwoNodes(southernNode, northernNode, wallBetween);
		float degrees = (southernNode.getHeight() > northernNode.getHeight() ? -1 : 1) * 90F;
		modelInstance.transform.rotate(Vector3.X, degrees);
	}

	private void adjustWallBetweenEastAndWest(final MapNode eastNode,
											  final MapNode westNode) {
		Wall wallBetween = Optional.ofNullable(eastNode.getWestWall())
				.orElse(westNode.getEastWall());
		ModelInstance modelInstance = wallBetween.getModelInstance();
		TextureAttribute textureAttribute = (TextureAttribute) modelInstance.materials.get(0).get(TextureAttribute.Diffuse);
		textureAttribute.scaleU = adjustWallBetweenTwoNodes(eastNode, westNode, wallBetween);
		modelInstance.transform.rotate(Vector3.Z, (eastNode.getHeight() > westNode.getHeight() ? 1 : -1) * 90F);
	}

	private float adjustWallBetweenTwoNodes(final MapNode eastOrSouthNode,
											final MapNode westOrNorthNode,
											final Wall wallBetween) {
		Vector3 wallBetweenThemPos = wallBetween.getModelInstance().transform.getTranslation(auxVector);
		float eastOrSouthHeight = eastOrSouthNode.getHeight();
		float westOrNorthHeight = westOrNorthNode.getHeight();
		float sizeHeight = Math.abs(westOrNorthHeight - eastOrSouthHeight);
		float y = Math.min(eastOrSouthHeight, westOrNorthHeight) + (eastOrSouthHeight > westOrNorthHeight ? 0 : sizeHeight);
		wallBetween.getModelInstance().transform.setToTranslationAndScaling(wallBetweenThemPos.x, y, wallBetweenThemPos.z,
				1, sizeHeight, 1);
		return sizeHeight;
	}

	private void adjustSouthWall(final MapNode northernNode,
								 final MapNode southernNode) {
		if (southernNode.getNorthWall() == null && northernNode.getSouthWall() == null) {
			createSouthWall(northernNode.getRow(), southernNode.getCol(), northernNode);
		}
		adjustWallBetweenNorthAndSouth(southernNode, northernNode);
	}

	private void adjustEastWall(final MapNode westernNode,
								final MapNode easternNode) {
		if (westernNode.getEastWall() == null && easternNode.getWestWall() == null) {
			createEastWall(westernNode.getRow(), westernNode.getCol(), westernNode);
		}
		adjustWallBetweenEastAndWest(easternNode, westernNode);
	}

	private void adjustWestWall(final MapNode easternNode, final MapNode westernNode) {
		if (westernNode.getEastWall() == null && easternNode.getWestWall() == null) {
			createWestWall(easternNode.getRow(), easternNode.getCol(), easternNode);
		}
		adjustWallBetweenEastAndWest(easternNode, westernNode);
	}

	private void createNorthWall(final int row, final int col, final MapNode n) {
		Wall northWall = createWall();
		n.setNorthWall(northWall);
		ModelInstance modelInstance = northWall.getModelInstance();
		modelInstance.transform.setToTranslation(col, 0, row);
		modelInstance.transform.rotate(Vector3.X, -90);
	}

	private void createSouthWall(final int row, final int col, final MapNode selected) {
		Wall southWall = createWall();
		selected.setSouthWall(southWall);
		ModelInstance modelInstance = southWall.getModelInstance();
		modelInstance.transform.setToTranslation(col, 0, row + 1);
		modelInstance.transform.rotate(Vector3.X, 90);
	}

	private void createEastWall(final int row, final int col, final MapNode selected) {
		Wall eastWall = createWall();
		selected.setEastWall(eastWall);
		ModelInstance modelInstance = eastWall.getModelInstance();
		modelInstance.transform.setToTranslation(col + 1F, 0, row);
		modelInstance.transform.rotate(Vector3.Z, -90);
	}

	private Wall createWall() {
		ModelInstance modelInstance = new ModelInstance(wallModel);
		TextureAttribute textureAttribute = (TextureAttribute) modelInstance.materials.get(0).get(TextureAttribute.Diffuse);
		textureAttribute.textureDescription.texture = assetsManager.getTexture(Assets.FloorsTextures.FLOOR_0);
		return new Wall(modelInstance, Assets.FloorsTextures.FLOOR_0);
	}

	private void createWestWall(final int row, final int col, final MapNode selected) {
		Wall westWall = createWall();
		selected.setWestWall(westWall);
		ModelInstance modelInstance = westWall.getModelInstance();
		modelInstance.transform.setToTranslation(col, 0, row);
		modelInstance.transform.rotate(Vector3.Z, 90);
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
