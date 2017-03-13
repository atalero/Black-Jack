package black_jack;

import java.util.Arrays;

public class Card {

	String cardSymbol, cardNumber;
	
	public Card(){
		cardSymbol = null;
		cardNumber = null;
	}
	
	public Card(String cardSymbol, String cardNumber){
		this.cardNumber = cardNumber;
		this.cardSymbol = cardSymbol;
	}
	
	public String getNumber(){
		return cardNumber;
	}
	
	public String getSuit(){
		return cardSymbol;
	}
	
    public String toString() {
    	if (cardNumber != ""){
        	return (cardNumber + "\t of \t" + cardSymbol);
    	} else {
    		return("");
    	}
    } 
    
    public int valueAceIs11(){
    	
    	if (cardNumber == "Jack" || cardNumber == "King" || cardNumber == "Queen"){
    		return(10);
    	} else if (cardNumber == "Ace"){
    		return(11);	
    	} else if (cardNumber == ""){
    		return (0);
    	}
    	else {
    		return(Integer.parseInt(cardNumber));
    	}
    	
    }
    
    public int valueAceIs1(){
    	
    	if (cardNumber == "Jack" || cardNumber == "King" || cardNumber == "Queen"){
    		return(10);
    	} else if (cardNumber == "Ace"){
    		return(1);	
    	} else if (cardNumber == ""){
    		return (0);
    	}
    	else {
    		return(Integer.parseInt(cardNumber));
    	}
    	
    }
	
}
