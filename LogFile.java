import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collections;

/** 
* <h1>LogFile</h1>
* Class to keep track of logfiledata
*
* @author Fredrik Skoglind (group 11/root)
* @version 1.0 MVP
* @since 2018-04-13
*/
public class LogFile {
	private ArrayList<LogFileLine> lines;

	public LogFile() {
		lines = new ArrayList<LogFileLine>();
	}

	/**
	 * Add a "logfileline"-object to the logilfe-object.
	 * @param data 		TIMESTAMP;EVENTCODE;VARIABLES
	 */
	public boolean addLine(String data) {
		String[] dataParts = data.split(";");
		if(dataParts.length == 3) {
			// Retrive timestamp
			int dataTS = Integer.parseInt(dataParts[0].trim());

			// Retrive eventtype
			String dataEventString = dataParts[1].trim().toUpperCase();
			EventType dataEvent = EventType.NULL;

			if(dataTS < 1) { dataTS = 1; } // Safety valve, "fulhack" för att en HUMAN alltid ska skapas innan dess events läggs till

			if(dataEventString.equals("START")) { dataEvent = EventType.START; dataTS = 0; }
			else if(dataEventString.equals("STOP")) { dataEvent = EventType.STOP; }
			else if(dataEventString.equals("HUMAN")) { dataEvent = EventType.HUMAN; dataTS = 0; }
			else if(dataEventString.equals("MAP")) { dataEvent = EventType.MAP; dataTS = 0; }
			else if(dataEventString.equals("POSITION")) { dataEvent = EventType.POSITION; }
			else if(dataEventString.equals("SETDATA")) { dataEvent = EventType.SETDATA; }
			else if(dataEventString.equals("CHECKDATA")) { dataEvent = EventType.CHECKDATA; }
			else if(dataEventString.equals("ACTION")) { dataEvent = EventType.ACTION; }

			// Retrive variables
			HashMap<String, String> dataVariables = new HashMap<String, String>();
			String dataVariablesString = dataParts[2].replaceAll("\\{","").replaceAll("\\}","").trim();

			String[] dataVector = dataVariablesString.split(",");
			for(int i = 0; i < dataVector.length; i++) {
				String[] inner = dataVector[i].split(":");
				if(inner.length == 2) {
					String dataKey = inner[0].trim().toLowerCase();
					String dataValue = inner[1].trim().toLowerCase();

					dataVariables.put(dataKey, dataValue);
				}
			}

			// Save loglinedata
			lines.add(new LogFileLine(dataTS, dataEvent, dataVariables));

			return true;
		} else { return false; }
	}

	// Sort all sets, using timestamp in descending order.
	public void sortlines() {
		Collections.sort(lines);
	}

	/**
	 * Return all "logfilelines"-objects from this logfile-object
	 */
	public ArrayList<LogFileLine> getLines() {
		return lines;
	}

	/**
	 * Returns true if this logfile is complete
	 * @return 			boolean
	 */
	public boolean isCompleteSet() {
		System.out.println("Warning: [LogFile.isCompleteSet()] Not implemented yet.");
		
		// Check if STOP and START, and START is 0 and STOP is last in set
		// Only 1 START and 1 STOP
		// Has a MAP
		// Check if any HUMAN exists
		// Check if any position data is there

		// Correct values for every post? Extended goal...

		return true;
	}

	/** 
	 * Returns the number of errors that occured in the logfile
	 * during parsing.
	 * @return 			number of errors
	 */
	public int errorcount() {
		int errCnt = 0;

		Iterator it = lines.iterator();
		while(it.hasNext()) {
			LogFileLine data = (LogFileLine)it.next();
			if(!data.getParseStatus()) { errCnt++; }
		}

		return errCnt;
	}

	/** 
	 * Print objectdata to console.
	 * @param depth		number of tabs
	 */
	public void print(int depth) {
		String tabsize = "";
		for(int i = 0; i < depth; i++) { tabsize += "  "; }
		System.out.println(tabsize + "# LogFile (Class) [lines:" + lines.size() + "]");

		Iterator it = lines.iterator();
		while(it.hasNext()) {
			LogFileLine data = (LogFileLine)it.next();
			data.print(depth + 1);
		}
	}
}