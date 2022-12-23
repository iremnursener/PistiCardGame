package consoleApp;

import java.util.Random;
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
		Name = name;
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

	public int getActiveHandLength() {
		int lengthCounter = 0;
		for (int i = 0; i < ActiveHand.length; i++) {

			if (ActiveHand[i] != null) {
				lengthCounter++;

			}
		}

		return lengthCounter;
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
		System.out.print(player.Name + "'s owned cards:  ");
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

//For player1
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

		int selectedCardOrder = FindBestCardToPlay(topCard);
		return RemoveCardFromActiveHand(selectedCardOrder);

	}

//For computer to find best card to play
	public int FindBestCardToPlay(Card topCard) { // computer finds the best index to play
		Random rand = new Random();
		int activeHandLength = getActiveHandLength();
		int randomCardOrder = rand.nextInt(activeHandLength) + 1;

		if (topCard != null) {
			return thereIsATopcard(topCard, randomCardOrder);
		} else {
			return thereIsNoTopcard(randomCardOrder);

		}

	}

//If there is a topcard
	public int thereIsATopcard(Card topCard, int randomCardOrder) {
		for (int i = 0; i < ActiveHand.length; i++) {
			if (ActiveHand[i] != null) {
				if (ActiveHand[i].CardNumber.equals(topCard.CardNumber)) {
					return GetOrderFromIndex(i);
				} else if (ActiveHand[i].CardNumber.equals("J")) {
					return GetOrderFromIndex(i);
				}
			}
		}
		return randomCardOrder;
	}

//Checks if all cards are J
	public boolean isAllCardsJ() {
		boolean notJ = false;
		for (int i = 0; i < ActiveHand.length; i++) {
			if (ActiveHand[i] != null && !ActiveHand[i].CardNumber.equals("J")) {
				notJ = true;
				break;
			}
		}

		return notJ;
	}

// If there is no topcard
	public int thereIsNoTopcard(int randomCardOrder) {
		boolean notJ = isAllCardsJ();
		if (notJ == false) {
			return randomCardOrder;
		} else {
			for (int i = 0; i < ActiveHand.length; i++) {

				if (ActiveHand[i] != null && !ActiveHand[i].CardNumber.equals("J")) {
					return GetOrderFromIndex(i);

				}

			}
		}
return 0;
	}

//For computer
	private int GetOrderFromIndex(int index) {
		int order = 0;
		for (int i = index; i >= 0; i--) {
			if (ActiveHand[i] != null) {
				order++;
			}
		}

		return order;
	}

}
