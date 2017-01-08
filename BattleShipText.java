/*====================================================================
|File Name:				BattleShip													|
|Class:					ICS3U															|
|Date Last Modified:	14/06/15														|
|Description:			Starts a battleship game for user					|
|====================================================================|
|To Do:                                                              |
|-enhance player boat placement?!?!? (drag& drop)                    |
|====================================================================|
|Change log:                                                         |
|-normal AI added                                                    |
|-click GUI added                                                    |
|-drawn boards of each player                                        |
|-sound effects when attack/hit                                      |
|-smooth background music                                            |
|-welcome screen inputs                                              |
|-welcome screen display                                             |
|-check victory                                                      |
|-check sunken                                                       |
|-computer guessing (easy mode)                                      |
|-player guessing                                                    |
|-player ship placement                                              |
|-computer ship placement                                            |
|-display board (empty atm)                                          |
|                                                                    |
====================================================================*/
import java.util.Scanner;
import java.applet.*;
import java.io.*;
import java.net.*;

public class BattleShipText
{  
   static Scanner sc=new Scanner(System.in);
   static int sound;                                              //for choosing sound effects
   static final String[] names={"Player","Computer"};
   static final String SHIPA="Aircraft Carier",							//Names for each ship
   						SHIPB="Battleship",
   						SHIPC="Cruiser",
   						SHIPD="Destroyer",
   						SHIPS="Submarine",
   						SUNKEN=" has sunken ";
   static final char EMPTY='-',												//Every possible symbol for a slot on the board
   						MISS='O',
   						HIT='X',
   						DESTROYER='D',
   						CRUISER='C',
   						SUBMARINE='S',
   						BATTLESHIP='B',
   						AIRCRAFT='A';

//static char [][] player=new char [BattleShip.boardSize][BattleShip.boardSize],playerHit=new char[BattleShip.boardSize][BattleShip.boardSize],computer=new char [BattleShip.boardSize][BattleShip.boardSize];
   static char [][][]player=new char[3][BattleShip.boardSize][BattleShip.boardSize];		/*initialize 3 boards, 
																						player's ship board, 
																						player's attack board, 
																						computer's ship board(hidden)*/
   static int turn=0,X,Y,initX,initY,lastX,lastY,user=turn%2,board=(user==0)?2:0;
   static int[]healthD={BattleShip.small,BattleShip.small},
   				healthC={BattleShip.medium,BattleShip.medium},
   				healthS={BattleShip.medium,BattleShip.medium},
   				healthB={BattleShip.big,BattleShip.big},
   				healthA={BattleShip.huge,BattleShip.huge};   
   static boolean[]sunkD=new boolean[2],									//state of each player's & computer's ship
   					sunkC=new boolean[2],									//if sunk is true, sunken message will not display
   					sunkS=new boolean[2],									//the next turn
   					sunkB=new boolean[2],
   					sunkA=new boolean[2];
   static boolean run=false,playerWon=false;													//check for if game is running
   static boolean direction[]=new boolean [5];        //state of direction in normal mode
   static final int UP=1,DOWN=2,RIGHT=3,LEFT=4;            //for normal mode direction arraye

	/*====================================================================
   |soundEffects(int i)																	|
   |--------------------------------------------------------------------|
   |plays sound FX              								               	|
   |																							|
   ====================================================================*/
   public static void soundEffects(int i)
   {
      AudioClip blastSound = null;
      AudioClip explosionSound = null;
   
      File soundEffect = new File("resources//blast.wav");
      File soundEffect2 = new File("resources//explosion.wav");
   
      try
      {
         blastSound = Applet.newAudioClip(soundEffect.toURL());
         explosionSound = Applet.newAudioClip(soundEffect2.toURL());
      }
      catch(MalformedURLException e)
      {
         System.out.println(e);
      }
      if (i == 1)                      //chooses different sound effects
         blastSound.play();
      else if (i == 2)
         explosionSound.play();
   }

	/*====================================================================
	|runGame()																				|
	|--------------------------------------------------------------------|
	|--------------------------------------------------------------------|
	|main method for running the game												|
	====================================================================*/
   public static void runGame()
   {  
      run=true;
      int input, surrender, difficulty=0;
      setup();//initializes empty boards
      do
      {
         System.out.println("What difficulty level do you want?");//player chooses difficulty
         System.out.println("1)    Easy      - For beginners or unlucky people.");
         System.out.println("2)    Normal    - For people who like a challenge.");
         System.out.println("3)    Impossible- To be sent to Davy Jones' locker.");
         difficulty=sc.nextInt();
         if(difficulty>3||difficulty<1)
         {
            System.out.println("Invalid difficulty");
         }
      }while(difficulty>3||difficulty<1);
      //Board.drawBoard();
   
      while(run)
      {
         if(user==0)
         {
            displayTogether();
            System.out.println("1. Attack\n2.Surrender");//player chooses to attack or surrender
            input=sc.nextInt();
            while (input!=2&&input!=1)//checks for invalid input
            {
               displayTogether();  //display game board             
               System.out.println("Error: Invalid choice");
               System.out.println("1. Attack\n2.Surrender");
               input=sc.nextInt();
            }
            if(input==2)
            {
               System.out.println("Are you sure?");//confirms user wants to surrender
               System.out.println("1. Yes\n2. No");
               surrender = sc.nextInt();
               if (surrender == 1)
               {
                  displayBoard("GAMEOVER",player[1]);//displays hidden computer's ships
                  break;//ends game, goes back to starting menu
               }
            }
            Board.f.repaint();
            guessPlayer();
         }
         else
         {
            guessComputer(difficulty);
         }
         checkShips();//check for sunken ships
         checkWon();//check for a victory
         turn++;
         user=turn%2;
         board=(user==0)?2:0;
         Board.f.repaint();
      }
      displayBoard(names[1],player[1]);
      System.out.println("Enter anything to continue");
      Welcome.sc.nextLine();
   }
	/*====================================================================
	|clearAll()										   									|
	|--------------------------------------------------------------------|
	|--------------------------------------------------------------------|
	|restores everything for a new game                      				|
	====================================================================*/
   public static void clearAll()
   {
      player=new char[3][BattleShip.boardSize][BattleShip.boardSize];
      playerWon=false;
      for(int p=0;p<player.length;p++)//clear player boards
      {
         for (int i=0;i<BattleShip.boardSize;i++)
         {
            for (int n=0;n<BattleShip.boardSize;n++)
            {
               player[p][i][n]=EMPTY;
            } 
         }
      }
      for(int i=0;i<2;i++)
      {
         healthD[i]=BattleShip.small;//restore ship health 
         healthC[i]=BattleShip.medium;
         healthS[i]=BattleShip.medium;
         healthB[i]=BattleShip.big;
         healthA[i]=BattleShip.huge;
         sunkA[i]=false;//reset sunken status
         sunkB[i]=false;
         sunkC[i]=false;
         sunkD[i]=false;
         sunkS[i]=false;
      }
      for(int i=0;i<direction.length;i++)//reset normal ai
      {
         direction[i]=false;
      }
      turn=0;//reset turn#
   }
	/*====================================================================
	|setup()																					|
	|--------------------------------------------------------------------|
	|--------------------------------------------------------------------|
	|sets all the ships for computer and prompts user for locations of 	|
	|their ships																			|
	====================================================================*/
   public static void setup()//initializes board to empty oceans
   {
      clearAll();
      setCompShip(AIRCRAFT,BattleShip.huge);         //randomely places computer's ships
      setCompShip(BATTLESHIP,BattleShip.big);
      setCompShip(CRUISER,BattleShip.medium); 
      setCompShip(SUBMARINE,BattleShip.medium);
      setCompShip(DESTROYER,BattleShip.small);
   	
      setPlayerShip(AIRCRAFT,BattleShip.huge);    //place player's ships
      setPlayerShip(BATTLESHIP,BattleShip.big);
      setPlayerShip(CRUISER,BattleShip.medium); 
      setPlayerShip(SUBMARINE,BattleShip.medium);
      setPlayerShip(DESTROYER,BattleShip.small);
      //QUICKSETPLAYER();//for ease of testing TURNOFF WHEN DONE
   }
	/*====================================================================
	|userIn(msg)																			|
	|--------------------------------------------------------------------|
	|msg		-the message used to prompt the user for input					|
	|--------------------------------------------------------------------|
	|prompts user for an input of coordinates									 	|
	|																							|
	====================================================================*/
   public static void userIn(String msg)
   {
      System.out.print(msg);
      do
      {	
         System.out.print("X: ");
         X=sc.nextInt();
         System.out.print("Y: ");
         Y=sc.nextInt();
         if(X>=BattleShip.boardSize||X<0||Y>=BattleShip.boardSize||Y<0)//checks for out-of-bounds coordinates
         {
            System.out.println("Error: coordinate is out of bounds!");
         }
         else if(player[board][Y][X]==HIT||player[board][Y][X]==MISS)//checks for already guessed coordinates
         {
            System.out.println("Error: coordinate has been guessed already! Try again.");
         }
      }while(X>=BattleShip.boardSize||X<0||Y>=BattleShip.boardSize||Y<0||player[board][Y][X]==HIT||player[board][Y][X]==MISS);
   }
	/*====================================================================
	|setPlayerShip(char ship,int sizeShip)											|
	|--------------------------------------------------------------------|
	|ship				-the symbol for the ship that will be generated			|
	|sizeShip		-the size of sahip to be generated							|
	|--------------------------------------------------------------------|
	|asks user to input the starting coordinate of their ship				|
	====================================================================*/
   public static void setPlayerShip(char ship,int size)//sets the player[user]'s ships
   {
      boolean out,over;
      int choice;
      String shipName="USO";
      switch(ship)
      {
         case AIRCRAFT:shipName=SHIPA;
            break;
         case BATTLESHIP:shipName=SHIPB;
            break;
         case CRUISER:shipName=SHIPC;
            break;
         case DESTROYER:shipName=SHIPD;
            break;
         case SUBMARINE:shipName=SHIPS;
            break;
      }
      do
      {
         displayBoard("YOUR SHIPS",player[0]);
         System.out.println("Enter the direction of your ship: ");
         System.out.println("1. Horizontal\n2. Vertical");
         choice = sc.nextInt();//chooses direction of ships
         if(choice != 1 && choice != 2)//checks for invalid input
         {
            System.out.println("Error: Invalid direction!");
         }
      }while(choice != 1 && choice != 2);
      do
      {
         displayBoard("YOUR SHIPS",player[0]);
         out=false;
         over=false;        
         if (choice==2)
         {
         
            userIn("Enter the coordinates(0-9) of your "+shipName+"("+size+"units): ");//player enters coordinates of ship     	
            for(int i=0;i<size;i++)
            {  
               if(Y+i>=BattleShip.boardSize)
               {
                  out=true;
                  break;
               }
               else if(player[0][Y+i][X]!=EMPTY)					//checks if there is an overlap
               {
                  over=true;										//if indeed an overlap, keep generating coordinates until			
                  break;											//no overlap	
               }
            }
         }
         else
         {  
            userIn("Enter the coordinates(0-9) of your "+shipName+"("+size+"units): ");     	  
            for(int i=0;i<size;i++)
            {            
               if(X+i>=BattleShip.boardSize)
               {
                  out=true;
                  break;
               }
               else	if(player[0][Y][X+i]!=EMPTY)					//checks if there is an overlap
               {
                  over=true;										//if indeed an overlap, keep generating coordinates until			
                  break;											//no overlap
               }    
            }
         }
         if (over)//prints error message
         {
            System.out.println("Error: two or more ships are overlapping!");
         }
         else if(out)
         {
            System.out.println("Error: ship will not fit on board!");
         }
      }while(out||over);      
   
      if (choice==2)
      {
         for(int i=0;i<size;i++)//sets vertical ships on grid
         {  
            player[0][Y+i][X]=ship;
         }
      }
      else
      {
         for(int i=0;i<size;i++)//sets horizontal ships on grid
         {            
            player[0][Y][X+i]=ship;     
         }
      }
   }
	/*====================================================================
	|randCoord()																			|
	|--------------------------------------------------------------------|
	|--------------------------------------------------------------------|
	|randomizes the X and Y variable used for computer guessing				|
	====================================================================*/
   public static void randCoord()
   {
      X=(int)(Math.random()*(BattleShip.boardSize));
      Y=(int)(Math.random()*(BattleShip.boardSize));
   }
	/*====================================================================
	|randCoord(int mode,int size)														|
	|--------------------------------------------------------------------|
	|mode				-indicates vertical or horizontal direction of ship	|
	|size				-indicates the size of the ship								|
	|--------------------------------------------------------------------|
	|generates a random coordinate for the ship that will fit the board	|
	====================================================================*/
   public static void randCoord(int mode,int size)
   {
      switch(mode)
      {
         case 0:X=(int)(Math.random()*(BattleShip.boardSize-size));
            Y=(int)(Math.random()*(BattleShip.boardSize));
            break;//player[user] ship spawning keep it in bounds
         case 1:Y=(int)(Math.random()*(BattleShip.boardSize-size));
            X=(int)(Math.random()*(BattleShip.boardSize));
            break;
      }
   }
	/*====================================================================
	|setCompShip(char ship,int size)													|
	|--------------------------------------------------------------------|
	|ship				-indicates symbol used for the ship							|
	|size				-indicates the size of the ship								|
	|--------------------------------------------------------------------|
	|randomly sets the ships for the computer										|
	====================================================================*/
   public static void setCompShip(char ship,int size)
   {
      boolean direction=false,retry;
      if(Math.round(Math.random())==1)
      {
         direction=true;											//vertical or horizontal random
      }
      do
      {
         retry=false;
         if (direction)
         {  
            randCoord(1,size);
            //System.out.println(X+","+Y);
            for(int i=0;i<size;i++)
            {  
               if(player[1][Y+i][X]!=EMPTY)					//checks if there is an overlap
               {
                  retry=true;										//if indeed an overlap, keep generating coordinates until			
                  break;											//no overlap	
               }
            }
         }
         else
         {    
            randCoord(0,size);
            System.out.println(X+","+Y);
            for(int i=0;i<size;i++)
            {            
               if(player[1][Y][X+i]!=EMPTY)					//checks if there is an overlap
               {
                  retry=true;										//if indeed an overlap, keep generating coordinates until			
                  break;											//no overlap
               }    
            }
         }
      }while(retry);
   
      if (direction)													//after coordinates generated are good (no overlap)
      {																	//places sets the ships onto the grid
         for(int i=0;i<size;i++)
         {  
            player[1][Y+i][X]=ship;
         }
      }
      else
      {
         for(int i=0;i<size;i++)
         {            
            player[1][Y][X+i]=ship;     
         }
      }
   }
	/*====================================================================
	|guessPlayer()																			|
	|--------------------------------------------------------------------|
	|--------------------------------------------------------------------|
	|ask user to guess coordinates of the computer's ships makes user		|
	|retry if they have previously guessed that coordinate					|
	====================================================================*/
   public static void guessPlayer()							//player[user] turn to guess
   {
      userIn("Guess a coordinate of the enemy!");
      sound = 1;
      soundEffects(sound);//plays blast sound
      System.out.print("Player guessed ("+X+","+Y+"), ");
   }
	/*====================================================================
	|guessComputer(int i)																|
	|--------------------------------------------------------------------|
	|i- indicates the difficulty with which the computer guesses			|
	|--------------------------------------------------------------------|
	|i=1		-randomly generates coordinates for computer to guess			|
	|i=2		-if computer gets a hit, guess around the hit					|
	|i=3		-computer does not miss at all										|
	====================================================================*/
   public static void guessComputer(int i)					//Computer guessing (parameters for difficulty level)
   {
      int countup=0, countdown=0, countright=0, countleft=0;
      switch(i)
      {
         case 1:														//random coordinates for guessing
            do
            {
               randCoord();
            }while(player[board][Y][X]==HIT||player[board][Y][X]==MISS);
            break;
      
         case 2:
            do
            {
               if(direction[UP])
               {
                  if(lastY-1<0||player[0][lastY-1][X]==MISS)//if out of bounds or miss, change direction
                  {
                     lastY=initY;//goes back to original hit
                     lastX=initX;
                     direction[UP]=false;
                     direction[DOWN]=true;
                  }
                  
                  else
                  {
                     lastY-=1;   
                     X=initX;
                     Y=lastY;
                  }
               }
               if(direction[DOWN])
               {
                  
                  if(lastY+1>=BattleShip.boardSize||player[0][lastY+1][X]==MISS)//if out of bounds or miss, change direction
                  {
                     lastY=initY;//goes back to original hit
                     lastX=initX;
                     direction[DOWN]=false;
                     direction[LEFT]=true;
                  
                  }
                  else
                  {
                     lastY+=1;   
                     X=initX;
                     Y=lastY;
                  } 
               }
               if(direction[LEFT])
               {
               
                  if(lastX-1<0||player[0][Y][lastX-1]==MISS)//if out of bounds or miss, change direction
                  {
                     lastY=initY;//goes back to original hit
                     lastX=initX;
                     direction[LEFT]=false;
                     direction[RIGHT]=true;
                  }
                  else
                  {
                     lastX-=1;
                     X=lastX;
                     Y=initY;
                  } 
               }
               if(direction[RIGHT])
               {
               
                  if(lastX+1>=BattleShip.boardSize||player[0][Y][lastX+1]==MISS)//if out of bounds or miss, change direction
                  {
                     lastY=initY;//goes back to original hit
                     lastX=initX;
                     direction[RIGHT]=false;
                        
                  } 
                  else
                  {
                     lastX+=1;
                     X=lastX;
                     Y=initY;
                  }
               }
                                          
               if(!direction[UP]&&!direction[DOWN]&&!direction[LEFT]&&!direction[RIGHT])
               {//if no directions left, return to random fire
                  do
                  {
                     randCoord();
                  }while(player[0][Y][X]==HIT||player[0][Y][X]==MISS);//guess is not repeated
                  if(player[0][Y][X]!=HIT&&player[0][Y][X]!=EMPTY&&player[0][Y][X]!=MISS)
                  {//if hit start guessing up direction next turn
                     direction[UP]=true;
                     initX=X;//save coords of original hit
                     initY=Y;
                     lastX=initX;
                     lastY=initY;            
                  }
               }
               if(player[0][Y][X]==EMPTY)//if the guess will be a miss, change directions
               {
                  if(direction[UP])
                  {
                     direction[UP]=false;
                     direction[DOWN]=true;
                  }
                  else if(direction[DOWN])
                  {
                     direction[DOWN]=false;
                     direction[LEFT]=true;
                  }
                  else if(direction[LEFT])
                  {
                     direction[LEFT]=false;
                     direction[RIGHT]=true;
                  }
                  else if(direction[RIGHT])
                  {
                     direction[RIGHT]=false;
                  }
               
               }
            
            
            }while(player[0][Y][X]==HIT||player[0][Y][X]==MISS);
            break;
         
         default:														//computer runs through the board to find ships and
         																//guesses those coordinates	
            for(int y=0;y<BattleShip.boardSize;y++)
            {
               for(int x=0;x<BattleShip.boardSize;x++)
               {
                  if (player[0][y][x]!=EMPTY&&player[0][y][x]!=HIT&&player[0][y][x]!=MISS)
                  {
                     Y=y;
                     X=x;
                  }
               }
            }
            break;
      
      }
      //Board.message.setText("Computer guessed ("+X+","+Y+")");
      //prints coordinates that computer has guessed
      System.out.print("Computer guessed ("+X+","+Y+"), ");
   }
	/*====================================================================
	|checkShips()																			|
	|--------------------------------------------------------------------|
	|--------------------------------------------------------------------|
	|every time a ship is it, its hit points, stored in an array ticks	|
	|down by one, when it equals 0 and the corresponding sunken boolean	|
	|is false, then it will print the sunken message and set sunken true	|
	====================================================================*/
   public static void checkShips()//check for sinks
   {     
      sound = 2;
      switch(player[(user==0)?1:0][Y][X])						//manage ship hit points
      {
         case AIRCRAFT:player[board][Y][X]=HIT;
            soundEffects(sound); //plays explosion sound effect
            if(user==0)
            {
               player[1][Y][X]=HIT;//changes the computer board aswell (for surrender screen)      
            }
            healthA[user]--;//health of the ship is now lower
            break;
         case BATTLESHIP:player[board][Y][X]=HIT;
            soundEffects(sound); 
            if(user==0)
            {
               player[1][Y][X]=HIT;
            }               
            healthB[user]--;
            break;
         case CRUISER:player[board][Y][X]=HIT;
            soundEffects(sound); 
            if(user==0)
            {
               player[1][Y][X]=HIT;
            }               
            healthC[user]--;
            break;
         case DESTROYER:player[board][Y][X]=HIT;
            soundEffects(sound); 
            if(user==0)
            {
               player[1][Y][X]=HIT;
            }               
            healthD[user]--;
            break;
         case SUBMARINE:player[board][Y][X]=HIT;
            soundEffects(sound); 
            if(user==0)
            {
               player[1][Y][X]=HIT;
            }               
            healthS[user]--;
            break;
         case EMPTY:
            if(user==0)
            {
               player[1][Y][X]=MISS;
            }
            player[board][Y][X]=MISS;
         
            break;
      }
      if(player[board][Y][X]==HIT)//prints status of the attack
      {
         System.out.println("and it was a hit!");
      }
      else
      {
         System.out.println("and it was a miss!");
      }
      if(healthA[user]==0&&!sunkA[user])//checks for sinking and prints it once
      {
         //Board.sunken.setText(names[user]+SUNKEN+SHIPA);
         System.out.println(names[user]+SUNKEN+SHIPA);
         sunkA[user]=true;
      }
      else if(healthB[user]==0&&!sunkB[user])
      {
         //Board.sunken.setText(names[user]+SUNKEN+SHIPB);
         System.out.println(names[user]+SUNKEN+SHIPB);
         sunkB[user]=true;
      }
      else if(healthC[user]==0&&!sunkC[user])
      {
         //Board.sunken.setText(names[user]+SUNKEN+SHIPC);
         System.out.println(names[user]+SUNKEN+SHIPC);
         sunkC[user]=true;
      }
      else if(healthD[user]==0&&!sunkD[user])
      {
         //Board.sunken.setText(names[user]+SUNKEN+SHIPD);
         System.out.println(names[user]+SUNKEN+SHIPD);
         sunkD[user]=true;
      }
      else if(healthS[user]==0&&!sunkS[user])
      {
         //Board.sunken.setText(names[user]+SUNKEN+SHIPS);
         System.out.println(names[user]+SUNKEN+SHIPS);
         sunkS[user]=true;
      }
   }
	/*====================================================================
	|checkWon()																				|
	|--------------------------------------------------------------------|
	|--------------------------------------------------------------------|
	|at the end of each turn, runs to check if a player has won by doing	|
	|a search of the opposing players board and counting amount of hits	|
	|if hits is the amount of units taken up by the ships then victory	|
	====================================================================*/
   public static void checkWon()								//works by looking at grid if hits = number of ships
   {  
      int hit=0;
   
      for(int i=0;i<BattleShip.boardSize;i++)
      {
         for(int n=0;n<BattleShip.boardSize;n++)
         {
            if(player[board][i][n]==HIT)
            {
               hit++;
            }
         }
      }
   	
      if(hit==BattleShip.huge+BattleShip.big+BattleShip.medium*2+BattleShip.small)
      {
         run=false;//game end
         System.out.println(names[user]+" has won!");
      }
   }
	/*====================================================================
	|displayBoard(String title,char array[][])									|
	|--------------------------------------------------------------------|
	|title		-title of the board that is being printed						|
	|array[][]	-the actual board that is being referenced to be printed	|
	|--------------------------------------------------------------------|
	|prints the specified 2D array with the title indicating whos board	|
	|it is and numbers surrounding array to indicate the coordinate 		|
	|used for setting ships	and gameover                 						|
	====================================================================*/
   public static void displayBoard(String title,char array[][])						//show player[user] their attacks
   {
      System.out.println(title);
   
      for(int i=0;i<BattleShip.boardSize;i++)
      {
         System.out.print(" "+i);//the values on x-axis
      }
      
      
   
      System.out.println();			
      for(int i=0;i<BattleShip.boardSize;i++)
      {
         for(int n=0;n<BattleShip.boardSize;n++)
         {	
            if(n==0)
            {
               System.out.print(i);//the values on y axis
            }
            System.out.print(array[i][n]+" ");
         }
      	
         System.out.println();
      }
   }
   /*====================================================================
	|displayTogether()                        									|
	|--------------------------------------------------------------------|
	|--------------------------------------------------------------------|
	|prints player board and computer board together                  	|
	|used for when user is prompt after game is started 						|
	====================================================================*/
   public static void displayTogether()
   {
      System.out.print("YOU\t");
      for(int i=0;i<BattleShip.boardSize*2/4-1;i++)
      {
         System.out.print("\t");
      }
      System.out.println("ENEMY");
      for(int n=0;n<2;n++)
      {
         for(int i=0;i<BattleShip.boardSize;i++)
         {
            System.out.print(" "+i);
         }
      
         System.out.print("\t");
      
      }
      System.out.println();
      for(int i=0;i<BattleShip.boardSize;i++)
      {
         for(int n=0;n<BattleShip.boardSize;n++)
         {	
            if(n==0)
            {
               System.out.print(i);
            }
            System.out.print(player[0][i][n]+" ");//player  board
         }
         System.out.print("\t");
         for(int n=0;n<BattleShip.boardSize;n++)
         {	
            if(n==0)
            {
               System.out.print(i);
            }
            System.out.print(player[2][i][n]+" ");//computer board
         }
      	
         System.out.println();
      }
   }
   public static void QUICKSETPLAYER()//QUICKTESTING
   {
      player[0][0][0]=SUBMARINE;
      player[0][0][1]=SUBMARINE;
      player[0][0][2]=SUBMARINE;
   		
      player[0][2][0]=CRUISER;
      player[0][2][1]=CRUISER;
      player[0][2][2]=CRUISER;
         
      player[0][4][0]=DESTROYER;
      player[0][4][1]=DESTROYER;
         
      player[0][6][0]=BATTLESHIP;
      player[0][6][1]=BATTLESHIP;
      player[0][6][2]=BATTLESHIP;
      player[0][6][3]=BATTLESHIP;
         
      player[0][8][0]=AIRCRAFT;
      player[0][8][1]=AIRCRAFT;
      player[0][8][2]=AIRCRAFT;
      player[0][8][3]=AIRCRAFT;
      player[0][8][4]=AIRCRAFT;
   
   }
}