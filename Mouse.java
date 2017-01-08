/*====================================================================
|File Name:				Mouse.java													|
|Class:					ICS3U															|
|Date Last Modified:	10/06/15														|
|Description:			Manages mouse input              					|
|===================================================================*/
import java.awt.event.*;
public class Mouse implements MouseListener
{
   static int click=0;
   /*====================================================================
   |mouseClicked(MouseEvent e)   													|
	|--------------------------------------------------------------------|
   |e    -details about the mouse when it was clicked                   |
	|--------------------------------------------------------------------|
	|Uses mouse input to proccess a turn each time the user does a valid |
   |shot by clicking or surrrender if user clicks the surrender button 	|
   |or display appropiate message otherwise                             |
	====================================================================*/
	public void mouseClicked(MouseEvent e)//Mouse clicked
	{ 
 

      if(BattleShip.run)
      {  //record the coordinates of the click 
   		int x=e.getX()-8;//to adjust for difference in mouse Coords and window Coords
   		int y=e.getY()-30;
         //convert coordinates of the click into addresses on the array
			int boardY=y/(Draw.height/BattleShip.boardSize);//Y-coordinate of the array
   		int boardX=(x-Draw.width*2)/(Draw.width/BattleShip.boardSize);//X-coordinate of the array
			
         //check if surrender button was clicked
   		if(x>=Board.WIDTH/2-Draw.width/BattleShip.boardSize
               &&x<=Board.WIDTH/2-Draw.width/BattleShip.boardSize+2*Draw.width/BattleShip.boardSize
   					&&y<=Draw.height/BattleShip.boardSize
                  	&&y>=0)
         {
            BattleShip.run=false;//end game
            BattleShip.playerWon=false;//red background
   		   System.out.println("Player has surrendered.");
   
         }
         //if player clicked on their own board
         else if(x<=Draw.width&&y<Draw.height)//if player attacks their own board
         {
         
            System.out.println("Error: You can't attack yourself!");
         }
         //if enemy board was clicked
			else if(boardY>=0	&&boardY<BattleShip.boardSize
					&&boardX>=0	&&boardX<BattleShip.boardSize)
			{
            //if that coordinate was already attacked
   			if(BattleShip.player[2][boardY][boardX]==BattleShip.HIT
   				||BattleShip.player[2][boardY][boardX]==BattleShip.MISS)//if already  attacked
				{
					System.out.println("Error: You have already guessed here!");
				}
				else if(x<=Board.WIDTH&&x>=Draw.width*2&&y<=Draw.height&&y>=0)
				{  
               //players turn
			  		BattleShip.X=boardX;
			  		BattleShip.Y=boardY;
			  		System.out.print("You have guessed ("+BattleShip.X+","+BattleShip.Y+")");
			      BattleShip.soundEffects(1);//play attack FX
			      BattleShip.checkShips();//check for sunken ships
			      BattleShip.checkWon();//check for a victory
					//computers turn (only if player has not won  yet)
               if(BattleShip.run)
					{
					 	BattleShip.turn++;//next turn
					   BattleShip.user=BattleShip.turn%2;
					   BattleShip.board=(BattleShip.user==0)?2:0;//changes which board gets changed by the turn
					 	BattleShip.guessComputer(BattleShip.difficulty);//guess based on difficulty level
					   BattleShip.checkShips();//check for sunken ships
					   BattleShip.checkWon();//check for a victory
					   BattleShip.turn++;
					   BattleShip.user=BattleShip.turn%2;
					   BattleShip.board=(BattleShip.user==0)?2:0;
						System.out.println("Guess the enemies' coordinates.");
					}
				}
         }
		}
      //gameover screen input
      else
      {

         Board.f.setVisible(false);  //if game over click to dispose window
         int menuNum=(int)(Math.random()*4);//random ship menu generator
         Welcome.ship(menuNum);
      }
      Board.f.repaint();//redraw the screen

   }
	public void mouseEntered(MouseEvent e)//Mouse entered window
	{}
	public void mousePressed(MouseEvent e)//Mouse button down
	{}
	public void mouseReleased(MouseEvent e)//Mouse button down ->up
	{}
   public void mouseExited(MouseEvent e)//mouse exit window
	{}
	
}