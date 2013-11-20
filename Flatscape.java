import java.io.*;
import java.util.*;
public class Flatscape { 
	public static void main(String[] args) {
		/* Flatscape Alpha Version 1.6
		   Orignally done by the Prinecton Programming webstie in flash, now adapted to java
		   Credit to bleach and tim for various help and ideas for the program
		   Things to add:
		    -Full Opening
		    -Ability to restart once done
		    -Spinning squares
		    -Upgrades
		    -Different enemies
		    -Hats
		    -More random squares 
		    -Chaging difficulty
		    -Limiting Number of Entities on the screen
		    -Debug menu (entities, fps, postition)
		    -Hacks (enable invulnerability, instant death button)
		    -Longer high score list, with names
		*/
		// set the scale of the coordinate system
		StdDraw.setXscale(-1.0, 1.0);
		StdDraw.setYscale(-1.0, 1.0);

		// initial values 
		Scanner in = null;
		try {in = new Scanner(new File("High Score.txt"));} 
		catch (FileNotFoundException e1) {e1.printStackTrace();}
		double rx  = .48; double ry = .86;   // position (character)
		double randX = 0; double randY = 0;  // position (square)
		ArrayList<double[]> bp = new ArrayList<double[]>();  // position (bullet)
		ArrayList<double[]> bv = new ArrayList<double[]>();  // velocity (bullet)
		ArrayList<double[]> sp = new ArrayList<double[]>();  // position (square)
		ArrayList<double[]> sv = new ArrayList<double[]>();  // velocity (square)
		double[] Pos = null; // individual position of each bullet
		double[] Vel = null; // individual velocity of each bullet
		double[] sPos = null; // individual postiton of each square
		double[] sVel = null; // individual velocity of each square
		double vx = 0, vy = 0;     // velocity (character)
		double TriAngle = 0;  // angle of the triangle
		double velocity = 0.014; // speed of the bullet
		double sVelocity = 0.001; // squares velocity
		double angle = 0; double adj = 0; double opp = 0; //used for determining velocity
		int score = 0; int delay = 0; int squareDelay = 0; int diffDelay = 10;   int highScore = in.nextInt(); int level = 1; //Score, changes how fast bullets spawn, can be based on difficulty and level
		ArrayList<Double> sType = new ArrayList<Double>(); //Random variable that chooses if the square is big or small
		boolean end = false; //ends the program
		double randSqr = 0; //Indiviual size of the square
		
		StdDraw.picture(0, 0, "Title.png");
		// main animation loop
		while (!StdDraw.mousePressed()) {}
		while (!end)  { 

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
			StdDraw.setPenColor(StdDraw.BLUE); 
			StdDraw.picture(rx, ry, "FixedTriangle.png",.1,.1,TriAngle);
			
			//Changes the direction of the bullet, based on the direction of the character (Revised by me, then bleach,
			//then me again, then bleach again)
			if (delay <= 0) { 
				delay = diffDelay;
				//Creates the velocity of the bullet, based on where the mouse is angle-wise
				adj = StdDraw.mouseX() - rx;
				opp = StdDraw.mouseY() - ry;
				angle = Math.atan(opp / adj);
				Pos = new double[]{rx,ry};
				Vel = new double[]{(Math.cos(angle) * velocity * (adj / Math.abs(adj))),(Math.sin(angle) * velocity * (adj / Math.abs(adj)))};
				bp.add(Pos);
				bv.add(Vel);
				}
			
			//Creates the square, giving it a random place
			if (squareDelay <= 0) {
				squareDelay = diffDelay * 5;
				randX = Math.random() + Math.random() - 1;
				randY = Math.random() + Math.random() - 1;
				
				//Prevents blocks from spawning on your character
				while (rx + .15 >= randX && rx - .15 <= randX) {randX = Math.random() + Math.random() - 1;}
				while (ry + .15 >= randY && ry - .15 <= randY) {randY = Math.random() + Math.random() - 1;}
				sType.add(Math.random());
				
				//Sets position and velocity, which goes toward the edge of the screen
				sPos = new double[]{randX,randY};
				if (Math.random() <= .5) {
					sVel = new double[]{sVelocity * (randX / Math.abs(randX)) , sVelocity * (randY / Math.abs(randY))};
					}
				else {
					sVel = new double[]{Math.cos(Math.atan(ry - randY/rx - randX)) * ((rx - randX)/Math.abs(rx - randX)) * sVelocity , Math.sin(Math.atan(randY - ry/randX -rx)) * ((randY - ry)/Math.abs(randY - ry)) * sVelocity };
				}
				sp.add(sPos);
				sv.add(sVel);	
				}
			 
			//Creates two more arrays, each instance will carry two variables, represeting x and y positon and velocity
			double [][] posArray = bp.toArray(new double[0][0]);
			double [][] velArray = bv.toArray(new double[0][0]);
			double [][] sposArray = sp.toArray(new double[0][0]);
			double [][] svelArray = sv.toArray(new double[0][0]);
			double [] stypeArray = new double[sType.size()];
			for (int i = 0; i < sType.size(); i++) {
				stypeArray[i] = sType.get(i);
				}
					
			//Clears bp and bv so the arrays can correctly have all current bullets added as well as sp and sv and stype array for squares
			bp.clear();
			bv.clear();
			sp.clear();
			sv.clear();
			sType.clear();
			
			//Creates sqaures (needs to be .15 or .04
			for (int i = 0; i < sposArray.length; i++) {
				sPos = sposArray[i];
				sVel = svelArray[i];
				randSqr = stypeArray[i];
				sPos[0] = sPos[0] + sVel[0];
				sPos[1] = sPos[1] + sVel[1];
				StdDraw.setPenColor(StdDraw.GRAY);
				if (randSqr < .8) {
					StdDraw.square(sPos[0], sPos[1], .04);
					}
				else {StdDraw.square(sPos[0], sPos[1], .10);}
				sp.add(sPos);
				sv.add(sVel);
				sType.add(randSqr);
				if ((rx + .08 >= sPos[0] && rx - .08 <= sPos[0]) && (ry + .08 >= sPos[1] && ry - .08 <= sPos[1])) {end = true;}
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
				
				//Checks if the bullet hits the edge of the screen
				if (Math.abs(Pos[0]) >=1.2 || Pos[1] >= 1.2 || Pos[1] <= -.8) {
					bp.remove(Pos);
					bv.remove(Vel);
					continue;
					}
				//Checks for hit detection of the bllet hits the square
				for (int j = 0; j < sposArray.length; j++) {
					sPos = sposArray[j];
					sVel = svelArray[j];
					randSqr = stypeArray[j];
					if (randSqr < .8) {
						if ((Pos[0] + .04 >= sPos[0] && Pos[0] - .04 <= sPos[0]) && (Pos[1] + .04 >= sPos[1] && Pos[1] - .04 <= sPos[1])) {
							sp.remove(sPos);
							sv.remove(sVel);
							bp.remove(Pos);
							bv.remove(Vel);
							sType.remove(randSqr);
							score++;
							continue;
							}
						}
					else {
						if ((Pos[0] + .1 >= sPos[0] && Pos[0] - .1 <= sPos[0]) && (Pos[1] + .1 >= sPos[1] && Pos[1] - .1 <= sPos[1])) {
							sp.remove(sPos);
							sv.remove(sVel);
							bp.remove(Pos);
							bv.remove(Vel);
							sType.remove(randSqr);
							score = score + 10;
							continue;
							}
						}
					}	
				}
			
			if (score%50 == 0 && score != 0) {
				diffDelay--;
				score++;
				level++;
				}
			if (level < ((score - score%50) /50) + 1) {
				level++;
				diffDelay = diffDelay -2;
				}
			
			//Delays on how often it makes squares
			delay--;
			squareDelay--;
			
			// Displays scoreboard
			StdDraw.setPenColor(StdDraw.BLACK);
			StdDraw.filledRectangle(0, -1, 1.5,.2);
			StdDraw.setPenColor(StdDraw.WHITE);
			StdDraw.textLeft(-1, -.9, "Score:" + score);
			StdDraw.textLeft(-1, -1, "High Score:" + highScore);
			StdDraw.textRight(1, -1, "Level " + level);

			// Displays and sets the background
			StdDraw.show(0); 
			StdDraw.picture(0,0, "Background.png", 2.4, 2.4);
			
			if (score > highScore) {highScore = score;}
			}
		
		//Losing screen
		try {
			PrintWriter out = new PrintWriter("High Score.txt");
			out.print(highScore);
			out.close();
			}
		catch (FileNotFoundException e) {e.printStackTrace();}
		StdDraw.picture(0, 0, "loser.jpg", 2.5, 2.2);
		StdDraw.text(0, -.9, "Final Score: " + score);
		StdDraw.show(0);
		} 
	} 
