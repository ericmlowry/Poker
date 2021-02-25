import java.util.ArrayList;
import java.util.Scanner;

public class PokerCompare {
	/**
	 * The array of Hands
	 */
	private Hand[] playerHands;
	
	/**
	 * The ArrayList of claim ArrayLists
	 */
	private ArrayList<ArrayList<Integer>> claimArrays;

	/**
	 * Forms a new PokerCompare object - 2+ players!
	 * @param h The array of the string representations of player hands
	 * @param n The array of names
	 */
	public PokerCompare(String[] h, String[] n) {
		claimArrays = new ArrayList<ArrayList<Integer>>();
		playerHands = new Hand[h.length];
		for (int i = 0; i < h.length; i++) {
			playerHands[i] = new Hand(h[i], n[i]);
			claimArrays.add(playerHands[i].highestClaim());
		}
	}

	/**
	 * Finds the winner
	 * @return Array -  [0]: The index of the winning player in playerHands. -1 if there's a tie.
	 * 					[1]: The defining tie breaker value of the winning hand.
	 */
	public int[] findWinner() {
		int[] ans = new int[2];
		int maxClaim = 0;
		ArrayList<Integer> challengers = new ArrayList<Integer>(); //keeps track of the players with the max claim
		
		for (int i = 0; i < playerHands.length; i++) {
			Integer c = claimArrays.get(i).get(0);
			if (c > maxClaim) {
				challengers.clear();
				maxClaim = c;
				challengers.add(i);
			} else if (c == maxClaim) {
				challengers.add(i);
			}
		}
		
		int winningChallengeIndex = 0; //keeps track of the index of the winning player within challengers
		ArrayList<Integer> winningClaim; //holds the winning hand's claim array
		boolean tie = false; //keeps track if there's a tie for the winning claim
		if (challengers.size() > 1) {
			winningClaim = claimArrays.get(challengers.get(winningChallengeIndex));
			for (int i = 1; i < challengers.size(); i++) {
				ArrayList<Integer> contestingClaim = claimArrays.get(challengers.get(i));
				for (int j = 1; j < winningClaim.size(); j++) {
					if (winningClaim.get(j) == contestingClaim.get(j)) {
						if (j == winningClaim.size()-1) {
							//gone through all cards and they are the same
							tie = true;
						}
						continue;
					} else if (winningClaim.get(j) < contestingClaim.get(j)) {
						//if contesting has the higher claim, set variables and reset tie
						winningChallengeIndex = i;
						winningClaim = claimArrays.get(challengers.get(winningChallengeIndex));
						if (j != 1) {
							ans[1] = contestingClaim.get(j);
						}
						tie = false;
						break;
					} else if (winningClaim.get(j) > contestingClaim.get(j)) {
						//if winning has the higher claim, stop claim array comparison
						if (j != 1 && (ans[1] > winningClaim.get(j)) || ans[1] == 0) {
							ans[1] = winningClaim.get(j);
						}
						break;
					}
				}
				if (i == challengers.size()-1 && tie) {
					//if we have gone through all claims, there was a tie, and tie wasn't reset
					ans[0] = -1;
					return ans;
				}
			}
		}
		ans[0] = challengers.get(winningChallengeIndex);
		return ans;
	}
	
	/**
	 * Returns a string denoting winner and winning hand
	 * @param a Array from findWinner
	 * @return Descriptive string
	 */
	public String printWinner(int[] a) {
		if (a[0] == -1) {
			return "Tie.";
		}
		String ans = "";
		ans += playerHands[a[0]].getName() + " wins. - with ";
		ans += Hand.printHand(claimArrays.get(a[0]), playerHands[a[0]].getSameSuit());
		
		if (a[1] != 0) {
			ans += " and tiebreaker high card: " + Card.convert(a[1]);
		}
		return ans;
	}

	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in); 

		System.out.println("Input in this format - Player1: KS AH 2C 3D 5D  Player2: 4C 5C 6C 7C 8C  Player...");
		System.out.println("                              ^               ^               ^");
		System.out.println("                             1x':'           2x' '           1x' '");

		System.out.println("Hit Enter to assess the round and produce the winner. Hit Enter again to terminate.");

		String input = sc.nextLine();
		while (!input.equals("")) {
			String[] playerNamesHands = input.split("  ");
			String[] names = new String[playerNamesHands.length];
			String[] hands = new String[playerNamesHands.length];
			for (int i = 0; i < playerNamesHands.length; i++) {
				hands[i] = playerNamesHands[i].substring(playerNamesHands[i].indexOf(':') + 2);
				names[i] = playerNamesHands[i].substring(0, playerNamesHands[i].indexOf(':'));
			}
			PokerCompare game = new PokerCompare(hands, names);
			System.out.println(game.printWinner(game.findWinner()));
			input = sc.nextLine();

		}
		
	}

}
