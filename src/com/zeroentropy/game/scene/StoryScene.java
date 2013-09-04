package com.zeroentropy.game.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.opengl.util.GLState;

import com.zeroentropy.game.scene.SceneManager.SceneType;

public class StoryScene extends BaseScene {

	private Sprite storyBackground;

	@Override
	public void createScene() {
		this.setBackground(new Background(1,1,1));
		storyBackground = new Sprite(0, 0, resourcesManager.bg_story_region, vbom) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		// splash.setScale(1.5f);
		// splash.setPosition(400, 240);
		attachChild(storyBackground);
	}

	@Override
	public void onBackKeyPressed() {
		// TODO Auto-generated method stub

	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_STORY;
	}

	@Override
	public void disposeScene() {
		storyBackground.detachSelf();
		storyBackground.dispose();
		this.detachSelf();
		this.dispose();

	}
	
}
