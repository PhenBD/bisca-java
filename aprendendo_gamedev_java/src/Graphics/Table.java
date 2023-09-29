package Graphics;

import java.awt.Graphics;

public class Table {
    private Card[] cards = new Card[4];
    private Card winnerCard;
    private int size = 0;
    private int winnerHand;

    public void addCard(Card card, int i) {
        cards[i] = card;
        size++;
    }

    public void render(Graphics g){
        if (cards[0] != null) {
            g.drawImage(cards[0].getSprite(), ((320/2)-11), (240/2)+20, 23, 32, null);
        }
        if (cards[1] != null) {
            g.drawImage(cards[1].getSprite(), ((320/2)+22), (240/2)-16, 23, 32, null);
        }
        if (cards[2] != null) {
            g.drawImage(cards[2].getSprite(), ((320/2)-11), (240/2)-52, 23, 32, null);
        }
        if (cards[3] != null) {
            g.drawImage(cards[3].getSprite(), ((320/2)-48), (240/2)-16, 23, 32, null);
        }
    }

    public int handWinner(int suitJoker, int started) {
        winnerHand = started;
        winnerCard = cards[started];

        for (int i = 0; i < 4; i++) {
            if ((cards[i].getValue() > winnerCard.getValue() && winnerCard.getSuit() != suitJoker && cards[i].getSuit() == winnerCard.getSuit()) || (cards[i].getSuit() == suitJoker && winnerCard.getSuit() != suitJoker) || (cards[i].getSuit() == suitJoker && winnerCard.getSuit() == suitJoker && cards[i].getValue() > winnerCard.getValue())) {
                winnerCard = cards[i];
                winnerHand = i;
            }
        }

        return winnerHand;
    }

    public boolean roundedSeven(int suitJoker, int startPlaying){
        boolean rounded = false;
    
        if(winnerCard.getValue() == 9 && winnerCard.getSuit() == suitJoker && winnerHand == startPlaying){
            rounded = true;
        }
        
        return rounded;
    }

    public boolean wasHelo(int suitJoker) {
        boolean helo = false;
        int ace = 0;
        for (int i = 0; i < 4; i++) {
            if (cards[i].getSuit() == suitJoker && cards[i].getValue() == 10) {
                ace = i;
            }
        }
        
        for (int i = 0; i < 4; i++) {
            boolean isNext = false;
            if(ace == i + 1){
                isNext = true;
            }
            else if(ace == 3 && i == 0){
                isNext = true;
            }

            if (cards[i].getSuit() == suitJoker && winnerCard.getSuit() == suitJoker && cards[i].getValue() == 9 && winnerCard.getValue() == 10 && isNext) {
                helo = true;
            }
        }
        return helo;
    }

    public int tableValue() {
        int value = 0;
        for (int i = 0; i < size; i++) {
            if(cards[i].getValue() == 9){
                value += 10;
            }
            else if(cards[i].getValue() == 10){
                value += 11;
            }
            else if(cards[i].getValue() == 6){
                value += 2;
            }
            else if(cards[i].getValue() == 7){
                value += 3;
            }
            else if(cards[i].getValue() == 8){
                value += 4;
            }
        }
        return value;
    }

    public int tableSize() {
        return size;
    }

    public void clearTable() {
        cards = new Card[4];
        size = 0;
    }
}
