package com.necromine.editor.mode;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.gadarts.necromine.assets.GameAssetsManager;
import com.gadarts.necromine.model.map.MapNodeData;
import com.necromine.editor.CameraManipulation;
import com.necromine.editor.actions.processes.MappingProcess;
import com.necromine.editor.handlers.action.ActionsHandler;
import com.necromine.editor.utils.Utils;
import lombok.Getter;

import java.util.Set;

@Getter
public enum ViewModes implements EditorMode {
	PAN((lastMouseTouchPosition, camera, screenX, screenY, vector3) -> {
		Vector2 velocity = lastMouseTouchPosition.sub(screenX, screenY).scl(0.03f);
		Vector3 left = Utils.auxVector3_1.set(camera.direction).crs(camera.up).nor().scl(0.3f);
		float x = camera.direction.x * -velocity.y + left.x * velocity.x;
		float z = camera.direction.z * -velocity.y + left.z * velocity.x;
		camera.translate(x, 0, z);
	}),

	ROTATE((lastMouseTouchPosition, camera, screenX, screenY, rotationPoint) -> {
		Vector2 velocity = lastMouseTouchPosition.sub(screenX, screenY).scl(0.12f);
		camera.rotateAround(rotationPoint, Vector3.Y, velocity.x);
	}),

	ZOOM((lastMouseTouchPosition, camera, screenX, screenY, vector3) -> {
		Vector2 velocity = lastMouseTouchPosition.sub(screenX, screenY).scl(0.005f);
		camera.zoom = Math.min(Math.max(0.2f, camera.zoom + velocity.y), 2f);
	});

	private final CameraManipulation manipulation;

	ViewModes(final CameraManipulation cameraManipulation) {
		this.manipulation = cameraManipulation;
	}

	@Override
	public void onTouchDownLeft(final MappingProcess<? extends MappingProcess.FinishProcessParameters> currentProcess,
								final ActionsHandler tool,
								final GameAssetsManager actionsHandler,
								final Set<MapNodeData> initializedTiles) {

	}
}
