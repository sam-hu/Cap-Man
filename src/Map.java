/*
 * Do not change this class.
 * If you do, it may break all keyboard-based controls that you have
 * written for other programs.
 */
import java.util.ArrayList;

import info.gridworld.actor.Actor;
import info.gridworld.actor.ActorWorld;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

public class Map extends ActorWorld {
	public Map() {
		super();
	}

	public Map(Grid<?> g) {
		super();
	}

	public boolean keyPressed(String description, Location loc) {
		Grid<Actor> gr = getGrid();
		ArrayList<Actor> actors = new ArrayList<Actor>();
		for (Location location : gr.getOccupiedLocations())
			actors.add(gr.get(location));
		for (Actor a : actors)
			if (a.getGrid() == gr && a instanceof KeyboardControllable)
				((KeyboardControllable) a).actionToPerform(description);
		return true;
	}

	public void runGame() {
		setMessage("Score: " + Boy.getScore());
	}

	public void spawnCaps() {
		Grid<Actor> gr = getGrid();
		for (int i = 0; i < gr.getNumRows(); i++)
			for (int j = 0; j < gr.getNumCols(); j++) {
				Location temp = new Location(i, j);
				if (!(gr.get(temp) instanceof Boy)
						&& !(gr.get(temp) instanceof Rock)
						&& !(gr.get(temp) instanceof Roach)
						&& !(gr.get(temp) instanceof SmartRoach)
						&& !(gr.get(temp) instanceof Spawner)
						&& !(gr.get(temp) instanceof PowerUp)
						&& !(gr.get(temp) instanceof ScoreBoost)) {
					Cap cap = new Cap();
					cap.putSelfInGrid(gr, temp);
				}
			}
	}

	public void clearCaps() {
		Grid<Actor> gr = getGrid();
		for (int i = 0; i < gr.getNumRows(); i++)
			for (int j = 0; j < gr.getNumCols(); j++) {
				Location temp = new Location(i, j);
				Actor tempActor = gr.get(temp);
				if (tempActor instanceof Cap)
					tempActor.removeSelfFromGrid();
			}
	}

	public int getNumCaps() {
		Grid<Actor> gr = getGrid();
		int count = 0;
		ArrayList<Location> locs = gr.getOccupiedLocations();
		for (Location a : locs) {
			if (gr.get(a) instanceof Cap)
				count++;
		}
		return count;
	}
}