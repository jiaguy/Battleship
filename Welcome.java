/*====================================================================
|File Name:				Welcome.java   											|
|Class:					ICS3U															|
|Date Last Modified:	14/06/15														|
|Description:			Gets input for what to load at the menu			|
|===================================================================*/
import java.util.Scanner;
import java.applet.*;
import java.io.*;
import java.net.*;

public class Welcome
{
   static boolean graphics=true;
   static Scanner sc = new Scanner(System.in);
   static AudioClip backgroundMusic = null;
   
   /*====================================================================
   |kgMusic()																		      |
   |--------------------------------------------------------------------|
   |--------------------------------------------------------------------|
   |plays background music              											|
   |																							|
   ====================================================================*/
   public static void bkgMusic()
   {
      //load music
      File soundFile = new File("resources\\endure.wav");
   	
      try
      {
         backgroundMusic = Applet.newAudioClip(soundFile.toURL());
      }
      catch(MalformedURLException e)
      {
         System.out.println(e);
      }
   	
      backgroundMusic.loop();//loop music
   }

	/*====================================================================
	|welcome()																				|
	|--------------------------------------------------------------------|
   |--------------------------------------------------------------------|
	|user selects choice from main menu												|
	====================================================================*/
   public static void welcome()
   {
      String input;
      boolean valid=true,quit=false;	
      final String START = "S",//these are the valid inputs
                  OPTIONS="O",
         			INSTRUCTIONS = "I",
         	   	QUIT = "Q";
      end:				
      do
      {
         do
         {
            valid=true;
            if(!BattleShip.run&&!BattleShipText.run)
            {
               int menuNum=(int)(Math.random()*4);//random ship menu generator
               ship(menuNum);
            }
            input = sc.nextLine();
            	
            if (input.equalsIgnoreCase(START))
            {
               if(graphics)
               {
                  BattleShip.runGame();
               }
               else
               {
                  BattleShipText.runGame();
               }
            }
            else if(input.equalsIgnoreCase(OPTIONS))
            {
               options();//options menu
            }
            else if(input.equalsIgnoreCase(INSTRUCTIONS))
            {
               instructions();//instructions
            }
            else if (input.equalsIgnoreCase(QUIT))
            {
               backgroundMusic.stop();//ends background music
               System.out.println("Exiting program.....\nThis may take a few seconds.....");
               quit=true;
               Board.f.dispose();
               System.exit(0);//force exit         
            }
            else
            {
               System.out.println("Error: invalid input! try again!");
               valid=false;
            }
         }while(!valid);
              
      }while(!quit);
   }
	
	/*====================================================================
	|ship(int i)																			|
	|--------------------------------------------------------------------|
	|i		-choose which menu to display												|
	|--------------------------------------------------------------------|
	|displays a main menu based on parameters										|
	|																							|
	====================================================================*/
   public static void ship(int i)
   {
      final String	WELCOME="Welcome to Battleship!",
                     OPTIONSTRING="For options, enter <O>.",
         				STARTSTRING="To start, enter <S>.",
         				RULESTRING="For instructions, enter <I>.",
         				QUITSTRING="To quit, enter <Q>.";
      switch (i)//randomly generated ASCII ship for main menu
      {
         case 0:
            System.out.println("                                    # #  ( )");
            System.out.println("                                 ___#_#___|__");
            System.out.println("                             _  |____________|  _");
            System.out.println("                      _=====| | |            | | |==== _");
            System.out.println("                =====| |.---------------------------. | |====");
            System.out.println("  <--------------------'   .  .  .  .  .  .  .  .   '--------------/");
            System.out.println("    \\                      "+WELCOME+"                 /");
            System.out.println("     \\                      "+STARTSTRING+"                 /");
            System.out.println("      \\                 "+RULESTRING+"            /");
            System.out.println("       \\                   "+OPTIONSTRING+"             /");
            System.out.println("        \\                    "+QUITSTRING+"              /");
            System.out.println("         \\___________________________________________________/");
            break;
         case 1:
            System.out.println("                        |");
            System.out.println("                       -+- ");
            System.out.println("                     ---#---");
            System.out.println("                     __|_|__            __");
            System.out.println("                     \\_____/           ||\\________");
            System.out.println("       __   __   __  \\_____/            ^---------^");
            System.out.println("      ||\\__||\\__||\\__|___  | '-O-`");
            System.out.println("      -^---------^--^----^___.-------------.___.--------.___.------");
            System.out.println("      `-------------|-------------------------------|-------------'");
            System.out.println("            \\___     |           o O o            |      ___/");
            System.out.println("                \\____/   "+WELCOME+"   \\____/");
            System.out.println("                     |     "+STARTSTRING+"   |");
            System.out.println("                     |"+RULESTRING+"|");
            System.out.println("                     |   "+OPTIONSTRING+"  |");
            System.out.println("                     |     "+QUITSTRING+"    |");
            System.out.println("   ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            break;
         case 2:
            System.out.println("        o                   |                  o      __o");
            System.out.println("         o                  |                 o     |X__>");
            System.out.println("	   ___o              |                __o");
            System.out.println("	 (X___>--         __|__            |X__>     o");
            System.out.println("                         |      \\                  __o");
            System.out.println("  "+WELCOME+" |       \\                |X__>");
            System.out.println("  _______________________|________\\_______________");
            System.out.println(" <                                                \\____________");
            System.out.println("  \\                          "+STARTSTRING+"              \\   _");
            System.out.println("   \\                         "+RULESTRING+"       \\ (_)");
            System.out.println("   \\                         "+OPTIONSTRING+"              >=)");
            System.out.println("    \\   O       O       O    "+QUITSTRING+"                  >=)");
            System.out.println("     \\__________________________________________________________/ (_)");
            System.out.println("\n                            ___");
            System.out.println("                           / o \\");
            System.out.println("                      __   \\   /   _");
            System.out.println("                        \\__/ | \\__/ \\");
            System.out.println("                       \\___//|\\\\___/\\");
            System.out.println("                        ___/ | \\___");
            System.out.println("                             |     \\");
            System.out.println("                            /");	
            break;
         case 3:
            System.out.println("                                   |__");
            System.out.println("                                     |\\/");
            System.out.println("                                     ---");
            System.out.println("                                     / | [");
            System.out.println("                              !      | |||");
            System.out.println("                            _/|     _/|-++'");
            System.out.println("                        +  +--|    |--|--|_ |-");
            System.out.println("                     { /|__|  |/\\__|  |--- |||__/");
            System.out.println("                    +---------------___[}-_===_.'____                 /\\");
            System.out.println("                ____`-' ||___-{]_| _[}-  |     |_[___\\==--            \\/   _");
            System.out.println(" __..._____--==/___]_|__|_____________________________[___\\==--____,-------' .7");
            System.out.println("|                                                                          BB-61/");
            System.out.println("|		       	      "+WELCOME+"	         	       /");
            System.out.println("|			       "+STARTSTRING+"        	              /");
            System.out.println("|		            "+RULESTRING+"                     /");
            System.out.println("|		               "+OPTIONSTRING+"                      /");
            System.out.println("|			        "+QUITSTRING+"       	           /");
            System.out.println(" \\________________________________________________________________________|");					
            break;
      }
   }
	
	/*====================================================================
	|instructions()																		|
	|--------------------------------------------------------------------|
	|displays the instructions on how to play Battleship						|
	====================================================================*/
   public static void instructions()
   {
      System.out.println("Instructions For Battleship");
      System.out.println("==========================================================================================");
      System.out.println("Objective");
      System.out.println("Be the first to sink your opponent's 5 ships before they sink all your ships.");
      System.out.println("==========================================================================================");
      System.out.println("Symbols                                           Texture/Non=textured");
      System.out.println("------------------------------------------------------------------------------------------");
      System.out.println("Text:                           Graphical:           Ship/gray tile");
      System.out.println("DESTROYER:        D             DESTROYER:               D");
      System.out.println("CRUISER:          C             CRUISER:                 C");
      System.out.println("SUBMARINE:        S             SUBMARINE:               S");
      System.out.println("BATTLESHIP:       B             BATTLESHIP:              B");
      System.out.println("AIRCRAFT CARRIER: A             AIRCRAFT CARRIER:        A");
      System.out.println("EMPTY OCEAN:      -             EMPTY OCEAN:         cyan tile");
      System.out.println("HIT:              X             HIT:       explosion tile/red tile");
      System.out.println("MISS:             O             MISS:          water tile/dark blue tile");
      System.out.println("==========================================================================================");
      System.out.println("Preparing For Battle ");
      System.out.println("Secretly place your ships on your grid. Ships may be placed horizontally or vertically but");
      System.out.println("not diagonally. They must all fit within the board and cannot overlap with one another. Sizes");
      System.out.println("of the board and individual ships can be changed under OPTIONS in the main menu.");
      System.out.println("Choose a playing mode and get started!");
      System.out.println("==========================================================================================");
      System.out.println("How To Play: ");
      System.out.println("For best experience, have the \"Run I/O\" window open largest on the bottom of the game window");
      System.out.println("The text will inform you whether the shot was a hit, miss, or error.");
      System.out.println("\nText Version:  ");
      System.out.println("Your attack board will be displayed on the right grid and your ship board will be displayed on");
      System.out.println("the left grid. On your turn, guess the placement of the computer's ships and take a shot by");
      System.out.println("entering the X and Y coordinates on your keyboard. The computer will then take a shot at your");
      System.out.println("ships. Hits and misses will be updated each turn.");
      System.out.println("\nGraphics Version: ");
      System.out.println("In the graphics window, your ship board will be displayed on the left and your attack board");
      System.out.println("displayed on the right. On your turn, guess the placement of your computer's ships and take a");
      System.out.println("shot by clicking a spot on your attack board. The computer will then take a shot at your ships.");
      System.out.println("Hits and misses will be updated each turn.");
      System.out.println("===========================================================================================");
      System.out.println("Enter anything to return to main menu...");
      sc.nextLine();
      System.out.println("Have fun!");
   }
   /*====================================================================
	|options()													      					|
	|--------------------------------------------------------------------|
	|allows user to customize the values of the game      					|
	====================================================================*/
   public static void options()
   {//allow user to change certain values of the game
      char change;
      int input;
      do
      {
         System.out.println("OPTIONS");
         System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
         System.out.println("a)    Board size("+BattleShip.boardSize+")");
         System.out.println("b)    Huge ship size("+BattleShip.huge+")");
         System.out.println("c)    Big ship size("+BattleShip.big+")");
         System.out.println("d)    Medium ship size("+BattleShip.medium+")");
         System.out.println("e)    Small ship size("+BattleShip.small+")");
         System.out.println("f)    Turn on/off textures("+Draw.textures+")");
         System.out.println("g)    Turn on/off graphics mode ("+graphics+")");
         System.out.println("NOTE: graphics need to be enabled in order to view textures.");
         System.out.println("Other)Exit to main menu");
      
         System.out.print("Which value would you like to change?");
         change=sc.nextLine().toLowerCase().charAt(0);//"allow" for uppercase input
         switch(change)
         {
            case 'a'://boardSize is changed
               do
               {
                  System.out.println("What will the new board dimensions be?");
                  input=Integer.parseInt(sc.nextLine());
                  if(input<1+Math.max(Math.max(BattleShip.huge,BattleShip.big),Math.max(BattleShip.medium,BattleShip.small))//dimensions must be >=largest ship +1 ensures for vertical and horizontal placement
                     ||Math.pow(input,2)<BattleShip.huge+BattleShip.big+2*BattleShip.medium+BattleShip.small)//checks if dimensions are big enough for ships
                  {
                     System.out.println("Invalid size! Must be able to hold your ships and then some or else it is too easy ;)");
                  }
               }
               while(input<=0);
               BattleShip.boardSize=input;
               break;
            //Ship sizes are changed
            case'b':
               do
               {
                  System.out.println("What will the new HUGE size be?");
                  input=Integer.parseInt(sc.nextLine());
                  if(input<=0||input>BattleShip.boardSize/2)//the half board check is for the computer to not be stuck in an infinite loop from generating stuck ships
                  {
                     System.out.println("Invalid size!");
                  }
               }
               while(input<=0||input>BattleShip.boardSize/2);
               BattleShip.huge=input;
               break;
            case'c':
               do
               {
                  System.out.println("What will the new BIG size be?");
                  input=Integer.parseInt(sc.nextLine());
                  if(input<=0||input>BattleShip.boardSize/2)
                  {
                     System.out.println("Invalid size!");
                  }
               }
               while(input<=0||input>BattleShip.boardSize/2);
               BattleShip.big=input;
               break;
            case'd':
               do
               {
                  System.out.println("What will the new MEDIUM size be?");
                  input=Integer.parseInt(sc.nextLine());
                  if(input<=0||input>BattleShip.boardSize/2)
                  {
                     System.out.println("Invalid size!");
                  }
               }
               while(input<=0||input>BattleShip.boardSize/2);
               BattleShip.medium=input;
               break;
            case'e':
               do
               {
                  System.out.println("What will the new SMALL size be?");
                  input=Integer.parseInt(sc.nextLine());
                  if(input<=0||input>BattleShip.boardSize/2)
                  {
                     System.out.println("Invalid size!");
                  }
               }
               while(input<=0||input>BattleShip.boardSize/2);
               BattleShip.small=input;
               break;
            case'f':
            
               Draw.textures=!Draw.textures;//toggle textures
               System.out.println("Textures are now "+((Draw.textures)?"enabled":"disabled"));
               break;
            case'g':
            
               graphics=!graphics;//toggle graphics
               System.out.println("Graphics are now "+((graphics)?"enabled":"disabled"));
               break;
         }
      }
      while(change>='a' && change <= 'g');//while changing variables
   }
}