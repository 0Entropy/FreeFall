package com.zeroentropy.game.sprite.visual;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseQuadOut;

import com.zeroentropy.game.ResourcesManager;

public class FlashVisual extends Entity {

	private final static float FLASH_W = 60;
	private final static float FLASH_H = 120;
	private final static int FLASH_NUM = 5;
	private final static float ANGULAR_VELOCITY = 450;
	private final static float ANGULAR_DEGREE = 360 / FLASH_NUM;

	private final static float SHOW_DURATION = .35f;
	private final static float HIDE_DURATION = .65f;

	// private Sprite mFlash;
	private Flash[] mFlashs;

	public FlashVisual() {
		// mFlash = new Sprite(-FLASH_W * .5f, -FLASH_H,
		// ResourcesManager.getInstance().mFlashRegion,//
		// ResourcesManager.getInstance().vbom);
		//
		// this.attachChild(mFlash);

		mFlashs = new Flash[FLASH_NUM];
		for (int i = 0; i < FLASH_NUM; i++) {
			mFlashs[i] = new Flash(i);
			this.attachChild(mFlashs[i]);
		}
	}

	public void onInit() {
		for (int i = 0; i < FLASH_NUM; i++) {
			mFlashs[i].onInit();
		}
	}

	public void onShow() {
		for (int i = 0; i < FLASH_NUM; i++) {
			mFlashs[i].onShow();
		}
	}

	public void onHide() {
		for (int i = 0; i < FLASH_NUM; i++) {
			mFlashs[i].onHide();
		}
	}

	class Flash extends Sprite {

		private PhysicsHandler mPhysicsHandler;
		private ScaleModifier mShowModifier;
		private AlphaModifier mHideModifier;
		private int mIndex;

		public Flash(final int index) {
			super(-FLASH_W * .5f, -FLASH_H,
					ResourcesManager.getInstance().mFlashRegion,
					ResourcesManager.getInstance().vbom);
			setRotationCenter(FLASH_W * .5f, FLASH_H);
			setScaleCenter(FLASH_W * .5f, FLASH_H);
			mPhysicsHandler = new PhysicsHandler(this);
			this.registerUpdateHandler(mPhysicsHandler);
			mIndex = index;
			onInit();
		}

		private void onInit() {
			// setRotationCenter(ZeroConstants.HALF_SPRITE_WIDTH,
			// ZeroConstants.HALF_SPRITE_HEIGHT);
			mPhysicsHandler.setEnabled(false);
			mPhysicsHandler.setAngularVelocity(0);
			setRotation(MathUtils.random(360));//ANGULAR_DEGREE*mIndex);//(MathUtils.random(30)+//(MathUtils.random(360)
			setScale(0);
			setAlpha(0);
		}

		private void onShow() {
			final float scale_x = MathUtils.random(0.5f, 1.5f);// 1.3
			final float scale_y = MathUtils.random(.75f, 1.25f);// 1.8
			final float rotate = MathUtils.randomSign()
					* (scale_y * ANGULAR_VELOCITY);

			if (mShowModifier == null) {
				mShowModifier = new ScaleModifier(SHOW_DURATION, 0, scale_x, 0,
						scale_y, new IEntityModifierListener() {

							@Override
							public void onModifierStarted(
									final IModifier<IEntity> pModifier,
									final IEntity pItem) {
								Flash.this.setAlpha(1.0f);
								mPhysicsHandler.setAngularVelocity(rotate);
								mPhysicsHandler.setEnabled(true);
							}

							@Override
							public void onModifierFinished(
									final IModifier<IEntity> pModifier,
									final IEntity pItem) {

								onHide();
							}

						});// , EaseQuadOut.getInstance()) {
				mShowModifier.setAutoUnregisterWhenFinished(false);
				this.registerEntityModifier(mShowModifier);
			} else {
				mShowModifier.reset(SHOW_DURATION, 0, scale_x, 0, scale_y);
//				mPhysicsHandler.setAngularVelocity(rotate);
			}
		}

		private void onHide() {
			final float rotate = MathUtils.randomSign() * ANGULAR_VELOCITY;
			if (mHideModifier == null) {
				mHideModifier = new AlphaModifier(HIDE_DURATION, 1.0f, 0,
						new IEntityModifierListener() {

							@Override
							public void onModifierStarted(
									final IModifier<IEntity> pModifier,
									final IEntity pItem) {
								// Flash.this.setAlpha(1.0f);
								// mPhysicsHandler.setAngularVelocity(rotate);
								// mPhysicsHandler.setEnabled(true);
							}

							@Override
							public void onModifierFinished(
									final IModifier<IEntity> pModifier,
									final IEntity pItem) {

								onInit();
							}

						}) {

				};
				mHideModifier.setAutoUnregisterWhenFinished(false);
				this.registerEntityModifier(mHideModifier);
			} else {
				mHideModifier.reset();
			}
		}
	}
}
