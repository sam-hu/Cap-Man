import java.util.ArrayList;

import info.gridworld.actor.Actor;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

public class Spawner extends Rock {
	public void act() {
		if (getNumEnemies() < 2)
			spawnRoach(getSpawnLocations());
		if (getNumPowerUps() < 6
				&& Math.random() * 100 < PowerUp.getSpawnChance())
			spawnPowerUp(getSpawnLocations());
		if (getNumScoreBoosts() < 6 && Math.random() * 100 < ScoreBoost.getSpawnChance())
			spawnScoreBoost(getSpawnLocations());
	}

	public void spawnRoach(ArrayList<Location> locs) {
		Grid<Actor> gr = getGrid();
		int n = locs.size();
		int r = (int) (Math.random() * n);
		Roach roach = new Roach();
		roach.putSelfInGrid(gr, locs.get(r));
	}

	public void spawnPowerUp(ArrayList<Location> locs) {
		Grid<Actor> gr = getGrid();
		int n = locs.size();
		int r = (int) (Math.random() * n);
		PowerUp powerup = new PowerUp();
		powerup.putSelfInGrid(gr, locs.get(r));
	}

	public void spawnScoreBoost(ArrayList<Location> locs) {
		Grid<Actor> gr = getGrid();
		int n = locs.size();
		int r = (int) (Math.random() * n);
		ScoreBoost scoreboost = new ScoreBoost();
		scoreboost.putSelfInGrid(gr, locs.get(r));
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
						&& !(gr.get(temp) instanceof SmartRoach)
						&& !(gr.get(temp) instanceof Spawner))
					locs.add(temp);
			}
		return locs;
	}

	public int getNumPowerUps() {
		int count = 0;
		Grid<Actor> gr = getGrid();
		for (int i = 0; i < gr.getNumRows(); i++)
			for (int j = 0; j < gr.getNumCols(); j++) {
				Location temp = new Location(i, j);
				if (gr.get(temp) instanceof PowerUp)
					count++;

			}
		return count;
	}

	public int getNumEnemies() {
		int count = 0;
		Grid<Actor> gr = getGrid();
		for (int i = 0; i < gr.getNumRows(); i++)
			for (int j = 0; j < gr.getNumCols(); j++) {
				Location temp = new Location(i, j);
				if (gr.get(temp) instanceof Roach
						|| gr.get(temp) instanceof SmartRoach)
					count++;
			}
		return count;
	}

	public int getNumScoreBoosts() {
		int count = 0;
		Grid<Actor> gr = getGrid();
		for (int i = 0; i < gr.getNumRows(); i++)
			for (int j = 0; j < gr.getNumCols(); j++) {
				Location temp = new Location(i, j);
				if (gr.get(temp) instanceof ScoreBoost)
					count++;
			}
		return count;
	}
}