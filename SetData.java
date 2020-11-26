public class SetData implements Comparable<SetData> {
	private int ts; // Timestamp
	private TriageState triage;

	public SetData(int ts, TriageState triage) {
		this.ts = ts;
		this.triage = triage;
	}

	// Getters
	public int getTimeStamp() { return this.ts; }
	public TriageState getTriage() { return this.triage; }

	/** 
	 * Print objectdata to console.
	 * @param depth		number of tabs
	 */
	public void print(int depth) {
		String tabsize = "";
		for(int i = 0; i < depth; i++) { tabsize += "  "; }
		System.out.println(tabsize + "# SetData (Class) [ts:" + ts + "] [triage:" + triage + "]");
	}

	@Override
    public int compareTo(SetData compareObj) {
        int compareTS = ((SetData)compareObj).getTimeStamp();
        return this.ts - compareTS;
    }

    @Override
    public String toString() {
        return "[ ts=" + this.ts + ", triage=" + this.triage + "]";
    }
}