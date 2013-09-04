package com.zeroentropy.game.sprite;

import java.util.LinkedList;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

import com.zeroentropy.game.ResourcesManager;

public class Ink extends Sprite {

	private static final int mCurrentTileIndex = 0;
	private static final float BASE_DURATIONS = 16;
	private static final int TYPE_COUNT = 3;

	
	public static LinkedList<Ink> mDisposedInks = new LinkedList<Ink>();

	public Ink(final float pX, final float pY) {
		super(pX, pY, ResourcesManager.getInstance().mInkRegion,
				ResourcesManager.getInstance().vbom);
		mTransparentBackground = new Sprite(pX, pY,
				ResourcesManager.getInstance().mInkRegion,
				ResourcesManager.getInstance().vbom);
		// this.stopAnimation(mCurrentTileIndex);
	}

//	public static int reduisDisposedInksSize() {
//		int extraNum = 0;
//		if (mDisposedInks.size() > MAX_DISPOSEDLIST_SIZE) {
//			extraNum = mDisposedInks.size() - MAX_DISPOSEDLIST_SIZE;
//			while (extraNum-- > 0) {
//				mDisposedInks.poll().detachSelf();
//			}
//		}
//		return extraNum;
//	}

	public void onManagedDestroy() {
		// this.clearEntityModifiers();
		// this.clearUpdateHandlers();
		// this.registerEntityModifier(mFadeOutModifier);
		// final float pScale = this.getScaleX();
		// final float pAlpha = this.getAlpha();
		if (!mDurationsTimerHandler.isTimerCallbackTriggered()) {
			mDurationsTimerHandler.setTimerSeconds(1);
		}
		// else{
		// this.detachSelf();
		// }
	}

	private ParallelEntityModifier mParallelModifier;
	private TimerHandler mDurationsTimerHandler;
	private ScaleModifier mScaleModifier;
	private FadeOutModifier mFadeOutModifier;

	private ParallelEntityModifier mBackgroundParallelModifier;
//	private TimerHandler mDurationsTimerHandler;
	private ScaleModifier mBackgroundScaleModifier;
	private AlphaModifier mBackgroundFadeOutModifier;
	private static final float INIT_BACKGROUND_TRANSPARENCE = 0.1f;
	
	public void onManagedCreate() {

		
		final int z = 1 << MathUtils.random(TYPE_COUNT);// 1,2,4,8
		final float scale = 1.0f / z;
		final float durations = scale * BASE_DURATIONS;
		if (mBackgroundParallelModifier == null) {
			mBackgroundScaleModifier = new ScaleModifier(.5f, scale, scale * 2);
			mBackgroundFadeOutModifier = new AlphaModifier(.5f, INIT_BACKGROUND_TRANSPARENCE, 0);
			mBackgroundParallelModifier = new ParallelEntityModifier( mBackgroundScaleModifier, mBackgroundFadeOutModifier);
		}else{
			mBackgroundScaleModifier.reset(.5f, scale, scale*2, scale, scale*2);
//			mFadeOutModifier.reset(pDuration, pFromValue, pToValue)
			mBackgroundParallelModifier.reset();
		}
		
		if (mParallelModifier == null) {
			mScaleModifier = new ScaleModifier(.5f, scale, scale * 2);
			mFadeOutModifier = new FadeOutModifier(.5f);
			mParallelModifier = new ParallelEntityModifier(
					new IEntityModifierListener() {

						@Override
						public void onModifierStarted(
								IModifier<IEntity> pModifier, IEntity pItem) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onModifierFinished(
								IModifier<IEntity> pModifier, IEntity pItem) {

							// Ink.this.detachSelf();
							// if (mDisposedInks.size() < MAX_DISPOSEDLIST_SIZE)
							// {
							setIgnoreUpdate(true);
							mTransparentBackground.setIgnoreUpdate(true);
							mDisposedInks.addFirst(Ink.this);
							// mDisposedBackground.addFirst(Ink.this.mTransparentBackground);
							// } else {
							// Ink.this.detachSelf();
							// }
							// mInksNum--;

						}
					}, mScaleModifier, mFadeOutModifier);
		}else{
			mScaleModifier.reset(.5f, scale, scale*2, scale, scale*2);
//			mFadeOutModifier.reset(pDuration, pFromValue, pToValue)
			mParallelModifier.reset();
		}

		if (mDurationsTimerHandler == null) {
			mDurationsTimerHandler = new TimerHandler(durations, false,
					new ITimerCallback() {

						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							// Ink.this.unregisterUpdateHandler(pTimerHandler);
							registerEntityModifier(mParallelModifier);
							mTransparentBackground.registerEntityModifier(mBackgroundParallelModifier);

						}
					});
			this.registerUpdateHandler(mDurationsTimerHandler);
		} else {
			mDurationsTimerHandler.setTimerSeconds(durations);
			mDurationsTimerHandler.reset();
		}

		// this.setPosition(pX, pY);
		setScale(scale);
		if (getAlpha() != 1.0f)
			setAlpha(1.0f);
		
//		Ink.this.mTransparentBackground
		mTransparentBackground.setScale(scale);
		if (mTransparentBackground.getAlpha() != INIT_BACKGROUND_TRANSPARENCE)
			mTransparentBackground.setAlpha(INIT_BACKGROUND_TRANSPARENCE);
		
		setIgnoreUpdate(false);
		mTransparentBackground.setIgnoreUpdate(false);

	}// End of onManagedAttached();

	// °ëÍ¸Ã÷µ×²¿TransParent
	private Sprite mTransparentBackground;

	// public static LinkedList<Ink> mDisposedBackgrounds = new
	// LinkedList<Ink>();
	public Sprite getBackground() {
		return mTransparentBackground;
	}
}// End of class InkSprite