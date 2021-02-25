package com.necromine.editor.actions.types;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.necromine.editor.GameMap;
import com.necromine.editor.MapNode;
import com.necromine.editor.Node;
import com.necromine.editor.actions.MappingAction;

import java.util.Optional;

public class LiftTileAction extends MappingAction {
    public static final float STEP = 0.1F;
    private final static Vector3 auxVector = new Vector3();
    private final Node node;
    private final int direction;
    private final Model wallModel;

    public LiftTileAction(final GameMap map, final Node node, final int direction, final Model wallModel) {
        super(map);
        this.node = node;
        this.direction = direction;
        this.wallModel = wallModel;
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
        ModelInstance wallBetween = Optional.ofNullable(southernNode.getNorthWall()).orElse(northernNode.getSouthWall());
        adjustWallBetweenTwoNodes(southernNode, northernNode, wallBetween);
        float degrees = (southernNode.getHeight() > northernNode.getHeight() ? -1 : 1) * 90F;
        wallBetween.transform.rotate(Vector3.X, degrees);
    }

    private void adjustWallBetweenEastAndWest(final MapNode eastNode,
                                              final MapNode westNode) {
        ModelInstance wallBetween = Optional.ofNullable(eastNode.getWestWall()).orElse(westNode.getEastWall());
        adjustWallBetweenTwoNodes(eastNode, westNode, wallBetween);
        wallBetween.transform.rotate(Vector3.Z, (eastNode.getHeight() > westNode.getHeight() ? 1 : -1) * 90F);
    }

    private void adjustWallBetweenTwoNodes(final MapNode eastOrSouthNode,
                                           final MapNode westOrNorthNode,
                                           final ModelInstance wallBetween) {
        Vector3 wallBetweenThemPos = wallBetween.transform.getTranslation(auxVector);
        float eastOrSouthHeight = eastOrSouthNode.getHeight();
        float westOrNorthHeight = westOrNorthNode.getHeight();
        float sizeHeight = Math.abs(westOrNorthHeight - eastOrSouthHeight);
        float offset = eastOrSouthHeight > westOrNorthHeight ? 0 : sizeHeight;
        float y = Math.min(eastOrSouthHeight, westOrNorthHeight) + offset;
        wallBetween.transform.setToTranslationAndScaling(wallBetweenThemPos.x, y, wallBetweenThemPos.z,
                1, sizeHeight, 1);
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
        ModelInstance northWall = new ModelInstance(wallModel);
        n.setNorthWall(northWall);
        northWall.transform.setToTranslation(col, 0, row);
        northWall.transform.rotate(Vector3.X, -90);
    }

    private void createSouthWall(final int row, final int col, final MapNode selected) {
        ModelInstance southWall = new ModelInstance(wallModel);
        selected.setSouthWall(southWall);
        southWall.transform.setToTranslation(col, 0, row + 1);
        southWall.transform.rotate(Vector3.X, 90);
    }

    private void createEastWall(final int row, final int col, final MapNode selected) {
        ModelInstance eastWall = new ModelInstance(wallModel);
        selected.setEastWall(eastWall);
        eastWall.transform.setToTranslation(col + 1F, 0, row);
        eastWall.transform.rotate(Vector3.Z, -90);
    }

    private void createWestWall(final int row, final int col, final MapNode selected) {
        ModelInstance wastWall = new ModelInstance(wallModel);
        selected.setWestWall(wastWall);
        wastWall.transform.setToTranslation(col, 0, row);
        wastWall.transform.rotate(Vector3.Z, 90);
    }

    @Override
    public boolean isProcess() {
        return false;
    }
}
