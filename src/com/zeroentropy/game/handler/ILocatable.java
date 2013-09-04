package com.zeroentropy.game.handler;

import com.zeroentropy.game.sprite.Enemy;
import com.zeroentropy.game.sprite.Player;

public interface ILocatable {

	public float getLocationX();
	public float getLocationY();
	public boolean getLocatable();
}
