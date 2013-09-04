package com.zeroentropy.game.sprite;

import java.util.LinkedList;

import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.util.modifier.IModifier;
import org.andengine.util.modifier.ease.EaseSineIn;

import com.zeroentropy.game.ResourcesManager;
import com.zeroentropy.game.handler.IPlayerCollidable;
import com.zeroentropy.game.handler.bonus.EyesHandler;
import com.zeroentropy.game.handler.bonus.ISupplyHandler;
import com.zeroentropy.game.handler.bonus.MantraHandler;
import com.zeroentropy.game.handler.bonus.PeachHandler;

//import com.zeroentropy.game.world.SupplyStorage.SupplySprite;

public class Bonus extends Entity implements IPlayerCollidable {

	public final static int TYPE_EYE = 0;
	public final static int TYPE_MANTRA = 1;
	public final static int TYPE_PEACH = 2;
	public final static int TYPE_FISH = 3;
	public final static int TYPE_DOUBLE = 4;

	private AnimatedSprite mBackgroundSprite;
	protected AnimatedSprite mForegroundSprite;

	private FadeOutModifier mBackgroundModifier;
	private MoveYModifier mForegroundModifier;

	private final float mBackgroundFadeOutTime = 1.0f;
	private final float mSupplyFallDownTime = 2.0f;

	protected boolean mCollided;

	public static LinkedList<Bonus> mDisposedChilden = new LinkedList<Bonus>();

	private ISupplyHandler mCurrentHandler;
	private int mType;

	public Bonus(final float pX, final int pType) {

		super(pX, 0);
		// this.setTag(pTag);
		this.onChangeHandler(pType);

	}

	protected boolean isFinished() {
		return mForegroundModifier.isFinished();
	}

	private ISupplyHandler getHandler(int pType) {

		switch (pType) {
		case TYPE_EYE:
			return EyesHandler.getInstance();
		case TYPE_MANTRA:
			return MantraHandler.getInstance();
		case TYPE_PEACH:
			return PeachHandler.getInstance();
		case TYPE_FISH:
			return null;
		case TYPE_DOUBLE:
			return null;
		default:
			return null;
		}

	}

	public synchronized void onChangeHandler(final int pType) {
		final ISupplyHandler pHandler = getHandler(pType);
		if (pHandler == null) {
			return;
		} else if (this.mCurrentHandler == null) {

			this.mCurrentHandler = pHandler;
			mCurrentHandler.enter(this);

		}
		/*
		 * else if (this.mCurrentHandler.equals(pHandler)) {
		 * mCurrentHandler.enter(this); }
		 */
		else {

			mCurrentHandler.exit(this);
			this.mCurrentHandler = pHandler;
			mCurrentHandler.enter(this);
		}
		mType = pType;
		// this.setTag(pType);
		// pElapsedTime = 0;
	}

	@Override
	public boolean onCollision(final Player pPlayer) {
		if (pPlayer == null)
			return false;
		final boolean isCollided = !mCollided && !isFinished()
				&& mForegroundSprite.collidesWith(pPlayer.mHand);
		if (isCollided) {
			mCurrentHandler.executeCollides(this, pPlayer);

			mForegroundSprite.setVisible(false);
			mCollided = true;
		}
		return isCollided;
	}

	public void onManagedAttached() {

		mCollided = false;

		if (mBackgroundSprite == null) {
			mBackgroundSprite = new AnimatedSprite(0, 0,
					ResourcesManager.getInstance().mSupplyBackgroundRegion,
					ResourcesManager.getInstance().vbom);
			mBackgroundSprite
					.registerEntityModifier(mBackgroundModifier = new FadeOutModifier(
							mBackgroundFadeOutTime, EaseSineIn.getInstance()));
			mBackgroundModifier.setAutoUnregisterWhenFinished(false);
			this.attachChild(mBackgroundSprite);
		} else {
			mBackgroundSprite.reset();
		}

		if (mForegroundSprite == null) {
			mForegroundSprite = new AnimatedSprite(0, 0,
					ResourcesManager.getInstance().mSupplyRegion,
					ResourcesManager.getInstance().vbom);

			mForegroundSprite
					.registerEntityModifier(mForegroundModifier = new MoveYModifier(
							mSupplyFallDownTime, 0, ResourcesManager
									.getInstance().camera.getSurfaceHeight(),
							new IEntityModifierListener() {

								@Override
								public void onModifierStarted(
										IModifier<IEntity> pModifier,
										IEntity pItem) {
									// mFallOut = false;

								}

								@Override
								public void onModifierFinished(
										IModifier<IEntity> pModifier,
										IEntity pItem) {
									// mFallOut = true;
									mForegroundSprite.setVisible(false);
									mDisposedChilden.addFirst(Bonus.this);
								}
							}, EaseSineIn.getInstance()));
			mForegroundModifier.setAutoUnregisterWhenFinished(false);
			this.attachChild(mForegroundSprite);
		} else {
			mForegroundSprite.reset();
		}

		mForegroundSprite.stopAnimation(mType);

	}// End of onManagedAttached();

}// End of class InkSprite

