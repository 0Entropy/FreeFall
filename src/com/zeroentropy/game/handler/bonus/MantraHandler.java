package com.zeroentropy.game.handler.bonus;

import com.zeroentropy.game.handler.enemy.AnglerHandler;
import com.zeroentropy.game.sprite.Player;
import com.zeroentropy.game.sprite.Bonus;

public class MantraHandler implements ISupplyHandler {

	public static MantraHandler getInstance(){return INSTANCE;}
	private static MantraHandler INSTANCE = new MantraHandler();
	private MantraHandler(){}
	@Override
	public void exit(Bonus pItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void execute(float pSecondsElapsed, Bonus pItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enter(Bonus pItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void executeCollides(Bonus pItem, Player pTarget) {
		pTarget.onFlash();
		

	}

}
