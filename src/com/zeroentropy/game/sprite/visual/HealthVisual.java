package com.zeroentropy.game.sprite.visual;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.primitive.Rectangle;

import com.zeroentropy.game.ResourcesManager;

/**
 * 
 * @author TAZUdesign02 蜇刺效果要再想想更好的方案。。。
 * 
 */
public class HealthVisual extends Entity {

	private final static float W = 100;
	private final static float H = 10;
	private final static float PAD = 2;

	private final static int MAX_HEALTH = 100;

	private int mHealth, mActulHealth;

	private Rectangle mBackgroundRectangle;
	private Rectangle mForegroundRectangle;
	private Rectangle mHealthRectangle;

	private TimerHandler mTimer;
	private FadeOutModifier mFadeOut;

	public HealthVisual(final float x, final float y) {

		super(x, y);

		mBackgroundRectangle = new Rectangle(-W * 0.5f - PAD, -H * 0.5f - PAD,
				W + 2 * PAD, H + 2 * PAD, ResourcesManager.getInstance().vbom);
		mBackgroundRectangle.setColor(1, 1, 1);

		this.attachChild(mBackgroundRectangle);

		mForegroundRectangle = new Rectangle(-W * 0.5f, -H * 0.5f, W, H,
				ResourcesManager.getInstance().vbom);
		mForegroundRectangle.setColor(1, 1, 0);

		this.attachChild(mForegroundRectangle);

		mHealthRectangle = new Rectangle(-W * 0.5f, -H * 0.5f, W, H,
				ResourcesManager.getInstance().vbom);
		mHealthRectangle.setColor(1, 0, 0);
		mHealthRectangle.setScaleCenterX(0);

		this.attachChild(mHealthRectangle);

		onInit();

	}

	public void onInit() {
		mHealth = mActulHealth = MAX_HEALTH;
		mHealthRectangle.setScaleX(1.0f);

		if (this.getAlpha() != 0.0f) {
			this.setAlpha(0.0f);
		}

		if (mTimer == null)
			this.registerUpdateHandler(mTimer = new TimerHandler(0.05f, false,
					new ITimerCallback() {

						@Override
						public void onTimePassed(TimerHandler pTimerHandler) {
							// TODO Auto-generated method stub
							// plusOneScore();
							final int actulHealth = mActulHealth;
							if (mHealth < actulHealth) {
								mHealth++;
								mTimer.reset();

							} else if (mHealth > actulHealth) {
								mHealth--;
								mTimer.reset();
							} else {
								if (mFadeOut == null) {
									mFadeOut = new FadeOutModifier(0.3f);
									mFadeOut.setAutoUnregisterWhenFinished(false);
									HealthVisual.this
											.registerEntityModifier(mFadeOut);
								} else {
									mFadeOut.reset();
								}
							}
							mHealthRectangle.setScaleX((float) mHealth
									/ MAX_HEALTH);

						}

					}));
	}

	public void addHealth(final int value) {
		//
		if (this.getAlpha() != 1.0f) {
			this.setAlpha(1.0f);
		}

		//
		mActulHealth += value;
		if (mActulHealth > MAX_HEALTH)
			mActulHealth = MAX_HEALTH;
		else if (mActulHealth < 0)
			mActulHealth = 0;
		if (mTimer.isTimerCallbackTriggered()) {
			mTimer.reset();
		}
	}

	public int getHealth() {
		return mHealth;
	}

	@Override
	protected void onUpdateColor() {
		super.onUpdateColor();

		final float alpha = this.getAlpha();
		mBackgroundRectangle.setAlpha(alpha);
		mForegroundRectangle.setAlpha(alpha);
		mHealthRectangle.setAlpha(alpha);

	}

}
