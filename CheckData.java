public class CheckData implements Comparable<CheckData> {
	private int ts; // Timestamp
	private String action;

	public CheckData(int ts, String action) {
		this.ts = ts;
		this.action = action;
	}

	// Getters
	public int getTimeStamp() { return this.ts; }
	public String getAction() { return this.action; }

	/** 
	 * Print objectdata to console.
	 * @param depth		number of tabs
	 */
	public void print(int depth) {
		String tabsize = "";
		for(int i = 0; i < depth; i++) { tabsize += "  "; }
		System.out.println(tabsize + "# CheckData (Class) [ts:" + ts + "] [action:" + action + "]");
	}

	@Override
    public int compareTo(CheckData compareObj) {
        int compareTS = ((CheckData)compareObj).getTimeStamp();
        return this.ts - compareTS;
    }

    @Override
    public String toString() {
        return "[ ts=" + this.ts + ", action=" + this.action + "]";
    }
}