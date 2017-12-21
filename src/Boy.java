import java.awt.Color;
import info.gridworld.actor.Actor;
import info.gridworld.actor.Rock;
import info.gridworld.grid.Grid;
import info.gridworld.grid.Location;

public class Boy extends Actor implements KeyboardControllable {
	private static int score;
	static boolean powerUpActive;
	static boolean powerUpNext;
	private int steps;
	private int duration;
	private Color color;

	public Boy() {
		score = 0;
		powerUpActive = false;
		steps = 0;
		duration = 20;
		color = getColor();
	}

	public static int getScore() {
		return score;
	}

	public static void setScore(int score) {
		Boy.score = score;
	}

	public void actionToPerform(String description) {
		if (description.equals("W"))
			setDirection(0);
		else if (description.equals("S"))
			setDirection(180);
		else if (description.equals("A"))
			setDirection(270);
		else if (description.equals("D"))
			setDirection(90);
	}

	public void act() {
		if (steps > duration)
			powerUpActive = false;
		if (!powerUpActive) {
			setColor(color);
			if (canMove())
				move();
			else
				wrapAround();
		} else {
			setColor(Color.RED);
			if (canMove())
				powerUpMove();
			else
				wrapAround();
			steps++;
		}
	}

	public void move() {
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return;
		Location loc = getLocation();
		Location next = loc.getAdjacentLocation(getDirection());
		if (gr.isValid(next)) {
			if (getGrid().get(next) instanceof Cap)
				score++;
			else if (getGrid().get(next) instanceof PowerUp) {
				powerUpNext = true;
				steps = 0;
			} else if (getGrid().get(next) instanceof ScoreBoost)
				score+=10;
			else if (getGrid().get(next) instanceof Roach
					|| getGrid().get(next) instanceof SmartRoach)
				removeSelfFromGrid();
			else {
				powerUpNext = false;
			}
			moveTo(next);
		}
	}

	public void powerUpMove() {
		Grid<Actor> gr = getGrid();
		if (gr == null)
			return;
		Location loc = getLocation();
		Location next = loc.getAdjacentLocation(getDirection());
		if (gr.isValid(next)) {
			if (getGrid().get(next) instanceof Cap)
				score++;
			else if (getGrid().get(next) instanceof PowerUp)
				steps = 0;
			else if (getGrid().get(next) instanceof ScoreBoost)
				score+=10;
			else if (getGrid().get(next) instanceof Roach
					|| getGrid().get(next) instanceof SmartRoach)
				score += 5;
			moveTo(next);
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
		return (!(gr.get(next) instanceof Rock) && (!(gr.get(next) instanceof Spawner)));
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