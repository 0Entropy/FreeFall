package com.zeroentropy.game.sprite.visual;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.modifier.SingleValueSpanEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.util.modifier.IModifier;

import com.zeroentropy.game.ResourcesManager;
import com.zeroentropy.game.util.ZeroConstants;

public class LightVisual extends Entity {

	private Rectangle mBackgroundRectangle;
	private Line mLine;

	private final static float SHOW_DURATION = 2.0f;
	private final static float HIDE_DURATION = 0.5f;
	private final static float DELAY_DURATION = 10.0f;
	
	
	private final static int W = 200;
	private final static int H = ZeroConstants.SCENE_HEIGHT * 2
			+ ZeroConstants.WUKONG_HEIGHT * 4;
	private boolean mFinished = true;

	public LightVisual() {
		mBackgroundRectangle = new Rectangle(-W * .5f, -H * .5f,
				W, H, ResourcesManager.getInstance().vbom);
		mBackgroundRectangle.setScaleX(0.0f);
		mBackgroundRectangle.setColor(0.6f, 1.0f, 1.0f, .3f);

		this.attachChild(mBackgroundRectangle);

	}

	public boolean isFinished() {
		return mFinished;
	}

	public float getVisionWidth() {
		return mBackgroundRectangle.getWidthScaled();
	}

	private SequenceEntityModifier mStartModifier;
	private SingleValueSpanEntityModifier mStopModifier;

	// public void onStop(){
	// mBackgroundRectangle.setScaleX(0.0f);
	// // mFinished = true;
	// // mStartModifier.
	// // this.unregisterEntityModifier(mStartModifier);
	// // this.unregisterEntityModifier(mStopModifier);
	//
	// }

	public void onInit() {
		mBackgroundRectangle.setScaleX(0.0f);
		mFinished = true;
	}

	public void onFinish() {

		if (mFinished)
			return;

		final float scale_x_fromValue = mBackgroundRectangle.getScaleX();
		final float scale_x_toValue = 0;
		final float duration = HIDE_DURATION * (scale_x_fromValue / 1.0f);

		if (mStopModifier == null) {
			mStopModifier = new SingleValueSpanEntityModifier(duration,
					scale_x_fromValue, scale_x_toValue,
					new IEntityModifierListener() {

						@Override
						public void onModifierStarted(
								final IModifier<IEntity> pModifier,
								final IEntity pItem) {

//							unregisterEntityModifier(mStartModifier);
						}

						@Override
						public void onModifierFinished(
								final IModifier<IEntity> pModifier,
								final IEntity pItem) {

							mFinished = true;
						}

					}) {

				@Override
				protected void onSetInitialValue(final IEntity pItem,
						final float pValue) {

					mBackgroundRectangle.setScaleX(pValue);

				}

				@Override
				protected void onSetValue(final IEntity pItem,
						final float pPercentageDone, float pValue) {

					mBackgroundRectangle.setScaleX(pValue);
				}

				@Override
				public SingleValueSpanEntityModifier deepCopy()
						throws org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException {
					return null;
				}

			};
			mStopModifier.setAutoUnregisterWhenFinished(false);
			this.registerEntityModifier(mStopModifier);
		} else {
			mStopModifier.reset(duration, scale_x_fromValue, scale_x_toValue);
		}

	}

	public void onStart() {

		if (!mFinished)
			return;

		if (mStartModifier == null) {

			final SingleValueSpanEntityModifier mScaleXIncreaseModifier = new SingleValueSpanEntityModifier(
					SHOW_DURATION, 0.0f, 1.0f) {

				@Override
				protected void onSetInitialValue(final IEntity pItem,
						final float pValue) {

					mBackgroundRectangle.setScaleX(pValue);
				}

				@Override
				protected void onSetValue(final IEntity pItem,
						final float pPercentageDone, float pValue) {

					mBackgroundRectangle.setScaleX(pValue);
				}

				@Override
				public SingleValueSpanEntityModifier deepCopy()
						throws org.andengine.util.modifier.IModifier.DeepCopyNotSupportedException {
					return null;
				}

			};

			final DelayModifier mDelayModifier = new DelayModifier(DELAY_DURATION);

			mStartModifier = new SequenceEntityModifier(
					new IEntityModifierListener() {

						@Override
						public void onModifierStarted(
								final IModifier<IEntity> pModifier,
								final IEntity pItem) {
							mFinished = false;
						}

						@Override
						public void onModifierFinished(
								final IModifier<IEntity> pModifier,
								final IEntity pItem) {
							onFinish();
						}

					}, mScaleXIncreaseModifier, mDelayModifier);
			mStartModifier.setAutoUnregisterWhenFinished(false);
			this.registerEntityModifier(mStartModifier);
		} else {
			mStartModifier.reset();
		}

	}

}
