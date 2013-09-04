package com.zeroentropy.game.handler.world;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
//import org.andengine.entity.IEntity;

public abstract class AttachInkCallback implements ITimerCallback {

	private IInkAttachable mInkAttachable;

	protected abstract float getX();
	protected abstract float getY();
	
	public AttachInkCallback(final IInkAttachable pInkAttachable) {
		mInkAttachable = pInkAttachable;

	}

	@Override
	public void onTimePassed(TimerHandler pTimerHandler) {
		// TODO Auto-generated method stub
		if (mInkAttachable != null)
			mInkAttachable.attachInk(getX(), getY());
	}

}
