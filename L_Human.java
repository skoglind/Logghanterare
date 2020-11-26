import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/** 
* <h1>L_Human</h1>
* Class to keep track of victim data 
* during simulation
*
* @author Fredrik Skoglind (group 11/root)
* @version 1.0 MVP
* @since 2018-04-12
*/
public class L_Human {
	private ArrayList<SetData> sets;
	private ArrayList<ActionData> actions;
	private ArrayList<CheckData> checks;
	private int positionX;
	private int positionY;
	private int positionZ;
	private int breathing;
	private int pulse;
	private boolean throat;
	private TriageState triage;
	private boolean sortedSet;

	public L_Human(int x, int y, int z, int breathing, int pulse, boolean throat, TriageState triage) {
		this.actions = new ArrayList<ActionData>();
		this.sets = new ArrayList<SetData>();
		this.checks = new ArrayList<CheckData>();
		this.positionX = x;
		this.positionY = y;
		this.positionZ = z;
		this.breathing = breathing;
		this.pulse = pulse;
		this.throat = throat;
		this.triage = triage;
		this.sortedSet = false;
	}

	// Getters
	public int getX() { return this.positionX; }
	public int getY() { return this.positionY; }
	public int getZ() { return this.positionZ; }
	public int getBreathing() { return this.breathing; }
	public int getPulse() { return this.pulse; }
	public boolean getThroat() { return this.throat; }
	public TriageState getTriage() { return this.triage; }

	/**
	 * Return last TriageState on a given timestamp.
	 * @param ts
	 * @return TriageState
	 */
	public TriageState getLastTriageState(int ts) {
		ArrayList<SetData> data = getSets(ts);
		if(data.size() > 0) {
			SetData e = data.get(data.size() - 1);
			return e.getTriage();
		} else {
			return TriageState.NULL;	
		}
	} 

	/**
	 * Add a "setdata"-object to the human-object.
	 * @param ts
	 * @param triage 	1|2|3|dead
	 */
	public void addHumanSet(int ts, TriageState triage) {
		sets.add(new SetData(ts, triage));
		this.sortedSet = false;
	}

	/**
	 * Add a "actiondata"-object to the human-object.
	 * @param ts
	 * @param action
	 * @param status 	
	 */
	public void addHumanAction(int ts, String action, boolean status) {
		actions.add(new ActionData(ts, action, status));
		this.sortedSet = false;
	}

	/**
	 * Add a "checkdata"-object to the human-object.
	 * @param ts
	 * @param action
	 */
	public void addHumanCheck(int ts, String action) {
		checks.add(new CheckData(ts, action));
		this.sortedSet = false;
	}

	// Sort all sets, using timestamp in descending order.
	private void sortSets() {
		this.sortedSet = true;
		Collections.sort(sets);
		Collections.sort(actions);
		Collections.sort(checks);
	}

	/**
	 * Return all "setdata"-objects from this human-object
	 * until a given timestamp.
	 * @param timestamp
	 */
	public ArrayList<SetData> getSets(int timestamp) {
		if(!this.sortedSet) { this.sortSets(); }

		ArrayList<SetData> returnset = new ArrayList<SetData>();
		Iterator it = sets.iterator();
		boolean wDone = false;

		while(!wDone && it.hasNext()) {
			SetData data = (SetData) it.next();
			if(data.getTimeStamp() < timestamp) {
				returnset.add(data);
			} else { wDone = true; }
		}

		return returnset;
	}

	/**
	 * Return all "actiondata"-objects from this human-object
	 * until a given timestamp.
	 * @param timestamp
	 */
	public ArrayList<ActionData> getActions(int timestamp) {
		if(!this.sortedSet) { this.sortSets(); }

		ArrayList<ActionData> returnset = new ArrayList<ActionData>();
		Iterator it = actions.iterator();
		boolean wDone = false;

		while(!wDone && it.hasNext()) {
			ActionData data = (ActionData) it.next();
			if(data.getTimeStamp() < timestamp) {
				returnset.add(data);
			} else { wDone = true; }
		}

		return returnset;
	}

	/**
	 * Return all "checkdata"-objects from this human-object
	 * until a given timestamp.
	 * @param timestamp
	 */
	public ArrayList<CheckData> getChecks(int timestamp) {
		if(!this.sortedSet) { this.sortSets(); }

		ArrayList<CheckData> returnset = new ArrayList<CheckData>();
		Iterator it = checks.iterator();
		boolean wDone = false;

		while(!wDone && it.hasNext()) {
			CheckData data = (CheckData) it.next();
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
		System.out.println(tabsize + "# L_Human (Class) [x:" + positionX + "] [y:" + positionY + "] [breath:" + breathing + "] [pulse:" + pulse + "] [throat:" + throat + "] [triage:" + triage + "] ");

		Iterator it = sets.iterator();
		while(it.hasNext()) {
			SetData data = (SetData)it.next();
			data.print(depth + 1);
		}

		it = checks.iterator();
		while(it.hasNext()) {
			CheckData data = (CheckData)it.next();
			data.print(depth + 1);
		}

		it = actions.iterator();
		while(it.hasNext()) {
			ActionData data = (ActionData)it.next();
			data.print(depth + 1);
		}
	}
}