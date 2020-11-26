public class ActionData implements Comparable<ActionData> {
	private int ts; // Timestamp
	private String action;
	private boolean status;

	public ActionData(int ts, String action, Boolean status) {
		this.ts = ts;
		this.action = action;
		this.status = status;
	}

	// Getters
	public int getTimeStamp() { return this.ts; }
	public String getAction() { return this.action; }
	public boolean getStatus() { return this.status; }

	/** 
	 * Print objectdata to console.
	 * @param depth		number of tabs
	 */
	public void print(int depth) {
		String tabsize = "";
		for(int i = 0; i < depth; i++) { tabsize += "  "; }
		System.out.println(tabsize + "# ActionData (Class) [ts:" + ts + "] [action:" + action + "] [status:" + status + "]");
	}

	@Override
    public int compareTo(ActionData compareObj) {
        int compareTS = ((ActionData)compareObj).getTimeStamp();
        return this.ts - compareTS;
    }

    @Override
    public String toString() {
        return "[ ts=" + this.ts + ", action=" + this.action + ", status=" + this.status + "]";
    }
}