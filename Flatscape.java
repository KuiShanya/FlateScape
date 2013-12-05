import java.awt.Font;
import java.io.*;
import java.util.*;
public class FlatSpace { 
	public static void main(String[] args) throws InterruptedException {
		/* FlatSpace Alpha Version 1.9.9 -Beta once HD textures for powerups and opening are done
		   Orignally done by the Prinecton Programming webstie in flash, now adapted to java
		   Credit to bleach and tim for various help and ideas for the program
		   Things to add:
		    -Different enemies
		    -More Textures
		    -Chaging difficulty
		    -Longer high score list, with names
		    -Final HD textures
		    Bugs:
		     -IMPORTANT Seemingly after level 11 when delay is 0, earliest happened is 12, 
		      the game will crash because the array that controls the powerup type and timer does not exist
		     -Health may not be completely working
		     -Make Squares not inside each other
		     -Squares still stuck at the bottom of the screen
		*/
		String version = "Alpha Version 1.9.9";
		
		//Sets the pictures used
		String[] pictures = new String[3];
		pictures[0] = "Default/FixedTriangle.png";
		pictures[1] = "Default/smallSquare.png";
		pictures[2] = "Default/Square.png";
		
		// set the scale of the coordinate system
		StdDraw.setXscale(-1.0, 1.0);
		StdDraw.setYscale(-1.0, 1.0);
		
		//End equaling true terminates the game, skip used for credits and menu
		boolean end = false;
		boolean skip = false;
		
		//Gives you the option to skip or watch the credits
		StdDraw.picture(0, 0, "Choose.png", 2.2, 2.2);
		while (!StdDraw.isKeyPressed(10) && !StdDraw.isKeyPressed(8)) {}
		if(StdDraw.isKeyPressed(8)) skip = true;
		else {}
		
		//Credits
		while (!skip) {
			StdDraw.picture(0, 0, "Java.png", 2.2, 2.2);
			Thread.sleep(3000);
			StdDraw.picture(0, 0, "Ready.png", 2.2, 2.2);
			Thread.sleep(1000);
			StdDraw.picture(0, 0, "Red.png", 2.2, 2.2);
			Thread.sleep(500);
			StdDraw.picture(0, 0, "Yellow.png", 2.2, 2.2);
			Thread.sleep(500);
			StdDraw.picture(0, 0, "Title.png", 2.2, 2.2);
			Thread.sleep(3000);
			skip = true;
			}	
		
		// initial values 
		while (true) {
		double startTime = 0; //Established when game starts
		Scanner in = null;
		try {in = new Scanner(new File("High Score.txt"));}  //Imports high score
		catch (FileNotFoundException e1) {e1.printStackTrace();}
		double rx  = .48; double ry = .86;   // position (character)
		double randX = 0; double randY = 0;  // position (square)
		ArrayList<double[]> bp = new ArrayList<double[]>();  // position 
		ArrayList<double[]> bv = new ArrayList<double[]>();  // velocity
		ArrayList<Double> sType = new ArrayList<Double>(); //Random variable that chooses if the square is big or small
		ArrayList<Integer> health = new ArrayList<Integer>(); //Health of the large squares
		double[] Pos = null; // These 4 arrays are used in varying ways, usally to take a specific instance of the object
		double[] Vel = null; 
		double[] sPos = null;  
		double[] sVel = null; 
		double[] pPos = null; //Extra array needed for powerups due to glitchy java
		double vx = 0, vy = 0;     // velocity (character)
		double TriAngle = 0;  // angle of the triangle
		double velocity = 0.028; // speed of the bullet
		double sVelocity = 0.003; // squares velocity
		double randSqr = 0;//Indiviual size of the square
		double angle = 0; double adj = 0; double opp = 0; //used for determining velocity
		int delay = 0; int squareDelay = 0; int diffDelay = 10;  //Delays for spawning, can be changed based on level or difficulty
		int level = 1; int count = 0; 	int score = 0; int highScore = in.nextInt();// Level, score and count to dispaly levelup, and high score
		boolean invul = false; int invulCount = 0; int offInvul = 0; //invulnerbility cheat
		int powerup = 0; boolean[] powerType = new boolean[2]; //Powerup stuff
		int f3count = 0; int f3offcount = 0; boolean f3 = false; int ents = 0; //Debug menu
		int degree = 0; 
		int FPS = 0; int FPScount = 0;
		boolean Inv = true;
		
		//Waits on ending screen
		if (end) {
			while(!StdDraw.isKeyPressed(27)) {}
			end = false;
			}
		
		
		//Main Menu
		skip = false;
		while (!skip) {
			
			//Title Screen	
			StdDraw.picture(0, 0, "StartScreen.jpg", 2.2, 2.2);
			StdDraw.show(0);
			while (!StdDraw.isKeyPressed(10) && !StdDraw.isKeyPressed(112) && !StdDraw.isKeyPressed(113) && !StdDraw.isKeyPressed(114) && !StdDraw.isKeyPressed(115)) {}
			
			//Start Game
			if (StdDraw.isKeyPressed(10)) {skip = true;}
			
			//Inventory
			else if (StdDraw.isKeyPressed(112)) {
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledRectangle(0, 0, 1.1, 1.1);
				StdDraw.setPenColor(StdDraw.WHITE);
				StdDraw.text(0, .7, "Inventory");
				StdDraw.text(0, .5, "1: JoyStick");
				StdDraw.text(0, .4, "2: Bad Drow Hat");
				StdDraw.text(0, .3, "3: Old Valve Sale Announcment");
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.text(0, -1, "Press Esc to return to Main Menu");
				StdDraw.show(0);
				while (Inv) {
					if (StdDraw.isKeyPressed(49)) {
						pictures[0] = "Default/FixedTriangle.png";
						pictures[1] = "Default/smallSquare.png";
						pictures[2] = "Default/Square.png";
						}
					if (StdDraw.isKeyPressed(50)) {
						pictures[0] = "Bad Drow/Drow.jpg";
						pictures[1] = "Bad Drow/Daedalus.png";
						pictures[2] = "Bad Drow/MantaStyle.png";
						}
					if (StdDraw.isKeyPressed(51)) {
						pictures[0] = "Steam Sale/Wallet.gif";
						pictures[1] = "Steam Sale/Discount.png";
						pictures[2] = "Steam Sale/HalfLife.png";
						}
					if (StdDraw.isKeyPressed(27)) {Inv = false;}
					}
				}
			
			//How to Play
			else if (StdDraw.isKeyPressed(113)) {
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledRectangle(0, 0, 1.1, 1.1);
				StdDraw.setPenColor(StdDraw.WHITE);
				StdDraw.text(0, .7, "How to Play");
				StdDraw.text(0, .6, "Your goal is to kill as many enemies as possible without dying");
				StdDraw.text(0, .5, "Bullets will shoot from your ship to the mouse");
				StdDraw.text(0, .4, "Click and hold to have your shp move towards your mouse");
				StdDraw.text(0, .3, "Kill enemies to level up");
				StdDraw.text(0, .2, "As you level up, the enemies are tougher but your weapons are better");
				StdDraw.text(0, .1, "Larger squares spawn less frequently but take more hits to kill");
				StdDraw.text(0, 0, "Watch for power ups to gain the edge");
				StdDraw.text(0, -.1, "Go to your inventory to try out the different skins");
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.text(0, -1, "Press Esc to return to Main Menu");
				StdDraw.show(0);
				while (!StdDraw.isKeyPressed(27)) {}
				}
			
			//High Score
			else if (StdDraw.isKeyPressed(114)) {
				StdDraw.setPenColor(StdDraw.BLACK);
				StdDraw.filledRectangle(0, 0, 1.1, 1.1);
				StdDraw.setPenColor(StdDraw.WHITE);
				StdDraw.text(0, .7, "High Score" );
				StdDraw.text(0, .6, " High Score: " + highScore);
				StdDraw.text(0, .5, "See if you can beat the record");
				StdDraw.setPenColor(StdDraw.RED);
				StdDraw.text(0, -1, "Press Esc to return to Main Menu");
				StdDraw.show(0);
				while (!StdDraw.isKeyPressed(27)) {}
				}
			
			//Quit
			else if (StdDraw.isKeyPressed(115)) {System.exit(0);}
			}
		
		double fpsStart = System.currentTimeMillis();
		
		// main animation loop
		while (!end)  { 
			
			//arrays set up for various things that emtpied if they werent redeclared every time
			double[] sFast = {10,1,4};
			double[] sRapid = {10,2,4};
			double[] sBig = {1,20,3};
			double[] sSmall = {0,20,3};
			
			//FPS counter
			FPScount++;
			if (System.currentTimeMillis() - fpsStart > 1000) {
				FPS = FPScount;
				FPScount = 0;
				fpsStart = System.currentTimeMillis();
				}
			
			//Loading the game
			if (startTime == 0) {
				StdDraw.setFont(new Font("SansSerif" , Font.PLAIN, 68));
				StdDraw.setPenColor(StdDraw.WHITE);
				for (int i = 3; i > 0; i--) {
					StdDraw.picture(0,0, "Background.png", 2.4, 2.4);
					StdDraw.picture(0,0, "Load.png", 2.4, 2.4);
					StdDraw.text(0,0,"" + i);
					StdDraw.show(0);
					Thread.sleep(1000);
					}
				startTime = System.currentTimeMillis();
				}
			StdDraw.setFont();
			
			// changes angle on character (Stolen from tim's program)
			TriAngle = Math.toDegrees(Math.atan((StdDraw.mouseY() - ry)/(StdDraw.mouseX() - rx))) - 90;
			if (StdDraw.mouseX() < rx) {TriAngle = TriAngle - 180;}
			
			// updates position, moving character towards mouse
			if (StdDraw.mouseX() != rx && StdDraw.mouseY() != ry && StdDraw.mousePressed()) {
				vx = (StdDraw.mouseX() - rx) / 30;
				vy = (StdDraw.mouseY() - ry) / 30;
				rx = rx + vx;
				ry = ry + vy; 
				}

			// Target drawn
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.circle(StdDraw.mouseX(), StdDraw.mouseY(), .05);

			// draw character on the screen
			StdDraw.picture(rx, ry, pictures[0],.1,.1,TriAngle);
			
			//Changes the direction of the bullet, based on the direction of the character (Revised by me, then bleach,
			//then me again, then bleach again)
			if (delay <= 0) { 
				if (diffDelay > 3) {
					delay = diffDelay;}
				else {
					delay = 3;
				}
				//Creates the velocity of the bullet, based on where the mouse is angle-wise
				adj = StdDraw.mouseX() - rx;
				opp = StdDraw.mouseY() - ry;
				angle = Math.atan(opp / adj);
				Pos = new double[]{rx,ry,1};
				Vel = new double[]{(Math.cos(angle) * velocity * (adj / Math.abs(adj))),(Math.sin(angle) * velocity * (adj / Math.abs(adj))),1};
				bp.add(Pos);
				bv.add(Vel);
				}
			
			//Creates the square, giving it a random place
			if (squareDelay <= 0 && bp.size() <=150) {
				squareDelay = diffDelay * 5;
				randX = Math.random() + Math.random() - 1;
				randY = Math.random() + Math.random() - 1;
				
				//Prevents blocks from spawning on your character
				while (rx + .1 >= randX && rx - .1 <= randX) {randX = Math.random() + Math.random() - 1;}
				while (ry + .1 >= randY && ry - .1 <= randY && randY < -.75) {randY = Math.random() + Math.random() - 1;}
				sType.add(Math.random());
				
				//Sets position and velocity, which goes toward the edge of the screen
				sPos = new double[]{randX,randY,2};
				if (Math.random() <= .5) {
					sVel = new double[]{sVelocity * (randX / Math.abs(randX)) , sVelocity * (randY / Math.abs(randY)), 2};
					}
				else {
					sVel = new double[]{Math.cos(Math.atan(ry - randY/rx - randX)) * ((rx - randX)/Math.abs(rx - randX)) * sVelocity , Math.sin(Math.atan(ry - randY/rx - randX)) * ((ry- randY)/Math.abs(ry - randY)) * sVelocity,2};
					}
				bp.add(sPos);
				bv.add(sVel);
				health.add(4);
					}
			
			 
			//Recreated array system for drawing all objects on the screen, done for more easy modulation, just change the number of instance in counts and add more arrays 
			int[] counts = new int[4];
			
			//Figures out how many of each type of object there are
			for (int i = 0; i <bp.size(); i++) {
				if (bp.get(i)[2] == 1) counts[0]++;
				else if (bp.get(i)[2] == 2) counts[1]++;
				else if (bp.get(i)[2] == 3) counts[2]++;
				else if (bp.get(i)[2] == 4) counts[3]++;
				}
			
			//Creates arrays
			double [][] posArray = new double[counts[0]][counts[0]];
			double [][] velArray = new double[counts[0]][counts[0]];
			double [][] sposArray = new double[counts[1]][counts[1]];
			double [][] svelArray = new double[counts[1]][counts[1]];
			double [][] explArray = new double[counts[2]][counts[2]];
			double [][] evelArray = new double[counts[2]][counts[2]];
			double [][] pposArray = new double[counts[3]][counts[3]];
			double [][] pvelArray = new double[counts[3]][counts[3]];
			
			//Builds arrays with correct object only
			while (bp.size() > bv.size()) {bp.remove(bp.size() -1);}
			while (bv.size() > bp.size()) {bv.remove(bv.size() -1);}
			counts = new int[4];
			for (int i = 0; i < bp.size(); i++) {
				if (bp.get(i)[2] == 1 && bv.get(i)[2] == 1) {
					posArray[counts[0]] = bp.get(i);
					velArray[counts[0]] = bv.get(i);
					counts[0]++;
					}
				else if (bp.get(i)[2] == 2 && bv.get(i)[2] == 2) {
					sposArray[counts[1]] = bp.get(i);
					svelArray[counts[1]] = bv.get(i);
					counts[1]++;
					}
				else if (bp.get(i)[2] == 3 && bv.get(i)[2] == 3) {
					explArray[counts[2]] = bp.get(i);
					evelArray[counts[2]] = bv.get(i);
					counts[2]++;
					}
				else if (bp.get(i)[2] == 4 && bv.get(i)[2] == 4) {
					pposArray[counts[3]] = bp.get(i);
					pvelArray[counts[3]] = bv.get(i);
					counts[3]++;
					}
				}
			
			//Saves entities size for later
			ents = bp.size();
					
			//Some way to make intergrated maybe?
			double [] stypeArray = new double[sType.size()];
			for (int i = 0; i < sType.size(); i++) {stypeArray[i] = sType.get(i);}
					
			//Clears bp and bv so the arrays can correctly have all current bullets added as well as sp and sv and stype array for squares
			bp.clear();
			bv.clear();
			sType.clear();
			
			//Creates sqaures (needs to be .15 or .04
			for (int i = 0; i < sposArray.length; i++) {
				sPos = sposArray[i];
				sVel = svelArray[i];
				randSqr = stypeArray[i];
				sPos[0] = sPos[0] + sVel[0];
				sPos[1] = sPos[1] + sVel[1];
				StdDraw.setPenColor(StdDraw.WHITE);
				if (randSqr < .8) {
					StdDraw.picture(sPos[0], sPos[1], pictures[1], .08, .084,degree);
					}
				else {StdDraw.picture(sPos[0], sPos[1], pictures[2], .2, .2,degree);}
				bp.add(sPos);
				bv.add(sVel);
				sType.add(randSqr);
				if (randSqr < .8) {
					if ((rx + .04 >= sPos[0] && rx - .04 <= sPos[0]) && (ry + .04 >= sPos[1] && ry - .04 <= sPos[1]) && !invul) {end = true;}
					}
				else {
					if ((rx + .1 >= sPos[0] && rx - .1 <= sPos[0]) && (ry + .1 >= sPos[1] && ry - .1 <= sPos[1]) && !invul) {end = true;}
					}
				if (Math.abs(sPos[0]) >= 1.1) {sVel[0] = -sVel[0];}
				if (sPos[1] >= 1.1 || sPos[1] <= -.8) {sVel[1] = -sVel[1];}
				}
			
			//Takes the two arrays, makes them one bullets x and y postion and velocity, and tells it to move and removes it if it hits the edge of the screen
			for (int i = 0; i < posArray.length; i++) {
				Pos = posArray[i];
				Vel = velArray[i];
				Pos[0] = Pos[0] + Vel[0];
				Pos[1] = Pos[1] + Vel[1];
				StdDraw.setPenColor(StdDraw.YELLOW);
				StdDraw.filledCircle(Pos[0], Pos[1], .009);
				bp.add(Pos);
				bv.add(Vel);
				for (int j = 0; j< pposArray.length; j++) {
					sPos = pposArray[j];
					sVel = pvelArray[j];
					if (sPos.length != 3) {continue;}
					if (sVel.length != 3) {continue;}
					if ((Pos[0] + .04 >= sPos[0] && Pos[0] - .04 <= sPos[0]) && (Pos[1] + .04 >= sPos[1] && Pos[1] - .04 <= sPos[1])) {
						sVel[0]--;
						bp.remove(Pos);
						bv.remove(Vel);
						}
				}
				
				//Checks if the bullet hits the edge of the screen
				if (Math.abs(Pos[0]) >=1.1 || Pos[1] >= 1.1 || Pos[1] <= -.8) {
					bp.remove(Pos);
					bv.remove(Vel);
					continue;
					}
				
				//Checks for hit detection of the bullet hits the square
				for (int j = 0; j < sposArray.length; j++) {
					sPos = sposArray[j];
					sVel = svelArray[j];
					randSqr = stypeArray[j];
					if (randSqr < .8) {
						
						//If it hits, remove the bullet and sqaure, add to the score
						if ((Pos[0] + .04 >= sPos[0] && Pos[0] - .04 <= sPos[0]) && (Pos[1] + .04 >= sPos[1] && Pos[1] - .04 <= sPos[1])) {
							bp.remove(sPos);
							bv.remove(sVel);
							bp.remove(Pos);
							bv.remove(Vel);
							sType.remove(randSqr);
							if (Math.random() < .05 && !powerType[0] && !powerType[1]) {
								pPos = new double[] {sPos[0],sPos[1],4};
								bp.add(pPos); 
								if (Math.random() < .5) bv.add(sRapid);
								else bv.add(sFast);
								}
							else {
								sPos[2] = 3;
								bp.add(sPos);
								bv.add(sSmall);
								}
							score++;
							continue;
							}
						}
					else {
						
						//Hit detection for larger squares
						if ((Pos[0] + .1 >= sPos[0] && Pos[0] - .1 <= sPos[0]) && (Pos[1] + .1 >= sPos[1] && Pos[1] - .1 <= sPos[1])) {
							
							//Only dies after 4 hits
							if (health.get(j) > 0) {
								bp.remove(Pos);
								bv.remove(Vel);
								health.set(j,health.get(j) - 1);
								}
							
							//Adds 10 points instead
							else {
								bp.remove(sPos);
								bv.remove(sVel);
								sType.remove(randSqr);
								health.remove(j);
								if (Math.random() < .05 && !powerType[0] && !powerType[1]) {
									pPos = new double[] {sPos[0],sPos[1],4};
									bp.add(pPos);
									if (Math.random() < .5) bv.add(sRapid);
									else bv.add(sFast);
									}
								else {
									sPos[2] = 3;
									bp.add(sPos);
									bv.add(sBig);
								}
								score = score + 10;
								}
							continue;
							}
						}
					}
				}
			
			//Powerups
			for (int j = 0; j < pposArray.length; j++) {
				sPos = pposArray[j];
				sVel = pvelArray[j];
				if (sVel.length != 3) {continue;}
				if (sVel[0] <= 0){
					powerType = new boolean[2];
					if (sVel[1] == 1) powerType[0] = true;
					else if (sVel[1] == 2) powerType[1] = true;
					bp.remove(sPos);
					bv.remove(sVel);
					powerup = 300;
					}
				else {
					if (sVel[1] == 1) {
						StdDraw.setPenColor(StdDraw.ORANGE);
						StdDraw.filledSquare(sPos[0],sPos[1], .04);
						}
					else if (sVel[1] == 2) {
						StdDraw.setPenColor(StdDraw.RED);
						StdDraw.filledSquare(sPos[0],sPos[1], .04);
						}
					bp.add(sPos);
					bv.add(sVel);
					}
				}
			
			//Cheats to add points
			if (StdDraw.isKeyPressed(33)) {score++;}
			
			//Confusing score system, level up is at 50 and then every 100's
			if (score%100 == 0 && score != 0) {
				diffDelay--;
				score++;
				level++;
				count = 40;
				}
			if (level < ((score - score%100) /100) + 2 && score > 100) {
				level++;
				diffDelay--;
				count = 40;
				}
			if (level < ((score - score%50) /50) + 1 && level == 1) {
				level++;
				diffDelay--;
				count = 40;
				}
			
			//For showing explosiosn and point values
			for (int i = 0; i < explArray.length; i++) {
				Pos = explArray[i];
				Vel = evelArray[i];
				if (Vel[0] == 1) {
					StdDraw.picture(Pos[0], Pos[1], "cartoon-explosion.jpg",.2,.2);
					StdDraw.setPenColor(StdDraw.CYAN);
					StdDraw.text(Pos[0],Pos[1] + .1, "+10" );
					}
				else {
					StdDraw.picture(Pos[0], Pos[1], "cartoon-explosion.jpg",.08,.08);
					StdDraw.setPenColor(StdDraw.CYAN);
					StdDraw.text(Pos[0],Pos[1] + .1, "+1" );
					}
				if (Vel[1] <= 0) {
					bp.remove(Pos);
					bv.remove(Vel);
					continue;
					}
				else{
					Vel[1]--;
					bp.add(Pos);
					bv.add(Vel);
					}
				}
			
			//Changes powerups make
			if (powerup > 0) {
				StdDraw.setPenColor(StdDraw.WHITE);
				if (powerType[0]) {
					velocity = .084;
					StdDraw.textRight(1.05,.8,"Fast Bullets Active");
					}
				else if (powerType[1])  {
					delay = 0; 
					velocity = .02;
					StdDraw.textRight(1.05,.8,"Rapid Fire Active");
					}
				}
			else {velocity = .028; powerType = new boolean[2];}
			
			//Shows level on each level up
			if (count != 0) {
				StdDraw.setFont(new Font("SansSerif" , Font.PLAIN, 68));
				StdDraw.setPenColor(StdDraw.CYAN);
				StdDraw.text(0, 0, "Level " + level);
				count--;
				}
			StdDraw.setFont();
			
			//counts for powerups
			if (powerup > 0) {powerup--;}
			
			degree++;
			if (degree >= 360) {degree = 0;}
			
			//Delays on how often it makes squares
			delay--;
			squareDelay--;
			
			//counts for invulnerbility cheat
			if (invulCount > 0) invulCount--; 
			if (offInvul >0) offInvul--;
			if (f3count > 0) f3count--;
			if (f3offcount > 0) f3offcount--;
			// Displays scoreboard
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.filledRectangle(0, -1, 1.5,.2);
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.textLeft(-1.05, -.9, "Score:" + score);
			StdDraw.textLeft(-1.05, -1, "High Score:" + highScore);
			StdDraw.textRight(1.05, -1, "Level " + level);
			StdDraw.textRight(1.05, 1, version );
			StdDraw.text(0, -1,(( (double)Math.round(((System.currentTimeMillis() - startTime) /1000) *10) /10) +" Seconds"));
			if (invul == true) StdDraw.textRight(1.05, .9, "Invulnerable");
			
			//F3 menu print
			if (f3 == true) {
				StdDraw.setPenColor(StdDraw.WHITE);
				StdDraw.textLeft(-1.05,1,"F3 Menu");
				StdDraw.textLeft(-.675, .9, "FPS: " + FPS);
				StdDraw.textLeft(-1.05,.9,"Entities: " + ents);
				StdDraw.textLeft(-1.05,.8,"Bullets:" + posArray.length);
				StdDraw.textLeft(-1.05,.7,"Squares:" + sposArray.length);
				StdDraw.textLeft(-1.05,.6,"Explosions:" + explArray.length);
				StdDraw.textLeft(-1.05,.5,"Power Ups:" + pposArray.length);
				}
			
			// Displays and sets the background
			StdDraw.show(0); 
			StdDraw.picture(0,0, "Background.png", 2.4, 2.4);
			
			//checks if the high score has been beaten
			if (score > highScore) highScore = score;
			
			//death cheat
			if (StdDraw.isKeyPressed(68)) end = true;
			
			//invulnerbility cheat
			if (StdDraw.isKeyPressed(83)) {
				if (!invul && offInvul == 0) {invul = true; invulCount = 10;}
				if (invul == true && invulCount == 0) {invul = false; offInvul = 10;}
				}
			
			//F3 menu
			if (StdDraw.isKeyPressed(114)) {
				if (!f3 && f3offcount == 0) {f3 = true; f3count = 10;}
				if (f3 == true && f3count == 0) {f3 = false; f3offcount = 10;}
				}
			
			}
		
		//Losing screen
		try {
			File outFile = new File("High Score.txt");
			if (outFile.exists()) {outFile.delete();}
			PrintWriter out = new PrintWriter(outFile);
			out.print(highScore);
			outFile.setReadOnly();
			out.close();
			}
		catch (FileNotFoundException e) {e.printStackTrace();}
		StdDraw.picture(0, 0, "loser.jpg", 2.2, 2.2);
		StdDraw.text(0, -.9, "Final Score: " + score);
		StdDraw.text(0, -1, "To Restart, Press Esc");
		StdDraw.show(0);
		} 
	}
} 
