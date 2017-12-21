import info.gridworld.actor.Actor;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

import java.awt.Color;
import java.util.ArrayList;

public class SmartRoach extends Roach {
	private boolean onCap;
	private double breedChance;
	private int lifespan;
	private int age;

	public SmartRoach() {
		onCap = false;
		lifespan = 30;
		age = 0;
		breedChance = 0.05;
		setColor(Color.BLUE);
	}

	public void act() {
		Location tempLoc = getLocation();
		Grid<Actor> gr = getGrid();
		Location next = getLocation().getAdjacentLocation(getDirection());
		if (canTurn() && !(getPlayerLocation() == null)) {
			int loc = getLocation().getDirectionToward(getPlayerLocation());
			if (loc % 90 != 0) {
				loc = loc + 45;
				setDirection(loc);
			} else {
				ArrayList<Location> moveLocs = getMoveLocations();
				Location moveloc = selectMoveLocation(moveLocs);
				setDirection(getLocation().getDirectionToward(moveloc));
			}
		}
		if (canMove()) {
			move();
			if (onCap) {
				Cap cap = new Cap();
				cap.putSelfInGrid(gr, tempLoc);
			}
		} else if (!getGrid().isValid(next))
			wrapAround();
		if (Math.random() < breedChance)
			breed(tempLoc);
		age++;
		if (age > lifespan)
			removeSelfFromGrid();
	}

//	public void move() {
//		Grid<Actor> gr = getGrid();
//		if (gr == null)
//			return;
//		Location loc = getLocation();
//		Location next = loc.getAdjacentLocation(getDirection());
//		Actor tempActor = gr.get(next);
//		if (gr.isValid(next)) {
//			if (tempActor instanceof Roach)
//				setDirection(getDirection() + 180);
//			else if (tempActor instanceof Cap)
//				onCap = true;
//			else
//				onCap = false;
//			moveTo(next);
//		}
//	}

	public Location getPlayerLocation() {
		Grid<Actor> gr = getGrid();
		for (int i = 0; i < gr.getNumRows(); i++)
			for (int j = 0; j < gr.getNumCols(); j++) {
				Location temp = new Location(i, j);
				if (gr.get(temp) instanceof Boy)
					return temp;
			}
		return null;
	}

	public void breed(Location temp) {
		Grid<Actor> gr = getGrid();
		Roach roach = new Roach();
		roach.putSelfInGrid(gr, temp);
		roach.setDirection(this.getDirection() + 180);
		if (onCap)
			roach.onCap = true;
	}
}
