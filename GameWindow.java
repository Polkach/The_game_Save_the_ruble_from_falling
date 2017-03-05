package ru.polkach;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

public class GameWindow extends JFrame{

    private static  GameWindow game_window;
    private static long last_frame_time;
    private  static  Image background;
    private static Image game_over;
    private static Image drop;
    private static float drop_left = 200;
    private static float drop_top = -100;
    private static float drop_v = 200;
    private static int score = 0;

    public static void main(String[] args) throws IOException{
        background = ImageIO.read(GameWindow.class.getResourceAsStream("neft-mmvb3.jpg"));
        game_over = ImageIO.read(GameWindow.class.getResourceAsStream("Over.png"));
        drop = ImageIO.read(GameWindow.class.getResourceAsStream("rubl.png"));
        game_window = new GameWindow();
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        game_window.setLocation(200,100);
        game_window.setSize(901,489);
        game_window.setResizable(false);
        last_frame_time = System.nanoTime();
        final GameField game_field = new GameField();
        game_field.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();
                int y  = e.getY();
                float drop_rigth = drop_left + drop.getWidth(null);
                float drop_bottom = drop_top + drop.getHeight(null);
                boolean is_drop = x>=drop_left && x<=drop_rigth && y>=drop_top && y<= drop_bottom;
                if(is_drop){
                    drop_top = -100;
                    drop_left = (int) (Math.random() * (game_field.getWidth() - drop.getWidth(null)));
                    drop_v = drop_v+20;
                    score++;
                    if(score%10==0 | (11<=score%100 && 14>=score%100)){
                        game_window.setTitle("Вы спасли: " + score + " рублей");
                    } else if(score%10==1){
                        game_window.setTitle("Вы спасли: " + score + " рубль");
                    } else if(2<=score%10 && 4>=score%10){
                        game_window.setTitle("Вы спасли: " + score + " рубля");
                    } else {
                        game_window.setTitle("Вы спасли: " + score + " рублей");
                    }

                }
            }
        });
        game_window.add(game_field);
        game_window.setVisible(true);
    }

    private static void onRepaint(Graphics g){
        long current_time = System.nanoTime();
        float delta_time = (current_time - last_frame_time)*0.000000001f;
        last_frame_time = current_time;

        drop_top = drop_top + drop_v * delta_time;
        g.drawImage(background,25,25,null);
        g.drawImage(drop,(int)drop_left,(int)drop_top,null);
        if(drop_top>game_window.getHeight()) g.drawImage(game_over,230,70,null);

    }

    private static class  GameField extends  JPanel{

        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            onRepaint(g);
            repaint();
        }
    }
}
