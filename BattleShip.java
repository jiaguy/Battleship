/*====================================================================
|File Name:				BattleShip.java											|
|Class:					ICS3U															|
|Date Last Modified:	14/06/15														|
|Description:			Starts a battleship game for user					|
|Names:              Jiaying Li, Jonathan Ng                         |
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

public class BattleShip
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
   static int boardSize=10,huge=5,big=4,medium=3,small=2;	      //THE DIFFERENT SIZES OF THE SHIPS
   static final char EMPTY='-',												//Every possible symbol for a slot on the board
   							MISS='O',
   							HIT='X',
   							DESTROYER='D',
   							CRUISER='C',
   							SUBMARINE='S',
   							BATTLESHIP='B',
   							AIRCRAFT='A';
   static char [][][]player=new char[4][boardSize][boardSize];		/*initialize 3 boards,
																							0-player's ship board,
                                                                     1-computer's ship board(hidden), 
																							2-player's attack board, 
																							3-players's ship board(hidden)*/
   static int turn=0,X,Y,initX,initY,lastX,lastY,user=turn%2,board=(user==0)?2:0,difficulty=1;
   static int[]healthD={small,small},
   					healthC={medium,medium},
   					healthS={medium,medium},
   					healthB={big,big},
   					healthA={huge,huge};   
   static boolean[]sunkD=new boolean[2],									//state of each player's & computer's ship
   						sunkC=new boolean[2],								//if sunk is true, sunken message will not display
   						sunkS=new boolean[2],								//the next turn
   						sunkB=new boolean[2],
   						sunkA=new boolean[2];
   static boolean run,playerWon;												//check for if game is running
   static boolean direction[]=new boolean [5];                    //state of direction in normal mode
   static final int SINK=0,UP=1,DOWN=2,RIGHT=3,LEFT=4;            //for normal mode direction array
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
      
      File soundEffect = new File("resources\\blast.wav");//load sound FX
      File soundEffect2 = new File("resources\\explosion.wav");
   	
      try
      {
         blastSound = Applet.newAudioClip(soundEffect.toURL());
         explosionSound = Applet.newAudioClip(soundEffect2.toURL());
      }
      catch(MalformedURLException e)
      {
         System.out.println(e);
      }
      if (i == 1)
         blastSound.play();                              //shots
      else if (i == 2)
         explosionSound.play();//hits
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
      setup();                                                 //initializes empty boards
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
      }while(difficulty>3||difficulty<1);                      //check valid option
      Board.f.setVisible(true);                                //show frame
      System.out.println("Guess the enemies' coordinates.");   //for the first turn
   
   }
		/*====================================================================
		|clearAll()																		   	|
		|--------------------------------------------------------------------|
		|--------------------------------------------------------------------|
		|resets everything for a new game                        				|
		====================================================================*/
   public static void clearAll()
   {
      Draw.count=new int[5];
      player=new char[4][boardSize][boardSize];
   
      for(int p=0;p<player.length;p++)//clear player boards
      {
         for (int i=0;i<boardSize;i++)
         {
            for (int n=0;n<boardSize;n++)
            {
               player[p][i][n]=EMPTY;
            } 
         }
      }
      for(int i=0;i<2;i++)
      {
         healthD[i]=small;             //restore ship health 
         healthC[i]=medium;
         healthS[i]=medium;
         healthB[i]=big;
         healthA[i]=huge;
         sunkA[i]=false;               //reset sunken status
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
   public static void setup()
   {
      clearAll();                         //initializes board to empty oceans
      setCompShip(AIRCRAFT,huge);         //randomly places computer's ships
      setCompShip(BATTLESHIP,big);
      setCompShip(CRUISER,medium); 
      setCompShip(SUBMARINE,medium);
      setCompShip(DESTROYER,small);
   		
      setPlayerShip(AIRCRAFT,huge,0);     //place player's ships
      setPlayerShip(BATTLESHIP,big,1);
      setPlayerShip(CRUISER,medium,2); 
      setPlayerShip(SUBMARINE,medium,4);
      setPlayerShip(DESTROYER,small,3);
   		
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
      do
      {	
         displayBoard("YOUR SHIPS",player[0]);
         System.out.println(msg);
         System.out.print("X: ");
         X=sc.nextInt();
         System.out.print("Y: ");
         Y=sc.nextInt();
      
         if((X>=boardSize||X<0||Y>=boardSize||Y<0))//checks for out-of-bounds coordinates
         {
            System.out.println("Error: coordinate is out of bounds!");
         }
      
      }while(X>=boardSize||X<0||Y>=boardSize||Y<0);
   }
		/*====================================================================
		|setPlayerShip(char ship,int sizeShip)											|
		|--------------------------------------------------------------------|
		|ship				-the symbol for the ship that will be generated			|
		|sizeShip		-the size of sahip to be generated							|
		|--------------------------------------------------------------------|
		|asks user to input the starting coordinate of their ship				|
		====================================================================*/
   public static void setPlayerShip(char ship,int size,int shipNum)
   {
      boolean out,over;
      int choice;
      String shipName="USO";
      switch(ship)
      {
         case AIRCRAFT:shipName=SHIPA;//sets the name of the ship to be used for promt
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
         displayBoard("YOUR SHIPS",player[0]);  //display player's current board
         System.out.println("Enter the direction of your ship: ");
         System.out.println("1. Horizontal\n2. Vertical");
         choice = sc.nextInt();                 //chooses direction of ships
         if(choice != 1 && choice != 2)         //checks for invalid input
         {
            System.out.println("Error: Invalid direction!");
         }
      }while(choice != 1 && choice != 2);
      Draw.dirShip[shipNum]=choice;             //stores direction for drawing in graphics
      do
      {
         out=false;                             //if ship is out of bounds
         over=false;                            //if ship is overlapping
         if (choice==2)
         {
            userIn("Enter the coordinates(0-9) of your "+shipName+"("+size+"units): ");//player enters coordinates of ship     	
            for(int i=0;i<size;i++)
            {  
               if(Y+i>=boardSize)
               {
                  out=true;
                  break;
               }
               else if(player[0][Y+i][X]!=EMPTY)			//checks if there is an overlap
               {
                  over=true;										//if indeed an overlap, keep prompting for coordinates until			
                  break;											//no overlap	
               }
            }
         }
         else
         {  
            userIn("Enter the coordinates(0-9) of your "+shipName+"("+size+"units): ");     	  
            for(int i=0;i<size;i++)
            {            
               if(X+i>=boardSize)
               {
                  out=true;
                  break;
               }
               else	if(player[0][Y][X+i]!=EMPTY)			//checks if there is an overlap
               {
                  over=true;										//if indeed an overlap, keep prompting for coordinates until			
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
            player[3][Y+i][X]=ship;
         }
      }
      else
      {
         for(int i=0;i<size;i++)//sets horizontal ships on grid
         {            
            player[0][Y][X+i]=ship; 
            player[3][Y][X+i]=ship;     
         
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
      X=(int)(Math.random()*(boardSize));
      Y=(int)(Math.random()*(boardSize));
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
         case 0:X=(int)(Math.random()*(boardSize-size));
            Y=(int)(Math.random()*(boardSize));
            break;//more efficient ship spawning keeping coordinates in bounds
         case 1:Y=(int)(Math.random()*(boardSize-size));
            X=(int)(Math.random()*(boardSize));
            break;//more efficient ship spawning keeping coordinates in bounds
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
            player[1][Y+i][X]=ship;//actually set the ships
         }
      }
      else
      {
         for(int i=0;i<size;i++)
         {            
            player[1][Y][X+i]=ship; //actually set the ships    
         }
      }
   }

		/*====================================================================
		|guessComputer(int i)																|
		|--------------------------------------------------------------------|
		|i       -indicates the difficulty with which the computer guesses	|
		|--------------------------------------------------------------------|
		|i=1		-randomly generates coordinates for computer to guess			|
		|i=2		-if computer gets a hit, guess around the hit					|
		|other	-computer does not miss       										|
		====================================================================*/
   public static void guessComputer(int i)					//Computer guessing (parameters for difficulty level)
   {
      switch(i)
      {
         case 1:														//random coordinates for guessing
            do
            {
               randCoord();
            }while(player[0][Y][X]==HIT||player[0][Y][X]==MISS);//while it hasnt been guessed
            break;
      
         case 2:
            do
            {
               if(direction[UP])
               {
                  if(lastY-1<0||player[0][lastY-1][X]==MISS)//if out of bounds or miss, change direction
                  {
                     lastY=initY;//returns to initial hit point
                     lastX=initX;
                     direction[UP]=false;
                     direction[DOWN]=true;
                  }
                  
                  else//otherwise continue with guessing
                  {
                     lastY-=1;   
                     X=initX;
                     Y=lastY;
                  }
               }
               if(direction[DOWN])
               {
               
                     
                  if(lastY+1>=boardSize||player[0][lastY+1][X]==MISS)//if out of bounds or miss, change direction
                  {
                     lastY=initY;//returns to initial hit point
                     lastX=initX;
                     direction[DOWN]=false;
                     direction[LEFT]=true;
                  
                  }
                  else//otherwise continue with guessing
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
                     lastY=initY;//returns to initial hit point
                     lastX=initX;
                     direction[LEFT]=false;
                     direction[RIGHT]=true;
                  }
                  else//otherwise continue with guessing
                  {
                     lastX-=1;
                     X=lastX;
                     Y=initY;
                  } 
               }
               if(direction[RIGHT])
               {
               
                  if(lastX+1>=boardSize||player[0][Y][lastX+1]==MISS)//if out of bounds or miss, change direction
                  {
                     lastY=initY;//returns to initial hit point
                     lastX=initX;
                     direction[RIGHT]=false;
                        
                  } 
                  else//otherwise continue with guessing
                  {
                     lastX+=1;
                     X=lastX;
                     Y=initY;
                  }
               }
                                          
               if(!direction[UP]&&!direction[DOWN]&&!direction[LEFT]&&!direction[RIGHT])
               {
                  do
                  {
                     randCoord();
                  }while(player[0][Y][X]==HIT||player[0][Y][X]==MISS);
                  if(player[0][Y][X]!=HIT&&player[0][Y][X]!=EMPTY&&player[0][Y][X]!=MISS)
                  {
                     direction[UP]=true;
                     initX=X;//initial x,y position computer landed a hit
                     initY=Y;
                     lastX=initX;//these will be used for where the computer will attempt to hit
                     lastY=initY;            
                  }
               }
               if(player[0][Y][X]==EMPTY)//change direction if the shot was going to be a miss
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
            
            
            }while(player[0][Y][X]==HIT||player[0][Y][X]==MISS);//final check for a valid shot
            break;
         
         default:														//computer runs through the board to find ships and
         																//guesses those coordinates	
            for(int y=0;y<boardSize;y++)
            {
               for(int x=0;x<boardSize;x++)
               {
                  if (player[0][y][x]!=EMPTY&&player[0][y][x]!=HIT&&player[0][y][x]!=MISS)//if that coord has a ship fire
                  {
                     Y=y;
                     X=x;
                  }
               }
            }
            break;
      
      
      }
      System.out.print("Computer guessed ("+X+","+Y+")");//prints coordinates that computer has guessed
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
         case AIRCRAFT:
            player[board][Y][X]=HIT;//change value of playerboard or playerhit board
            soundEffects(sound);             //plays explosion sound effect
         				
            if(user==0)
            {
               player[1][Y][X]=HIT;//change value of computer board as well for gameover screen
            }
            
            healthA[user]--;
            break;
         case BATTLESHIP:
            player[board][Y][X]=HIT;
            soundEffects(sound);
            
            if(user==0)
            {
               player[1][Y][X]=HIT;
            }
                           
            healthB[user]--;
            break;
         case CRUISER:
            player[board][Y][X]=HIT;
            soundEffects(sound);
            
            if(user==0)
            {
               player[1][Y][X]=HIT;
            
            }
                           
            healthC[user]--;
            break;
         case DESTROYER:
            player[board][Y][X]=HIT;
            soundEffects(sound);
            
            if(user==0)
            {
               player[1][Y][X]=HIT;
            
            }
                           
            healthD[user]--;
            break;
         case SUBMARINE:
            player[board][Y][X]=HIT;
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
      if(player[board][Y][X]==HIT)//print the shot fired status
      {
         System.out.println("and it was a hit!");
      }
      else
      {
         System.out.println("and it was a miss!");
      }
      if(healthA[user]==0&&!sunkA[user])						//checks for sinking and prints it once
      {
      
         System.out.println(names[user]+SUNKEN+SHIPA+"!");
         sunkA[user]=true;
      }
      else if(healthB[user]==0&&!sunkB[user])
      {
      
         System.out.println(names[user]+SUNKEN+SHIPB+"!");
         sunkB[user]=true;
      }
      else if(healthC[user]==0&&!sunkC[user])
      {
      
         System.out.println(names[user]+SUNKEN+SHIPC+"!");
         sunkC[user]=true;
      }
      else if(healthD[user]==0&&!sunkD[user])
      {
      
         System.out.println(names[user]+SUNKEN+SHIPD+"!");
         sunkD[user]=true;
      }
      else if(healthS[user]==0&&!sunkS[user])
      {
         System.out.println(names[user]+SUNKEN+SHIPS+"!");
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
      
      for(int i=0;i<boardSize;i++)
      {
         for(int n=0;n<boardSize;n++)
         {
            if(player[board][i][n]==HIT)
            {
               hit++;
            }
         }
      }
   		
      if(hit==huge+big+medium*2+small)
      {
         run=false;//end game
         playerWon=(turn%2==0)?true:false;//if player won, the gameover screen will be green
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
		|used for setting ships		                        						|
		====================================================================*/
   public static void displayBoard(String title,char array[][])						//show player[user] their attacks
   {
      System.out.println(title);
      for(int i=0;i<boardSize;i++)
      {
         System.out.print(" "+i);			
      }    
      for(int i=0;i<boardSize;i++)
      {
         System.out.println();
         for(int n=0;n<boardSize;n++)
         {	
            if(n==0)
            {
               System.out.print(i);
            }
            System.out.print(array[i][n]+" ");
         }
      }
      System.out.println();
   }
}