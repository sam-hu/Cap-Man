public class ScoreBoost extends Collectable {

	private static int spawnChance;

	public ScoreBoost() {
		spawnChance = 5;
	}

	public void act() {
	}

	public static int getSpawnChance() {
		return spawnChance;
	}

	public void setSpawnChance(int sc) {
		spawnChance = sc;
	}

	public void takeEffect() {
		Boy.setScore(Boy.getScore() + 10);
	}
}
