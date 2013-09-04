package com.zeroentropy.game.scene;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.util.GLState;
import org.andengine.util.color.Color;

import com.zeroentropy.game.scene.SceneManager.SceneType;

public class LoadingScene extends BaseScene {

	@Override
	public void createScene() {
		setBackground(new Background(1,1,1));
//		createBackground();
		// or: new Background(1,1,1));
		final Text text = new Text(0, 0, resourcesManager.lFont, "Loading...", vbom);
		float w = text.getLineWidthMaximum()/2;
		attachChild(new Text(SCENE_CENTER_X-w, SCENE_CENTER_Y, resourcesManager.lFont, "Loading...", vbom));
	}

//	private void createBackground() {
//		attachChild(new Sprite(0, 0,
//				resourcesManager.story_region, vbom) {
//			@Override
//			protected void preDraw(GLState pGLState, Camera pCamera) {
//				super.preDraw(pGLState, pCamera);
//				pGLState.enableDither();
//			}
//		});
//	}
	
	@Override
	public void onBackKeyPressed() {
		return;
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_LOADING;
	}

	@Override
	public void disposeScene() {

	}
	
}
