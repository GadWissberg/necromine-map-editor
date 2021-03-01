package com.necromine.editor.actions.types;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.Assets;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.necromine.editor.GameMap;
import com.necromine.editor.actions.MappingAction;
import com.necromine.editor.model.node.MapNode;
import com.necromine.editor.model.node.Node;
import com.necromine.editor.model.node.Wall;
import lombok.AccessLevel;
import lombok.Setter;

import java.util.Optional;

import static com.necromine.editor.MapEditor.auxVector3_1;

@Setter(AccessLevel.PACKAGE)
public class LiftTileAction extends MappingAction {
	public static final float STEP = 0.1F;

	private Node node;
	private int direction;
	private Model wallModel;
	private GameAssetsManager assetsManager;

	LiftTileAction(final GameMap map) {
		super(map);
	}

	public static void adjustWallBetweenNorthAndSouth(final MapNode southernNode,
													  final MapNode northernNode) {
		Wall wallBetween = Optional.ofNullable(southernNode.getNorthWall()).orElse(northernNode.getSouthWall());
		ModelInstance modelInstance = wallBetween.getModelInstance();
		TextureAttribute textureAttribute = (TextureAttribute) modelInstance.materials.get(0).get(TextureAttribute.Diffuse);
		textureAttribute.scaleV = adjustWallBetweenTwoNodes(southernNode, northernNode, wallBetween);
		float degrees = (southernNode.getHeight() > northernNode.getHeight() ? -1 : 1) * 90F;
		modelInstance.transform.rotate(Vector3.X, degrees);
	}

	public static void createNorthWall(final MapNode n,
									   final Model wallModel,
									   final GameAssetsManager assetsManager,
									   final Assets.FloorsTextures definition) {
		Wall northWall = createWall(wallModel, assetsManager, definition);
		n.setNorthWall(northWall);
		ModelInstance modelInstance = northWall.getModelInstance();
		modelInstance.transform.setToTranslation(n.getCol(), 0, n.getRow());
		modelInstance.transform.rotate(Vector3.X, -90);
	}

	public static Wall createWall(final Model wallModel,
								  final GameAssetsManager assetsManager,
								  final Assets.FloorsTextures definition) {
		ModelInstance modelInstance = new ModelInstance(wallModel);
		TextureAttribute textureAttribute = (TextureAttribute) modelInstance.materials.get(0).get(TextureAttribute.Diffuse);
		textureAttribute.textureDescription.texture = assetsManager.getTexture(definition);
		return new Wall(modelInstance, Assets.FloorsTextures.FLOOR_0);
	}

	public static void adjustWallBetweenEastAndWest(final MapNode eastNode,
													final MapNode westNode) {
		Wall wallBetween = Optional.ofNullable(eastNode.getWestWall()).orElse(westNode.getEastWall());
		ModelInstance modelInstance = wallBetween.getModelInstance();
		TextureAttribute textureAttribute = (TextureAttribute) modelInstance.materials.get(0).get(TextureAttribute.Diffuse);
		textureAttribute.scaleU = adjustWallBetweenTwoNodes(eastNode, westNode, wallBetween);
		modelInstance.transform.rotate(Vector3.Z, (eastNode.getHeight() > westNode.getHeight() ? 1 : -1) * 90F);
	}

	public static float adjustWallBetweenTwoNodes(final MapNode eastOrSouthNode,
												  final MapNode westOrNorthNode,
												  final Wall wallBetween) {
		Vector3 wallBetweenThemPos = wallBetween.getModelInstance().transform.getTranslation(auxVector3_1);
		float eastOrSouthHeight = eastOrSouthNode.getHeight();
		float westOrNorthHeight = westOrNorthNode.getHeight();
		float sizeHeight = Math.abs(westOrNorthHeight - eastOrSouthHeight);
		float y = Math.min(eastOrSouthHeight, westOrNorthHeight) + (eastOrSouthHeight > westOrNorthHeight ? 0 : sizeHeight);
		wallBetween.getModelInstance().transform.setToTranslationAndScaling(wallBetweenThemPos.x, y, wallBetweenThemPos.z,
				1, sizeHeight, 1);
		return sizeHeight;
	}

	public static void createWestWall(final MapNode n,
									  final Model wallModel,
									  final GameAssetsManager assetsManager,
									  final Assets.FloorsTextures definition) {
		Wall westWall = createWall(wallModel, assetsManager, definition);
		n.setWestWall(westWall);
		ModelInstance modelInstance = westWall.getModelInstance();
		modelInstance.transform.setToTranslation(n.getCol(), 0, n.getRow());
		modelInstance.transform.rotate(Vector3.Z, 90);
	}

	public static void createSouthWall(final MapNode n,
									   final Model wallModel,
									   final GameAssetsManager assetsManager,
									   final Assets.FloorsTextures definition) {
		Wall southWall = createWall(wallModel, assetsManager, definition);
		n.setSouthWall(southWall);
		ModelInstance modelInstance = southWall.getModelInstance();
		modelInstance.transform.setToTranslation(n.getCol(), 0, n.getRow() + 1);
		modelInstance.transform.rotate(Vector3.X, 90);
	}

	public static void createEastWall(final MapNode n,
									  final Model wallModel,
									  final GameAssetsManager assetsManager,
									  final Assets.FloorsTextures definition) {
		Wall eastWall = createWall(wallModel, assetsManager, definition);
		n.setEastWall(eastWall);
		ModelInstance modelInstance = eastWall.getModelInstance();
		modelInstance.transform.setToTranslation(n.getCol() + 1F, 0, n.getRow());
		modelInstance.transform.rotate(Vector3.Z, -90);
	}

	@Override
	protected void execute() {
		int row = node.getRow();
		int col = node.getCol();
		MapNode[][] tiles = map.getNodes();
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
			createNorthWall(southernNode, wallModel, assetsManager, Assets.FloorsTextures.FLOOR_0);
		}
		adjustWallBetweenNorthAndSouth(southernNode, northernNode);
	}

	private void adjustSouthWall(final MapNode northernNode,
								 final MapNode southernNode) {
		if (southernNode.getNorthWall() == null && northernNode.getSouthWall() == null) {
			createSouthWall(northernNode, wallModel, assetsManager, Assets.FloorsTextures.FLOOR_0);
		}
		adjustWallBetweenNorthAndSouth(southernNode, northernNode);
	}

	private void adjustEastWall(final MapNode westernNode,
								final MapNode easternNode) {
		if (westernNode.getEastWall() == null && easternNode.getWestWall() == null) {
			createEastWall(westernNode, wallModel, assetsManager, Assets.FloorsTextures.FLOOR_0);
		}
		adjustWallBetweenEastAndWest(easternNode, westernNode);
	}

	private void adjustWestWall(final MapNode easternNode, final MapNode westernNode) {
		if (westernNode.getEastWall() == null && easternNode.getWestWall() == null) {
			createWestWall(easternNode, wallModel, assetsManager, Assets.FloorsTextures.FLOOR_0);
		}
		adjustWallBetweenEastAndWest(easternNode, westernNode);
	}

	@Override
	public boolean isProcess() {
		return false;
	}
}
