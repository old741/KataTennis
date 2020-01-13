package com.kata.tennis;

public class Player {
	
	/** Name of player **/
    private String name;
    /** score of player **/
    private int scorePlayer;
    /** game set of player **/
    private int setScorePlayer;
    /** Is advantaged **/
	private boolean advantaged;
	/** Tie Break Score of player **/
    private int tieBreakScorePlayer;

	public Player(String name) {
		super();
		this.name = name;
		this.scorePlayer =0;
		this.setScorePlayer=0;
		this.tieBreakScorePlayer =0;
	}
	
	/** player win a point **/
	public void winOnePoint() {
		scorePlayer++;
	}

	/** player win a Set **/
	public void winOneSet() {
		setScorePlayer++;
	}	
	/** player win a Tie Break **/
	public void winOneTieBreak() {
		tieBreakScorePlayer++;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getScorePlayer() {
		return scorePlayer;
	}
	public void setScorePlayer(int scorePlayer) {
		this.scorePlayer = scorePlayer;
	}
	public int getGameSetScorePlayer() {
		return setScorePlayer;
	}
	
	public void setGameSetScorePlayer(int setScorePlayer) {
		this.setScorePlayer = setScorePlayer;
	}
	
	public boolean isAdvantaged() {
		return advantaged;
	}

	public void setIsAdvantaged(boolean advantaged) {
		this.advantaged = advantaged;
	}

	public int getTieBreakScorePlayer() {
		return tieBreakScorePlayer;
	}

	public void setTieBreakScorePlayer(int tieBreakScorePlayer) {
		this.tieBreakScorePlayer = tieBreakScorePlayer;
	}
	
	
}
