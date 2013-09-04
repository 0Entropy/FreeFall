package com.zeroentropy.game.handler.enemy;

import java.util.Arrays;

import org.andengine.engine.handler.physics.PhysicsHandler;

import android.util.Log;

import com.zeroentropy.game.handler.ILocatable;
import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Player;
import org.andengine.util.math.MathUtils;

public class BindStarfishHandler implements IEnemyHandler {
	
	public static BindStarfishHandler getInstance() {
		return INSTANCE;
	}
	private static BindStarfishHandler INSTANCE = new BindStarfishHandler();
	private BindStarfishHandler() {	}

//	private static final float BASE_RADIUS = 80;
	private static final float ANGULAR_VELOCITY = 120;
//	private static final float MAX_ANGULAR_VELOCITY = 240;

	private ILocatable mLocation;
	public void setLocation(final ILocatable pLocation) {
		mLocation = pLocation;
	}

	public void enter(final Enemy pSprite) {

//		if (pSprite.getRotation() != 0)
//			pSprite.setRotation(0);
		pSprite.setFlippedHorizontal(MathUtils.randomBoolean());
		pSprite.resetPhysics();
		
		pSprite.setRotation(MathUtils.random(360));

		pSprite.setAngularVelocity(pSprite.getDir() * MathUtils.randomFactor(ANGULAR_VELOCITY, 1.0f));

	}

	public void execute(final float pSecondsElapsed, final Enemy pSprite) {
		if (mLocation != null) {

			if (mLocation.getLocatable()) {
				pSprite.setX(mLocation.getLocationX() - StarfishHandler.ROTATION_CENTER_X);
				pSprite.setY(mLocation.getLocationY() - StarfishHandler.ROTATION_CENTER_Y);
				
			} else {
				pSprite.onChangeHandler(Enemy.TYPE_ESCAPE_STARFISH);
			}
		}
	}

	public void exit(final Enemy pSprite) {
		// pSprite.setFlippedHorizontal(false);
//		pSprite.clearUpdateHandlers();
//		pSprite.setRotation(0);
		// pSprite.setRotationCenter(ZeroConstants.BASE_SPRITE_WIDTH*.5f,
		// ZeroConstants.BASE_SPRITE_HEIGHT*.5f);
	}

	@Override
	public void executeCollides(final Enemy pSprite, final Player pPlayer) {

	}

}
