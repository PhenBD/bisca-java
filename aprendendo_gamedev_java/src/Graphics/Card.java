package Graphics;

import java.awt.image.BufferedImage;

public class Card {
    private int value;
    private BufferedImage sprite;
    private int suit;

    public Card(Card card) {
        this.value = card.getValue();
        this.sprite = card.getSprite();
        this.suit = card.getSuit();
    }
    
    public Card(int value, int suit, BufferedImage sprite) {
        this.value = value;
        this.sprite = sprite;
        this.suit = suit;
    }

    public int getSuit() {
        return suit;
    }

    public int getValue() {
        return value;
    }

    public BufferedImage getSprite() {
        return sprite;
    }
}
