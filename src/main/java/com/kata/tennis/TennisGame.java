package com.kata.tennis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TennisGame  {
	
	public static final int  INITAL_MIN_POINTS = 0;
    public static final int SET_SCORE_FOUR = 4;
    public static final int SET_SCORE_SIX = 6;
    public static final int SET_SCORE_SEVEN = 7;
	public static final String POINT_0 = "0";
	public static final String POINT_15 = "15";
	public static final String POINT_30 = "30";
	public static final String POINT_40 = "40";
	public static final String DESC_INITIAL_SCORE_LABEL = "0 - 0";
	public static final String DESC_ADVANTAGE_LABEL = "Advantage";
	public static final String DESC_DEUCE_LABEL = "Deuce";
	
	private static final String DESC_PLAYER1_LABEL = "Player 1";
	private static final String DESC_PLAYER2_LABEL = "Player 2";
	private static final String DESC_SCORE_LABEL= "Score";
	private static final String DESC_CURR_GAME_STATUS = "Current game status";
	private static final String DESC_MATCH_STATUS = "Match Status";
	private static final String DESC_SEPARATOR = " : ";
	private static final String DESC_LINE_BREAK = "\n";

 
    /** Player One **/ 
	private Player player1;
	/** Player Two **/ 
	private Player player2;
    /** Match state **/
	private MatchStateEnum matchState;
	/** Current game status **/
	private String currentGameStatus;
	// flag on first call is Deuce
	boolean isFirstTimeActivatedDeuce = true;
	/** List of sets won for each player **/
	private List<Player>scoreSetsKey;
	/** List of value of sets won for each player **/
	private List<String>scoreSetsValue;
	
    public TennisGame(Player player1, Player player2) {
    	
        this.player1 = player1;
        this.player2 = player2;
        this.scoreSetsKey= new ArrayList<Player>();
        this.scoreSetsValue= new ArrayList<String>();
        matchState = MatchStateEnum.IN_PROGRESS;
        currentGameStatus = DESC_INITIAL_SCORE_LABEL;

    }
    
    public Map<Integer, String> points = new HashMap<Integer, String>() {
        {
            put(0, POINT_0);
            put(1, POINT_15);
            put(2, POINT_30);
            put(3, POINT_40);
        }
    };
    
	/**
	 * Manage Score of players after to score(s) point(s)
	 * @return
	 */
    public String getManageScore() {
        currentGameStatus = getScoreValue(player1.getScorePlayer()) + " - " + getScoreValue(player2.getScorePlayer());

        if (isMaxSetScoreReach()) {
            if (player1.getGameSetScorePlayer() == SET_SCORE_SIX && player2.getGameSetScorePlayer() <= SET_SCORE_FOUR){
            	canTerminateSet(player1);
                return currentGameStatus;
            }
            if (player2.getGameSetScorePlayer() == SET_SCORE_SIX && player1.getGameSetScorePlayer() <= SET_SCORE_FOUR) {
            	canTerminateSet(player2);
                return currentGameStatus;
            }
            if (isTieBreakActivated()) {
            	currentGameStatus = getCurrentTieBreakScore();
                if (canFinishTieBreak()) {
                    if (player1.getTieBreakScorePlayer() - player2.getTieBreakScorePlayer() >= 2) {
                    	canTerminateSet(player1);
                    }
                    if (player2.getTieBreakScorePlayer() - player1.getTieBreakScorePlayer() >= 2) {
                    	canTerminateSet(player2);
                    }
                }
                return currentGameStatus;
            }
            // If a player wins a Game and reach the Set score of 6 and the other player 
            //has a Set score of 5, a new Game must be played and the first player who reach the
            //score of 7 wins the match
            if (player1.getGameSetScorePlayer() == SET_SCORE_SEVEN) {
            	canTerminateSet(player1);
                return currentGameStatus;
            }
            if (player2.getGameSetScorePlayer() == SET_SCORE_SEVEN) {
             	canTerminateSet(player2);
                return currentGameStatus;
            }
        }

        if (isDeuce()) {
        	currentGameStatus =DESC_DEUCE_LABEL;
            if (isFirstTimeActivatedDeuce && (player1.getScorePlayer() < 5 && player2.getScorePlayer() < 5)) {
                isFirstTimeActivatedDeuce = false;
                currentGameStatus= getScoreValue(player1.getScorePlayer()) + " - " + getScoreValue(player2.getScorePlayer());
                return currentGameStatus;
            }
            return currentGameStatus;
        }
        if (isAdvantagePlayerOne()) {
        	currentGameStatus = DESC_ADVANTAGE_LABEL+ " - " + getScoreValue(player2.getScorePlayer());
            return currentGameStatus;
        }
        if (isAdvantagePlayerTwo()) {
        	currentGameStatus= getScoreValue(player1.getScorePlayer()) + " - " + DESC_ADVANTAGE_LABEL;
        	return currentGameStatus;
        }
        if (playerOneWinGame()) {
            managePlayerSpecificPoint(player1);
            return currentGameStatus;
        }
        if (playerTwoWinGame()) {
        	managePlayerSpecificPoint(player2);
            return currentGameStatus;
        }
        return currentGameStatus;
    }
    
    /** Add player specefic point **/
	private void managePlayerSpecificPoint(Player player) {
		if (isTieBreakActivated()) {
			player.winOneTieBreak();
		} else {
			player.winOneSet();
		}
		ReLauchGame();
	}
	   /** Add player point **/
	private void managePlayerPoint(Player player) {
		if (isTieBreakActivated()) {
			player.winOneTieBreak();
		} else {
			player.winOnePoint();
		}
	}
	
	private void canTerminateSet(Player playerWinSet) {
     	registerSetScore(playerWinSet,player1.getGameSetScorePlayer(),player2.getGameSetScorePlayer());
		if(isMatchFinished()) {
			updateMatchState();
		}
		ReLauchSets();
	}
	/** Register a set when the player win the set **/
	private void registerSetScore(Player player, int scorePlayerOne, int scorePlayerTwo) {
		scoreSetsKey.add(player);
		scoreSetsValue.add(printSet(player1.getGameSetScorePlayer(),player2.getGameSetScorePlayer()));
	}
	/** post the result of current tie break **/
	private String getCurrentTieBreakScore() {
		if(isTieBreakActivated()){
			StringBuilder score = new StringBuilder("[").append(player1.getTieBreakScorePlayer()).append("-").append(player2.getTieBreakScorePlayer()).append("]");
			return score.toString();
		}
		return "";
	}
    /** post the score format ( scorePlayerOne - ScorePlayer2) if tie break post [tieBreakScorePlayer1-tieBreakScorePlayer2] **/
    private String printSet(int scorePlayerOne, int scorePlayerTwo) {
		StringBuilder score = new StringBuilder("(").append(player1.getGameSetScorePlayer()).append(" - ").append(player2.getGameSetScorePlayer()).append(")");
		if(isTieBreakActivated()){
			score.append("[").append(player1.getTieBreakScorePlayer()).append("-").append(player2.getTieBreakScorePlayer()).append("]");
		}
		return score.toString();
	}
    
	/**
	 * Full description of the game
	 * @return
	 */
	public String getMatchFullDescription(){
		StringBuilder sb = new StringBuilder();
		sb.append(DESC_PLAYER1_LABEL).append(DESC_SEPARATOR)
		.append((player1 != null && player1.getName()!=null && !player1.getName().isEmpty()) ? player1.getName() : "").append(DESC_LINE_BREAK)
		.append(DESC_PLAYER2_LABEL).append(DESC_SEPARATOR)
		.append((player2 != null && player2.getName()!=null && !player2.getName().isEmpty()) ? player2.getName() : "").append(DESC_LINE_BREAK);
		if(!getScoreDescription().isEmpty()) {
		sb.append(DESC_SCORE_LABEL).append(DESC_SEPARATOR).append(getScoreDescription()).append(DESC_LINE_BREAK);
		}
		if(matchState.equals(MatchStateEnum.IN_PROGRESS)){
			sb.append(DESC_CURR_GAME_STATUS).append(DESC_SEPARATOR).append(getCurrentGameStatus()).append(DESC_LINE_BREAK);
		}
		sb.append(DESC_MATCH_STATUS).append(DESC_SEPARATOR).append(matchState.toString())
			.append(DESC_LINE_BREAK).append(DESC_LINE_BREAK);
		return sb.toString();
	}
	
	/**
	 * Get the full score of the match
	 * @return
	 */
	public String getScoreDescription() {
		StringBuilder scoresValues = new StringBuilder();
		for(String scoreSetResult : scoreSetsValue){
			scoresValues.append(scoreSetResult).append(" ");
		}
		return scoresValues.toString();
	}
	public String getCurrentGameStatus() {
		return currentGameStatus;
	}

    public String getScoreValue(int point) {
        return (points.get(point) == null) ? POINT_40 : points.get(point);
    }
	
	/**
	 * Return true if the match is finished
	 * @return boolean
	 */
	private boolean isMatchFinished() {
		
		return isPlayerWinMatch(player1)|| isPlayerWinMatch(player2) ;
	}
	/**
	 * Return true if player win the match
	 * @return boolean
	 */
	private boolean isPlayerWinMatch(Player player) {
		int compteurScorePlayer = 0;
		for(Player playerSetScore : scoreSetsKey){
			if(playerSetScore.equals(player)){
				compteurScorePlayer++;
			}
		}if(compteurScorePlayer>2)
			return true;
		 else
		   return false;
	}
	/**
	 * Update the match state {@link MatchStateEnum}
	 */
	private void updateMatchState() {
		if(isMatchFinished()) {
			this.matchState = isPlayerWinMatch(player1) ? MatchStateEnum.FINISHED_PLAYER1_WINS : MatchStateEnum.FINISHED_PLAYER2_WINS;
		}else {
			this.matchState = MatchStateEnum.IN_PROGRESS;
		}
	}

    protected boolean playerOneWinGame() {
        return player1.getScorePlayer() > 3 && player1.getScorePlayer() > player2.getScorePlayer();
    }

    protected boolean playerTwoWinGame() {
        return player2.getScorePlayer() > 3 && player2.getScorePlayer() > player1.getScorePlayer();
    }
    protected boolean isDeuce() {
        return player1.getScorePlayer() == player2.getScorePlayer() && player2.getScorePlayer() > 2;
    }

    protected boolean isAdvantagePlayerOne() {
        return player1.getScorePlayer() == player2.getScorePlayer() + 1 && player1.getScorePlayer() > 3;
    }

    protected boolean isAdvantagePlayerTwo() {
        return player1.getScorePlayer() + 1 == player2.getScorePlayer() && player2.getScorePlayer() > 3;
    }

	private boolean isMaxSetScoreReach() {
        return player1.getGameSetScorePlayer() >= SET_SCORE_SIX || player2.getGameSetScorePlayer() >= SET_SCORE_SIX;
    }

    private boolean isTieBreakActivated() {
        if (player1.getGameSetScorePlayer() == SET_SCORE_SIX && player2.getGameSetScorePlayer() == SET_SCORE_SIX) {
            return true;
        }
        return false;
    }

    private boolean canFinishTieBreak() {
        if (player1.getTieBreakScorePlayer() >= 7 || player2.getTieBreakScorePlayer() >= 7) {
            return true;
        }
        return false;
    }
    /**
     * Increment to 1 score game for player 1
     */
    public void playerOneWinPoint() {
    	managePlayerPoint(player1);
    	getManageScore();
    }
    /**
     * Increment to 1 score game for player 2
     */
    public void playerTwoWinPoint() {
       managePlayerPoint(player2);
       getManageScore();
    }
    /**
     * Increment to 1 the score set for player 1
     */
    public void playerOneWinSet() {
    	managePlayerSpecificPoint(player1);
    	getManageScore();
    }
    /**
     * Increment to 1 the score set for player 2
     */
    public void playerTwoWinSet() {
       managePlayerSpecificPoint(player2);
       getManageScore();
    }
    
    public void ReLauchGame() {
    	player1.setScorePlayer(INITAL_MIN_POINTS); 
    	player2.setScorePlayer(INITAL_MIN_POINTS);
    	if(!isTieBreakActivated())
    	currentGameStatus =DESC_INITIAL_SCORE_LABEL;
        isFirstTimeActivatedDeuce= true;    
    }
    
    public void ReLauchSets() {
        ReLauchGame();
        player1.setGameSetScorePlayer(INITAL_MIN_POINTS);
        player2.setGameSetScorePlayer(INITAL_MIN_POINTS);
        player1.setTieBreakScorePlayer(INITAL_MIN_POINTS);
        player2.setTieBreakScorePlayer(INITAL_MIN_POINTS);
    }

	public MatchStateEnum getMatchState() {
		return matchState;
	}
}
