package consoleApp;

import java.util.Scanner;

public class Player {
	public String Name;
	public int PistiCount;
	public Card[] ActiveHand;
	public Card[] OwnedCards;
	public int Point;

	public Player(String name) {
		this.ActiveHand = new Card[4];
		this.OwnedCards = new Card[52];
		this.PistiCount = 0;
		Name=name;
	}

	public int getPistiCount() {

		return this.PistiCount;
	}
	

	public Card[] getOwnedCards() {

		return this.OwnedCards;
	}

	public Card[] getActiveHand() {
		return this.ActiveHand;
	}

	public void PrintActiveHand() {

		for (int i = 0; i < 4; i++) {
			if (ActiveHand[i] != null) {
				System.out.print("█" + ActiveHand[i].GetCardName() + "  ");
			}
		}
	}

	public void PrintOwnedCards(Player player) {
		int OwnedCardsLength = getOwnedCardsLength();
		System.out.print(player.Name+"'s owned cards:  ");
		for (int i = 0; i < OwnedCardsLength; i++) {

			System.out.print("█" + OwnedCards[i].GetCardName() + "  ");

		}
	}

	public Card RemoveCardFromActiveHand(int selectedCardIndex) {
		Card selectedCard = null;
		int indexCounter = 0;
		for (int i = 0; i < 4; i++) {
			if (ActiveHand[i] != null) {
				indexCounter++;
				if (selectedCardIndex == indexCounter) {
					selectedCard = ActiveHand[i];
					ActiveHand[i] = null;
					break;
				}

			}
		}

		return selectedCard;

	}

	public Card PlayHand(int selectedCardIndex, Card topCard) {

		return RemoveCardFromActiveHand(selectedCardIndex);

	}

	public int getOwnedCardsLength() {

		for (int i = 0; i < OwnedCards.length; i++) {
			if (OwnedCards[i] == null) {
				return i;
			}
		}

		return OwnedCards.length;
	}

//For computer
	public Card PlayHand(Card topCard) {

		int selectedCardIndex = FindBestCardToPlay(topCard);
		return RemoveCardFromActiveHand(selectedCardIndex);

	}

//For computer
	private int FindBestCardToPlay(Card topCard) {
		// TODO Auto-generated method stub
		return 0;
	}

}
