package com.zeroentropy.game.handler.bonus;

import com.zeroentropy.game.sprite.Player;
import com.zeroentropy.game.sprite.Bonus;

public class EyesHandler implements ISupplyHandler {
	public static EyesHandler getInstance(){return INSTANCE;}
	private static EyesHandler INSTANCE = new EyesHandler();
	private EyesHandler(){}
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
		pTarget.getVision();

	}

}
