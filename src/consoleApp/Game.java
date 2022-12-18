package consoleApp;

import java.util.Random;
import java.util.Scanner;

public class Game {

	public static void main(String[] args) {

		Deck gameDeck = new Deck();
		Player player1 = new Player();
		Player computer = new Player();
		Card[] board = new Card[52];
		Player lastWinner = null;
		int cutPoint = GetCutPointFromUser(); // gets the cut point from user

		// PrintDeck(gameDeck.getCards()); // CONTROL
		// gameDeck.CutDeck(cutPoint); // Cards are ready to play CONTROL
		// System.out.println("---"); // CONTROL
		// PrintDeck(gameDeck.getCards()); // CONTROL

		gameDeck.CutDeck(cutPoint); // cuts the deck
		// PrintDeck(gameDeck.getCards()); // CONTROL

// Game-------------------------------
		for (int hand = 1; hand <= 6; hand++) { // we will play the game until all cards(52) are used so game will last
												// until 6th hand is played
			System.out.println();
			System.out.println("HAND" + hand + "⛿");

			Scanner scan = new Scanner(System.in);
			Random rand = new Random();

			DealHands(gameDeck.getCards(), player1, computer, board, hand); // deals hands before starting the game

			for (int times = 1; times <= 4; times++) { // a player(computer and player1) have 4 cards so players will
														// play cards for 4 times

				PrintGameTable(board, computer, player1); // we show the current table(before starting the hand and
															// after playing a card)

				System.out.println();
				System.out.println("════════════════════════════════════════");
				System.out.println("Choose one card to play:");
				int selectedCardIndex = scan.nextInt();
				//CheckSelectedIndex(selectedCardIndex);

				int randomCardOrder = rand.nextInt(4 - times + 1) + 1;

				Card topCard = GetTopCard(board);

				Card selectedCard = player1.PlayHand(selectedCardIndex, topCard);

				EvaluatePlayedCard(player1, lastWinner, selectedCard, board, topCard);

				topCard = GetTopCard(board);

				selectedCard = computer.PlayHand(randomCardOrder, topCard);

				EvaluatePlayedCard(computer, lastWinner, selectedCard, board, topCard);

				CheckLastBoard(board, times, hand, lastWinner); // For last hand
				player1.PrintOwnedCards();
				System.out.println("Pişti:" + player1.getPistiCount());
			}
		}

		System.out.println("Game is finished");
		PrintGameTable(board, computer, player1);

	}

	public static int CheckSelectedIndex(int selectedCardIndex) {
		Scanner scan = new Scanner(System.in);
		while (!(selectedCardIndex < 5) && !(selectedCardIndex > 0)) {
			System.out.println("Choose one card to play again:");
			selectedCardIndex = scan.nextInt();
		}
		return selectedCardIndex;
	}

	private static Player EvaluatePlayedCard(Player player, Player lastWinner, Card selectedCard, Card[] board,
			Card topCard) {

		int boardLength = GetBoardLength(board);
		board[boardLength] = selectedCard;

		if (boardLength == 1 && selectedCard.CardNumber.equals(topCard.CardNumber)) {
			System.out.println("PİŞTİ!!");
			player.PistiCount++;
			AddCardsToOwnedCards(player, board, player.OwnedCards);
			RemoveCardsFromBoard(board);
			player = lastWinner;
			return lastWinner;
		}

		if (topCard == null) {
			board[0] = selectedCard;
			player = lastWinner;
			return lastWinner;

		}

		if (topCard != null) {

			if (selectedCard.CardNumber.equals(topCard.CardNumber) || selectedCard.CardNumber.equals("J")) {
				AddCardsToOwnedCards(player, board, player.OwnedCards);
				RemoveCardsFromBoard(board);
				player = lastWinner;
				return lastWinner;
			}

		}
		return lastWinner;
	}

	// Checks table and add last cards on the board to the last player who got cards
	// from board.
	public static void CheckLastBoard(Card[] board, int times, int hand, Player lastWinner) {
		int boardLength = GetBoardLength(board);
		if (times == 4 && hand == 6 && boardLength != 0) {
			AddCardsToOwnedCards(lastWinner, board, lastWinner.OwnedCards);

		}
	}

	public static void AddCardsToOwnedCards(Player player, Card[] board, Card[] OwnedCards) {
		int boardLength = GetBoardLength(board);
		if (OwnedCards[0] == null) {
			for (int i = 0; i < boardLength; i++) {
				player.OwnedCards[i] = board[i];
			}
		} else {
			int currentOwnedCardsLength = player.getOwnedCardsLength();
			int boardIndex = 0;
			for (int i = currentOwnedCardsLength; i < currentOwnedCardsLength + boardLength; i++) {
				player.OwnedCards[i] = board[boardIndex];
				boardIndex++;
			}
		}

	}

	public static void RemoveCardsFromBoard(Card[] board) {
		int boardLength = GetBoardLength(board);

		for (int i = 0; i < boardLength; i++) {
			board[i] = null;
		}

	}

	private static Card GetTopCard(Card[] board) { // we need to see the top card to decide which card we should play

		int boardLength = GetBoardLength(board); // we need to know the length of the board to see the last card on the
		if (boardLength != 0) {

			return board[boardLength - 1];
		}
		return null;
	}

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

				player1.ActiveHand[i] = cards[cardindex];
				cardindex++;
				computer.ActiveHand[i] = cards[cardindex];
				cardindex++;
				board[i] = cards[cardindex];
				cardindex++;
			}
			// Dealing Hands( 2-6 )
		} else {
			int cardindex = 4 + ((hand - 1) * 8);
			for (int i = 0; i < 4; i++) {

				player1.ActiveHand[i] = cards[cardindex];
				cardindex++;
				computer.ActiveHand[i] = cards[cardindex];
				cardindex++;

			}
		}

	}

	public static Card[] AddCardsToBoard(Card[] board, Card[] ActiveHand, int cardOrder, int times) {
		if (times == 1) {
			int lastCardIndex = 3;
			if (board[lastCardIndex].CardNumber != ActiveHand[cardOrder].CardNumber) {
				lastCardIndex++;
				board[lastCardIndex] = ActiveHand[cardOrder];

			}

		}
		return board;
	}

//Getting CutPoint From User
	public static int GetCutPointFromUser() {
		Scanner scan = new Scanner(System.in);
		System.out.println("Where do you want me to cut the deck?");
		int cutPoint = scan.nextInt();
		return cutPoint;
	}

//Printing Shuffled Deck
	public static void PrintDeck(Card[] cards) {
		for (int i = 0; i < cards.length; i++) {
			System.out.println(cards[i].GetCardName());
		}
	}

//Printing ActiveHand
	public static void PrintGameTable(Card[] board, Player computer, Player player1) {
		System.out.println();
		System.out.print("Computer ➤  ");
		computer.PrintActiveHand();
		System.out.println();
		System.out.println("════════════════════════════════════════");
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

	/*
	 * private static void CalculatePoints(Player[] players) {
	 * 
	 * for (int i = 0; i < players.length; i++) { Player player = players[i];
	 * 
	 * int point = CalculatePlayerPoint(player.OwnedCards);
	 * 
	 * player.Point = point; } }
	 * 
	 * private static int CalculatePlayerPoint(Card[] ownedCards) {
	 * 
	 * return 0; }
	 * 
	 * private static void playHands(Player[] players) {
	 * 
	 * }
	 */

}
