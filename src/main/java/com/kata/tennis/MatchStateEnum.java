package com.kata.tennis;


public enum MatchStateEnum {
	/**
	 * The match is in progress
	 */
	IN_PROGRESS("In progress"),
	/**
	 * Player 1 wins the match
	 */
	FINISHED_PLAYER1_WINS("Player 1 wins"),
	/**
	 * PLayer 2 wins the match
	 */
	FINISHED_PLAYER2_WINS("Player 2 wins");
	
	private String desccription;
	
	
	MatchStateEnum(String desccription){
		this.desccription = desccription;
	}
	/**
	 * Status description
	 */
	
	public String toString(){
		return desccription;
	}
}
