import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import javax.imageio.*;
import java.io.*;
import java.util.Random;

public class BlackjackJFrame extends JPanel
{   
    static int lockedBetAmount;
    static int betAmount = 1;
    static int dealerPoints = 100;
    static int playerPoints = 100;
    static int playerWin = 0;
    static int endCheck = 0;
    static int dealerPlay = 0;
    static int gameEnd = 0;
    static int dealerFinalValue = 0;
    static int playerFinalValue = 0;
    static int paintedCards = 0;
    static boolean firstCheck = true, needCardsPainted = true;
    
    static ArrayList<Card> deck = new ArrayList<Card>();
    static ArrayList<Card> dealerHand = new ArrayList<Card>();
    static ArrayList<Card> playerHand = new ArrayList<Card>();
    
    public static void main (String[] args)
    {
        BlackjackJFrame m = new BlackjackJFrame();
        
        m.init();
    }
    
    public void init()
    {
        BackgroundPanel backgroundPanel = new BackgroundPanel(dealerPoints, playerPoints);
        
        JFrame frame = new JFrame("Blackjack");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.add(backgroundPanel);
        
        
        JPanel gameButtonPanel = new JPanel();
        JButton hit = new JButton();
        JButton stay = new JButton();
        hit.setText("Hit");
        stay.setText("Stay");
        
        gameButtonPanel.add(hit);
        gameButtonPanel.add(stay);
        
        JPanel continuePanel = new JPanel();
        JButton contin = new JButton();
        JButton quit = new JButton();
        contin.setText("Continue");
        quit.setText("Quit");
        
        continuePanel.add(contin);
        continuePanel.add(quit);
        
        JPanel quitButtonPanel = new JPanel();
        quitButtonPanel.add(new JButton(new AbstractAction("Quit"){
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }));
        
        JPanel bettingButtonPanel = new JPanel();
        bettingButtonPanel.add(new JButton(new AbstractAction("Raise Bet"){
            public void actionPerformed(ActionEvent e) {
                if (betAmount < 10) {
                    betAmount++;
                }
                backgroundPanel.setBetAmount(betAmount);
                backgroundPanel.repaint();
                setVisible(true);
            }
        }));
        
        bettingButtonPanel.add(new JButton(new AbstractAction("Lock Bet"){
            public void actionPerformed(ActionEvent e) {
                lockedBetAmount = betAmount;
                firstCheck = true;
                
                for (int i = 0; i < 4; i++)
                {
                    dealCards();
                }
                    
                backgroundPanel.addDealerCards(dealerHand);
                backgroundPanel.addPlayerCards(playerHand);
                
                backgroundPanel.lockBet(lockedBetAmount);
                backgroundPanel.draw();
                backgroundPanel.setCardNum();
                backgroundPanel.repaint();
                frame.setVisible(true);
                checkWin();
                
                if (gameEnd == 1)
                {
                    backgroundPanel.setPoints(dealerPoints, playerPoints);
                    backgroundPanel.endGame();
                    backgroundPanel.repaint();
                    frame.remove(bettingButtonPanel);
                    frame.add(quitButtonPanel, BorderLayout.SOUTH);
                    frame.revalidate();
                    frame.repaint();
                    setVisible(true);
                }
                else if (gameEnd == -1)
                {
                    backgroundPanel.setPoints(dealerPoints, playerPoints);
                    backgroundPanel.endGame();
                    backgroundPanel.repaint();
                    frame.remove(bettingButtonPanel);
                    frame.add(quitButtonPanel, BorderLayout.SOUTH);
                    frame.revalidate();
                    frame.repaint();
                    setVisible(true);
                }
                else
                {
                    if (playerWin == 1 || playerWin == -1)
                    {
                        backgroundPanel.endRound(playerWin);
                        backgroundPanel.showDeal();
                        frame.remove(bettingButtonPanel);
                        frame.add(continuePanel, BorderLayout.SOUTH);
                        frame.revalidate();
                        frame.repaint();
                        backgroundPanel.repaint();
                        resetHands();
                        setVisible(true);
                    }
                    else
                    {
                        frame.remove(bettingButtonPanel);
                        frame.add(gameButtonPanel, BorderLayout.SOUTH);
                        frame.revalidate();
                        frame.repaint();
                        setVisible(true);
                    }
                }
                
            }
        }));
        
        bettingButtonPanel.add(new JButton(new AbstractAction("Lower Bet"){
            public void actionPerformed(ActionEvent e) {
                if (betAmount > 1) {
                    betAmount--;
                }
                backgroundPanel.setBetAmount(betAmount);
                backgroundPanel.repaint();
                setVisible(true);
            }
        }));
        
        JPanel mainButtonPanel = new JPanel();
        mainButtonPanel.add(new JButton(new AbstractAction("Start Game") {
            public void actionPerformed(ActionEvent e) {
                genDeck();
                frame.remove(mainButtonPanel);
                frame.add(bettingButtonPanel, BorderLayout.SOUTH);
                frame.revalidate();
                frame.repaint();
                backgroundPanel.setBetRepaint();
                backgroundPanel.repaint();
                frame.setVisible(true);
            }
        }));
        
        mainButtonPanel.add(new JButton(new AbstractAction("Quit") {
            public void actionPerformed(ActionEvent e) {
                frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }));
        
        contin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                genDeck();
                backgroundPanel.resetti();
                frame.remove(continuePanel);
                frame.add(bettingButtonPanel, BorderLayout.SOUTH);
                frame.revalidate();
                frame.repaint();
                backgroundPanel.repaint();
                setVisible(true);
            }
        });
        
        quit.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e) {
               frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        });
            
        hit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dealCards();
                backgroundPanel.addPlayerCards(playerHand);
                backgroundPanel.repaint();
                setVisible(true);
                checkWin();
                
                if (gameEnd == 1)
                {
                    backgroundPanel.setPoints(dealerPoints, playerPoints);
                    backgroundPanel.endGame();
                    backgroundPanel.repaint();
                    frame.remove(gameButtonPanel);
                    frame.add(quitButtonPanel, BorderLayout.SOUTH);
                    frame.revalidate();
                    frame.repaint();
                    setVisible(true);
                }
                else if (gameEnd == -1)
                {
                    backgroundPanel.setPoints(dealerPoints, playerPoints);
                    backgroundPanel.endGame();
                    backgroundPanel.repaint();
                    frame.remove(gameButtonPanel);
                    frame.add(quitButtonPanel, BorderLayout.SOUTH);
                    frame.revalidate();
                    frame.repaint();
                    setVisible(true);
                }
                else
                {
                    if (playerWin == 1 || playerWin == -1)
                    {
                        backgroundPanel.endRound(playerWin);
                        backgroundPanel.showDeal();
                        frame.remove(gameButtonPanel);
                        frame.add(continuePanel, BorderLayout.SOUTH);
                        frame.revalidate();
                        frame.repaint();
                        backgroundPanel.repaint();
                        setVisible(true);
                        resetHands();
                    }
                }
            }
        });
        
        stay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dealerPlay = 1;
                backgroundPanel.stopDraw();
                backgroundPanel.prepareShow();
                backgroundPanel.showDeal();
                paintedCards = 2;
                if (dealerFinalValue < 16)
                {
                    addCardDealer();
                }
                if (dealerHand.size() > 2)
                {
                    backgroundPanel.startDrawDealer();
                    backgroundPanel.addDealerCards(dealerHand);
                }
                
                checkWin();
                
                if (playerWin == 0 && gameEnd == 0)
                {
                    endCheck();
                }
                
                if (gameEnd == 1)
                {
                    backgroundPanel.setPoints(dealerPoints, playerPoints);
                    backgroundPanel.endGame();
                    if (dealerHand.size() > 2)
                    {
                        backgroundPanel.setImagePaint(dealerHand.size());
                        backgroundPanel.repaint();
                    }
                    frame.remove(gameButtonPanel);
                    frame.add(quitButtonPanel, BorderLayout.SOUTH);
                    frame.revalidate();
                    frame.repaint();
                    setVisible(true);
                }
                else if (gameEnd == -1)
                {
                    backgroundPanel.setPoints(dealerPoints, playerPoints);
                    backgroundPanel.endGame();
                    if (dealerHand.size() > 2)
                    {
                        backgroundPanel.setImagePaint(dealerHand.size());
                        backgroundPanel.repaint();
                    }
                    frame.remove(gameButtonPanel);
                    frame.add(quitButtonPanel, BorderLayout.SOUTH);
                    frame.revalidate();
                    frame.repaint();
                    setVisible(true);
                }
                else
                {
                    if (playerWin == 1 || playerWin == -1)
                    {
                        backgroundPanel.endRound(playerWin);
                        frame.remove(gameButtonPanel);
                        frame.add(continuePanel, BorderLayout.SOUTH);
                        frame.revalidate();
                        frame.repaint();
                        if (dealerHand.size() > 2)
                        {
                            backgroundPanel.setImagePaint(dealerHand.size());
                            backgroundPanel.repaint();
                        }
                        setVisible(true);
                        resetHands();
                    }
                }
            }
        });
        
        frame.add(mainButtonPanel, BorderLayout.SOUTH);
        
        frame.setVisible(true);
    }
    
    public void addCardDealer()
    {
        int k = 1;
        int check = 0;
        
        while(dealerFinalValue < 16)
        {
            if (dealerFinalValue < 16)
            {
                dealerHand.add(deck.get(0));
                deck.remove(0);
                needCardsPainted = true;
            }
            
            checkDealer();
            k++;
        }
        
   }
    
    public void checkDealer()
    {
        int total = 0;
        int aceCount = 0;
        
        for (int i = 0; i < dealerHand.size(); i++)
        {
            if (dealerHand.get(i).getValue() == 1)
            {
                if ((total + 11) > 21)
                {
                    total += 1;
                }
                else
                {
                    total += 11;
                    aceCount++;
                }
            }
            else
            {
                total = total + dealerHand.get(i).getValue();
            }
            
            if ((total > 21) && (aceCount > 0))
            {
                do
                {
                    total = total - 10;
                    aceCount--;
                }while(aceCount > 0);
            }
        } 
            
        if ((total >= 16) && (!firstCheck))
        {
            dealerFinalValue = total;
        }
        else if (firstCheck)
        {
            dealerFinalValue = total;
            firstCheck = false;
        }
    }
    
    public void checkPlayer()
    {
        int total = 0;
        int aceCount = 0;
        
        for (int i = 0; i < playerHand.size(); i++)
        {
            if ((playerHand.get(i).getValue() == 1))
            {
                if ((total + 11) > 21)
                {
                    total += 1;
                }
                else
                {
                    total += 11;
                    aceCount++;
                }
            }
            else
            {
                total += playerHand.get(i).getValue();
            }
            
            if ((total > 21) && (aceCount > 0))
            {
                do
                {
                    total = total - 10;
                    aceCount--;
                }while(aceCount > 0);
            }
        }
        
        playerFinalValue = total;
    }
    
    public void endCheck()
    {
        if (dealerFinalValue > playerFinalValue)
        {
            playerWin = -1;
            playerPoints -= lockedBetAmount;
            dealerPoints += lockedBetAmount;
        }
        else
        {
            playerWin = 1;
            playerPoints += lockedBetAmount;
            dealerPoints -= lockedBetAmount;
        }
    }
    
    public void checkWin()
    {
        checkPlayer();
        checkDealer();
        
        if (playerFinalValue > 21)
        {
            playerWin = -1;
            playerPoints -= lockedBetAmount;
            dealerPoints += lockedBetAmount;
            
            if (playerPoints <= 0)
            {
                gameEnd = 1;
            }
        }
        else if (dealerFinalValue > 21)
        {
            playerWin = 1;
            playerPoints += lockedBetAmount;
            dealerPoints -= lockedBetAmount;
            
            if (playerPoints >= 200)
            {
                gameEnd = -1;
            }
        }
        else if (playerFinalValue == 21 && dealerFinalValue != 21)
        {
            playerWin = 1;
            playerPoints += lockedBetAmount;
            dealerPoints -= lockedBetAmount;
            
            if (playerPoints >= 200)
            {
                gameEnd = 1;
            }
        }
        else if (dealerFinalValue == 21)
        {
            playerWin = -1;
            playerPoints -= lockedBetAmount;
            dealerPoints += lockedBetAmount;
            
            if (playerPoints <= 0)
            {
                gameEnd = -1;
            }
        }
    }
    
    public void genDeck()
    {
        int k = 1;
        int v = 1;
        Random rnd = new Random();
        ArrayList<Card> temp = new ArrayList<Card>();
        
        if (!temp.isEmpty())
        {
            for (int i = 0; i < temp.size(); i++)
            {
                temp.remove(i);
            }
            
            for (int i = 0; i < deck.size(); i++)
            {
                deck.remove(i);
            }
        }
        
        for (int i = 1; i <= 52; i++)
        {
            if (v > 13)
            {
                v = 1;
                k++;
            }
            
            Card card = new Card(k, v);
            
            temp.add(card);
            
            v++;
        }
        
        while(!temp.isEmpty())
        {
            int loc = rnd.nextInt(temp.size());
            deck.add(temp.get(loc));
            temp.remove(loc);
        }
    }
    
    public void dealCards()
    {
        if (dealerHand.size() < 2)
        {
            dealerHand.add(deck.get(0));
            deck.remove(0);
        }
        else if (dealerPlay == 0)
        {
            playerHand.add(deck.get(0));
            deck.remove(0);
        }
    }
   
    public void resetHands()
    {
        for (int i = dealerHand.size() - 1; i >= 0; i--)
        {
            dealerHand.remove(0);
        }
        
        for (int i = playerHand.size() - 1; i >= 0; i--)
        {
            playerHand.remove(0);
        }
        
        playerWin = 0;
        endCheck = 0;
        lockedBetAmount = 0;
        dealerPlay = 0;
        playerFinalValue = 0;
        dealerFinalValue = 0;
    }
    
    public void checkGameEnd()
    {
        if (playerPoints <= 0)
        {
            gameEnd = 1;
        }
        
        if (dealerPoints <= 0)
        {
            gameEnd = -1;
        }
    }
}

class BackgroundPanel extends JPanel
{
    ArrayList<Card> dealerHand = new ArrayList<Card>();
    ArrayList<Card> playerHand = new ArrayList<Card>();
    Image background;
    static int dealerPoints, playerPoints, betAmount = 1, lockedBetAmount, cardNum = 0, cardCount = 0, dealCardCount = 0, playCardCount = 0, dealCardNum = 0;
    static boolean startDraw = false, paintBetAmount = false, paintLockedBet = false, paintPoints = false, endOfRound = false, playerWin = false, resettiBool = false, showDealerCards = false, drawDealer = false, resetDealer = false, endOfGame = false;
    static JLabel card1, card2, card3, card4, card5, card6, card7, card8, card9, card10, card11, card12, card13, cardDealer3, cardDealer4, cardDealer5, cardDealer6, cardDealer7, cardDealer8, cardDealer9, cardDealer10;
    
    
    public BackgroundPanel(int dealP, int playP)
    {
        background = Toolkit.getDefaultToolkit().createImage("background.jpg");
        dealerPoints = dealP;
        playerPoints = playP;
        
    }
    
    public void setBetRepaint()
    {
        paintPoints = true;
        paintBetAmount = true;
    }
    
    public void setBetAmount(int bet)
    {
        betAmount = bet;
    }
    
    public void lockBet(int lockedBetAmount)
    {
        this.lockedBetAmount = lockedBetAmount;
        paintBetAmount = false;
        paintLockedBet = true;
    }
    
    public void addDealerCards(ArrayList<Card> deal)
    {
        dealerHand.clear();
        for (int i = 0; i < deal.size(); i++)
        {
            dealerHand.add(deal.get(i));
        }
    }
    
    public void addPlayerCards(ArrayList<Card> play)
    {
        playerHand.clear();
        for(int i = 0; i < play.size(); i++)
        {
            playerHand.add(play.get(i));
        }
    }
    
    public void setPoints(int dealP, int playP)
    {
        dealerPoints = dealP;
        playerPoints = playP;
    }
    
    public void draw()
    {
        startDraw = true;
    }
    
    public void stopDraw()
    {
        startDraw = false;
    }
    
    public void setImagePaint(int size)
    {
        dealCardNum = size;
    }
    
    public void startDrawDealer()
    {
        drawDealer = true;
    }
    
    public void setCardNum()
    {
        cardNum = 0;
    }
    
    public void setDealCardNum()
    {
        dealCardNum = 2;
    }
    
    public void showDeal()
    {
        showDealerCards = true;
    }
    
    public void prepareShow()
    {
        card1.setVisible(false);
        card2.setVisible(false);
        card1 = null;
        card2 = null;
    }
    
    public void endRound(int bool)
    {
        endOfRound = true;
        paintLockedBet = false;
        
        if (bool == 1)
        {
            playerWin = true;
            dealerPoints -= betAmount;
            playerPoints += betAmount;
        }
        else
        {
            playerWin = false;
            playerPoints -= betAmount;
            dealerPoints += betAmount;
        }
    }
    
    public void endGame()
    {
        endOfGame = true;
        paintLockedBet = false;
    }
    
    public void resetti()
    {
        if (drawDealer)
        {
            drawDealer = false;
            resetDealer = true;
        }
        
        endOfRound = false;
        startDraw = false;
        paintBetAmount = true;
        resettiBool = true;
        showDealerCards = false;
        playCardCount = 0;
        dealCardCount = 0;
        cardCount = 0;
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        g.drawImage(background, 0, 0, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("TimesRoman", Font.PLAIN, 13)); 
        
        setLayout(null);
        if (paintPoints)
        {
            g.drawString("Dealer points: " + dealerPoints, 475, 20);
            g.drawString("Player points: " + playerPoints, 10, 520);
        }
        
        if (paintBetAmount)
        {
            g.drawString("Bet amount: " + betAmount, 475, 520);
        }
        
        if (paintLockedBet)
        {
            g.drawString("Your bet: " + lockedBetAmount, 475, 520);
        }
        
        if (endOfRound && playerWin && dealerPoints > 0)
        {
            g.drawString("You won the round!", 460, 520);
            playerWin = false;
            endOfRound = false;
        }
        else if (endOfRound && !playerWin && playerPoints > 0)
        {
            g.drawString("You lost the round.", 460, 520);
            endOfRound = false;
        }
        
        if (endOfGame && playerPoints <= 0)
        {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 26)); 
            g.drawString("Sorry, you lost the game.", 200, 300);
        }
        else if (endOfGame && dealerPoints <= 0)
        {
            g.setFont(new Font("TimesRoman", Font.PLAIN, 26)); 
            g.drawString("Congratulations, you won the game!", 50, 300);
        }
        
        if (startDraw)
        {
            switch(cardNum)
            {
                case 0: {
                    card1 = null;
                    card1 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage("CardImages/Cardback.png")));
                    card1.setSize(71, 96);
                    card1.setLocation(200, 70);
                    add(card1, 1, 0);
                    card1.setVisible(true);
                    setVisible(true);
                }
                case 1: {
                    card2 = null;
                    card2 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage("CardImages/Cardback.png")));
                    card2.setSize(71, 96);
                    card2.setLocation(213, 70);
                    add(card2, 2, 0);
                    card2.setVisible(true);
                    setVisible(true);
                }
                case 2: {
                    card3 = null;
                    card3 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(playerHand.get(cardCount).getImageLoc())));
                    card3.setSize(71, 96);
                    card3.setLocation(200, 400);
                    add(card3, 3, 0);
                    setVisible(true);
                    card3.setVisible(true);
                    cardCount++;
                }
                case 3: {
                    card4 = null;
                    card4 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(playerHand.get(cardCount).getImageLoc())));
                    card4.setSize(71, 96);
                    card4.setLocation(213, 400);
                    add(card4, 4, 0);
                    setVisible(true);
                    card4.setVisible(true);
                    cardCount++;
                    cardNum = 4;
                }
                break;
                case 4: {
                    card5 = null;
                    card5 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(playerHand.get(cardCount).getImageLoc())));
                    card5.setSize(71, 96);
                    card5.setLocation(226, 400);
                    add(card5, 5, 0);
                    setVisible(true);
                    card5.setVisible(true);
                    cardCount++;;
                    cardNum++;
                }
                break;
                case 5: {
                    card6 = null;
                    card6 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(playerHand.get(cardCount).getImageLoc())));
                    card6.setSize(71, 96);
                    card6.setLocation(239, 400);
                    add(card6, 6, 0);
                    setVisible(true);
                    card6.setVisible(true);
                    cardCount++;
                    cardNum++;
                }
                break;
                case 6: {
                    card7 = null;
                    card7 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(playerHand.get(cardCount).getImageLoc())));
                    card7.setSize(71, 96);
                    card7.setLocation(252, 400);
                    add(card7, 7, 0);
                    setVisible(true);
                    card7.setVisible(true);
                    cardCount++;
                    cardNum++;
                }
                break;
                case 7: {
                    card8 = null;
                    card8 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(playerHand.get(cardCount).getImageLoc())));
                    card8.setSize(71, 96);
                    card8.setLocation(265, 400);
                    add(card8, 8, 0);
                    setVisible(true);
                    card8.setVisible(true);
                    cardCount++;
                    cardNum++;
                }
                break;
                case 8: {
                    card9 = null;
                    card9 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(playerHand.get(cardCount).getImageLoc())));
                    card9.setSize(71, 96);
                    card9.setLocation(278, 400);
                    add(card9, 9 ,0);
                    setVisible(true);
                    card9.setVisible(true);
                    cardNum++;
                    cardCount++;
                }
                break;
                case 9: {
                    card10 = null;
                    card10 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(playerHand.get(cardCount).getImageLoc())));
                    card10.setSize(71, 96);
                    card10.setLocation(292, 400);
                    add(card10, 10, 0);
                    setVisible(true);
                    card10.setVisible(true);
                    cardCount++;
                    cardNum++;
                }
                break;
                case 10: {
                    card11 = null;
                    card11 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(playerHand.get(cardCount).getImageLoc())));
                    card11.setSize(71, 96);
                    card11.setLocation(305, 400);
                    add(card11, 11, 0);
                    setVisible(true);
                    card11.setVisible(true);
                    cardCount++;
                    cardNum++;
                }
                break;
                case 11: {
                    card12 = null;
                    card12 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(playerHand.get(cardCount).getImageLoc())));
                    card12.setSize(71, 96);
                    card12.setLocation(318, 400);
                    add(card12, 12, 0);
                    setVisible(true);
                    card12.setVisible(true);
                    cardCount++;
                    cardNum++;
                }
                break;
                case 12: {
                    card13 = null;
                    card13 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(playerHand.get(cardCount).getImageLoc())));
                    card13.setSize(71, 96);
                    card13.setLocation(331, 400);
                    add(card13, 13, 0);
                    setVisible(true);
                    card13.setVisible(true);
                    cardCount++;
                    cardNum++;
                }
                break;
            }
        }
        
        if (showDealerCards)
        {
            card1 = null;
            card2 = null;
            card1 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(0).getImageLoc())));
            card2 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(1).getImageLoc())));
            card1.setSize(71, 96);
            card2.setSize(71, 96);
            card1.setLocation(200, 70);
            card2.setLocation(213, 70);
            add(card1, 1, 0);
            add(card2, 2, 0);
            setVisible(true);
            card1.setVisible(true);
            card2.setVisible(true);
        }
        
        if (drawDealer)
        {
            switch(dealCardNum)
            {
                case 3: {
                    
                    cardDealer3 = null;
                    cardDealer3 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(2).getImageLoc())));
                    cardDealer3.setSize(71, 96);
                    cardDealer3.setLocation(226, 70);
                    add(cardDealer3, 3, 0);
                    setVisible(true);
                    cardDealer3.setVisible(true);
                }
                break;
                case 4: {
                    
                    cardDealer3 = null;
                    cardDealer3 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(2).getImageLoc())));
                    cardDealer3.setSize(71, 96);
                    cardDealer3.setLocation(226, 70);
                    add(cardDealer3, 3, 0);
                    setVisible(true);
                    cardDealer3.setVisible(true);
                    
                    
                    cardDealer4 = null;
                    cardDealer4 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(3).getImageLoc())));
                    cardDealer4.setSize(71, 96);
                    cardDealer4.setLocation(239, 70);
                    add(cardDealer4, 4, 0);
                    setVisible(true);
                    cardDealer4.setVisible(true);
                }
                break;
                case 5: {
                    
                    cardDealer3 = null;
                    cardDealer3 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(2).getImageLoc())));
                    cardDealer3.setSize(71, 96);
                    cardDealer3.setLocation(226, 70);
                    add(cardDealer3, 3, 0);
                    setVisible(true);
                    cardDealer3.setVisible(true);
                    
                    
                    cardDealer4 = null;
                    cardDealer4 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(3).getImageLoc())));
                    cardDealer4.setSize(71, 96);
                    cardDealer4.setLocation(239, 70);
                    add(cardDealer4, 4, 0);
                    setVisible(true);
                    cardDealer4.setVisible(true);
                    
                    cardDealer5 = null;
                    cardDealer5 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(4).getImageLoc())));
                    cardDealer5.setSize(71, 96);
                    cardDealer5.setLocation(253, 70);
                    add(cardDealer5, 5, 0);
                    setVisible(true);
                    cardDealer5.setVisible(true);
                }
                break;
                case 6: {
                    
                    cardDealer3 = null;
                    cardDealer3 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(2).getImageLoc())));
                    cardDealer3.setSize(71, 96);
                    cardDealer3.setLocation(226, 70);
                    add(cardDealer3, 3, 0);
                    setVisible(true);
                    cardDealer3.setVisible(true);
                    
                    
                    cardDealer4 = null;
                    cardDealer4 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(3).getImageLoc())));
                    cardDealer4.setSize(71, 96);
                    cardDealer4.setLocation(239, 70);
                    add(cardDealer4, 4, 0);
                    setVisible(true);
                    cardDealer4.setVisible(true);
                    
                    cardDealer5 = null;
                    cardDealer5 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(4).getImageLoc())));
                    cardDealer5.setSize(71, 96);
                    cardDealer5.setLocation(253, 70);
                    add(cardDealer5, 5, 0);
                    setVisible(true);
                    cardDealer5.setVisible(true);
                    
                    cardDealer6 = null;
                    cardDealer6 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(5).getImageLoc())));
                    cardDealer6.setSize(71, 96);
                    cardDealer6.setLocation(266, 70);
                    add(cardDealer6, 6, 0);
                    setVisible(true);
                    cardDealer6.setVisible(true);
                }
                break;
                case 7: {
                    
                    cardDealer3 = null;
                    cardDealer3 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(2).getImageLoc())));
                    cardDealer3.setSize(71, 96);
                    cardDealer3.setLocation(226, 70);
                    add(cardDealer3, 3, 0);
                    setVisible(true);
                    cardDealer3.setVisible(true);
                    
                    
                    cardDealer4 = null;
                    cardDealer4 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(3).getImageLoc())));
                    cardDealer4.setSize(71, 96);
                    cardDealer4.setLocation(239, 70);
                    add(cardDealer4, 4, 0);
                    setVisible(true);
                    cardDealer4.setVisible(true);
                    
                    cardDealer5 = null;
                    cardDealer5 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(4).getImageLoc())));
                    cardDealer5.setSize(71, 96);
                    cardDealer5.setLocation(253, 70);
                    add(cardDealer5, 5, 0);
                    setVisible(true);
                    cardDealer5.setVisible(true);
                    
                    cardDealer6 = null;
                    cardDealer6 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(5).getImageLoc())));
                    cardDealer6.setSize(71, 96);
                    cardDealer6.setLocation(266, 70);
                    add(cardDealer6, 6, 0);
                    setVisible(true);
                    cardDealer6.setVisible(true);
                    
                    cardDealer7 = null;
                    cardDealer7 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(6).getImageLoc())));
                    cardDealer7.setSize(71, 96);
                    cardDealer7.setLocation(279, 70);
                    add(cardDealer7, 7, 0);
                    setVisible(true);
                    cardDealer7.setVisible(true);
                }
                break;
                case 8: {
                    
                    cardDealer3 = null;
                    cardDealer3 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(2).getImageLoc())));
                    cardDealer3.setSize(71, 96);
                    cardDealer3.setLocation(226, 70);
                    add(cardDealer3, 3, 0);
                    setVisible(true);
                    cardDealer3.setVisible(true);
                    
                    
                    cardDealer4 = null;
                    cardDealer4 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(3).getImageLoc())));
                    cardDealer4.setSize(71, 96);
                    cardDealer4.setLocation(239, 70);
                    add(cardDealer4, 4, 0);
                    setVisible(true);
                    cardDealer4.setVisible(true);
                    
                    cardDealer5 = null;
                    cardDealer5 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(4).getImageLoc())));
                    cardDealer5.setSize(71, 96);
                    cardDealer5.setLocation(253, 70);
                    add(cardDealer5, 5, 0);
                    setVisible(true);
                    cardDealer5.setVisible(true);
                    
                    cardDealer6 = null;
                    cardDealer6 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(5).getImageLoc())));
                    cardDealer6.setSize(71, 96);
                    cardDealer6.setLocation(266, 70);
                    add(cardDealer6, 6, 0);
                    setVisible(true);
                    cardDealer6.setVisible(true);
                    
                    cardDealer7 = null;
                    cardDealer7 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(6).getImageLoc())));
                    cardDealer7.setSize(71, 96);
                    cardDealer7.setLocation(279, 70);
                    add(cardDealer7, 7, 0);
                    setVisible(true);
                    cardDealer7.setVisible(true);
                    
                    cardDealer8 = null;
                    cardDealer8 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(7).getImageLoc())));
                    cardDealer8.setSize(71, 96);
                    cardDealer8.setLocation(292, 70);
                    add(cardDealer8, 8, 0);
                    setVisible(true);
                    cardDealer8.setVisible(true);
                }
                break;
                case 9: {
                    
                    cardDealer3 = null;
                    cardDealer3 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(2).getImageLoc())));
                    cardDealer3.setSize(71, 96);
                    cardDealer3.setLocation(226, 70);
                    add(cardDealer3, 3, 0);
                    setVisible(true);
                    cardDealer3.setVisible(true);
                    
                    
                    cardDealer4 = null;
                    cardDealer4 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(3).getImageLoc())));
                    cardDealer4.setSize(71, 96);
                    cardDealer4.setLocation(239, 70);
                    add(cardDealer4, 4, 0);
                    setVisible(true);
                    cardDealer4.setVisible(true);
                    
                    cardDealer5 = null;
                    cardDealer5 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(4).getImageLoc())));
                    cardDealer5.setSize(71, 96);
                    cardDealer5.setLocation(253, 70);
                    add(cardDealer5, 5, 0);
                    setVisible(true);
                    cardDealer5.setVisible(true);
                    
                    cardDealer6 = null;
                    cardDealer6 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(5).getImageLoc())));
                    cardDealer6.setSize(71, 96);
                    cardDealer6.setLocation(266, 70);
                    add(cardDealer6, 6, 0);
                    setVisible(true);
                    cardDealer6.setVisible(true);
                    
                    cardDealer7 = null;
                    cardDealer7 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(6).getImageLoc())));
                    cardDealer7.setSize(71, 96);
                    cardDealer7.setLocation(279, 70);
                    add(cardDealer7, 7, 0);
                    setVisible(true);
                    cardDealer7.setVisible(true);
                    
                    cardDealer8 = null;
                    cardDealer8 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(7).getImageLoc())));
                    cardDealer8.setSize(71, 96);
                    cardDealer8.setLocation(292, 70);
                    add(cardDealer8, 8, 0);
                    setVisible(true);
                    cardDealer8.setVisible(true);
                    
                    cardDealer9 = null;
                    cardDealer9 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(8).getImageLoc())));
                    cardDealer9.setSize(71, 96);
                    cardDealer9.setLocation(305, 70);
                    add(cardDealer9, 9, 0);
                    setVisible(true);
                    cardDealer9.setVisible(true);
                }
                break;
                case 10: {
                    
                    cardDealer3 = null;
                    cardDealer3 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(2).getImageLoc())));
                    cardDealer3.setSize(71, 96);
                    cardDealer3.setLocation(226, 70);
                    add(cardDealer3, 3, 0);
                    setVisible(true);
                    cardDealer3.setVisible(true);
                    
                    
                    cardDealer4 = null;
                    cardDealer4 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(3).getImageLoc())));
                    cardDealer4.setSize(71, 96);
                    cardDealer4.setLocation(239, 70);
                    add(cardDealer4, 4, 0);
                    setVisible(true);
                    cardDealer4.setVisible(true);
                    
                    cardDealer5 = null;
                    cardDealer5 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(4).getImageLoc())));
                    cardDealer5.setSize(71, 96);
                    cardDealer5.setLocation(253, 70);
                    add(cardDealer5, 5, 0);
                    setVisible(true);
                    cardDealer5.setVisible(true);
                    
                    cardDealer6 = null;
                    cardDealer6 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(5).getImageLoc())));
                    cardDealer6.setSize(71, 96);
                    cardDealer6.setLocation(266, 70);
                    add(cardDealer6, 6, 0);
                    setVisible(true);
                    cardDealer6.setVisible(true);
                    
                    cardDealer7 = null;
                    cardDealer7 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(6).getImageLoc())));
                    cardDealer7.setSize(71, 96);
                    cardDealer7.setLocation(279, 70);
                    add(cardDealer7, 7, 0);
                    setVisible(true);
                    cardDealer7.setVisible(true);
                    
                    cardDealer8 = null;
                    cardDealer8 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(7).getImageLoc())));
                    cardDealer8.setSize(71, 96);
                    cardDealer8.setLocation(292, 70);
                    add(cardDealer8, 8, 0);
                    setVisible(true);
                    cardDealer8.setVisible(true);
                    
                    cardDealer9 = null;
                    cardDealer9 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(8).getImageLoc())));
                    cardDealer9.setSize(71, 96);
                    cardDealer9.setLocation(305, 70);
                    add(cardDealer9, 9, 0);
                    setVisible(true);
                    cardDealer9.setVisible(true);
                    
                    cardDealer10 = null;
                    cardDealer10 = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().createImage(dealerHand.get(9).getImageLoc())));
                    cardDealer10.setSize(71, 96);
                    cardDealer10.setLocation(318, 70);
                    add(cardDealer10, 10, 0);
                    setVisible(true);
                    cardDealer10.setVisible(true);
                }
                break;
            }
        }
        
        if (resettiBool)
        {
            int checkDealerNum = dealerHand.size();
            
            card1.setVisible(false);
            card2.setVisible(false);
            card3.setVisible(false);
            card4.setVisible(false);
            switch(cardNum)
            {
                case 5: {
                    card5.setVisible(false);
                    setVisible(true);
                }
                break;
                case 6: {
                    card5.setVisible(false);
                    card6.setVisible(false);
                    setVisible(true);
                }
                break;
                case 7: {
                    card5.setVisible(false);
                    card6.setVisible(false);
                    card7.setVisible(false);
                    setVisible(true);
                }
                break;
                case 8: {
                    card5.setVisible(false);
                    card6.setVisible(false);
                    card7.setVisible(false);
                    card8.setVisible(false);
                    setVisible(true);
                }
                break;
                case 9: {
                    card5.setVisible(false);
                    card6.setVisible(false);
                    card7.setVisible(false);
                    card8.setVisible(false);
                    card9.setVisible(false);
                    setVisible(true);
                }
                break;
                case 10: {
                    card5.setVisible(false);
                    card6.setVisible(false);
                    card7.setVisible(false);
                    card8.setVisible(false);
                    card9.setVisible(false);
                    card10.setVisible(false);
                    setVisible(true);
                }
                break;
                case 11: {
                    card5.setVisible(false);
                    card6.setVisible(false);
                    card7.setVisible(false);
                    card8.setVisible(false);
                    card9.setVisible(false);
                    card10.setVisible(false);
                    card11.setVisible(false);
                    setVisible(true);
                }
                break;
                case 12: {
                    card5.setVisible(false);
                    card6.setVisible(false);
                    card7.setVisible(false);
                    card8.setVisible(false);
                    card9.setVisible(false);
                    card10.setVisible(false);
                    card11.setVisible(false);
                    card12.setVisible(false);
                    setVisible(true);
                }
                break;
                case 13: {
                    card5.setVisible(false);
                    card6.setVisible(false);
                    card7.setVisible(false);
                    card8.setVisible(false);
                    card9.setVisible(false);
                    card10.setVisible(false);
                    card11.setVisible(false);
                    card12.setVisible(false);
                    card13.setVisible(false);
                    setVisible(true);
                }
            }
            if (resetDealer)
            {
                switch (dealCardNum)
                {
                    case 3: {
                        cardDealer3.setVisible(false);
                        setVisible(true);
                    }
                    break;
                    case 4: {
                        cardDealer3.setVisible(false);
                        cardDealer4.setVisible(false);
                        setVisible(true);
                    }
                    break;
                    case 5: {
                        cardDealer3.setVisible(false);
                        cardDealer4.setVisible(false);
                        cardDealer5.setVisible(false);
                        setVisible(true);
                    }
                    break;
                    case 6: {
                        cardDealer3.setVisible(false);
                        cardDealer4.setVisible(false);
                        cardDealer5.setVisible(false);
                        cardDealer6.setVisible(false);
                        setVisible(true);
                    }
                    break;
                    case 7: {
                        cardDealer3.setVisible(false);
                        cardDealer4.setVisible(false);
                        cardDealer5.setVisible(false);
                        cardDealer6.setVisible(false);
                        cardDealer7.setVisible(false);
                        setVisible(true);
                    }
                    break;
                    case 8: {
                        cardDealer3.setVisible(false);
                        cardDealer4.setVisible(false);
                        cardDealer5.setVisible(false);
                        cardDealer6.setVisible(false);
                        cardDealer7.setVisible(false);
                        cardDealer8.setVisible(false);
                        setVisible(true);
                    }
                    break;
                    case 9: {
                        cardDealer3.setVisible(false);
                        cardDealer4.setVisible(false);
                        cardDealer5.setVisible(false);
                        cardDealer6.setVisible(false);
                        cardDealer7.setVisible(false);
                        cardDealer8.setVisible(false);
                        cardDealer9.setVisible(false);
                        setVisible(true);
                    }
                    break;
                    case 10: {
                        cardDealer3.setVisible(false);
                        cardDealer4.setVisible(false);
                        cardDealer5.setVisible(false);
                        cardDealer6.setVisible(false);
                        cardDealer7.setVisible(false);
                        cardDealer8.setVisible(false);
                        cardDealer9.setVisible(false);
                        cardDealer10.setVisible(false);
                        setVisible(true);
                    }
                    break;
                }
            }
                for (int i = dealerHand.size() - 1; i >= 0; i--)
                {
                    dealerHand.remove(0);
                }
                for (int i = playerHand.size() - 1; i >= 0; i--)
                {
                    playerHand.remove(0);
                }
                resettiBool = false;
                resetDealer = false;
                dealCardNum = 2;
        }
    }
}