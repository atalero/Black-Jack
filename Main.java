package black_jack;

public class Main {
	
	private static final int SUITS = 4;
	private static final int NUMBER_OR_SYMBOL = 13;
	private static final int INITIAL_CAPACITY = 52;
	private static final int STARTING_BET = 5000;
	
	private static void decideWinner(int userScore, int dealerScore, User_Interface ui, 
			Player user, Player dealer, int bet) {
		if (userScore > 21){
			ui.sendMessage("OOPS, you busted, you lose your bet\nyou now have $" + user.getMoney());
			
		} else if(dealerScore > 21){
			user.setMoney(user.getMoney() + (2*bet));
			ui.sendMessage("The dealer busted, you win your bet's amount\nyou now have $" + user.getMoney());
			
		} else if (userScore > dealerScore){
			user.setMoney(user.getMoney() + (2*bet));
			ui.sendMessage("No one busted, but you still win and get your bet's worth"
					+ "\nyou now have $" + user.getMoney());
			
		} else if (dealerScore > userScore){
			ui.sendMessage("No one busted, but the dealer wins\nYou now have $" + user.getMoney());
			
		} else if (userScore == dealerScore){
			user.setMoney(user.getMoney() + bet);
			ui.sendMessage("Stand-off, you get your money back\nYou now have $" + user.getMoney());
		}
		
	}
	
	private static void setHitStallPrompt(User_Interface ui, Player user, Player dealer) {
		ui.setPrompt("Dealer's cards:\n" + dealer.data[0] + "\nX\n\nYour cards:\n" + 
				user.data[0] + "\n" + user.data[1] + "\n" + user.data[2] + "\n" + user.data[3] + 
				"\n" + user.data[4] + "\n" + user.data[5]);
		
	}
	
	private static void userPlays(Player user, User_Interface ui, deckOfCards<Card> deck, Player dealer) {
		
		String[] commands = {
				"Stall",
				"Hit",
				};
		
		setHitStallPrompt(ui,user,dealer);
		
		while(calculateSum(user,ui,false) <= 21){
			int c = ui.getCommand(commands);
			switch (c) {
			case -1:
				System.exit(0);
			case 0:
				ui.sendMessage("You have stalled, the dealer will now play");
				return;
			case 1:
				user.AddCardToHand(deck.pop());
				setHitStallPrompt(ui,user,dealer);
				break;
			}
		}
		
		ui.sendMessage("OOPS, you busted, you lose your bet\nyou now have " + user.getMoney());
		displayCardsNoHole(user, dealer, ui);
	}
	
	private static void dealerPlays(Player dealer, deckOfCards<Card> deck, Player user, User_Interface ui) {
		boolean dealerHit = false;
		
		while(dealer.sumAceIs11() < 17){
			dealer.AddCardToHand(deck.pop());
			displayCardsDealerPlays(user, dealer, ui);
			dealerHit = true;
		}
		
		ui.sendMessage("The dealer has stalled");
		
		if(dealerHit == false){
			displayCardsNoHole(user, dealer,ui);
		}
		
	}
	
	private static void displayCardsDealerPlays(Player user, Player dealer, User_Interface ui) {
		ui.sendMessage("Dealer Now Playing\n\nDealer's cards:\n" + dealer.data[0] + "\n" + dealer.data[1] + "\n" + dealer.data[2] +
				"\n" + dealer.data[3] + "\n" + dealer.data[4] + "\n" + dealer.data[5]+
				"\n\nYour cards:\n" + 
				user.data[0] + "\n" + user.data[1] + "\n" + user.data[2] + "\n" + user.data[3] + "\n" + 
				user.data[4] + "\n" + user.data[5]);
	}
	
	private static int calculateSum(Player player, User_Interface ui, boolean dealer) {
			if(player.sumAceIs11() >= 21){
				return(player.sumAceIs1());
			}
			return player.sumAceIs11();
	}
	
	private static void displayCards(Player user, Player dealer, User_Interface ui) {
		ui.sendMessage("Dealer's cards:\n" + dealer.data[0] + "\nX\n\nYour cards:\n" + 
				user.data[0] + "\n" + user.data[1]);
	}
	
	private static void dealAgain(Player user, Player dealer, deckOfCards<Card> deck) {
			user.dealAgain(deck.pop(), deck.pop());
			dealer.dealAgain(deck.pop(), deck.pop());
	}
		
	private static boolean compareNaturals(boolean checkNaturals, boolean checkNaturals2, Player user, 
			Player dealer, int bet, User_Interface ui, deckOfCards<Card> deck, int insurance) {
		
		
		//Dealer and Player have naturals
		if (checkNaturals == true && checkNaturals2 == true){
			user.setMoney(user.getMoney()+ bet);
			ui.sendMessage("Looks like you both have naturals!, Let's deal the cards again\nYou get your bet back"
					+ "\nYou now have " + user.getMoney());
			
			if(insurance != 0){
				user.setMoney(user.getMoney() + (insurance*2));
				ui.sendMessage("Fortunately, you bought insurance, you will get paid back twice your insurance.\n You now have $"
						+ user.money);
			}
			
			displayCardsNoHole(user, dealer, ui);
			dealAgain(user, dealer, deck);
			return false;
		}
		
		//Only the player has a natural
		if (checkNaturals == true && checkNaturals2 == false){
			user.setMoney(user.getMoney()+ (int)(2.5*bet));	
			ui.sendMessage("Congrats, dealer pays 1.5 times you bet\n You now have $" + user.getMoney());
			displayCardsNoHole(user, dealer, ui);
			dealAgain(user, dealer, deck);
			return false;
		}
		
		//Only the dealer has a natural
		if (checkNaturals == false && checkNaturals2 == true){	
			ui.sendMessage("Oh no, the dealer has a natural and you don't\n You now have $" + user.getMoney());
			
			if(insurance != 0){
				user.setMoney(user.getMoney() + (insurance*2));
				ui.sendMessage("Fortunately, you bought insurance, you will get paid back twice your insurance.\n You now have $"
						+ user.money);
			}
			
			displayCardsNoHole(user, dealer, ui);
			dealAgain(user, dealer, deck);
			return false;
		}
		
		
		return true;
		
	}
	
	private static void displayCardsNoHole(Player user, Player dealer, User_Interface ui) {
		ui.sendMessage("Dealer's cards:\n" + dealer.data[0] + "\n" + dealer.data[1] + "\n" + dealer.data[2] +
				"\n" + dealer.data[3] + "\n" + dealer.data[4] + "\n" + dealer.data[5]+
				"\n\nYour cards:\n" + 
				user.data[0] + "\n" + user.data[1] + "\n" + user.data[2] + "\n" + user.data[3] + "\n" + 
				user.data[4] + "\n" + user.data[5]);
	}

	private static boolean checkNaturals(Player player) {

		if ((player.data[0].valueAceIs11() + player.data[1].valueAceIs11()) == 21)
			return true;
		
		return false;
	}
	
	public static void play(User_Interface ui, deckOfCards<Card> deck, Player user, Player dealer) {
		int numberOfPlays = 0;
		int bet;
		boolean doubled = false;
		Card [] data = new Card[INITIAL_CAPACITY];
		
		while(user.money > 0){
				bet = requestBet(user,ui);
				ui.sendMessage("Now dealing cards");
				displayCards(user, dealer, ui);
				int insurance = 0;
				
				if(dealer.data[0].valueAceIs1() == 1){
					insurance = askForInsurance(ui,user,dealer,bet);
				}
				
				if(dealer.data[0].valueAceIs1() == 10 || dealer.data[0].valueAceIs1() == 1){
					ui.sendMessage("The dealer's face up card is " + dealer.data[0] + ". Dealer will now check for Black Jack");
				}
				
				if(compareNaturals(checkNaturals(user),checkNaturals(dealer), user, dealer, bet, ui, deck, insurance)){
				//displayCards(user, dealer, ui);
					
					if(insurance != 0){
						ui.sendMessage("Unfortunately, you lose your insurance");
					}
				
/*					if(user.data[0].cardNumber.equals(user.data[1].cardNumber)){
						askIfSplitting(ui,deck,player);
					}
*/					
					//If a double is possible
					if (user.sumAceIs1() == 9 || user.sumAceIs1() == 10 || user.sumAceIs1() == 11){
						ui.setPrompt("The sum of you cards is " + user.sumAceIs1() +"!\nYou can double down. "
								+ " Would you like to? (Current bet is " + bet + ")");
					String[] commands = {
							"Yes",
							"No",
							};
					int c = ui.getCommand(commands);
					switch (c) {
					case -1:
						System.exit(0);
					case 0:
						if(user.money-(bet*2)>0){
							doubled = true;
							bet = bet*2;
							user.setMoney(user.getMoney()- bet);
							ui.sendMessage("You doubled down, you get will one more card\nYour bet is now " + bet);
							user.data[2] = deck.pop();
						} else if (user.money-(bet*2)<0){
							ui.sendMessage("Looks liks you don't have enough money to double down, sorry");
						}
						break;
					case 1:
						break;
					}
					
				}
				
				if (doubled == false){
					userPlays(user, ui, deck, dealer);
				}
				
				doubled = false;
				
				if(user.sumAceIs1() <= 21){
					dealerPlays(dealer,deck,user, ui);
					
					decideWinner(calculateSum(user, ui, false),calculateSum(dealer, ui, true),ui, user, dealer, bet);
				}
				
				
			}
			numberOfPlays++;
			if(numberOfPlays > 5){
				ui.sendMessage("New deck\nReshuffling");
				while(deck.size()!=0){
					deck.pop();
				}
				CreateDeck(deck);
				ShuffleDeck(data, deck);
				numberOfPlays = 0;
			}
			
			
			dealAgain(user, dealer, deck);
		}
		
		ui.sendMessage("GAME OVER\nYou ran out of money, go home");
		
		
	}
	
/*	private static void askIfSplitting(User_Interface ui) {
		
		Player split = new Player()
		
		ui.setPrompt("Your two cards match! You may split, yould you like to?");
		String[] commands = {
				"Yes",
				"No",
				};
		int c = ui.getCommand(commands);
		switch (c) {
		case -1:
			System.exit(0);
		case 0:
			
			break;
		case 1:
			break;
		}
		
	}
*/
	private static int askForInsurance(User_Interface ui, Player user, Player dealer, int bet) {
		ui.setPrompt("The dealer's face up card is " + dealer.data[0] + ". Would you like to buy insurance?\n"
				+ "Insurance can only be up to half your bet, your current bet is $" + bet);
		int sideBet = 0;
		
		String[] commands = {
				"Yes",
				"No",
				};	
		
		int c = ui.getCommand(commands);
		switch (c) {
		case -1:
			System.exit(0);
		case 0:

			boolean valid = false;
			
				while(valid == false){
						try{
							String getBet = ui.getInfo("How much money on your insurance (min is $2 and max is half your bet)?\nYour current bet is"
									+ " $" + bet);
							if(getBet == null){
								System.exit(0);
							}
							if(getBet.equals("0")) {
								ui.sendMessage("You cannot bet $0");
								valid = false;
								continue;
							}
							sideBet = Integer.parseInt(getBet);
							if(user.money < sideBet){
								ui.sendMessage("You do not have enough money to place that bet, try again");
								continue;
							}
							if(sideBet < 2){
								ui.sendMessage("Sorry, you must bet at least $2");
								continue;
							}
							
							if(sideBet > (int)(bet/2)){
								ui.sendMessage("Sorry, you cannot bet more than half your original bet");
								continue;
							}
							
						} catch (NumberFormatException e){
							ui.sendMessage("You must enter a valid numerical value");
							valid = false;
							continue;
						}
						valid = true;
				}
			return sideBet;
		case 1:
			return 0;
		}
		return 0;
	}

	private static int requestBet(Player user, User_Interface ui) {
		int bet = 0;
		boolean valid = false;
		
		while(valid == false){
				try{
					String getBet = ui.getInfo("How much would you like to bet on this hand (min is $2)?\nYou currently have $" + user.money + 
							"\n\n		Cancel or hit the Red X to withdraw");
					if(getBet == null){
						ui.sendMessage("You have chosen to withdraw, you leave with $" + user.money);
						System.exit(0);
					}
					if(getBet.equals("0")) {
						ui.sendMessage("You cannot bet $0");
						valid = false;
						continue;
					}
					bet = Integer.parseInt(getBet);
					if(user.money < bet){
						ui.sendMessage("You do not have enough money to place that bet, try again");
						continue;
					}
					if(bet < 2){
						ui.sendMessage("Sorry, you must bet at least $2");
						continue;
					}
				} catch (NumberFormatException e){
					ui.sendMessage("You must enter a valid numerical value");
					valid = false;
					continue;
				}
				valid = true;
		}
		
		user.setMoney(user.getMoney()-bet);
		return bet;
	}

	private static void ShuffleDeck(Card[] data, deckOfCards<Card> deck) {

		while(!deck.isEmpty()){
			int location = (int) Math.floor(((Math.random() * 52)));
			if(data[location] == null){
				
				data[location] = (Card) deck.pop();
			} else {
				while(data[location] != null){
					location = (++location)%INITIAL_CAPACITY;
				}
				data[location] = (Card) deck.pop();
			}
			//System.out.println(location);
		}
		
		//Reinsert cards into stack
		for (int i = 0; i < INITIAL_CAPACITY; i++){
			deck.push(data[i]);
		}
		
	}
	
	private static void CreateDeck(deckOfCards<Card> deck) {

		String cardValue;
				
		String[] Suits = {"Diamonds","Hearts", "Clovers","Pikes"};
		
		
		for (int i = 0; i < SUITS; i++){
			for (int x = 1; x <= NUMBER_OR_SYMBOL; x++ ){
				
				if (x == 11) {cardValue = "Jack";}
				else if (x == 12){cardValue = "King";}
				else if (x == 13) {cardValue = "Queen";}
				else if (x == 1){cardValue = "Ace";}
				else {
					cardValue = Integer.toString(x);
				}

				deck.push(new Card(Suits[i], cardValue));
				
			}
			
		}

		
	}
	
	public static void processCommands(User_Interface ui, deckOfCards<Card> deck) {
		String[] commands = {
				"Play",
				"Exit",
				"Black Jack Rules"
				};
		
		while (true) {
			ui.setPrompt("Lets play Black Jack!");
			int c = ui.getCommand(commands);
			switch (c) {
			case -1:
				ui.sendMessage("GAME ENDED...CLOSING...SORE LOSER.");
				return;
			case 0:
				ui.sendMessage("The cards have been shuffled. You have $5000 in chips\nGood luck!");
				
				//Test Case Naturals
				//Player dealer = new Player(new Card("Hearts", "Ace"),new Card("Hearts","Jack") ,0);
				//Player user = new Player(((Card)deck.pop()), (Card)deck.pop(), STARTING_BET);
				
				Player user = new Player(((Card)deck.pop()), (Card)deck.pop(),STARTING_BET);
				Player dealer = new Player(((Card)deck.pop()), (Card)deck.pop(),0);
				play(ui, deck, user, dealer);
				
				break;
			case 1:
				ui.sendMessage("GAME ENDED...CLOSING...SORE LOSER.");
				System.exit(0);
				return;
			case 2:
				getRules(ui);
				processCommands(ui, deck);
				break;

			}
		}
	}

	public static void main(String[] args) {

		User_Interface ui = new GUI();
		deckOfCards<Card> deck = new deckOfCards<Card>();
		Card [] data = new Card[INITIAL_CAPACITY];
		CreateDeck(deck);
		ShuffleDeck(data,deck);
		processCommands(ui, deck);
		
	}
	
	private static void getRules(User_Interface ui) {
		ui.sendMessage("Rules at Andres’ Casino\n\nIf any player has a natural "
				+ "(21 points to start with) and the dealer does not, the dealer "
				+ "immediately pays that player one and a half times the amount "
				+ "of his bet\n\nAn 'X' indicates the dealer's hole(the hidden card)\n\nYou may double down (double your bet) if your cards "
				+ "add up to 9, 10, 11. You only get one more card when you double down\n\n"
				+ "You may buy insurance if the dealer’s face-up card is an Ace, "
				+ "insurance cannot be more than half your original bet\n\nDealer will"
				+ " stand if the total of cards is 17 or more counting Ace as 11\n\n"
				+ "Unfortunately, we do not allow splitting in this casino\n\nThis is"
				+ " single-deck one-player black jack, cards get reshuffled every 5 rounds"
				+ " (you will be notified) \n\nYou bust, you lose immediately and the"
				+ " dealer doesn’t play\n\nYou start with $5000\n\nYou can withdraw "
				+ "at the start of any round and leave with your money, but you cant quit in the middle of a round\n\nYou reach $0, "
				+ "the game ends and you go home\n\nThis game only accepts valid whole numbers"
				+ " as inputs\n\nA valid ID is required to enter\n\n" );
	}

}
