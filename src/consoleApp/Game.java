package consoleApp;

import java.util.Scanner;

public class Game {

	public static void main(String[] args) {

		Deck gameDeck = new Deck();
		Player player1 = new Player();
		Player computer = new Player();
		Card[] board = new Card[52];

		int cutPoint = GetCutPointFromUser();

		PrintDeck(gameDeck.getCards()); // CONTROL
		gameDeck.CutDeck(cutPoint); // Cards are ready to play CONTROL
		System.out.println("---"); // CONTROL
		PrintDeck(gameDeck.getCards()); // CONTROL
		DealFirstHand(gameDeck.getCards(), player1, computer, board);
		PrintActiveHand(player1.getActiveHand(), board);

		/*
		 * 
		 * while (!gameDeck.IsAllCardsUsed()) {
		 * 
		 * DealHands();
		 * 
		 * PlayHands(players);
		 * 
		 * }
		 * 
		 * CalculatePoints(players);
		 */

	}

	public static void DealHands(Card[] cards, Player player1, Player computer, Card[] board) {
		int cardindex = 12;
		for (int i = 4; i < 8; i++) {
			player1.ActiveHand[i] = cards[cardindex];
			cardindex++;
			computer.ActiveHand[i] = cards[cardindex];
			cardindex++;
		}

	}

//Dealing First Hand (Including Board)
	public static void DealFirstHand(Card[] cards, Player player1, Player computer, Card[] board) {
		int cardindex = 0;
		for (int i = 0; i < 4; i++) {
			player1.ActiveHand[i] = cards[cardindex];
			cardindex++;
			computer.ActiveHand[i] = cards[cardindex];
			cardindex++;
			board[i] = cards[cardindex];
			cardindex++;
		}

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

//Printing First Hand
	public static void PrintActiveHand(Card[] ActiveHand, Card[] board) {
		System.out.println(" █ █ █ █ BOARD ");
		for (int i = 0; i < 4; i++) {

			System.out.print(board[i].GetCardName() + "  ");

		}
		System.out.println();
		System.out.println("█ █ █ █ YOU ");
		for (int i = 0; i < 4; i++) {
			System.out.print(ActiveHand[i].GetCardName() + "  ");
		}

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
