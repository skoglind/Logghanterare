import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.lang.Math;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.*;

/** 
* <h1>L_Run</h1>
* Class to keep track of run data 
* during simulation.
*
* @author Fredrik Skoglind (group 11/root)
* @version 1.0 MVP
* @since 2018-04-12
*/
public class L_Run {
	private L_Player player;
	private LogFile logfile;
	private HashMap<String, L_Human> humans;
	private String mapname;
	private int runtimedate;
	private int starttime;
	private int endtime;
	private int onTimestamp;
	private int mapX1;
	private int mapY1;
	private int mapX2;
	private int mapY2;
	private int mapWidth;
	private int mapHeight;
	private int mapOffsetX;
	private int mapOffsetY;
	private int mapBorder;
	private BufferedImage lastRenderedImage;

	public L_Run() {
		this.player = new L_Player();
		this.humans = new HashMap<String, L_Human>();
		this.logfile = new LogFile();
		this.mapname = "";
		this.starttime = 0;
		this.endtime = 0;
		this.onTimestamp = 0;
		this.mapX1 = 10000;
		this.mapY1 = 10000;
		this.mapX2 = 0;
		this.mapY2 = 0;
		this.mapWidth = 0;
		this.mapHeight = 0;
		this.mapOffsetX = 0;
		this.mapOffsetY = 0;
		this.mapBorder = 50;
		this.lastRenderedImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB); 
	}

    public int getStartTime() { return starttime; }
    public int getEndTime() { return endtime; }
    public String getMapname() { return mapname; }

	// Parse int from String
	private int cInt(String value) {
		int retValue = 0;

		if(value != null) {
			double d = Double.parseDouble(value);
			retValue = (int) d;
		}

		return retValue;
	}

	// Parse bool from String
	private Boolean cBool(String value) {
		Boolean retValue = false;

		if(value != null) {
			if(value.equals("-1") || value.equals("1") || value.equals("true")) {
				retValue = true;
			}
		}

		return retValue;
	}

	/** 
	 * Parse logfile into objects.
	 * @param filename		logfile to be parsed
	 */
	public void parse(String filename) {
		// Read logfile and objectify
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));

			String line = br.readLine();
			while (line != null) {
				logfile.addLine(line);
				line = br.readLine();
			}

			br.close();
		} catch (IOException e) { System.out.println(e.getMessage()); }

		// Sort lines by timestamp
		logfile.sortlines();

		if(logfile.isCompleteSet()) {
			// Read line by line and create datasets
			String humanID = "";
			TriageState triage;
			String triageString;
			ArrayList<LogFileLine> lines = logfile.getLines();

			Iterator it = lines.iterator();
			while(it.hasNext()) {
				LogFileLine line = (LogFileLine)it.next();
				line.setParseStatus(true);
				humanID = "";
				switch(line.getEvent()) {
					case START:
						starttime = line.getTimeStamp();
						break;
					case STOP:
						endtime = line.getTimeStamp();
						break;
					case MAP:
						mapname = line.getValues().get("name");
						break;
					case HUMAN:
						humanID = line.getValues().get("id");

						triage = TriageState.NULL;
						if(line.getValues().get("triage") != null) {
							triage = getTriageState(line.getValues().get("triage"));
						}

						L_Human human = new L_Human(cInt(line.getValues().get("pos_x")),
													cInt(line.getValues().get("pos_y")),
													cInt(line.getValues().get("pos_z")),
													cInt(line.getValues().get("breath")),
													cInt(line.getValues().get("pulse")),
													cBool(line.getValues().get("throat")),
													triage);
						humans.put(humanID, human);
						break;
					case POSITION:
						player.addPlayerPosition(line.getTimeStamp(),
												 cInt(line.getValues().get("x")),
												 cInt(line.getValues().get("y")),
												 cInt(line.getValues().get("z")));
						break;
					case SETDATA:
						humanID = line.getValues().get("human_id");
						if(humans.get(humanID) == null) { line.setParseStatus(false); }
						else {
							triage = TriageState.NULL;
							if(line.getValues().get("triage") != null) {
								triage = getTriageState(line.getValues().get("triage"));
							}
							humans.get(humanID).addHumanSet(line.getTimeStamp(),
															triage);
						}
						break;
					case CHECKDATA:
						humanID = line.getValues().get("human_id");
						if(humans.get(humanID) == null) { line.setParseStatus(false); }
						else {
							humans.get(humanID).addHumanCheck(line.getTimeStamp(),
															  line.getValues().get("action"));
						}
						break;
					case ACTION:
						humanID = line.getValues().get("human_id");
						if(humans.get(humanID) == null) { line.setParseStatus(false); }
						else {
							humans.get(humanID).addHumanAction(line.getTimeStamp(),
															   line.getValues().get("action"),
															   cBool(line.getValues().get("status")));
						}
						break;
				}
			}

			// Calculate mapsize
			this.calculateMapSize();

			System.out.println("Parsed file [" + filename + "], " + lines.size() + " lines, " + logfile.errorcount() + " errors\n");
		} else { System.out.println("Couldn't parse file, missing reading rights or file is missing!\n"); }
	}

	private TriageState getTriageState(String value) {
		TriageState triage = TriageState.NULL;
		if(value.equals("1")) { triage = TriageState.PRIO1; }
		else if(value.equals("2")) { triage = TriageState.PRIO2; }
		else if(value.equals("3")) { triage = TriageState.PRIO3; }
		else if(value.equals("dead")) { triage = TriageState.DEAD; }

		return triage;
	}

	// Calculate our mapsize, using all objects and positions
	private void calculateMapSize() {
		boolean firstRun = true;

		// Player positions
		Iterator it = player.getPositions(endtime).iterator();
		while(it.hasNext()) {
			PositionData pd = (PositionData)it.next();
			int posX = pd.getX();
			int posY = pd.getY();

			if(firstRun) { 
				firstRun = false;
				mapX1 = posX; mapY1 = posY;
				mapX2 = posX; mapY2 = posY;
			} else {
				mapX1 = Math.min(mapX1, posX); mapY1 = Math.min(mapY1, posY);
				mapX2 = Math.max(mapX2, posX); mapY2 = Math.max(mapY2, posY);
			}
		}

		// Human positions
		it = humans.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			int posX = humans.get(pair.getKey()).getX();
			int posY = humans.get(pair.getKey()).getY();
			mapX1 = Math.min(mapX1, posX); mapY1 = Math.min(mapY1, posY);
			mapX2 = Math.max(mapX2, posX); mapY2 = Math.max(mapY2, posY);
		}

		// Add map border
		mapX1 -= mapBorder; mapX2 += mapBorder;
		mapY1 -= mapBorder; mapY2 += mapBorder;

		// Move map to positive side
		if(mapX1 < 0) { 
			mapOffsetX = Math.abs(mapX1); 
			mapX1 = 0; mapX2 += mapOffsetX;
		} else if (mapX1 > 0) {
			mapOffsetX = -mapX1; 
			mapX1 = 0; mapX2 += mapOffsetX;
		}

		// Move map to positive side
		if(mapY1 < 0) { 
			mapOffsetY = Math.abs(mapY1); 
			mapY1 = 0; mapY2 += mapOffsetY;
		} else if (mapY1 > 0) {
			mapOffsetY = -mapY1; 
			mapY1 = 0; mapY2 += mapOffsetY;
		}

		// Calculate mapsize
		mapWidth = mapX2 - mapX1;
		mapHeight = mapY2 - mapY1;
	}

	/** 
	 * Render out an image from all data until selected timestamp to object.
	 * @param timestamp	given time for rendering
	 */
	public void render(int timestamp) {
		this.render(timestamp, "");
	}

	/** 
	 * Render out an image from all data until selected timestamp to file.
	 * @param timestamp	given time for rendering
	 * @param filename 
	 */
	public void render(int timestamp, String filename) {
		Renderer r = new Renderer(mapname, mapWidth, mapHeight);

		// Render Positions
		boolean firstRun = true;
		int firstX = 0;
		int firstY = 0;
		int lastX = 0;
		int lastY = 0;
		Iterator it = player.getPositions(timestamp).iterator();
		while(it.hasNext()) {
			PositionData pd = (PositionData)it.next();
			int posX = pd.getX();
			int posY = pd.getY();
			
			if(firstRun) { 
				firstRun = false;
				firstX = posX; firstY = posY;
				lastX = posX; lastY = posY;
			} else {
				r.drawTrace(timestamp, 
							lastX + mapOffsetX, lastY + mapOffsetY, 
							posX + mapOffsetX, posY + mapOffsetY);
				lastX = posX; lastY = posY;
			}
		}

		// Render Human objects
		it = humans.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			L_Human human = humans.get(pair.getKey());
			int posX = human.getX();
			int posY = human.getY();

			r.drawHuman(timestamp, 
						posX + mapOffsetX, posY + mapOffsetY, 
						pair.getKey().toString(), human);
		}

		// Render points
		r.drawPoint(firstX + mapOffsetX, firstY + mapOffsetY);
		r.drawPoint(lastX + mapOffsetX, lastY + mapOffsetY);

		if(!filename.equals("")) {
			r.saveImage(filename);
		} else {
			lastRenderedImage = r.getImage();
		}
	}

	/**
	  * Get last rendered image
	  * @return 		BufferedImage
	  */
	public BufferedImage getLastRenderedImage() {
		return lastRenderedImage;
	}

	/** 
	 * Print log object to console.
	 * @param depth		number of tabs
	 */
	public void printLog(int depth) {
		logfile.print(depth);
		System.out.println("");
		System.out.println("");
	}

	/** 
	 * Print objectdata to console.
	 * @param depth		number of tabs
	 */
	public void print(int depth) {
		String tabsize = "";
		for(int i = 0; i < depth; i++) { tabsize += "  "; }
		System.out.println(tabsize + "# L_Run (Class) [mapname:" + mapname + "] [humans:" + humans.size() + "] [starttime:" + starttime + "] [endtime:" + endtime + "] \n" + tabsize + "                [mapcoords:(" + mapX1 + ";" + mapY1 + ") (" + mapX2 + ";" + mapY2 + ")] [mapsize:(" + mapWidth + ";" + mapHeight + ")] [mapoffset:(" + mapOffsetX + ";" + mapOffsetY + ")]");

		player.print(depth + 1);

		Iterator it = humans.entrySet().iterator();
		while(it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			humans.get(pair.getKey()).print(depth + 1);
		}

		System.out.println("");
	}
}