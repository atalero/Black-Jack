package black_jack;

public class Player {
	private static final int MAX_CARDS = 6;
	
	Card [] data = new Card[MAX_CARDS];;
	int money;
	
	public Player(){
		data[0] = null;
		data[1] = null;
		money = 0;
	}
	
	public Player(Card card1, Card card2, int startingBet){
		this.data[0] = card1;
		this.data[1] = card2;
		this.data[2] =  new Card("","");
		this.data[3] =  new Card("","");
		this.data[4] =  new Card("","");
		this.data[5] =  new Card("","");
		this.money = startingBet;
	}
	
	public void setMoney(int money){
		this.money = money;
	}
	
	public int getMoney(){
		return(this.money);
	}
	
	public void dealAgain(Card card1, Card card2){
		this.data[0] =  card1;
		this.data[1] =  card2;
		this.data[2] =  new Card("","");
		this.data[3] =  new Card("","");
		this.data[4] =  new Card("","");
		this.data[5] =  new Card("","");
	}
	
	public void AddCardToHand(Card card){
		for(int i = 2; i < MAX_CARDS; i++){
			if (data[i].cardNumber == ""){
				data[i] = card;
				return;
			}
		}
	}
	
	public int sumAceIs1() {
		int sum = 0;
		for (int i = 0; i < MAX_CARDS; i++){
			sum += this.data[i].valueAceIs1();
		}
		return sum;

	}
	
	public int sumAceIs11() {
		int sum = 0;
		for (int i = 0; i < MAX_CARDS; i++){
			sum += this.data[i].valueAceIs11();
		}
		return sum;

	}
	
}
