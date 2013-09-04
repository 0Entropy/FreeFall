package com.zeroentropy.game.handler.enemy;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.IUpdateHandler.IUpdateHandlerMatcher;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;

import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Player;
import org.andengine.util.math.MathUtils;
import com.zeroentropy.game.util.ZeroConstants;

public class SquidHandler implements IEnemyHandler {
	// private static final int mFirstTileIndex = 12;
	private static final int mCurrentTileIndex = 21;

	public static SquidHandler getInstance() {
		return INSTANCE;
	}

	private static SquidHandler INSTANCE = new SquidHandler();

	private SquidHandler() {
		// Arrays.fill(mFrameDurations, FRAME_DURATION_EACH);
	}

	private static final int VELOCITY_Y = -80;// -64;
	private static final int VELOCITY_X = 240;// 192;

	public void enter(final Enemy pSprite) {

		if (pSprite.getRotation() != 0)
			pSprite.setRotation(0);
		pSprite.setFlippedHorizontal(MathUtils.randomBoolean());
		pSprite.resetPhysics();

		pSprite.stopAnimation(mCurrentTileIndex);

		final TimerHandler pFlipHandler = new TimerHandler(.6f, true,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {

						if (MathUtils.randomBoolean()) {

							pSprite.turnRound();
						}
					}
				});
		pSprite.registerUpdateHandler(pFlipHandler);

		pSprite.resetPhysics();
		pSprite.setVelocity(
				pSprite.getDir() * MathUtils.randomFactor(VELOCITY_X, 0.5f),
				MathUtils.randomFactor(VELOCITY_Y, 0.5f));
	}

	public void execute(final float pSecondsElapsed, final Enemy pSprite) {
		if (pSprite.getX() < 0) {
			pSprite.turnRight();
		} else if (pSprite.getX() > ZeroConstants.SCENE_WIDTH
				- ZeroConstants.BASE_SPRITE_WIDTH) {
			pSprite.turnLeft();
		}

	}

	public void exit(final Enemy pSprite) {
		// pSprite.setFlippedHorizontal(false);
		pSprite.unregisterUpdateHandlers();
	}

	@Override
	public void executeCollides(final Enemy pSprite, final Player pPlayer) {
		// TODO Auto-generated method stub
		pSprite.onChangeHandler(Enemy.TYPE_RUNNY_SQUID);
		// World.getInstance().mPlayer.setState(IPlayer.STATE_JUMP);
		// Ôö¼Ó»ðÑÛ£­Öµ
		// fiery_eyes++;
		pPlayer.getFieryEyes();
	}

}
