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

    private void adjustNorthWall(final MapNode selected, final MapNode neighbor, final ModelInstance neighborWall) {
        if (neighborWall == null) {
            createNorthWall(selected.getRow(), selected.getCol(), selected, neighbor);
        } else {
            adjustNeighborSouthWall(selected, neighbor, neighborWall);
        }
    }

    private void adjustNeighborSouthWall(final MapNode selected,
                                         final MapNode neighbor,
                                         final ModelInstance neighborWall) {
        Vector3 neighborWallPos = neighborWall.transform.getTranslation(auxVector);
        float height = selected.getHeight();
        float offset = height > neighbor.getHeight() ? 0 : Math.min(height, neighbor.getHeight());
        float modelInstanceHeight = Math.abs(neighbor.getHeight() - height) + offset;
        neighborWall.transform.setToTranslationAndScaling(neighborWallPos.x, modelInstanceHeight, neighborWallPos.z,
                1, modelInstanceHeight, 1);
        neighborWall.transform.rotate(Vector3.X, (height > neighbor.getHeight() ? -1 : 1) * 90F);
    }

    private void adjustSouthWall(final MapNode selected, final MapNode neighbor, final ModelInstance neighborWall) {
        int row = selected.getRow();
        int col = selected.getCol();
        if (neighborWall == null) {
            createSouthWall(row, col, selected, neighbor);
        } else {
            neighborWall.transform.setToTranslationAndScaling(col, selected.getHeight(), row,
                    1, Math.abs(neighbor.getHeight() - selected.getHeight()), 1);
            neighborWall.transform.rotate(Vector3.X, -90);
        }
    }

    private void adjustEastWall(final MapNode selected, final MapNode neighbor, final ModelInstance neighborWall) {
        int row = selected.getRow();
        int col = selected.getCol();
        if (neighborWall == null) {
            createEastWall(row, col, selected, neighbor);
        } else {
            neighborWall.transform.setToTranslationAndScaling(col, selected.getHeight(), row,
                    1, Math.abs(neighbor.getHeight() - selected.getHeight()), 1);
            neighborWall.transform.rotate(Vector3.Y, 90);
        }
    }

    private void adjustWestWall(final MapNode selected, final MapNode neighbor, final ModelInstance neighborWall) {
        int row = selected.getRow();
        int col = selected.getCol();
        if (neighborWall == null) {
            createWestWall(row, col, selected, neighbor);
        } else {
            float height = selected.getHeight();
            float offset = height > neighbor.getHeight() ? Math.min(height, neighbor.getHeight()) : 0;
            float modelInstanceHeight = Math.abs(neighbor.getHeight() - height);
            neighborWall.transform.setToTranslationAndScaling(col, Math.min(height, neighbor.getHeight()) + modelInstanceHeight, row,
                    1, Math.abs(neighbor.getHeight() - selected.getHeight()), 1);
            neighborWall.transform.rotate(Vector3.Z, (height > neighbor.getHeight() ? 1 : -1) * 90F);
        }
    }

    private void createNorthWall(final int row, final int col, final MapNode n, final MapNode northMapNode) {
        ModelInstance northWall = new ModelInstance(wallModel);
        northWall.transform.setToTranslationAndScaling(
                col, northMapNode.getHeight(), row,
                1, Math.abs(northMapNode.getHeight() - n.getHeight()), 1);
        northWall.transform.rotate(Vector3.X, -90);
        n.setNorthWall(northWall);
    }

    private void createSouthWall(final int row, final int col, final MapNode selected, final MapNode neighbor) {
        ModelInstance southWall = new ModelInstance(wallModel);
        southWall.transform.setToTranslationAndScaling(
                col, selected.getHeight(), row + 1,
                1, Math.abs(neighbor.getHeight() - selected.getHeight()), 1);
        southWall.transform.rotate(Vector3.X, 90);
        selected.setSouthWall(southWall);
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
