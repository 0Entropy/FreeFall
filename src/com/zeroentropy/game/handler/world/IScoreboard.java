package com.zeroentropy.game.handler.world;

public interface IScoreboard {

//	public void displayScore(final int pScore);
//	public void displayScore(final int pScoreWeights, final int pScoreValues);
	public void addScore();
//	public void addScore(final float x, final float y);
	public void addScore(final int score, final float x, final float y);
}
