import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/** 
* <h1>L_Player</h1>
* Class to keep track of player data 
* during simulation
*
* @author Fredrik Skoglind (group 11/root)
* @version 1.0 MVP
* @since 2018-04-12
*/
public class L_Player {
	private ArrayList<PositionData> positions;
	private boolean sortedSet;

	public L_Player() {
		this.positions = new ArrayList<PositionData>();
		this.sortedSet = false;
	}

	/**
	 * Add a "positiondata"-object to the player-object.
	 * @param ts
	 * @param x
	 * @param y
	 * @param z
	 */
	public void addPlayerPosition(int ts, int x, int y, int z) {
		positions.add(new PositionData(ts, x, y, z));
		this.sortedSet = false;
	}

	// Sort all sets, using timestamp in descending order.
	private void sortSets() {
		this.sortedSet = true;
		Collections.sort(positions);
	}

	/**
	 * Return all "positiondata"-objects from this player-object
	 * until a given timestamp.
	 * @param timestamp
	 */
	public ArrayList<PositionData> getPositions(int timestamp) {
		if(!this.sortedSet) { this.sortSets(); }

		ArrayList<PositionData> returnset = new ArrayList<PositionData>();
		Iterator it = positions.iterator();
		boolean wDone = false;

		while(!wDone && it.hasNext()) {
			PositionData data = (PositionData) it.next();
			if(data.getTimeStamp() < timestamp) {
				returnset.add(data);
			} else { wDone = true; }
		}

		return returnset;
	}

	/** 
	 * Print objectdata to console.
	 * @param depth		number of tabs
	 */
	public void print(int depth) {
		String tabsize = "";
		for(int i = 0; i < depth; i++) { tabsize += "  "; }
		System.out.println(tabsize + "# L_Player (Class)");

		Iterator it = positions.iterator();
		while(it.hasNext()) {
			PositionData data = (PositionData)it.next();
			data.print(depth + 1);
		}
	}
}