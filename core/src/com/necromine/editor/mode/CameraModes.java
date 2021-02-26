package com.necromine.editor.mode;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.necromine.editor.CameraManipulation;
import com.necromine.editor.MapHandler;
import lombok.Getter;

@Getter
public enum CameraModes implements EditorMode {
	PAN((lastMouseTouchPosition, camera, screenX, screenY) -> {
		Vector2 velocity = lastMouseTouchPosition.sub(screenX, screenY).scl(0.03f);
		Vector3 left = MapHandler.auxVector3_1.set(camera.direction).crs(camera.up).nor().scl(0.3f);
		float x = camera.direction.x * -velocity.y + left.x * velocity.x;
		float z = camera.direction.z * -velocity.y + left.z * velocity.x;
		camera.translate(x, 0, z);
	}),

	ROTATE((lastMouseTouchPosition, camera, screenX, screenY) -> {
		Vector2 velocity = lastMouseTouchPosition.sub(screenX, screenY).scl(0.12f);
		camera.rotate(Vector3.Y, velocity.x);
	}),

	ZOOM((lastMouseTouchPosition, camera, screenX, screenY) -> {
		Vector2 velocity = lastMouseTouchPosition.sub(screenX, screenY).scl(0.005f);
		camera.zoom = Math.min(Math.max(0.2f, camera.zoom + velocity.y), 2f);
	});

	private final CameraManipulation manipulation;

	CameraModes(final CameraManipulation cameraManipulation) {
		this.manipulation = cameraManipulation;
	}

}
