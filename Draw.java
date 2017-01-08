/*====================================================================
|File Name:				Draw.java													|
|Class:					ICS3U															|
|Date Last Modified:	06/06/15														|
|Description:			The graphical components of the Board frame  	|
|===================================================================*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Draw extends JPanel
{
   static boolean textures=true;
   static int width = Board.WIDTH/3;//commonly used values for drawing our board
   static int height= Board.HEIGHT/2;
   static int[]count=new int [5],//counter for each ship that indicates which part of the ship is being draw on the tile
               dirShip=new int[5];//the direction of the ship from BattleShip.setPlayerShip is stored here
   public void paintComponent(Graphics g)
   {
   
      super.paintComponent(g);
      //background is white during game, otherwise is green if player wins or red if computer wins or player surrenders
      this.setBackground((BattleShip.run)?Color.WHITE:((BattleShip.playerWon)?Color.GREEN:Color.RED));
   
      int w = width/BattleShip.boardSize;       //width and height of tile
      int h = height/BattleShip.boardSize;
   
      Image water = Toolkit.getDefaultToolkit().getImage("resources//waves.jpg");      //tile images
      Image hit = Toolkit.getDefaultToolkit().getImage("resources//explosion.png");
      //load ship files
      Image endH = Toolkit.getDefaultToolkit().getImage("resources//endH.jpg");//Horizontal ship images
      Image midH = Toolkit.getDefaultToolkit().getImage("resources//midH.jpg");
      Image tipH = Toolkit.getDefaultToolkit().getImage("resources//tipH.jpg");
      
      Image endV = Toolkit.getDefaultToolkit().getImage("resources//endV.jpg");//vertical ship images
      Image midV = Toolkit.getDefaultToolkit().getImage("resources//midV.jpg");
      Image tipV = Toolkit.getDefaultToolkit().getImage("resources//tipV.jpg");
      if(BattleShip.run)
      {
         for(int i=0;i<BattleShip.boardSize;i++)
         {
            for(int n=0;n<BattleShip.boardSize;n++)
            {
            
             //PLAYERS BOARD
               if(textures)
               {
                  if(BattleShip.player[0][i][n]==BattleShip.EMPTY)
                  {
                     g.setColor(Color.CYAN);
                     g.fill3DRect(n*w,i*h,w,h,true);//draw board tile
                  }
                  else if(BattleShip.player[0][i][n]==BattleShip.MISS)
                  {
                     g.drawImage(water, n*w, i*h, w, h, this);//water for misses
                  }
                  else if(BattleShip.player[0][i][n]==BattleShip.HIT)
                  {
                     g.drawImage(hit, n*w, i*h, w, h, this);//explosion for hits
                     //if it was a ship but was hit 
                     //counter is still needed so the images of the ship do not shift
                     switch(BattleShip.player[3][i][n])
                     {
                        
                        case BattleShip.AIRCRAFT:
                           count[0]++;//counter for which part of ship it is and for which ship
                        
                           if(count[0]==BattleShip.huge)
                           {
                              count[0]=0;//if counter reaches size of ship, it will reset itself
                           }
                           
                           
                           break;
                     
                        case BattleShip.BATTLESHIP:
                           count[1]++;
                        
                           if(count[1]==BattleShip.big)
                           {
                              count[1]=0;
                           }                           
                           break;
                     
                        case BattleShip.CRUISER:
                           count[2]++;
                        
                           if(count[2]==BattleShip.medium)
                           {
                              count[2]=0;
                           }                          
                           break;
                     
                        case BattleShip.DESTROYER:
                           count[3]++;
                        
                           if(count[3]==BattleShip.small)
                           {
                              count[3]=0;
                           }                          
                           break;
                     
                        case BattleShip.SUBMARINE:
                           count[4]++;
                        
                           if(count[4]==BattleShip.medium)
                           {
                              count[4]=0;
                           }                          
                           break;
                     }
                  }
                  else//if it is a ship and hasnt been  hit
                  {
                     //draws ships with proper orientation
                     switch(BattleShip.player[0][i][n])
                     {
                        
                        case BattleShip.AIRCRAFT:
                           count[0]++;
                           if(count[0]==BattleShip.huge)
                           {
                              //if equal to the size of the ship, draw the tip
                              //dirShip stores the orientation of each ship
                              g.drawImage((dirShip[0]==1)?tipH:tipV, n*w, i*h, w, h, this);
                              count[0]=0;
                           
                           }
                           else if(count[0]==1)
                           {  //if equal to the first point on the ship, draw the end
                              g.drawImage((dirShip[0]==1)?endH:endV, n*w, i*h, w, h, this);
                           }
                           else
                           {  //otherwise draw body of ship
                              g.drawImage((dirShip[0]==1)?midH:midV, n*w, i*h, w, h, this);
                           
                           }
                           break;
                           
                        case BattleShip.BATTLESHIP:
                           count[1]++;
                           if(count[1]==BattleShip.big)
                           {
                           
                              g.drawImage((dirShip[1]==1)?tipH:tipV, n*w, i*h, w, h, this);
                              count[1]=0;
                           }
                           else if(count[1]==1)
                           {
                              g.drawImage((dirShip[1]==1)?endH:endV, n*w, i*h, w, h, this);
                           
                           }
                           else
                           {
                              g.drawImage((dirShip[1]==1)?midH:midV, n*w, i*h, w, h, this);
                           
                           }                           
                           break;
                           
                        case BattleShip.CRUISER:
                           count[2]++;
                           if(count[2]==BattleShip.medium)
                           {
                           
                              g.drawImage((dirShip[2]==1)?tipH:tipV, n*w, i*h, w, h, this);
                              count[2]=0;
                           
                           }
                           else if(count[2]==1)
                           {
                              g.drawImage((dirShip[2]==1)?endH:endV, n*w, i*h, w, h, this);
                           
                           }
                           else
                           {
                              g.drawImage((dirShip[2]==1)?midH:midV, n*w, i*h, w, h, this);
                           
                           }
                           
                           break;
                           
                        case BattleShip.DESTROYER:
                           count[3]++;
                           if(count[3]==BattleShip.small)
                           {
                           
                              g.drawImage((dirShip[3]==1)?tipH:tipV, n*w, i*h, w, h, this);
                              count[3]=0;
                           
                           }
                           else if(count[3]==1)
                           {
                              g.drawImage((dirShip[3]==1)?endH:endV, n*w, i*h, w, h, this);
                           
                           }
                           else
                           {
                              g.drawImage((dirShip[3]==1)?midH:midV, n*w, i*h, w, h, this);
                           
                           }
                           
                           break;
                           
                        case BattleShip.SUBMARINE:
                           count[4]++;
                           if(count[4]==BattleShip.medium)
                           {
                           
                              g.drawImage((dirShip[4]==1)?tipH:tipV, n*w, i*h, w, h, this);
                              count[4]=0;
                           }
                           else if(count[4]==1)
                           {
                              g.drawImage((dirShip[4]==1)?endH:endV, n*w, i*h, w, h, this);
                           
                           }
                           else
                           {
                              g.drawImage((dirShip[4]==1)?midH:midV, n*w, i*h, w, h, this);
                           
                           }
                           break;
                     }
                  }
               }
               else
               {//non-textured mode
                  if(BattleShip.player[0][i][n]==BattleShip.EMPTY)
                  {
                     g.setColor(Color.CYAN);//empty tiles
                  }
                  else if(BattleShip.player[0][i][n]==BattleShip.MISS)
                  {
                     g.setColor(Color.BLUE);//missed tiles
                  }
                  else if(BattleShip.player[0][i][n]==BattleShip.HIT)
                  {
                     g.setColor(Color.RED);//hit tiles
                  }
                  else
                  {
                     g.setColor(Color.GRAY);//ship tiles
                  }
               //tiles scale between boardSize and screen dimensions
                  g.fill3DRect(n*w,i*h,w,h,true);//draw board tile
               }
               //draw ship labels
               if(BattleShip.player[0][i][n]!=BattleShip.EMPTY&&BattleShip.player[0][i][n]!=BattleShip.MISS&&BattleShip.player[0][i][n]!=BattleShip.HIT)
               {
                 
                  g.setColor(Color.BLACK);
                  g.drawString(String.valueOf(BattleShip.player[0][i][n]),n*width/BattleShip.boardSize+width/BattleShip.boardSize/2,i*height/BattleShip.boardSize+height/BattleShip.boardSize/2);
               
               }
            	
            
            	//surrender "button" graphics
               g.setColor(Color.GRAY);
               g.fill3DRect(Board.WIDTH/2-width/BattleShip.boardSize,0,2*width/BattleShip.boardSize,height/BattleShip.boardSize,true);        
               g.setColor(Color.BLACK);
               g.drawString("Surrender",Board.WIDTH/2-width/BattleShip.boardSize,height/BattleShip.boardSize/2);
                 
               //PLAYER HIT BOARD
               if(textures)
               {
                  if(BattleShip.player[2][i][n]==BattleShip.EMPTY)
                  {
                     g.setColor(Color.CYAN);
                     g.fill3DRect(2*width+n*w,i*h,w,h,true);//draw board tile
                  
                  }
                  else if(BattleShip.player[2][i][n]==BattleShip.MISS)
                  {
                     g.drawImage(water, 2*width+n*w, i*h, w, h, this);//water for misses
                  }
                  else if(BattleShip.player[2][i][n]==BattleShip.HIT)
                  {
                     g.drawImage(hit, 2*width+n*w, i*h, w, h, this);//explosion for hits
                  }
               //tiles scale between boardSize and screen dimensions
               
               }
               else
               {
                  if(BattleShip.player[2][i][n]==BattleShip.EMPTY)
                  {
                     g.setColor(Color.CYAN);
                  }
                  else if(BattleShip.player[2][i][n]==BattleShip.MISS)
                  {
                     g.setColor(Color.BLUE);
                  }
                  else if(BattleShip.player[2][i][n]==BattleShip.HIT)
                  {
                     g.setColor(Color.RED);
                  }
               //tiles scale between boardSize and screen dimensions
                  g.fill3DRect(2*width+n*w,i*h,w,h,true);//draw board tile
               }
               if(n==BattleShip.boardSize-1)
               {
                  g.setColor(Color.BLACK);
               		//Y axis labels
                  g.drawString(Integer.toString(i),Board.WIDTH/3+Board.WIDTH/100,Board.HEIGHT/BattleShip.boardSize/4+i*Board.HEIGHT/BattleShip.boardSize/2);
                  g.drawString(Integer.toString(i),2*Board.WIDTH/3-Board.WIDTH/100,Board.HEIGHT/BattleShip.boardSize/4+i*Board.HEIGHT/BattleShip.boardSize/2);
               
               }
            }
               		
         }
      
         for(int i=0;i<BattleShip.boardSize;i++)
         {
            //X axis labels
            g.drawString(Integer.toString(i),width/(2*BattleShip.boardSize)+i*width/(BattleShip.boardSize),height+height/10);
            g.drawString(Integer.toString(i),2*width+width/(2*BattleShip.boardSize)+i*width/(BattleShip.boardSize),height+height/10);
         
         }
         g.drawString("YOU",width/2,height+height/10*2);
         g.drawString("ENEMY",width/2*5,height+height/10*2);			
      }
      else
      {
         //shadowed text to see better with coloured background
         g.setColor(Color.BLACK);
         g.drawString("GAMEOVER",Board.WIDTH/2+1,11);
         g.drawString("ENEMY'S SHIPS",width/2*5+1,height+height/10*2+1);
         g.drawString("Click to continue",1,height+height/10*2+1);
         g.setColor(Color.WHITE);
         g.drawString("GAMEOVER",Board.WIDTH/2,10);
         g.drawString("ENEMY'S SHIPS",width/2*5,height+height/10*2);
         g.drawString("Click to continue",0,height+height/10*2);
         
      
         for(int i=0;i<BattleShip.boardSize;i++)
         {
            for(int n=0;n<BattleShip.boardSize;n++)
            {
            //COMPUTERS BOARD
               if(BattleShip.player[1][i][n]==BattleShip.EMPTY)
               {
                  g.setColor(Color.CYAN);//cyan for empty water
               }
               else if(BattleShip.player[1][i][n]==BattleShip.MISS)
               {
                  g.setColor(Color.BLUE);//blue for misses
               }
               else if(BattleShip.player[1][i][n]==BattleShip.HIT)
               {
                  g.setColor(Color.RED);//red for hits
               }
               else
               {
                  g.setColor(Color.GRAY);//gray for ships
               }
               g.fill3DRect(2*width+n*width/BattleShip.boardSize,i*height/BattleShip.boardSize,width/BattleShip.boardSize,height/BattleShip.boardSize,true);//draw board tile
            //draw ship labels
               if(BattleShip.player[1][i][n]!=BattleShip.EMPTY&&BattleShip.player[1][i][n]!=BattleShip.MISS&&BattleShip.player[1][i][n]!=BattleShip.HIT)
               {
                  g.setColor(Color.BLACK);
                  g.drawString(String.valueOf(BattleShip.player[1][i][n]),2*width+n*width/BattleShip.boardSize+width/BattleShip.boardSize/2,i*height/BattleShip.boardSize+height/BattleShip.boardSize/2);
               }
               if(n==BattleShip.boardSize-1)
               {
                  g.setColor(Color.BLACK);
               //Y axis labels
                  g.drawString(Integer.toString(i),2*Board.WIDTH/3-Board.WIDTH/100,Board.HEIGHT/BattleShip.boardSize/4+i*Board.HEIGHT/BattleShip.boardSize/2);
               
               }		
            }
         }
         for(int i=0;i<BattleShip.boardSize;i++)
         {
            //X axis labels
            g.drawString(Integer.toString(i),2*width+width/(2*BattleShip.boardSize)+i*width/(BattleShip.boardSize),height+height/10);
         }
      
      }	
   }
}