package consoleApp;

import java.util.Scanner;

public class Player {

	public Card[] ActiveHand;
	public Card[] OwnedCards;
	public int Point;

	public Player() {
		this.ActiveHand = new Card[4];
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
				System.out.print(ActiveHand[i].GetCardName() + "  ");
			}
		}
	}

	public void RemoveCardFromActiveHand(int cardOrder) {

		int indexCounter = 0;
		for (int i = 0; i < 4; i++) {
			if (ActiveHand[i] != null) {
				indexCounter++;
				if (cardOrder == indexCounter) {
					ActiveHand[i] = null;
					break;
				} 

			}
		}
	
	}

	public void PlayHand(int cardOrder) {

		RemoveCardFromActiveHand(cardOrder);
	
		
	}

}
