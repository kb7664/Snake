//********************************************************************
//  sBoard.java       Author: Karim Boyd
//
//  Snake video game board and behavior.
//********************************************************************

import java.awt.Color; 
import java.awt.Dimension; 
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class sBoard extends JPanel implements ActionListener 
{
   //constants used throughout the game
   private final int B_WIDTH = 300; //board width
   private final int B_HEIGHT = 300; //board height
   private final int DOT_SIZE = 10; //size of images on the board
   private final int ALL_DOTS = 900; //amount of total image locations
   private final int RAND_POS = 29; //food location randomizing constant 
   
   private final int x[] = new int[ALL_DOTS]; //stores the x values of each part of the snake
   private final int y[] = new int[ALL_DOTS]; //stores the y values of each part of the snake
   
   private int dots; 
   private int apple_x;
   private int apple_y;
   private int DELAY = 140; //determines the speed of the game (suggested minimum of 20)
   private int level = 0; //tracks the player's current level
   
   private boolean leftDirection = false;
   private boolean rightDirection = true;
   private boolean upDirection = false; 
   private boolean downDirection = false; 
   private boolean inGame = true;
   
   private Timer timer;
   private Image ball;
   private Image apple;
   private Image head;
   
   public sBoard()
   {
      addKeyListener(new TAdapter()); //WHAT?
      setBackground(Color.black);
      setFocusable(true);
      
      setPreferredSize(new Dimension(B_WIDTH, B_HEIGHT));
      loadImages();
      initGame();
   }
   
   private void loadImages() //loads in the images for the game
   {
      ImageIcon iid = new ImageIcon("dot.png"); //ImageIcon class is used to display PNG images
      ball = iid.getImage(); //reassigns the image to a variable
      
      ImageIcon iia = new ImageIcon("apple.png");
      apple = iia.getImage();
      
      ImageIcon iih = new ImageIcon("head.png");
      head = iih.getImage();
   }
   
   ActionListener action = new ActionListener()
         {   
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if ((x[0] == apple_x) && (y[0] == apple_y))
                {
                    DELAY = DELAY;
                    timer.restart();
                }
            }
        };
   
   @Override
   public void actionPerformed(ActionEvent e)
   {
      if (inGame)
      {
         checkApple();
         checkCollision();
         move();
         timer = new Timer(DELAY, action);
         timer.restart();
      }
      
      repaint();
   }
   
   private void initGame() 
   {
      dots = 3; //snake starting length
      
      for (int z = 0; z < dots; z++) //generates snake
      {
         x[z] = 50 - z * 10;
         y[z] = 50;
      }
      
      locateApple(); //picks a random location for the food
      
      //begin the game time count
      timer = new Timer(DELAY, this);
      timer.start();
   }
   
   @Override
   public void paintComponent(Graphics g)
   {
      super.paintComponent(g);
      
      doDrawing(g);
   }
   
   private void doDrawing(Graphics g)
   {
      if (inGame)
      {
         g.drawImage(apple, apple_x, apple_y, this);
         
         for (int z = 0; z < dots; z++)
         {
            if (z == 0)
            {
               g.drawImage(head, x[z], y[z], this);
            }
            else 
            {
               g.drawImage(ball, x[z], y[z], this);
            }
         }
         
         Toolkit.getDefaultToolkit().sync();
         
      }
      
      else
      {
         gameOver(g);
      }
   }
   
   private void gameOver(Graphics g) //displays end game message
   {
      String gg = "G A M E  O V E R !";
      String score = "SCORE: ";
      int scrMultiplier = 10;
      int realScore = (dots - 3) * scrMultiplier;
      String displayScore = Integer.toString(realScore);
      //Font small = new Font("Helvetica", Font.BOLD, 14);
      Font small = new Font("Courier", Font.BOLD, 14);
      FontMetrics metr = getFontMetrics(small);
      
      g.setColor(Color.white);
      g.setFont(small);
      g.drawString(gg, (B_WIDTH - metr.stringWidth(gg)) / 2, B_HEIGHT / 2);
      g.drawString(score, (B_WIDTH - metr.stringWidth(score)) / 2, (B_HEIGHT / 2) + 20);
      g.drawString(displayScore, (B_WIDTH - metr.stringWidth(displayScore)) / 2, (B_HEIGHT / 2) + 40);

   }
   
   private void checkApple() //increases the body length when food is eaten and generates new food
   {
      if ((x[0] == apple_x) && (y[0] == apple_y))
      {
         dots++;
         locateApple();
         //levels();
      }
   }
   
   private void move()
   {
      for (int z = dots; z > 0; z--) //moves the head location forward one space in the array
      {
         x[z] = x[(z - 1)];
         y[z] = y[(z - 1)];
      }
      
      //user directional controls
      if (leftDirection) 
      {
         x[0] -= DOT_SIZE;
      }
      if (rightDirection) 
      {
         x[0] += DOT_SIZE;
      }
      if (upDirection) 
      {
         y[0] -= DOT_SIZE;
      }
      if (downDirection) 
      {
         y[0] += DOT_SIZE;
      }
   }
   
   private void checkCollision() //ends the game if snake collides with itself or a wall
   {
      for (int z = dots; z > 0; z--)
      {
         if ((z > 4) && (x[0] == x[z]) && (y[0] == y[z]))
         {
            inGame = false;
         }
      }
      
      if (y[0] >= B_HEIGHT)
      {
         inGame = false;
      }
      if (y[0] < 0)
      {
         inGame = false;
      }
      if (x[0] >= B_WIDTH)
      {
         inGame = false;
      }
      if (x[0] < 0)
      {
         inGame = false;
      }
      if (!inGame)
      {
         timer.stop();
      }
   }
   
   private void locateApple() //generates the random location for the food
   {
      int r = (int) (Math.random() * RAND_POS);
      apple_x = ((r * DOT_SIZE));
      
      r = (int) (Math.random() * RAND_POS);
      apple_y = ((r * DOT_SIZE));
   }
      
   public void levels() 
   {
      level = dots - 2;
      DELAY = DELAY - 1;
      timer = new Timer(DELAY, this);
      timer.restart();
   }   
   
   //action event moved from here
   
   private class TAdapter extends KeyAdapter //reads in the physical key presses
   {
      @Override
      public void keyPressed(KeyEvent e)
      {
         int key = e.getKeyCode();
         
         if ((key == KeyEvent.VK_LEFT) && (!rightDirection))
         {
            leftDirection = true;
            upDirection = false; 
            downDirection = false;
         }
         if ((key == KeyEvent.VK_RIGHT) && (!leftDirection))
         {
            rightDirection = true;
            upDirection = false;
            downDirection = false;
         }
         if ((key == KeyEvent.VK_UP) && (!downDirection))
         {
            upDirection = true;
            rightDirection = false; 
            leftDirection = false;
         }
         if ((key == KeyEvent.VK_DOWN) && (!upDirection))
         {
            downDirection = true;
            rightDirection = false; 
            leftDirection = false; 
         }
      }
   }
}
