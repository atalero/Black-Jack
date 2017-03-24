package black_jack;

public class Main {
	
	private static final int SUITS = 4;
	private static final int NUMBER_OR_SYMBOL = 13;
	private static final int INITIAL_CAPACITY = 52;
	private static final int STARTING_BET = 5000;
	
	private static void decideWinner(int userScore, int dealerScore, User_Interface ui, 
			Player user, Player dealer, int bet) {
		///Decide who wins the round according to rules of precedence in the game
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
		//This method provides a commonly used prompt that shows changes in the cards when the
		//user is playing
		ui.setPrompt("Dealer's cards:\n" + dealer.data[0] + "\nX\n\nYour cards:\n" + 
				user.data[0] + "\n" + user.data[1] + "\n" + user.data[2] + "\n" + user.data[3] + 
				"\n" + user.data[4] + "\n" + user.data[5]);
	}
	
	private static void userPlays(Player user, User_Interface ui, deckOfCards<Card> deck, Player dealer) {
		//Method continously allowes user to hit or stall until he/she busts
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
	
	private static void dealerPlays(Player dealer, deckOfCards<Card> deck, Player 
			user, User_Interface ui) {
		//This simple algorithm is the one by which dealer's at most casinos play,
		//the dealer never makes 
		//decisions
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
		///Display the cards when the dealer is playing
		ui.sendMessage("Dealer Now Playing\n\nDealer's cards:\n" + dealer.data[0] + 
				"\n" + dealer.data[1] + 
				"\n" + dealer.data[2] +
				"\n" + dealer.data[3] + "\n" + dealer.data[4] + "\n" + dealer.data[5]+
				"\n\nYour cards:\n" + 
				user.data[0] + "\n" + user.data[1] + "\n" + user.data[2] +
				"\n" + user.data[3] + "\n" + 
				user.data[4] + "\n" + user.data[5]);
	}
	
	private static int calculateSum(Player player, User_Interface ui, boolean dealer) {
		//Calculate the sum of either players cards, count Ace as one if the total exceeds 21
		//when counting Ace as 11
			if(player.sumAceIs11() >= 21){
				return(player.sumAceIs1());
			}
			return player.sumAceIs11();
	}
	
	private static void displayCards(Player user, Player dealer, User_Interface ui) {
		//Display cards at the start of the round, hiding dealer's second card
		ui.sendMessage("Dealer's cards:\n" + dealer.data[0] + "\nX\n\nYour cards:\n" + 
				user.data[0] + "\n" + user.data[1]);
	}
	
	private static void dealAgain(Player user, Player dealer, deckOfCards<Card> deck) {
		//This method is called at the start of every round to give each player a new pair of cards
			user.dealAgain(deck.pop(), deck.pop());
			dealer.dealAgain(deck.pop(), deck.pop());
	}
		
	private static boolean compareNaturals(boolean checkNaturals, boolean checkNaturals2, Player user, 
			Player dealer, int bet, User_Interface ui, deckOfCards<Card> deck, int insurance) {
		//Method checks all possibilities for the dealer or the player having a natural		
		//Dealer and Player have naturals
		if (checkNaturals == true && checkNaturals2 == true){
			user.setMoney(user.getMoney()+ bet);
			ui.sendMessage("Looks like you both have naturals!, Let's deal the"
					+ " cards again\nYou get your bet back"
					+ "\nYou now have " + user.getMoney());
			
			if(insurance != 0){
				user.setMoney(user.getMoney() + (insurance*2));
				ui.sendMessage("Fortunately, you bought insurance, you will get "
						+ "paid back twice "
						+ "your insurance.\n You now have $"
						+ user.money);
			}
			
			displayCardsNoHole(user, dealer, ui);
			dealAgain(user, dealer, deck);
			return false;
		}
		
		//Only the player has a natural
		if (checkNaturals == true && checkNaturals2 == false){
			user.setMoney(user.getMoney()+ (int)(2.5*bet));	
			ui.sendMessage("Congrats, dealer pays 1.5 times you bet\n You now have $" +
			user.getMoney());
			displayCardsNoHole(user, dealer, ui);
			dealAgain(user, dealer, deck);
			return false;
		}
		
		//Only the dealer has a natural
		if (checkNaturals == false && checkNaturals2 == true){	
			ui.sendMessage("Oh no, the dealer has a natural and you don't\n You now have $" 
		+ user.getMoney());
			
			if(insurance != 0){
				user.setMoney(user.getMoney() + (insurance*2));
				ui.sendMessage("Fortunately, you bought insurance, you will get paid back "
						+ "twice your insurance.\n"
						+ " You now have $"
						+ user.money);
			}
			
			displayCardsNoHole(user, dealer, ui);
			dealAgain(user, dealer, deck);
			return false;
		}
		
		
		return true;
		
	}
	
	private static void displayCardsNoHole(Player user, Player dealer, User_Interface ui) {
		//Display the cards without hiding the dealer's second card
		ui.sendMessage("Dealer's cards:\n" + dealer.data[0] + "\n" + dealer.data[1] + "\n" 
				+ dealer.data[2] +
				"\n" + dealer.data[3] + "\n" + dealer.data[4] + "\n" + dealer.data[5]+
				"\n\nYour cards:\n" + 
				user.data[0] + "\n" + user.data[1] + "\n" + user.data[2] + "\n" + user.data[3] 
				+ "\n" + 
				user.data[4] + "\n" + user.data[5]);
	}

	private static boolean checkNaturals(Player player) {
		//Method checks if any player has 21 at the start of the round (naturals)
		if ((player.data[0].valueAceIs11() + player.data[1].valueAceIs11()) == 21)
			return true;
		
		return false;
	}
	
	public static void play(User_Interface ui, deckOfCards<Card> deck, Player user, Player dealer) {
		int numberOfPlays = 0;
		int bet;
		boolean doubled = false;
		//The game does not end unless the user is out of money or he/she chooses to withdraw
		while(user.money > 0){
				bet = requestBet(user,ui);
				ui.sendMessage("Now dealing cards");
				displayCards(user, dealer, ui);
				int insurance = 0;
				
				//If the dealer's face-up cars is an Ace, dealer must offer insurance
				if(dealer.data[0].valueAceIs1() == 1){
					insurance = askForInsurance(ui,user,dealer,bet);
				}
				
				//Dealer checks for black jack every time face up card value is J, Q, K, 10, or Ace
				if(dealer.data[0].valueAceIs1() == 10 || dealer.data[0].valueAceIs1() == 1){
					ui.sendMessage("The dealer's face up card is " + dealer.data[0] + ". Dealer will "
							+ "now check"
							+ " for Black Jack");
				}
				
				if(compareNaturals(checkNaturals(user),checkNaturals(dealer), user, dealer, bet, ui,
						deck, insurance)){
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
						ui.setPrompt("The sum of you cards is " + user.sumAceIs1() +"!\nYou "
								+ "can double down. "
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
						if(user.money-(bet*2)>=0){
							doubled = true;
							bet = bet*2;
							user.setMoney(user.getMoney()- bet);
							ui.sendMessage("You doubled down, you get will one more card\nYour bet"
									+ " is now " + bet);
							user.data[2] = deck.pop();
						} else if (user.money-(bet*2)<0){
							ui.sendMessage("Looks liks you don't have enough money to double down,"
									+ " sorry");
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
					
					decideWinner(calculateSum(user, ui, false),calculateSum(dealer, ui, true)
							,ui, user, dealer, bet);
				}								
			}
			//Reshuffle the cards every three rounds and reset the count for numberOfPlays
			numberOfPlays++;
			if(numberOfPlays > 3){
				ui.sendMessage("New deck\nReshuffling");
				while(!deck.isEmpty()){
					deck.pop();
				}
				deck = CreateDeck(deck);
				deck = ShuffleDeck(deck);
				numberOfPlays = 0;
			}
			
			dealAgain(user, dealer, deck);
		}
		
		ui.sendMessage("GAME OVER\nYou ran out of money, go home");
		
		
	}

//Method for splitting, under construction
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
		//In the case that an insurance bet it possible, this method is called
		//The user must enter a valid insurance bet, otherwise the game will send a message to the user
		//reporting an error
		ui.setPrompt("The dealer's face up card is " + dealer.data[0] + ". Would you like to"
				+ " buy insurance?\n"
				+ "Insurance can only be up to half your bet, your current bet is $" + bet);
		int sideBet = 0;
		
		String[] commands = {
				"Yes",
				"No",
				};	
		
		int c = ui.getCommand(commands);
		switch (c) {
		case -1:
			return 0;
		case 0:

			boolean valid = false;
			
				while(valid == false){
						try{
							String getBet = ui.getInfo("How much money on your insurance "
									+ "(min is $2 "
									+ "and max is half your bet)?\nYour current bet is"
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
								ui.sendMessage("You do not have enough money to place that bet,"
										+ " try again");
								continue;
							}
							if(sideBet < 2){
								ui.sendMessage("Sorry, you must bet at least $2");
								continue;
							}
							
							if(sideBet > (int)(bet/2)){
								ui.sendMessage("Sorry, you cannot bet more than half your"
										+ " original bet");
								continue;
							}
							if((user.getMoney()-sideBet)<0){
								ui.sendMessage("OOPS, looks like you don't have enough money to place"
										+ " that insurance bet, try again");
								continue;
							}
							if(user.getMoney() == 0){
								ui.sendMessage("You have $0, you cannot place an insurance bet");
								return 0;
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
		//Method takes the player's bet at the start of each round and checks that the answer is valid
		//Must have enough money to place bet, and the input must be logically valid
		int bet = 0;
		boolean valid = false;
		
		while(valid == false){
				try{
					String getBet = ui.getInfo("How much would you like to bet on this"
							+ " hand (min is $2)?\nYou"
							+ " currently have $" + user.money + 
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

	private static deckOfCards<Card> ShuffleDeck(deckOfCards<Card> deck) {
		//Array of 52 cards needed to play one-deck blackjack
		Card[] data = new Card[INITIAL_CAPACITY];
		
		//The array of cards gets randomly filled with cards, if a card hashes to an occupied location,
		//it probes linearly to the next empty slot
		while(!deck.isEmpty()){
			int location = (int) Math.floor(((Math.random() * INITIAL_CAPACITY)));
			if(data[location] == null){
				data[location] = (Card) deck.pop();
			} else {
				while(data[location] != null){
					location = (++location)%INITIAL_CAPACITY;
				}
				data[location] = (Card) deck.pop();
			}
			
			//TEST CASE: check that random values are being properly produced
			//System.out.println(location);
		}
		
		//Reinsert cards into stack
		for (int i = 0; i < INITIAL_CAPACITY; i++){
			deck.push(data[i]);
		}
		
		return deck;
		
	}
	
	private static deckOfCards<Card> CreateDeck(deckOfCards<Card> deck) {
	//Method that creates a new deck of cards
		//cardValue variable defines the value the card has when being added for the purposes of 
		//blackjack
		String cardValue;
				
		String[] Suits = {"Diamonds","Hearts", "Clovers","Pikes"};
		
		//Double for loop creates all possible cards: 4 suits with 13 cards each (Ace to King)
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
		return deck;

		
	}
	
	public static void processCommands(User_Interface ui, deckOfCards<Card> deck) {	
	//Method that displays the main menu and receives commands to make calls to other methods	
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
				//Quit the game
				ui.sendMessage("GAME ENDED...CLOSING...SORE LOSER.");
				return;
			case 0:
				//"Play" option
				ui.sendMessage("The cards have been shuffled. You have $5000 in chips\nGood luck!");
				
				//Test Case for when the round starts with a natural
				//Player dealer = new Player(new Card("Hearts", "Ace"),new Card("Hearts","Jack") ,0);
				//Player user = new Player(((Card)deck.pop()), (Card)deck.pop(), STARTING_BET);
				
				//Create the two players, user and dealer (program can be developed to add new players)
				Player user = new Player(((Card)deck.pop()), (Card)deck.pop(),STARTING_BET);
				Player dealer = new Player(((Card)deck.pop()), (Card)deck.pop(),0);
				//Sart the game
				play(ui, deck, user, dealer);
				
				break;
			case 1:
				ui.sendMessage("GAME ENDED...CLOSING...SORE LOSER.");
				System.exit(0);
				return;
			case 2:
				//Show the rules of the game in a new window
				getRules(ui);
				processCommands(ui, deck);
				break;

			}
		}
	}

	public static void main(String[] args) {
//Create GUI
		User_Interface ui = new GUI();
//Create Deck Object
		deckOfCards<Card> deck = new deckOfCards<Card>();
//Create cards and put them in the deck
		CreateDeck(deck);
//Shuffle the deck using a has function that randomized the place of the card in the deck
		ShuffleDeck(deck);
//Take the user to the main menu
		processCommands(ui, deck);
		
	}
//List instructions for when user clicks on "Instructions" Option from the main menu	
	private static void getRules(User_Interface ui) {
		ui.sendMessage("Rules at Andres’ Casino\n\nIf any player has a natural "
				+ "(21 points to start with) and the dealer does not, the dealer "
				+ "immediately pays that player one and a half times the amount "
				+ "of his bet\n\nAn 'X' indicates the dealer's hole(the hidden card)\n\nYou may double"
				+ " down (double your bet) if your cards "
				+ "add up to 9, 10, 11. You only get one more card when you double down\n\n"
				+ "You may buy insurance if the dealer’s face-up card is an Ace, "
				+ "insurance cannot be more than half your original bet\n\nDealer will"
				+ " stand if the total of cards is 17 or more counting Ace as 11\n\n"
				+ "Unfortunately, we do not allow splitting in this casino\n\nThis is"
				+ " single-deck one-player black jack, cards get reshuffled every 4 rounds"
				+ " (you will be notified) \n\nYou bust, you lose immediately and the"
				+ " dealer doesn’t play\n\nYou start with $5000\n\nYou can withdraw "
				+ "at the start of any round and leave with your money, but you cant quit in the middle"
				+ " of a round\n\nYou reach $0, "
				+ "the game ends and you go home\n\nThis game only accepts valid whole numbers"
				+ " as inputs\n\nA valid ID is required to enter\n\n" );
	}

}
