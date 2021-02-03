package com.necromine.editor;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;

public interface CameraManipulation {
	void run(Vector2 lastMouseTouchPosition, OrthographicCamera camera, int screenX, int screenY);
}
