package snakepackage;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.*;

import enums.GridSize;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author jd-
 *
 */
public class SnakeApp {

    private static SnakeApp app;
    public static final int MAX_THREADS = 8;
    Snake[] snakes = new Snake[MAX_THREADS];
    private static final Cell[] spawn = {
        new Cell(1, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2,
        3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, GridSize.GRID_HEIGHT - 2),
        new Cell(1, 3 * (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell(GridSize.GRID_WIDTH - 2, (GridSize.GRID_HEIGHT / 2) / 2),
        new Cell((GridSize.GRID_WIDTH / 2) / 2, 1),
        new Cell(3 * (GridSize.GRID_WIDTH / 2) / 2,
        GridSize.GRID_HEIGHT - 2)};
    private JFrame frame;
    private static Board board;
    int nr_selected = 0;
    Thread[] thread = new Thread[MAX_THREADS];

    public SnakeApp() {
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        frame = new JFrame("The Snake Race");
        frame.setLayout(new BorderLayout());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // frame.setSize(618, 640);
        frame.setSize(GridSize.GRID_WIDTH * GridSize.WIDTH_BOX + 17,
                GridSize.GRID_HEIGHT * GridSize.HEIGH_BOX + 40);
        frame.setLocation(dimension.width / 2 - frame.getWidth() / 2,
                dimension.height / 2 - frame.getHeight() / 2);
        board = new Board();
        
        
        frame.add(board,BorderLayout.CENTER);
        
        JPanel actionsBPabel=new JPanel();


        JButton startBtn = new JButton("Start");
        JButton pauseBtn = new JButton("Pause");
        JButton resumeBtn = new JButton("Resume");

        //Events
        final int[] clicks = {0};
        startBtn.addActionListener((actionEvent)->{
            app.playGame();
        });
        pauseBtn.addActionListener((actionEvent) ->{
            app.pauseGame();
        });
        resumeBtn.addActionListener((actionEvent)->{
            app.resumeGame();
        });


        actionsBPabel.setLayout(new FlowLayout());
        actionsBPabel.add(startBtn);
        actionsBPabel.add(pauseBtn);
        actionsBPabel.add(resumeBtn);
        frame.add(actionsBPabel,BorderLayout.SOUTH);

    }

    public static void main(String[] args) throws InterruptedException {
        app = new SnakeApp();
        app.init();
    }

    private void init() throws InterruptedException {
        for (int i = 0; i != MAX_THREADS; i++) {
            
            snakes[i] = new Snake(i + 1, spawn[i%8], i + 1);
            snakes[i].addObserver(board);
            thread[i] = new Thread(snakes[i]);
        }

        frame.setVisible(true);

            
        while (true) {
            int x = 0;
            for (int i = 0; i != MAX_THREADS; i++) {
                if (snakes[i].isSnakeEnd() == true) {
                    x++;
                }
            }
            if (x == MAX_THREADS) {
                break;
            }
        }

        Thread.sleep(5);
        System.out.println("Thread (snake) status:");
        for (int i = 0; i != MAX_THREADS; i++) {
            System.out.println("["+i+"] :"+thread[i].getState());
        }
    }
    private boolean play = false;
    private void playGame(){
        if(!play){
            for (Thread t:thread
                 ) {
                t.start();
            }
            play = true;
        }
    }

    private int idWorstSnake = -1;
    private void pauseGame(){
        Snake longestSnake = snakes[0];
        int idLongestSnake = 1;
        for (Snake s: snakes) {
            s.pause();
        }

        for (int i =0; i!= MAX_THREADS; i++) {
            if(snakes[i].size() > longestSnake.size()) {
                longestSnake = snakes[i];
                idLongestSnake = i + 1;
            }

            if(idWorstSnake == -1 && snakes[i].isSnakeEnd()){
                idWorstSnake = i + 1;
            }
        }

        if(idWorstSnake != -1){
            JOptionPane.showMessageDialog(frame,"The snake " + idLongestSnake + " is the longest snake, its length is "+ longestSnake.size() + " cells \n  The snake "+ idWorstSnake + " is the worst.");
        } else {
            JOptionPane.showMessageDialog(frame,"The snake " + idLongestSnake + " is the longest snake, its length is "+ longestSnake.size() + " cells \n  All snakes are alive.");
        }
    }
    private void resumeGame(){
        for (Snake s: snakes
             ) {
            s.resume();
        }
    }

    public static SnakeApp getApp() {
        return app;
    }

}
