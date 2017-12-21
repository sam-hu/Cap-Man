/*
 * Written By: Sam Gross & Thomas Dorsey
 * 
 * This class is the code for a bug that responds to the keys W, A, S,
 * & D. 
 * 
 * This code can be modified to respond to any keys from the main
 * section of the keyboard, including the block above the arrow keys.
 * The spacebar, windows key, and menu key are not usable, nor are any
 * combinations of the control key. 
 * 
 * This class, and the others contaiend within the package 
 * "KeyBugDistro" may not be sold or commercialized in any way. If
 * published, attribution must be made to the authors of the work.
 * Do not remove this message when modifying or distributing this code.
 */
import java.awt.Color;

import info.gridworld.actor.Bug;

public class WASDKeyBug extends Bug implements KeyboardControllable
{
	public void actionToPerform(String description)
	{
		if(description.equals("W"))
			setDirection(0);
		else if(description.equals("S"))
			setDirection(180);
		else if(description.equals("A"))
			setDirection(270);
		else if(description.equals("D"))
			setDirection(90);
		else if (description.equals("1"))
			setColor(Color.cyan);
	}
}