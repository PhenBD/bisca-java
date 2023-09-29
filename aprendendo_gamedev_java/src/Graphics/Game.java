package Graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, MouseListener{

    public static JFrame frame;
    private Thread thread;
    private boolean isRunning = true;
    private final int WIDTH = 320;
    private final int HEIGHT = 240;
    private final int SCALE = 3;

    private BufferedImage image;

    private Deck deck;
    private Table table;
    private Hand[] hands = new Hand[4];

    private static int mx,my;
    private boolean mousePressed;

    private int currentPlaying = 0;
    private int startPlaying = 0;

    private int suitJoker = -1;
    private boolean slamed = false;

    private int pointsTeam1 = 0;
    private int pointsTeam2 = 0;

    private int round = 0;
    private int timer = 0;
    private int timerResetTable = 0;

    private int fallPointsTeam1 = 0;
    private int fallPointsTeam2 = 0;

    public Game(){
        deck = new Deck();
        table = new Table();
        for (int i = 0; i < 4; i++) {
            hands[i] = new Hand();
        }

        this.addMouseListener(this);
        this.setPreferredSize(new Dimension(WIDTH*SCALE, HEIGHT*SCALE));
        initFrame();
        image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    }

    public void initFrame(){
        frame = new JFrame("Aprendendo GameDev");
        frame.add(this);
        frame.setResizable(false);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public synchronized void start(){
        thread = new Thread(this);
        this.isRunning = true;
        thread.start();
    }

    public synchronized void stop(){
        this.isRunning = false;
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void render() {
        BufferStrategy buffer = this.getBufferStrategy();
        if(buffer == null){
            this.createBufferStrategy(3);
            return;
        }
        Graphics g = image.getGraphics();
        g.setColor(new Color(0, 200, 100));
        g.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);

        // Desenhar aqui!

        Graphics2D g2 = (Graphics2D) g;

        Spritesheet spritesheet = new Spritesheet("/res/cartas.png");

        if(deck.getSize() == 40){
            g.drawImage(spritesheet.getSprite(240, 64, 32, 16), WIDTH-64, HEIGHT-50, 32, 16, null);
            g.drawImage(spritesheet.getSprite(240, 80, 32, 16), WIDTH-64, HEIGHT-30, 32, 16, null);

        }

        if(deck.getSize() != 0){
            g.drawImage(spritesheet.getSprite(230, 0, 23, 32), WIDTH/2-11, HEIGHT/2-16, 23, 32, null);
        }

        table.render(g);

        hands[0].render(g, WIDTH/2-38, HEIGHT-64, false);
        g2.rotate(Math.toRadians(90), WIDTH-86, HEIGHT/2-16);
        hands[1].render(g, HEIGHT-33, WIDTH/4, true);
        g2.rotate(Math.toRadians(-90), WIDTH-86, HEIGHT/2-16);
        hands[2].render(g, WIDTH/2-38, 32, true);
        g2.rotate(Math.toRadians(90), 32, HEIGHT/2-16);
        hands[3].render(g, 7, WIDTH/4-32, true);
        g2.rotate(Math.toRadians(-90), 32, HEIGHT/2-16);

        g.setFont(new Font("Arial", Font.BOLD, 10));
        g.setColor(Color.white);
        String pointsTeam1 = Integer.toString(this.pointsTeam1);
        g.drawString("Team Ally - " + pointsTeam1, 10, 25);
        String pointsTeam2 = Integer.toString(this.pointsTeam2);
        g.drawString("Team Enemy - " + pointsTeam2, 230, 25);
        g.drawString("Fall - " + fallPointsTeam1 + " x " + fallPointsTeam2, 10, HEIGHT-21);

        if(suitJoker == 0){
            g.drawString("Joker - ", WIDTH-70, HEIGHT-21);
            g.drawImage(spritesheet.getSprite(256, 0, 16, 16), WIDTH-32, HEIGHT-32, 16, 16, null);
        }
        else if(suitJoker == 1){
            g.drawString("Joker - ", WIDTH-70, HEIGHT-21);
            g.drawImage(spritesheet.getSprite(256, 16, 16, 16), WIDTH-32, HEIGHT-32, 16, 16, null);
        }
        else if(suitJoker == 2){
            g.drawString("Joker - ", WIDTH-70, HEIGHT-21);
            g.drawImage(spritesheet.getSprite(256, 32, 16, 16), WIDTH-32, HEIGHT-32, 16, 16, null);
        }
        else if(suitJoker == 3){
            g.drawString("Joker - ", WIDTH-70, HEIGHT-21);
            g.drawImage(spritesheet.getSprite(256, 48, 16, 16), WIDTH-32, HEIGHT-32, 16, 16, null);
        }

        g.dispose();
        g = buffer.getDrawGraphics();
        g.drawImage(image, 0, 0, WIDTH*SCALE, HEIGHT*SCALE, null);
        buffer.show();
    }

    private void tick() {
        if(deck.getSize() == 40){
            while(mousePressed)
            if(mousePressed){
                if(mx >= (WIDTH-64)*SCALE && mx <= (WIDTH-32)*SCALE && my >= (HEIGHT-50)*SCALE && my <= (HEIGHT-34)*SCALE){
                    suitJoker = 0;
                    slamed = true;

                    for (int i = 0; i < 4; i++) {
                        hands[i].buyCard(deck);
                        hands[i].buyCard(deck);
                        hands[i].buyCard(deck);
                    }
                }
                else if(mx >= (WIDTH-64)*SCALE && mx <= (WIDTH-32)*SCALE && my >= (HEIGHT-30)*SCALE && my <= (HEIGHT-14)*SCALE){
                    Random rand = new Random();
                    int n = rand.nextInt(0, 4);

                    suitJoker = n;

                    for (int i = 0; i < 4; i++) {
                        hands[i].buyCard(deck);
                        hands[i].buyCard(deck);
                        hands[i].buyCard(deck);
                    }
                }

                mousePressed = false;
            }
        }
        
        if(currentPlaying == 0){
            if(round < 4){
                if(mousePressed){
                    if(mx >= ((WIDTH/2)-38)*SCALE && mx <= ((WIDTH/2)-15)*SCALE && my >= (HEIGHT-64)*SCALE && my <= (HEIGHT-32)*SCALE){
                        if(hands[0].isFilled(0) && !(hands[0].getCard(0).getSuit() == suitJoker && hands[0].getCard(0).getValue() == 9 && round == 3)){
                            Card cardChoosed = hands[0].removeCard(0);
                            table.addCard(cardChoosed, 0);
                            currentPlaying = 1;
                            round++;
                        }
                    }
                    else if(mx >= ((WIDTH/2)-11)*SCALE && mx <= ((WIDTH/2)+12)*SCALE && my >= (HEIGHT-64)*SCALE && my <= (HEIGHT-32)*SCALE){
                        if(hands[0].isFilled(1) && !(hands[0].getCard(1).getSuit() == suitJoker && hands[0].getCard(1).getValue() == 9 && round == 3)){
                            Card cardChoosed = hands[0].removeCard(1);
                            table.addCard(cardChoosed, 0);
                            currentPlaying = 1;
                            round++;
                        }
                    }
                    else if(mx >= ((WIDTH/2)+16)*SCALE && mx <= ((WIDTH/2)+39)*SCALE && my >= (HEIGHT-64)*SCALE && my <= (HEIGHT-32)*SCALE){
                        if(hands[0].isFilled(2) && !(hands[0].getCard(2).getSuit() == suitJoker && hands[0].getCard(2).getValue() == 9 && round == 3)){
                            Card cardChoosed = hands[0].removeCard(2);
                            table.addCard(cardChoosed, 0);
                            currentPlaying = 1;
                            round++;
                        }
                    }
                    
                    mousePressed = false;
                }
            }
        }else if(currentPlaying == 1 && hands[1].getSize() != 0){
            if(timer < 100){
                timer++;
            }
            else if(round < 4){
                Random rand = new Random();
                int n = rand.nextInt(0, 3);
                if(!hands[1].isFilled(n)){
                    if(n == 0){
                        if(!hands[1].isFilled(1)){
                            n = 2;
                        }else{
                            n = 1;
                        }
                    }else if(n == 1){
                        if(!hands[1].isFilled(2)){
                            n = 0;
                        }else{
                            n = 2;
                        }
                    }else if(n == 2){
                        if(!hands[1].isFilled(1)){
                            n = 0;
                        }else{
                            n = 1;
                        }
                    }
                }
                Card cardChoosed = hands[1].removeCard(n);
                table.addCard(cardChoosed, 1);
                currentPlaying = 2;
                timer = 0;
                round++;
            }
        }else if(currentPlaying == 2 && hands[2].getSize() != 0){
            if(timer < 100){
                timer++;
            }
            else if(round < 4){
                Random rand = new Random();
                int n = rand.nextInt(0, 3);
                if(!hands[2].isFilled(n)){
                    if(n == 0){
                        if(!hands[2].isFilled(1)){
                            n = 2;
                        }else{
                            n = 1;
                        }
                    }else if(n == 1){
                        if(!hands[2].isFilled(2)){
                            n = 0;
                        }else{
                            n = 2;
                        }
                    }else if(n == 2){
                        if(!hands[2].isFilled(1)){
                            n = 0;
                        }else{
                            n = 1;
                        }
                    }
                }
                Card cardChoosed = hands[2].removeCard(n);
                table.addCard(cardChoosed, 2);
                currentPlaying = 3;
                timer = 0;
                round++;
            }
        }else if(currentPlaying == 3 && hands[3].getSize() != 0){
            if(timer < 100){
                timer++;
            }
            else if(round < 4){
                Random rand = new Random();
                int n = rand.nextInt(0, 3);
                if(!hands[3].isFilled(n)){
                    if(n == 0){
                        if(!hands[3].isFilled(1)){
                            n = 2;
                        }else{
                            n = 1;
                        }
                    }else if(n == 1){
                        if(!hands[3].isFilled(2)){
                            n = 0;
                        }else{
                            n = 2;
                        }
                    }else if(n == 2){
                        if(!hands[3].isFilled(1)){
                            n = 0;
                        }else{
                            n = 1;
                        }
                    }
                }
                Card cardChoosed = hands[3].removeCard(n);
                table.addCard(cardChoosed, 3);
                currentPlaying = 0;
                timer = 0;
                round++;
            }
        }
        
        if(timer < 150){
            timer++;
        }
        else{
            if(table.tableSize() == 4){
                if(table.handWinner(suitJoker, currentPlaying) == 0 || table.handWinner(suitJoker, currentPlaying) == 2){
                    if (table.wasHelo(suitJoker)){
                        if(slamed){
                            fallPointsTeam1 += 2;
                        }
                        else{
                            fallPointsTeam1++;
                        }
                    }
                    if(table.handWinner(suitJoker, currentPlaying) == 0){
                        currentPlaying = 0;
                    }
                    else if(table.handWinner(suitJoker, currentPlaying) == 2){
                        currentPlaying = 2;
                    }
                    pointsTeam1 += table.tableValue();
                }
                else if(table.handWinner(suitJoker, currentPlaying) == 1 || table.handWinner(suitJoker, currentPlaying) == 3){
                    if (table.wasHelo(suitJoker)){
                        if(slamed){
                            fallPointsTeam2 += 2;
                        }
                        else{
                            fallPointsTeam2++;
                        }
                    }
                    if(table.handWinner(suitJoker, currentPlaying) == 1){
                        currentPlaying = 1;
                    }
                    else if(table.handWinner(suitJoker, currentPlaying) == 3){
                        currentPlaying = 3;
                    }
                    pointsTeam2 += table.tableValue();
                }

                if(deck.getSize() == 28){
                    if(table.roundedSeven(suitJoker, startPlaying)){
                        if(startPlaying == 0 || startPlaying == 2){
                            if(slamed){
                                fallPointsTeam1 += 2;
                            }
                            else{
                                fallPointsTeam1++;
                            }
                        }
                        else if(startPlaying == 1 || startPlaying == 3){
                            if(slamed){
                                fallPointsTeam2 += 2;
                            }
                            else{
                                fallPointsTeam2++;
                            }
                        }
                    }
                }

                table.clearTable();

                if (deck.getSize() >= 4){
                    for (int i = 0; i < 4; i++) {
                        hands[i].buyCard(deck);
                    }
                }

                round = 0;
            }
            
            timer = 0;
        }

        if(deck.getSize() == 0 && hands[0].getSize() == 0){
            if(timerResetTable < 300){
                timerResetTable++;
            }
            else{
                if(pointsTeam2 < 30){
                    if(slamed){
                        fallPointsTeam1 += 3;
                    }
                    else{
                        fallPointsTeam1+=2;
                    }
                }
                else if(pointsTeam1 < 30){
                    if(slamed){
                        fallPointsTeam2 += 3;
                    }
                    else{
                        fallPointsTeam2+=2;
                    }
                }
                else if(pointsTeam1 == 61){
                    fallPointsTeam1 += 2;
                }
                else if(pointsTeam2 == 61){
                    fallPointsTeam2 += 2;
                }
                else if(pointsTeam1 > pointsTeam2){
                    fallPointsTeam1++;
                }
                else if(pointsTeam1 < pointsTeam2){
                    fallPointsTeam2++;
                }
                else{
                    fallPointsTeam1++;
                    fallPointsTeam2++;
                }
                
                deck = new Deck();
                pointsTeam1 = 0;
                pointsTeam2 = 0;
                startPlaying++;
                suitJoker = -1;
                slamed = false;
                if(startPlaying > 3){
                    startPlaying = 0;
                }
                currentPlaying = startPlaying;

                timerResetTable = 0;
            }
        }

        if(fallPointsTeam1 >= 5){
            System.out.println("Team 1 ganhou");
            System.exit(1);
        }
        else if(fallPointsTeam2 >= 5){
            System.out.println("Team 2 ganhou");
            System.exit(1);
        }
    }

    public static void main(String[] args) throws Exception {
        Game game = new Game();
        game.start();
    }

    public void run() {
        long lastTime = System.nanoTime();
        double ammountOfTicks = 60.0;
        double ns = 1000000000 / ammountOfTicks;
        double delta = 0;
        while(isRunning){
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if(delta >= 1){
                tick();
                render();
                
                delta--;
            }

            if(System.currentTimeMillis() - lastTime >= 1000){
                lastTime += 1000;
                System.out.println("FPS: " + 0);
            }
        }

        stop();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        mx = e.getX();
        my = e.getY();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }
}
