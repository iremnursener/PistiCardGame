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

	public Card[] RemoveCardFromActiveHand(int cardOrder, int times) {
		if (times == 1) {
			ActiveHand[cardOrder - 1] = null;
		} else {
			int indexCounter = 0;
			for (int i = 0; i < 4; i++) {
				if (ActiveHand[i] != null && ActiveHand[cardOrder ]!= ActiveHand[indexCounter]) {
					indexCounter++;
					if (ActiveHand[cardOrder ] ==ActiveHand[ indexCounter]) {
						cardOrder = indexCounter;
						break;
					} 

				}
				

			}

			ActiveHand[cardOrder] = null;
		}
		return ActiveHand;
	}

	public void PlayHand(int cardOrder, int times) {

		RemoveCardFromActiveHand(cardOrder, times);
		PrintActiveHand();

	}

}
