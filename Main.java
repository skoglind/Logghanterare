/** 
* <h1>Main</h1>
* Main class, for the full application
*
* @author Fredrik Skoglind (group 11/root)
* @version 1.0 MVP
* @since 2018-04-12
*/
public class Main {
	/**
	 * Main constructor.
	 * @param args 			arg1: filename, arg2: v for verbose
	 */
	public static void main(String[] args) {
		Main m = new Main();
		if(args.length > 0) {
			m.consoleMode(args);
		} else {
			m.guiMode(args);
		}
	}

	public void guiMode(String[] args) {
        GUI gui = new GUI();
        gui.showMe();
	}

	public void consoleMode(String[] args) {
		String filename = "";
		long startTime = 0;
		long stopTime = 0;

		long parseTime = 0;
		long renderTime = 0;

		filename = args[0];

		L_Run run = new L_Run();

		startTime = System.nanoTime();
		run.parse(filename);
		stopTime = System.nanoTime();
		parseTime = (stopTime - startTime) / 1000000;
		
		startTime = System.nanoTime();
		run.render(run.getEndTime(), "renderout/out.png");
		stopTime = System.nanoTime();
		renderTime = (stopTime - startTime) / 1000000;

		System.out.println("Parsetime: " + parseTime + "ms");
		System.out.println("Rendertime: " + renderTime + "ms");
		System.out.println("");

		if(args.length > 1) { if(args[1].equals("v")) { run.print(1); } }
		if(args.length > 1) { if(args[1].equals("vv")) { run.print(1); run.printLog(1); } }
	}
}