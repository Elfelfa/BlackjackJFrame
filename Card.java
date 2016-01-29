import javax.imageio.*;
import java.io.*;
import java.awt.image.*;
import java.awt.image.BufferedImage;

public class Card
{
    public String cardType;
    public String cardLoc;
    public int cardNum;
    public int cardValue;

    /**
    *cardType consists of the card's suit.
    *1 - Spades
    *2 - Clubs
    *3 - Hearts
    *4 - Diamonds
    *
    *--------------------------------------------------------------
    *
    *cardNum consists of the card's value in the suit. These are in order.
    *Spades - 1 through 13 - A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
    *Clubs -  14 through 26 - A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
    *Hearts - 27 through 39 - A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
    *Diamonds - 40 through 52 - A, 2, 3, 4, 5, 6, 7, 8, 9, 10, J, Q, K
    **/
    
    public Card(int cardType, int cardValue)
    {
        this.cardNum = cardValue;
        this.cardType = setType(cardType);
        
        if (cardValue <= 10)
        {
            this.cardValue = cardValue;
        }
        else
        {
            this.cardValue = 10;
        }
        
        setImageLoc();
    }
    
    public String setType (int cardType)
    {
        String out = "";
        
        switch (cardType)
        {
            case 1:{
                out = "Spades";
            }
            break;
            
            case 2:{
                out = "Clubs";
            }
            break;
            
            case 3:{
                out = "Hearts";
            }
            break;
            
            case 4:{
                out = "Diamonds";
            }
            break;
        }
        
        return out;
    }
    
    public void setImageLoc()
    {
        cardLoc =  "CardImages/" + cardType + "_" + cardNum + ".png"; 
    }
    
    public String getImageLoc()
    {
        return cardLoc;
    }
    
    public int getValue()
    {
        return cardValue;
    }
}