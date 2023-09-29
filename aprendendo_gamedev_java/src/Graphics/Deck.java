package Graphics;

import java.util.ArrayList;

public class Deck {
    private ArrayList<Card> cards = new ArrayList<>();
    private Spritesheet spritesheet;

    public Deck() {
        spritesheet = new Spritesheet("/res/cartas.png");

        int i = 0;
        for (int suit = 0; suit < 4; suit++) {
            for (int value = 1; value <= 10; value++) {
                cards.add(i, new Card(value, suit, spritesheet.getSprite(23 * (value-1), 32 * suit, 23, 32)));
                i++;
            }
        }
    }

    public Card removeCard(int i) {
        return cards.remove(i);
    }

    public Card getCard(int i) {
        return cards.get(i);
    }

    public int getSize() {
        return cards.size();
    }
}
