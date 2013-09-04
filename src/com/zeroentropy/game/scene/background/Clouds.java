package com.zeroentropy.game.scene.background;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.sprite.Sprite;

import com.zeroentropy.game.ResourcesManager;
import org.andengine.util.math.MathUtils;
import com.zeroentropy.game.util.ZeroConstants;

public class Clouds extends Entity {

	private int mFirstLay, mLaySize;
	private float mCloudWidth, mCloudHeight;
	private static final int VELOCITY_Y = -128;

	public Clouds(final int pFirstLay, final int pLaySize, final int pItemSize) {
		this.mCloudWidth = ResourcesManager.getInstance().mCloudRegion.getWidth();
		this.mCloudHeight = ResourcesManager.getInstance().mCloudRegion.getHeight();
		mFirstLay = pFirstLay;
		mLaySize = pLaySize;
		for (int i = 0; i < pItemSize; i++) {
			this.attachChild(new Cloud());
		}
	}


	class Cloud extends Sprite {

		private final PhysicsHandler mPhysicsHandler;

		public Cloud() {
			super(0, 0, ResourcesManager.getInstance().mCloudRegion, ResourcesManager.getInstance().vbom);
			this.mPhysicsHandler = new PhysicsHandler(this);
			this.registerUpdateHandler(this.mPhysicsHandler);
			onManagedInitialize();
		}

		private void onManagedInitialize() {
			final int z = mFirstLay + MathUtils.random(mLaySize);// 0~mMaxCloudLay-1
			final float v_y = VELOCITY_Y / z;
			final float w = mCloudWidth / z;
			// final float h = mCloudHeight / z;
			final float x = MathUtils.random(ZeroConstants.SCENE_WIDTH) - w / 2;
			final float y = MathUtils.randomFactor(ZeroConstants.SCENE_HEIGHT, 1.0f);
			this.setFlippedHorizontal(MathUtils.randomBoolean());
			this.setPosition(x, y);
			this.setScale(1.0f / z);
			mPhysicsHandler.setVelocityY(MathUtils.randomFactor(v_y, 0.5f));
		}

		@Override
		protected void onManagedUpdate(final float pSecondsElapsed) {

			if (this.mY < -mCloudHeight) {
				this.onManagedInitialize();
			}

			super.onManagedUpdate(pSecondsElapsed);

		}

	}
}
