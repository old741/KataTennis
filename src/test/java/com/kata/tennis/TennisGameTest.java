package com.kata.tennis;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static com.kata.tennis.TennisGame.DESC_INITIAL_SCORE_LABEL;
import static com.kata.tennis.TennisGame.POINT_0;
import static com.kata.tennis.TennisGame.POINT_15;
import static com.kata.tennis.TennisGame.POINT_30;
import static com.kata.tennis.TennisGame.POINT_40;
import static com.kata.tennis.TennisGame.DESC_ADVANTAGE_LABEL;
import static com.kata.tennis.TennisGame.DESC_DEUCE_LABEL;

import org.junit.Before;
import org.junit.Test;


public class TennisGameTest {
	
	public static final int KEY_POINT_0 = 0;
	public static final int KEY_POINT_15 = 1;
	public static final int KEY_POINT_30 = 2;
	public static final int KEY_POINT_40 = 3;
	public static final String DELIMITER =" - ";
	public static final String RIGHT_PARENTHESIS ="(";
	public static final String LEFT_PARENTHESIS =")";
	public static final String SPACE =" ";
	public static final int ENTIER_1=1;
	public static final int ENTIER_4=4;
	public static final int ENTIER_5=5;
	public static final int ENTIER_6=6;
	public static final int ENTIER_7=7;
	public static final String TIE_BREAK_INITIAL_SCORE="[0-0]";
	public static final String TIE_BREAK_SCORE_1_0="[1-0]";
	public static final String TIE_BREAK_SCORE_2_7="[2-7]";
	public static final String TIE_BREAK_SCORE_2_6="[2-6]";
	
	private static final String PLAYER1_NAME = "Tsonga";
	private static final String PLAYER2_NAME = "Nadal";

	private TennisGame game;
    private Player player1;
	private Player player2;

    @Before
    public void setUp() throws Exception {
    	player1 = new Player(PLAYER1_NAME);
		player2 = new Player(PLAYER2_NAME);
        game = new TennisGame(player1,player2);
    }
    


    @Test
    public void testInitalScore() {
        assertEquals("Current Game status should be equal 0 - 0",DESC_INITIAL_SCORE_LABEL, game.getCurrentGameStatus());
        assertEquals("Game Match state should be in progress",MatchStateEnum.IN_PROGRESS, game.getMatchState());
    	assertEquals("Player 1 initial score should be equal to 0", 0, player1.getScorePlayer());
		assertEquals("Player 2 initial score should be equal to 0", 0, player2.getScorePlayer());
        System.out.println(game.getMatchFullDescription());
    }
    
    @Test
    public void testScorePlayerMustBeIncrementedWhenPlayerWinPoint(){
    	setScore(1, 0);// Player one score 1 point
		assertEquals("Current Game status should be equal to 15", KEY_POINT_15, player1.getScorePlayer());
		setScore(1, 0);
		assertEquals("Current Game status should be equal to 30", KEY_POINT_30, player1.getScorePlayer());
		setScore(1, 0);
		assertEquals("Current Game status should be equal to 40", KEY_POINT_40, player1.getScorePlayer());
		setScore(0, 1);
		assertEquals("Current Game status should be equal to 15", KEY_POINT_15, player2.getScorePlayer());
		setScore(0, 1);
		assertEquals("Current Game status should be equal to 30", KEY_POINT_30, player2.getScorePlayer());
		setScore(0, 1);
		assertEquals("Current Game status should be equal to 40", KEY_POINT_40, player2.getScorePlayer());
		
        assertEquals("Game Match state should be in progress",MatchStateEnum.IN_PROGRESS, game.getMatchState());
		System.out.println(game.getMatchFullDescription());

    }
    @Test
    public void testScorePlayerResetTo0WhenHeWinTheGame(){
    	setScore(2, 2); 
		assertEquals("Current Game status should be equal to 30 - 30", POINT_30+DELIMITER+POINT_30, game.getCurrentGameStatus());
		//player 1 win the game old score is 40 - 30
		setScore(2, 0); 
		assertEquals("Current Game status should be equal to 0 - 0", DESC_INITIAL_SCORE_LABEL, game.getCurrentGameStatus());
		assertEquals("Game Match state should be in progress",MatchStateEnum.IN_PROGRESS, game.getMatchState());
		
		System.out.println(game.getMatchFullDescription());

    }
    @Test
    public void testPlayerWinsGameAfterDeuceRule() {
    	setScore(3, 3);
		assertEquals("Current Game status should be equal to 40 - 40", POINT_40+DELIMITER+POINT_40, game.getCurrentGameStatus());
		setScore(1, 0);// Player 1 add isAdvanteg
		assertEquals("Current Game status should be equal to Advantage - 40", DESC_ADVANTAGE_LABEL+DELIMITER+POINT_40, game.getCurrentGameStatus());
		setScore(0, 1);//Deuce Rule activated
		assertEquals("Current Game status should be equal to Deuce", DESC_DEUCE_LABEL, game.getCurrentGameStatus());
		setScore(1, 0);// Player add isAdvanteg
		assertEquals("Current Game status should be equal to Advantage - 40", DESC_ADVANTAGE_LABEL+DELIMITER+POINT_40, game.getCurrentGameStatus());
		setScore(1, 0);// Player 1 win the game 
		assertEquals("Current Game status should be equal to 0 - 0", DESC_INITIAL_SCORE_LABEL, game.getCurrentGameStatus());
		assertEquals("Game Match state should be in progress",MatchStateEnum.IN_PROGRESS, game.getMatchState());
		System.out.println(game.getMatchFullDescription());
    }
    
    @Test
    public void testPlayerWinsTheSet() {
    	setGameSet(1,0);// player 1 win the 1st game of the set
    	assertEquals("Current Game status should be equal to 0 - 0", DESC_INITIAL_SCORE_LABEL, game.getCurrentGameStatus());
		assertEquals("The set score Must be 1", ENTIER_1 , player1.getGameSetScorePlayer());
		setGameSet(0,6);// Player win the set
    	assertEquals("Match Score should be equal to (1 - 6)", RIGHT_PARENTHESIS+ENTIER_1+DELIMITER+ENTIER_6+LEFT_PARENTHESIS+SPACE, game.getScoreDescription());
		System.out.println(game.getMatchFullDescription());
    }
    @Test
    public void testPlayerWinTheSetAndMatchBySpecificRuleOfTieBreak() {
    	setGameSet(5,5);// player 1 and player 2 are same set score 5
		assertEquals("The set score Must be 5", ENTIER_5 , player1.getGameSetScorePlayer());
		assertEquals("The set score Must be 5", ENTIER_5 , player2.getGameSetScorePlayer());
		setGameSet(1, 1);// Tie Break is activated
		assertEquals("The set score Must be 6", ENTIER_6 , player1.getGameSetScorePlayer());
		assertEquals("The set score Must be 6", ENTIER_6 , player2.getGameSetScorePlayer());
		assertEquals("The Tie Break initial score must be [0-0]",TIE_BREAK_INITIAL_SCORE, game.getCurrentGameStatus());
		System.out.println(game.getMatchFullDescription());
		setScore(1,0);//Player 1 wins 1 point
		assertEquals("The Tie Break score must be [1-0]","[1-0]", game.getCurrentGameStatus());
		System.out.println(game.getMatchFullDescription());
		setScore(1,6);
		assertEquals("The Tie Break score must be [2-6]",TIE_BREAK_SCORE_2_6, game.getCurrentGameStatus());
		setScore(0,1);//Player 2 wins the Set and Match
		assertEquals("The Tie Break score must be [2-7]",TIE_BREAK_SCORE_2_7, game.getCurrentGameStatus());
    	assertEquals("Match Score should be equal to (6 - 6)[2-7]", RIGHT_PARENTHESIS+ENTIER_6+DELIMITER+ENTIER_6+LEFT_PARENTHESIS+TIE_BREAK_SCORE_2_7+SPACE, game.getScoreDescription());
		System.out.println(game.getMatchFullDescription());
    }

    @Test
    public void testPlayerOneWinTheMatch() {
    	setGameSet(5, 4);
		assertEquals("The set score Must be 5", ENTIER_5 , player1.getGameSetScorePlayer());
		assertEquals("The set score Must be 4", ENTIER_4 , player2.getGameSetScorePlayer());
		setGameSet(1, 0);// Player 1 wins the first set
    	assertEquals("Match Score should be equal to (6 - 4)", RIGHT_PARENTHESIS+ENTIER_6+DELIMITER+ENTIER_4+LEFT_PARENTHESIS+SPACE, game.getScoreDescription());
		assertEquals("Game Match state should be in progress",MatchStateEnum.IN_PROGRESS, game.getMatchState());
		System.out.println(game.getMatchFullDescription());
		setGameSet(5, 5);
		assertEquals("The set score Must be 5", ENTIER_5 , player1.getGameSetScorePlayer());
		assertEquals("The set score Must be 5", ENTIER_5 , player2.getGameSetScorePlayer());
		setGameSet(2, 0);// Player 1 wins the set
		assertEquals("Match Score should be equal to (6 - 4)(7 - 5)",
				RIGHT_PARENTHESIS+ENTIER_6+DELIMITER+ENTIER_4+LEFT_PARENTHESIS+SPACE+
				RIGHT_PARENTHESIS+ENTIER_7+DELIMITER+ENTIER_5+LEFT_PARENTHESIS+SPACE, game.getScoreDescription());
		assertEquals("Game Match state should be in progress",MatchStateEnum.IN_PROGRESS, game.getMatchState());
		System.out.println(game.getMatchFullDescription());
		setGameSet(5, 1);
		assertEquals("The set score Must be 5", ENTIER_5 , player1.getGameSetScorePlayer());
		assertEquals("The set score Must be 5", ENTIER_1 , player2.getGameSetScorePlayer());
		setGameSet(1, 0);// Player 1 wins the set
		assertEquals("Match Score should be equal to (6 - 1)",
				RIGHT_PARENTHESIS+ENTIER_6+DELIMITER+ENTIER_4+LEFT_PARENTHESIS+SPACE+
				RIGHT_PARENTHESIS+ENTIER_7+DELIMITER+ENTIER_5+LEFT_PARENTHESIS+SPACE+
				RIGHT_PARENTHESIS+ENTIER_6+DELIMITER+ENTIER_1+LEFT_PARENTHESIS+SPACE, game.getScoreDescription());
		
		assertEquals("Game Match state should be in progress",MatchStateEnum.FINISHED_PLAYER1_WINS, game.getMatchState());
		System.out.println(game.getMatchFullDescription());
    }
    @Test
    public void testPlayerTwoWinTheMatchWithATieBreak() {
    	setGameSet(5,5);// player 1 and player 2 are same set score 5
		assertEquals("The set score Must be 5", ENTIER_5 , player1.getGameSetScorePlayer());
		assertEquals("The set score Must be 5", ENTIER_5 , player2.getGameSetScorePlayer());
		setGameSet(1, 1);// Tie Break is activated
		assertEquals("The set score Must be 6", ENTIER_6 , player1.getGameSetScorePlayer());
		assertEquals("The set score Must be 6", ENTIER_6 , player2.getGameSetScorePlayer());
		setScore(2,7);//Player 2 wins the Set and Match
		assertEquals("The Tie Break score must be [2-7]",TIE_BREAK_SCORE_2_7, game.getCurrentGameStatus());
    	assertEquals("Match Score should be equal to (6 - 6)[2-7]", RIGHT_PARENTHESIS+ENTIER_6+DELIMITER+ENTIER_6+LEFT_PARENTHESIS+TIE_BREAK_SCORE_2_7+SPACE, game.getScoreDescription());
		System.out.println(game.getMatchFullDescription());
		
    	setGameSet(4, 5);
		assertEquals("The set score Must be 5", ENTIER_4 , player1.getGameSetScorePlayer());
		assertEquals("The set score Must be 4", ENTIER_5 , player2.getGameSetScorePlayer());
		setGameSet(0, 1);// Player 2 wins the first set
		
    	assertEquals("Match Score should be equal to (6 - 6)[2-7] (4 - 6)", 
    			RIGHT_PARENTHESIS+ENTIER_6+DELIMITER+ENTIER_6+LEFT_PARENTHESIS+TIE_BREAK_SCORE_2_7+SPACE+
    			RIGHT_PARENTHESIS+ENTIER_4+DELIMITER+ENTIER_6+LEFT_PARENTHESIS+SPACE, game.getScoreDescription());
		assertEquals("Game Match state should be in progress",MatchStateEnum.IN_PROGRESS, game.getMatchState());
		System.out.println(game.getMatchFullDescription());
		
		setGameSet(5, 5);
		assertEquals("The set score Must be 5", ENTIER_5 , player1.getGameSetScorePlayer());
		assertEquals("The set score Must be 5", ENTIER_5 , player2.getGameSetScorePlayer());
		setGameSet(0, 2);// Player 2 wins the set
		assertEquals("Match Score should be equal to (6 - 6)[2-7] (4 - 6)(5 - 7)",
				RIGHT_PARENTHESIS+ENTIER_6+DELIMITER+ENTIER_6+LEFT_PARENTHESIS+TIE_BREAK_SCORE_2_7+SPACE+
    			RIGHT_PARENTHESIS+ENTIER_4+DELIMITER+ENTIER_6+LEFT_PARENTHESIS+SPACE+
			    RIGHT_PARENTHESIS+ENTIER_5+DELIMITER+ENTIER_7+LEFT_PARENTHESIS+SPACE, game.getScoreDescription());
		assertEquals("Game Match state should be in progress",MatchStateEnum.FINISHED_PLAYER2_WINS, game.getMatchState());
		System.out.println(game.getMatchFullDescription());
    }
    
    /** Add player points **/
    private void setScore(int playerOneWinPoint, int playerTwoWinPoint) {
        for (int counter = 0; counter < playerOneWinPoint; counter++) {
            game.playerOneWinPoint();
        }
        for (int counter = 0; counter < playerTwoWinPoint; counter++) {
            game.playerTwoWinPoint();
        }
    }
    
    /** Add player set game **/
    private void setGameSet(int playerOneWinSet, int playerTwoWinSet) {
        for (int counter = 0; counter < playerOneWinSet; counter++) {
            game.playerOneWinSet();
        }
        for (int counter = 0; counter < playerTwoWinSet; counter++) {
        	game.playerTwoWinSet();
        }
    }
}
