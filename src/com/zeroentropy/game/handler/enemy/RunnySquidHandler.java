package com.zeroentropy.game.handler.enemy;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.IUpdateHandler.IUpdateHandlerMatcher;
import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;

import com.zeroentropy.game.handler.world.AttachInkCallback;
import com.zeroentropy.game.handler.world.IInkAttachable;
import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Player;
import org.andengine.util.math.MathUtils;
import com.zeroentropy.game.util.ZeroConstants;
import com.zeroentropy.game.world.World;

public class RunnySquidHandler implements IEnemyHandler {
	// private static final int mFirstTileIndex = 12;
	private static final int mCurrentTileIndex = 21;
	public static RunnySquidHandler getInstance(){return INSTANCE;}
	private static RunnySquidHandler INSTANCE = new RunnySquidHandler();
	private RunnySquidHandler() {
		// Arrays.fill(mFrameDurations, FRAME_DURATION_EACH);
	}

	private IInkAttachable mInkAttachable;
	public void registerInkAttachable(final IInkAttachable pInkAttachable){
		mInkAttachable = pInkAttachable;
	}
	private static final int VELOCITY_X = 480;//192;	
	private static final int VELOCITY_Y = -160;//-96;

	public void enter(final Enemy pSprite) {

		if (pSprite.getRotation() != 0)
			pSprite.setRotation(0);
		pSprite.setFlippedHorizontal(MathUtils.randomBoolean());
		pSprite.resetPhysics();
		
		if(mInkAttachable != null){
			pSprite.registerUpdateHandler(new TimerHandler(.08f, true,
					new AttachInkCallback(mInkAttachable){

						@Override
						protected float getX() {
							return pSprite.getX();
						}

						@Override
						protected float getY() {
							return pSprite.getY();
						}
				
			} ));
		}
		

		
		final TimerHandler pFlipHandler = new TimerHandler(.6f, true,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {

						if (MathUtils.randomBoolean()) {

							pSprite.turnRound();
						}
						
					}
				});
		pSprite.registerUpdateHandler(pFlipHandler);
		
		pSprite.stopAnimation(mCurrentTileIndex);

		pSprite.setVelocity(
				pSprite.getDir()*MathUtils.randomFactor(VELOCITY_X, 0.5f),
				MathUtils.randomFactor(VELOCITY_Y, 0.5f));

	}

	public void execute(final float pSecondsElapsed, final Enemy pSprite) {
		if (pSprite.getX() < 0 ) {
			pSprite.turnRight();
		}else if (pSprite.getX() > ZeroConstants.SCENE_WIDTH - ZeroConstants.BASE_SPRITE_WIDTH) {
			pSprite.turnLeft();
		}
	}

	public void exit(final Enemy pSprite) {
		//pSprite.setFlippedHorizontal(false);
		pSprite.unregisterUpdateHandlers();
		
	}

	@Override
	public void executeCollides(final Enemy pSprite, final Player pPlayer) {
		// TODO Auto-generated method stub
		pSprite.brokenSelf();
		// World.getInstance().mPlayer.setState(IPlayer.STATE_JUMP);
		// Ôö¼Ó»ðÑÛ£­Öµ
		// fiery_eyes++;
		pPlayer.jump();
	}
}
