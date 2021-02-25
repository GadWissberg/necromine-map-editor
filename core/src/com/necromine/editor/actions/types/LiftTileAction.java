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
            Optional.ofNullable(tiles[row - 1][col]).ifPresent(north -> adjustNorthWall(selectedNode, north, north.getSouthWall()));
            Optional.ofNullable(tiles[row][col + 1]).ifPresent(east -> adjustEastWall(selectedNode, east, east.getWestWall()));
            Optional.ofNullable(tiles[row + 1][col]).ifPresent(south -> adjustSouthWall(selectedNode, south, south.getNorthWall()));
            Optional.ofNullable(tiles[row][col - 1]).ifPresent(west -> adjustWestWall(selectedNode, west, west.getEastWall()));
        });
    }

    private void adjustNorthWall(final MapNode southernNode,
                                 final MapNode northernNode,
                                 final ModelInstance wallBetweenThem) {
        if (wallBetweenThem == null && southernNode.getNorthWall() == null) {
            createNorthWall(southernNode.getRow(), southernNode.getCol(), southernNode);
        }
        adjustNeighborSouthOrNorthWall(southernNode, northernNode);
    }

    private void adjustNeighborSouthOrNorthWall(final MapNode southernNode,
                                                final MapNode northernNode) {
        ModelInstance wallBetween = Optional.ofNullable(southernNode.getNorthWall()).orElse(northernNode.getSouthWall());
        Vector3 wallBetweenThemPos = wallBetween.transform.getTranslation(auxVector);
        float southHeight = southernNode.getHeight();
        float northHeight = northernNode.getHeight();
        float sizeHeight = Math.abs(northHeight - southHeight);
        float y = Math.min(southHeight, northHeight) + (southHeight > northHeight ? 0 : sizeHeight);
        wallBetween.transform.setToTranslationAndScaling(wallBetweenThemPos.x, y, wallBetweenThemPos.z,
                1, sizeHeight, 1);
        wallBetween.transform.rotate(Vector3.X, (southHeight > northHeight ? -1 : 1) * 90F);
    }

    private void adjustSouthWall(final MapNode northernNode,
                                 final MapNode southernNode,
                                 final ModelInstance wallBetweenThem) {
        if (wallBetweenThem == null && northernNode.getSouthWall() == null) {
            createSouthWall(northernNode.getRow(), southernNode.getCol(), northernNode);
        }
        adjustNeighborSouthOrNorthWall(southernNode, northernNode);
    }

    private void adjustEastWall(final MapNode selected, final MapNode neighbor, final ModelInstance neighborWall) {
        int row = selected.getRow();
        int col = selected.getCol();
        if (neighborWall == null) {
            createEastWall(row, col, selected, neighbor);
        } else {
            Vector3 neighborWallPos = neighborWall.transform.getTranslation(auxVector);
            float height = selected.getHeight();
            float neighborHeight = neighbor.getHeight();
            boolean isHigherThanNeighbor = height > neighborHeight;
            float sizeHeight = Math.abs(neighborHeight - height);
            neighborWall.transform.setToTranslationAndScaling(neighborWallPos.x, Math.min(height, neighborHeight) + (isHigherThanNeighbor ? sizeHeight : 0), neighborWallPos.z,
                    1, sizeHeight, 1);
            neighborWall.transform.rotate(Vector3.Z, (isHigherThanNeighbor ? -1 : 1) * 90F);
        }
    }

    private void adjustWestWall(final MapNode selected, final MapNode neighbor, final ModelInstance neighborWall) {
        int row = selected.getRow();
        int col = selected.getCol();
        if (neighborWall == null) {
            createWestWall(row, col, selected, neighbor);
        } else {
            float height = selected.getHeight();
            float modelInstanceHeight = Math.abs(neighbor.getHeight() - height);
            boolean isHigherThanNeighbor = height > neighbor.getHeight();
            neighborWall.transform.setToTranslationAndScaling(col, Math.min(height, neighbor.getHeight()) + (isHigherThanNeighbor ? 0 : modelInstanceHeight), row,
                    1, Math.abs(neighbor.getHeight() - selected.getHeight()), 1);
            neighborWall.transform.rotate(Vector3.Z, (isHigherThanNeighbor ? 1 : -1) * 90F);
        }
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

    private void createEastWall(final int row, final int col, final MapNode n, final MapNode neighbor) {
        ModelInstance eastWall = new ModelInstance(wallModel);
        eastWall.transform.setToTranslationAndScaling(
                col + 1, n.getHeight(), row,
                1, Math.abs(neighbor.getHeight() - n.getHeight()), 1);
        eastWall.transform.rotate(Vector3.Z, -90);
        n.setEastWall(eastWall);
    }

    private void createWestWall(final int row, final int col, final MapNode n, final MapNode neighbor) {
        ModelInstance westWall = new ModelInstance(wallModel);
        westWall.transform.setToTranslationAndScaling(
                col, neighbor.getHeight(), row,
                1, Math.abs(neighbor.getHeight() - n.getHeight()), 1);
        westWall.transform.rotate(Vector3.Z, 90);
        n.setWestWall(westWall);
    }

    @Override
    public boolean isProcess() {
        return false;
    }
}
