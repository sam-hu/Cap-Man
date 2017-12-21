import java.util.ArrayList;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

public class PowerUp extends Collectable {

	private static int spawnChance;

	public PowerUp() {
		spawnChance = 5;
	}

	public void act() {
		if (Boy.powerUpNext)
			takeEffect();
	}

	public static double getSpawnChance() {
		return spawnChance;
	}

	public void setSpawnChance(int sc) {
		spawnChance = sc;
	}

	public ArrayList<Location> getSpawnLocations() {
		ArrayList<Location> locs = new ArrayList<Location>();
		Grid<Actor> gr = getGrid();
		for (int i = 0; i < gr.getNumRows(); i++)
			for (int j = 0; j < gr.getNumCols(); j++) {
				Location temp = new Location(i, j);
				if (!(gr.get(temp) instanceof Boy)
						&& !(gr.get(temp) instanceof Rock)
						&& !(gr.get(temp) instanceof Roach)
						&& !(gr.get(temp) instanceof SmartRoach))
					locs.add(temp);
			}
		return locs;
	}

	public void takeEffect() {
		Boy.powerUpActive = true;
	}
}
