package Graphics;

import java.awt.Graphics;
import java.util.Random;

public class Hand {
    private Card[] cards = new Card[3];
    private int size = 0;

    public void buyCard(Deck deck) {
        Random rand = new Random();
        int i = rand.nextInt(0, deck.getSize());
        if (cards[0] == null){
            cards[0] = deck.removeCard(i);
            size++;
            return;
        }
        if (cards[1] == null){
            cards[1] = deck.removeCard(i);
            size++;
            return;
        }
        if (cards[2] == null){
            cards[2] = deck.removeCard(i);
            size++;
            return;
        }
    }

    public Card removeCard(int i) {
        Card removed = new Card(cards[i]);
        cards[i] = null;
        size--;
        return removed;
    }

    public boolean isFilled(int i) {
        if (cards[i] == null) {
            return false;
        }
        return true;
    }

    public int getSize() {
        return size;
    }

    public Card getCard(int i) {
        return cards[i];
    }

    public void render(Graphics g, int x, int y, boolean turn){
        if(turn){
            Spritesheet spritesheet = new Spritesheet("/res/cartas.png");
            if (cards[0] != null) {
                g.drawImage(spritesheet.getSprite(230, 0, 23, 32), x, y, 23, 32, null);
            }
            if (cards[1] != null) {
                g.drawImage(spritesheet.getSprite(230, 0, 23, 32), x + 28, y, 23, 32, null);
            }
            if (cards[2] != null) {
                g.drawImage(spritesheet.getSprite(230, 0, 23, 32), x + 56, y, 23, 32, null);
            }
        }
        else{
            if (cards[0] != null) {
                g.drawImage(cards[0].getSprite(), x, y, 23, 32, null);
            }
            if (cards[1] != null) {
                g.drawImage(cards[1].getSprite(), x + 28, y, 23, 32, null);
            }
            if (cards[2] != null) {
                g.drawImage(cards[2].getSprite(), x + 56, y, 23, 32, null);
            }
        }
    }
}
