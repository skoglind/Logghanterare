public class PositionData implements Comparable<PositionData> {
	private int ts; // Timestamp
	private int x;
	private int y;
	private int z;

	public PositionData(int ts, int x, int y, int z) {
		this.ts = ts;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	// Getters
	public int getTimeStamp() { return this.ts; }
	public int getX() { return this.x; }
	public int getY() { return this.y; }
	public int getZ() { return this.z; }

	/** 
	 * Print objectdata to console.
	 * @param depth		number of tabs
	 */
	public void print(int depth) {
		String tabsize = "";
		for(int i = 0; i < depth; i++) { tabsize += "  "; }
		System.out.println(tabsize + "# PositionData (Class) [ts:" + ts + "] [x:" + x + "] [y:" + y + "]");
	}

	@Override
    public int compareTo(PositionData compareObj) {
        int compareTS = ((PositionData)compareObj).getTimeStamp();
        return this.ts - compareTS;
    }

    @Override
    public String toString() {
        return "[ ts=" + this.ts + ", x=" + this.x + ", y=" + this.y + ", z=" + this.z + "]";
    }
}