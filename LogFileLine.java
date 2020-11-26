import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/** 
* <h1>LogFileLine</h1>
* Class to keep track of logfilelines
*
* @author Fredrik Skoglind (group 11/root)
* @version 1.0 MVP
* @since 2018-04-13
*/
public class LogFileLine implements Comparable<LogFileLine> {
	private int ts;
	private EventType event;
	private HashMap<String, String> values;
	private int parseStatus;

	public LogFileLine(int ts, EventType event, HashMap<String, String> values) {
		this.ts = ts;
		this.event = event;
		this.values = values;
		this.parseStatus = 0;
	}

	// Getters
	public int getTimeStamp() { return this.ts; }
	public EventType getEvent() { return this.event; }
	public HashMap<String, String> getValues() { return this.values; }

	/**
	 * Set parsestate of this line.
	 * @param status
	 */ 
	public void setParseStatus(boolean status) {
		if(status) { this.parseStatus = 1; }
		else { this.parseStatus = 0; }
	}

	/**
	 * Returns the parsestate of this line.
	 * @return 			boolean
	 */
	public boolean getParseStatus() {
		if(this.parseStatus == 1) { return true; } 
		else { return false; }
	}

	/** 
	 * Print objectdata to console.
	 * @param depth		number of tabs
	 */
	public void print(int depth) {
		String tabsize = "";
		for(int i = 0; i < depth; i++) { tabsize += "  "; }
		System.out.println(tabsize + "# LogFileLine (Class) [parsestatus:" + parseStatus + "] [ts:" + ts + "] [event:" + event + "]");

		if(values.size() > 0) {
			Iterator it = values.entrySet().iterator();
			System.out.print(tabsize + "  ");
			while(it.hasNext()) {
				Map.Entry pair = (Map.Entry)it.next();
				System.out.print("[" + pair.getKey() + ":" + pair.getValue() + "] ");
			}
			System.out.print("\n");
		}
	}

	@Override
    public int compareTo(LogFileLine compareObj) {
        int compareTS = ((LogFileLine)compareObj).getTimeStamp();
        return this.ts - compareTS;
    }

    @Override
    public String toString() {
        return "[ ts=" + this.ts + ", event=" + this.event + "]";
    }
}