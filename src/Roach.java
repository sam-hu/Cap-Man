import java.awt.Color;
import java.util.ArrayList;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Bug;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

public class Roach extends Bug {
	private int age;
	public boolean onCap;

	public Roach() {
		setColor(Color.YELLOW);
		age = 0;
		onCap = false;
	}

	public void act() {
		Location tempLoc = getLocation();
		Grid<Actor> gr = getGrid();
		if (age == 20) {
			SmartRoach smartroach = new SmartRoach();
			smartroach.putSelfInGrid(gr, tempLoc);
			smartroach.act();
		} else {
			if (canTurn()) {
				ArrayList<Location> moveLocs = getMoveLocations();
				Location loc = selectMoveLocation(moveLocs);
				setDirection(getLocation().getDirectionToward(loc));
			}
			if (canMove()) {
				move();
				if (onCap) {
					Cap cap = new Cap();
					cap.putSelfInGrid(gr, tempLoc);
				}
			} else
				setDirection(getDirection() + 180);
			Location next = getLocation().getAdjacentLocation(getDirection());
			if (!getGrid().isValid(next))
				wrapAround();
			age++;
		}
	}

	public void move() {
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return;
		Location loc = getLocation();
		Location next = loc.getAdjacentLocation(getDirection());
		Actor tempActor = gr.get(next);
		if (!(tempActor instanceof Boy) || !Boy.powerUpActive) {
			if (tempActor instanceof Roach)
				setDirection(getDirection() + 180);
			else if (tempActor instanceof Cap)
				onCap = true;
			else
				onCap = false;
			moveTo(next);
		} else if (tempActor instanceof Boy && Boy.powerUpActive) {
			Boy.setScore(Boy.getScore() + 10);
			removeSelfFromGrid();
		}
	}

	public boolean canMove() {
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return false;
		Location loc = getLocation();
		Location next = loc.getAdjacentLocation(getDirection());
		if (!gr.isValid(next))
			return false;
		return (!(gr.get(next) instanceof Rock)
				&& !(gr.get(next) instanceof Roach)
				&& !(gr.get(next) instanceof SmartRoach) && !(gr.get(next) instanceof Spawner));
	}

	public boolean canTurn() {
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return false;
		Location loc = getLocation();
		Location right = loc.getAdjacentLocation(getDirection() + 90);
		Location left = loc.getAdjacentLocation(getDirection() - 90);
		if (!gr.isValid(left) || !gr.isValid(right))
			return false;
		return (!(gr.get(left) instanceof Rock) || !(gr.get(right) instanceof Rock));
	}

	public ArrayList<Location> getMoveLocations() {
		ArrayList<Location> locs = new ArrayList<Location>();
		int[] dirs = { 0, 90, 270 };
		for (Location loc : getLocationsInDirections(dirs)) {
			Actor neighbor = getGrid().get(loc);
			if (!(neighbor instanceof Rock) && !(neighbor instanceof Roach)
					&& !(neighbor instanceof SmartRoach))
				locs.add(loc);
		}
		return locs;
	}

	public ArrayList<Location> getLocationsInDirections(int[] directions) {
		ArrayList<Location> locs = new ArrayList<Location>();
		Grid<?> gr = getGrid();
		Location loc = getLocation();
		for (int d : directions) {
			Location neighborLoc = loc.getAdjacentLocation(getDirection() + d);
			if (gr.isValid(neighborLoc))
				locs.add(neighborLoc);
		}
		return locs;
	}

	public Location selectMoveLocation(ArrayList<Location> locs) {
		int n = locs.size();
		if (n == 0)
			return getLocation();
		int r = (int) (Math.random() * n);
		return locs.get(r);
	}

	public void wrapAround() {
		Grid<Actor> gr = getGrid();
		Location loc = getLocation();
		Location next = loc.getAdjacentLocation(getDirection());
		int nextrow = next.getRow(), nextcol = next.getCol();
		if (nextrow > gr.getNumRows() - 1 || nextrow < 0) {
			nextrow = Math.abs((gr.getNumRows()) - (Math.abs(nextrow)));
		}
		if (nextcol > gr.getNumCols() - 1 || nextcol < 0) {
			nextcol = Math.abs((gr.getNumCols()) - (Math.abs(nextcol)));
		}
		Location wraparound = new Location(nextrow, nextcol);
		if (!(gr.get(wraparound) instanceof Roach)
				&& !(gr.get(wraparound) instanceof Rock))
			moveTo(wraparound);
	}
}
