package consoleApp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.Random;
import java.util.Scanner;

public class Game {

	public static String FileName = "leaderboard.txt";

	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);

		Deck gameDeck = new Deck();
		System.out.println("Enter your name:");
		String name = scan.next();
		Player player1 = new Player(name);
		Player computer = new Player("Computer");
		Card[] board = new Card[52];
		Player lastWinner = null;
		Boolean isWinner = null;
		Card topCard = null;
		Card selectedCard = null;
		Card[] pistiCards = null;
		FileOperations operator = new FileOperations();

		cutDeckFromPoint(gameDeck); // gets input and checks the entered number is valid or not for cutting the deck

		// PrintDeck(gameDeck.getCards()); // CONTROL
		// gameDeck.CutDeck(cutPoint); // Cards are ready to play CONTROL
		// System.out.println("---"); // CONTROL
		// PrintDeck(gameDeck.getCards()); // CONTROL

// Game Starts-------------------------------
		for (int hand = 1; hand <= 6; hand++) { // we will play the game until all cards(52) are used so game will last
												// until 6th hand is played
			System.out.println();
			System.out.println("HAND" + hand + "⛿");

			Random rand = new Random();

			DealHands(gameDeck.getCards(), player1, computer, board, hand); // deals hands before starting the game

			for (int times = 1; times <= 4; times++) { // a player(computer and player1) have 4 cards so players will
														// play cards for 4 times

				PrintGameTable(board, computer, player1); // we show the current table(before starting the hand and
															// after playing a card)

				System.out.println();
				System.out.println("════════════════════════════════════════");
				System.out.println("Choose one card to play:");
//Here we select a valid card order to play and evaluate the card
				int a = 0;
				int selectedCardIndex = 0;
				for (int i = 0; i < a + 1; i++) {

					try {

						if (scan.hasNextInt()) {
							selectedCardIndex = scan.nextInt();
						} else {
							System.out.println("Invalid input. Please try again.");
							System.out.println();
							a++;
							scan.nextLine();
							continue;
						}

						topCard = GetTopCard(board);

						selectedCard = player1.PlayHand(selectedCardIndex);
						pistiCards = player1.getPistiCards();
						isWinner = EvaluatePlayedCard(player1, selectedCard, board, topCard, pistiCards);

					} catch (Exception e) {
						System.out.println("Something went wrong.Please enter a valid card number.");
						a++;
					}
				}

				if (isWinner == true) {
					lastWinner = player1;
				}
//same process for the computer
				topCard = GetTopCard(board);

				selectedCard = computer.PlayHand(topCard);
				pistiCards = computer.getPistiCards();
				isWinner = EvaluatePlayedCard(computer, selectedCard, board, topCard, pistiCards);
				if (isWinner == true) {
					lastWinner = computer;
				}
//At the end of every play we show the current owned cards
				player1.PrintOwnedCards(player1);
				System.out.println("Pişti:" + player1.getPistiCount());
			}
		}
//Game is finished-----------------------------------------------------------
		// Announced the winner
		System.out.println("🏁Game is finished🏁");
		System.out.println("►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►►");
		System.out.println("Last winner is " + lastWinner.getName());
		CheckLastBoard(board, lastWinner);
//Prints owned cards for each player
		player1.PrintOwnedCards(player1);
		System.out.println("Pişti:" + player1.getPistiCount());
		player1.PrintPistiCards(player1);
		System.out.println(); // Space
		computer.PrintOwnedCards(computer);
		System.out.println("Pişti:" + computer.getPistiCount());
		computer.PrintPistiCards(computer);
//Prints empty game table
		PrintGameTable(board, computer, player1);
		System.out.println();
//gets total points for each player
		int point1 = CalculatePoints(player1);
		System.out.println();
		int point2 = CalculatePoints(computer);
		Player winner = null;
//determines who the winner is
		int winnerPoint = 0;
		if (point1 > point2) {
			winner = player1;
			winnerPoint = point1;
			System.out.println("THE WINNER IS  " + player1.getName());
		} else if (point1 < point2) {
			winner = computer;
			winnerPoint = point2;
			System.out.println("THE WINNER IS COMPUTER");
		} else {
			System.out.println("DEUCE");
		}

// Saving top scores to a file
		if (winner != null) { //point can be equal
			operator.SavePlayerToLeaderboard(winner);
		}
//---------------------------------------------------------------------------
	}

//cut deck from a given point(gets input)
	public static void cutDeckFromPoint(Deck gameDeck) {
		int a = 0;
		for (int i = 0; i < a + 1; i++) {
			try {

				int cutPoint = GetCutPointFromUser();
				gameDeck.CutDeck(cutPoint);
			} catch (Exception e) {
				System.out.println("Something went wrong.Enter a number between 1-52");
				a++;
			}
		}

	}

//Evaluates played cards 
	private static Boolean EvaluatePlayedCard(Player player, Card selectedCard, Card[] board, Card topCard,
			Card[] PistiCards) {
		Boolean isWinner = false;
		int boardLength = GetBoardLength(board);
		board[boardLength] = selectedCard;

		if (boardLength == 1 && selectedCard.getCardNumber().equals(topCard.getCardNumber())) {
			System.out.println("PİŞTİ!!");
			player.increasePistiCount();
			AddCardsToPistiCards(player, PistiCards, player.getPistiCount(), board);
			RemoveCardsFromBoard(board);
			isWinner = true;

		}

		if (topCard == null) {
			board[0] = selectedCard;

		}

		if (topCard != null) {

			if (selectedCard.getCardNumber().equals(topCard.getCardNumber())
					|| selectedCard.getCardNumber().equals("J")) {
				AddCardsToOwnedCards(player, board, player.getOwnedCards());
				RemoveCardsFromBoard(board);
				isWinner = true;

			}

		}
		return isWinner;
	}

	// Checks table and add last cards on the board to the last player who got cards
	// from board.
	public static void CheckLastBoard(Card[] board, Player lastWinner) {

		AddCardsToOwnedCards(lastWinner, board, lastWinner.getOwnedCards());
		RemoveCardsFromBoard(board);
	}

//add cards to players owned cards
	public static void AddCardsToOwnedCards(Player player, Card[] board, Card[] OwnedCards) {
		int boardLength = GetBoardLength(board);
		if (OwnedCards[0] == null) {
			for (int i = 0; i < boardLength; i++) {
				player.getOwnedCards()[i] = board[i];
			}
		} else {
			int currentOwnedCardsLength = player.getOwnedCardsLength();
			int boardIndex = 0;
			for (int i = currentOwnedCardsLength; i < currentOwnedCardsLength + boardLength; i++) {
				player.getOwnedCards()[i] = board[boardIndex];
				boardIndex++;
			}
		}

	}

//add cards to players pişti cards 
	public static void AddCardsToPistiCards(Player player, Card[] PistiCards, int PistiCount, Card[] board) {
		if (PistiCards[0] == null) {
			for (int i = 0; i < PistiCount * 2; i++) {
				player.getPistiCards()[i] = board[i];
			}
		} else {
			int currentLength = player.getPistiCardsLength();
			int boardIndex = 0;
			for (int i = currentLength; i < PistiCount * 2; i++) {
				player.getPistiCards()[i] = board[boardIndex];
				boardIndex++;
			}
		}
	}

//clears board 
	public static void RemoveCardsFromBoard(Card[] board) {
		int boardLength = GetBoardLength(board);

		for (int i = 0; i < boardLength; i++) {
			board[i] = null;
		}

	}

//gets the top card
	private static Card GetTopCard(Card[] board) { // we need to see the top card to decide which card we should play

		int boardLength = GetBoardLength(board); // we need to know the length of the board to see the last card on the
		if (boardLength != 0) {

			return board[boardLength - 1];
		}
		return null;
	}

//gets the current length of the board 
	private static int GetBoardLength(Card[] board) { // we search for all the indexes of board.the first "i" that gives
														// us the value null is the current length of the array.

		for (int i = 0; i < board.length; i++) {
			if (board[i] == null) {
				return i;
			}
		}

		return board.length; // if there is no null value, the length of the array is equal to the value we
								// defined first.
	}

//Dealing Hands 
	public static void DealHands(Card[] cards, Player player1, Player computer, Card[] board, int hand) {
		// Dealing First Hand (Including Board)
		if (hand == 1) {
			int cardindex = 0;
			for (int i = 0; i < 4; i++) {

				player1.getActiveHand()[i] = cards[cardindex];
				cardindex++;
				computer.getActiveHand()[i] = cards[cardindex];
				cardindex++;
				board[i] = cards[cardindex];
				cardindex++;
			}
			// Dealing Hands( 2-6 )
		} else {
			int cardindex = 4 + ((hand - 1) * 8);
			for (int i = 0; i < 4; i++) {

				player1.getActiveHand()[i] = cards[cardindex];
				cardindex++;
				computer.getActiveHand()[i] = cards[cardindex];
				cardindex++;

			}
		}

	}

//Getting CutPoint From User
	public static int GetCutPointFromUser() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Where do you want me to cut the deck?");
		int cutPoint = scan.nextInt();
		return cutPoint;
	}

//Printing Deck
	public static void PrintDeck(Card[] cards) {
		for (int i = 0; i < cards.length; i++) {
			System.out.println(cards[i].GetCardName());
		}
	}

//Printing ActiveHand
	public static void PrintGameTable(Card[] board, Player computer, Player player1) {
		System.out.println();
		//System.out.print("Computer ➤ ");
		//computer.PrintActiveHand(); // to see computers active hand
		System.out.println();
		//System.out.println("════════════════════════════════════════");
		System.out.println("❮ BOARD ❯");

		int boardLength = GetBoardLength(board);
		for (int i = boardLength - 1; i >= 0; i--) {
			if (board[i] != null) {

				if (i == boardLength - 1) {
					System.out.println("█" + board[i].GetCardName() + "  ");
					System.out.println();
				} else {
					System.out.print("█" + board[i].GetCardName() + "  ");
				}
			}
		}

		System.out.println();
		System.out.println("════════════════════════════════════════");
		System.out.print(" YOU ➤  ");
		player1.PrintActiveHand();

	}

//Calculating Total Points 
	private static int CalculatePoints(Player player) {

		int numberOfOwnedCards = player.getOwnedCardsLength();
		;
		int pistiCardsPoint = (player.getPistiCount()) * 10;
		int specialCardsPoints = 0;
		int specialCardCounter = 0;
		for (int i = 0; i < numberOfOwnedCards; i++) {
			if (player.getOwnedCards()[i].GetCardName().equals("♦10")) {
				specialCardsPoints += 3;
				specialCardCounter++;
			}
			if (player.getOwnedCards()[i].GetCardName().equals("♣2")) {
				specialCardsPoints += 2;
				specialCardCounter++;
			}

		}
		int normalCardsNumber = numberOfOwnedCards - specialCardCounter; // normal cards are 1 point
		player.setTotalPoint(pistiCardsPoint + specialCardsPoints + normalCardsNumber);

		if (numberOfOwnedCards + player.getPistiCount() * 2 > 26) {
			player.setTotalPoint(player.getTotalPoint() + 3);
		}
		System.out.println(player.getName() + "'s total point :" + player.getTotalPoint());
		return player.getTotalPoint();
	}

}
