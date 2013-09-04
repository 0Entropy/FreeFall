package com.zeroentropy.game.handler.bonus;

import com.zeroentropy.game.sprite.Player;
import com.zeroentropy.game.sprite.Bonus;

public class PeachHandler implements ISupplyHandler {
	
	public static PeachHandler getInstance(){return INSTANCE;}
	private static PeachHandler INSTANCE = new PeachHandler();
	private PeachHandler(){}
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
		pTarget.getCure(2);

	}

}
