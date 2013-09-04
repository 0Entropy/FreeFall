package com.zeroentropy.game.handler.enemy;

import org.andengine.util.math.MathUtils;
import com.zeroentropy.game.util.ZeroConstants;
import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Player;

public class NautilusHandler implements IEnemyHandler {
	// private static final int mFirstTileIndex = 12;
	private static final int mCurrentTileIndex = 22;
	public static NautilusHandler getInstance(){return INSTANCE;}
	private static NautilusHandler INSTANCE = new NautilusHandler();
	private NautilusHandler() {}

//	private static final int MIN_VELOCITY_Y = -180;//-144
	private static final int VELOCITY_Y = -120;//-96
	//private static final int BASE_VELOCITY_X = 192;
	
	private static final float BASE_RADIUS_X = 6;
	private static final float BASE_RADIUS_Y = 4;
	//pEntity.setRotation(pEntity.getRotation() + angularVelocity * pSecondsElapsed);
	private static final float ANGULAR_VELOCITY = -180;

	public void enter(final Enemy pSprite) {

//		if (pSprite.getRotation() != 0)
//			pSprite.setRotation(0);
		pSprite.setFlippedHorizontal(MathUtils.randomBoolean());
		pSprite.resetPhysics();
		
		pSprite.stopAnimation(mCurrentTileIndex);
		
		pSprite.setRotationCenter(ZeroConstants.HALF_SPRITE_WIDTH, ZeroConstants.HALF_SPRITE_HEIGHT);
		pSprite.setRotation(MathUtils.random(360));
				
		pSprite.setAngularVelocity(pSprite.getDir()*ANGULAR_VELOCITY);
		pSprite.setVelocityY( MathUtils.randomFactor(VELOCITY_Y, 0.5f));
	}

	public void execute(final float pSecondsElapsed, final Enemy pSprite) {
		
		final float pRotation = MathUtils.degToRad(pSprite.getRotation());
		final float pRadiusX = pSprite.getDir()*BASE_RADIUS_X;
		final float pRadiusY = pSprite.getDir()*BASE_RADIUS_Y;
		
		pSprite.setPosition(pSprite.getX()+(float) (pRadiusX*Math.cos(pRotation)),
				pSprite.getY()+(float) (pRadiusY*Math.sin(pRotation)));

	}

	public void exit(final Enemy pSprite) {
		
	}

	@Override
	public void executeCollides(final Enemy pSprite, final Player pPlayer) {
		// TODO Auto-generated method stub
		pSprite.brokenSelf();
		// World.getInstance().mPlayer.setState(IPlayer.STATE_JUMP);
		// Ôö¼Ó»ðÑÛ£­Öµ
		// fiery_eyes++;
		pPlayer.getSomersault();
	}

}
