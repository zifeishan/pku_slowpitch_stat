
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This program sucks!
 * 
 * This program sucks because of the second author ;)
 * The weird class name is also picked by the second author.
 * 
 * @author Zifei Shan (Robin), Yiyang Hao (Felix)
 * 
 */
public class SZFMain {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		/**
		 * Define the root directory. Output is also put into this directory.
		 * Default is data/
		 */
		String root = "data/";
		/**
		 * INPUT files (reports for each game) should be put under following directory
		 * default is input/
		 */
		String reportDir = root+"input/";
		
		/**
		 * If command line arguments specified, change directories
		 */
		if(args.length == 2)
		{
			root = args[0];
			reportDir = args[1];
		}
		else if (args.length != 0)
		{
			System.out.println("Arguments: <outputDir/> <inputDir/>");
			return;
		}
		
		File dir = new File(reportDir);
		File[] files = dir.listFiles();
		for (File f : files) {
			System.out.println("\n"+f);
			if(f.getName().startsWith(".")) continue;
			/*
			 * process each report file in the directory.
			 */
			handleFile(f);
		}

		/*
		 * output statistics into two files.
		 * Manually copy & paste them into Excel Templates provided.
		 */
	    File outb = new File(root+"batters.txt");
	    File outp = new File(root+"pitchers.txt");
	    
	    PrintStream bout = new PrintStream(outb);
	    PrintStream pout = new PrintStream(outp);

	    bout.println("Name\tTeam\tAB+BB\t1B\t2B\t3B\tHR\tBB\tK\tE\tEB\tRBI\tDP\tG\tFc\tFO\tSC\tDirections");
	    pout.println("Name\tNP\t1B\t2B\t3B\tHR\tK\tBB\tAO\tGO\tER\tRBI\tE");
	    
		for (Batter p : Batter._m.values()) {
			bout.println(p.toString());
		}
		bout.close();
		for (Pitcher p : Pitcher._m.values()) {
			if(p.qname.equals("Unknown"))
				continue;
			pout.println(p.toString());
		}
		pout.close();
	}

	/**
	 * Process one input file with record format.
	 * @param f
	 * @throws FileNotFoundException
	 */
	private static void handleFile(File f) throws FileNotFoundException {
		Scanner s = new Scanner(f);
		String teamName = s.next();
		s.nextLine();
		Map<Integer, Batter> m = new HashMap<Integer, Batter>();
		// read people
		while (true) {
			String line = s.nextLine();
			
			Scanner ts = new Scanner(line);
			if(!ts.hasNextInt())
				break;	// empty line
			int id = ts.nextInt();
			String name = ts.next();
			System.out.print(name+"\t");
			Batter p = Batter.getInstance(name, teamName);
			m.put(id, p);
			ts.close();
		}
		
		Pitcher pit = Pitcher.getInstance("Unknown");
		// read data
		while (s.hasNext()) {
			String line = s.nextLine();
			if(line.equals("") || line.equals(" "))
				continue;
			Scanner ts = new Scanner(line);
			if(line.startsWith("P")) // Pitcher change
			{
				String pname = line.split(" ")[1];
				pit = Pitcher.getInstance(pname);
				continue;
			}
			System.out.println(line);
			int id = ts.nextInt();
			Batter p = m.get(id);
			while (ts.hasNext()) {
				String tok = ts.next().toLowerCase();
				p.dxu++;
				pit.pitches++;
				
				int q = tok.indexOf(".");	// batting direction
				if(q != -1)
				{
					String tmp = tok.split("\\.")[1];
//					System.out.println(tmp+"!!!!!!!!!!");
					String[] tmp2 = tmp.split("[^a-zA-Z0-9!]");
					String direction = tmp2[0]; 
					p.AddDirection(direction);
				
					String[] parts = tok.split(direction);
					if(parts.length == 2) // left and right
					{
						tok = tok.substring(0, q) + parts[1];
						System.out.println("NEW:" +tok);
					}
					else if (parts.length == 1)
					{
						tok = tok.substring(0, q);
					}
					else {
						System.err.println("ERROR!!! "+tok);
					}
					
				}
				

				q = tok.indexOf('(');
				boolean hasrbi = false; 
				if (q != -1) {
					String rbi = tok.substring(q + 1, tok.indexOf(')'));
					tok = tok.substring(0, q);
					p.rbi += Integer.valueOf(rbi);
					hasrbi = true;
					
					pit.rbi += Integer.valueOf(rbi);
					if(!tok.startsWith("e")){	// count ER if this is not a E
						pit.er += Integer.valueOf(rbi);
						System.out.println("Pitcher "+pit.qname+": "+tok+" RBI: "+rbi);
						System.out.println("This is a ER: "+tok);
					}
					else {
						System.out.println("This is not a ER: "+tok);
					}
					
				}
								
				int indexOf = tok.indexOf('+');
				if (indexOf != -1) {
					String h = tok.substring(0, indexOf);
					String t = tok.substring(indexOf + 1);
					if (h.equals("1b")) {
						p.b1++;
						pit.b1++;
					} else if (h.equals("2b")) {
						p.b2++;
						pit.b2++;
					} else if (h.equals("3b")) {
						p.b3++;
						pit.b3++;
					} else if (h.equals("4b")) {
						p.hr++;
						pit.hr++;
					} else if (h.equals("hr")) {
						p.hr++;
						pit.hr++;
					} else if (h.equals("e")) { // e+e
						p.e++;
						p.eb++;
						pit.e++;
					}

					if (t.equals("e1b") || t.equals("e")) {	// e.g. 1b+e2b
						p.eb += 1;
					} else if (t.equals("e2b")) {
						p.eb += 2;
					} else if (t.equals("e3b")) {
						p.eb += 3;
					} else if (t.equals("e4b")) {
						p.eb += 4;
					} else if (t.equals("ehr")) {
						p.eb += 4;
					}
				} else {
					if (tok.equals("1b")) {
						p.b1++;
						pit.b1++;
					} else if (tok.equals("2b")) {
						p.b2++;
						pit.b2++;
					} else if (tok.equals("3b")) {
						p.b3++;
						pit.b3++;
					} else if (tok.equals("4b")) {
						p.hr++;
						pit.hr++;
					} else if (tok.equals("hr")) {
						p.hr++;
						pit.hr++;
					} else if (tok.equals("g")) {
						p.g++;
						pit.gfc++;
					} else if (tok.equals("k")) {
						p.k++;
						pit.k++;
					} else if (tok.equals("fo")) {
						if(hasrbi) { // sacrifice hit
							p.sc++;
							pit.fo++; // ?
							System.out.println("SC: "+p.qname);
						}
						else {
							p.fo++;
							pit.fo++;
						}
					} else if (tok.equals("dp")) {
						p.dp++;
						pit.gfc++;
					} else if (tok.equals("fc")) {
						p.fc++;
						pit.gfc++;
					} else if (tok.equals("bb") || tok.equals("ibb")) {
						p.bb++;
						pit.bb++;
					} else if (tok.equals("e1b") || tok.equals("e")) {
						p.e++;
						pit.e++;
//						System.out.println("ER?!"+tok);
						p.eb += 1;
					} else if (tok.equals("e2b")) {
						p.e++;
						pit.e++;
						p.eb += 2;
					} else if (tok.equals("e3b")) {
						p.e++;
						pit.e++;
						p.eb += 3;
					} else if (tok.equals("e4b")) {
						p.e++;
						pit.e++;
						p.eb += 4;
					} else if (tok.equals("ehr")) {
						p.e++;
						pit.e++;
						p.eb += 4;
					}
				}
			}
			ts.close();
		}
		s.close();
	}

}

/**
 * A repository for all batters.
 * Give static method to get a batter.
 * Statistics of pitchers / batters are accumulated in the whole program execution.
 * @author Robin
 *
 */
class Batter {
	public int dxu = 0;
	public int b1 = 0, b2 = 0, b3 = 0, hr = 0, k = 0, bb = 0, 
			e = 0, eb = 0, dp = 0, g = 0, fo = 0, fc = 0, sc = 0, 
			rbi = 0;
	public String qname;
	public HashMap<String, Integer> directions = new HashMap<String, Integer>();

	protected Batter(){}
	protected Batter(String name, String teamName) {
		this.qname = name + "\t" + teamName;
	}

	public static Map<String, Batter> _m = new HashMap<String, Batter>();

	public static Batter getInstance(String name, String teamName) {
		String qName = name + "\t" + teamName;
		if (!_m.containsKey(qName)) {
			Batter ret = new Batter(name, teamName);
			_m.put(ret.qname, ret);
			return ret;
		}
		return _m.get(qName);
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(qname);
		sb.append('\t');
		//sb.append('\t');
		sb.append(dxu);
		sb.append('\t');
		sb.append(b1);
		sb.append('\t');
		sb.append(b2);
		sb.append('\t');
		sb.append(b3);
		sb.append('\t');
		sb.append(hr);
		sb.append('\t');
		sb.append(bb);
		sb.append('\t');
		sb.append(k);
		sb.append('\t');
		sb.append(e);
		sb.append('\t');
		sb.append(eb);
		sb.append('\t');
		sb.append(rbi);
		sb.append('\t');
		sb.append(dp);
		sb.append('\t');
		sb.append(g);
		sb.append('\t');
		sb.append(fc);
		sb.append('\t');
		sb.append(fo);
		sb.append('\t');
		sb.append(sc);
		sb.append('\t');
		sb.append(GetDirections());
		return sb.toString();
	}

	public void AddDirection(String dir) {
		if(!directions.containsKey(dir))
			directions.put(dir, 1);
		else
			directions.put(dir, directions.get(dir) + 1);
	}
	public String GetDirections() {
		String ret = "";
//		System.out.println( qname + ": "+directions);
		for(String s : directions.keySet()) 
			ret += TranslateDir(s) + '=' + directions.get(s) + ", ";
		return ret;
//		return directions.toString();
	}
	private String TranslateDir(String s) {
//		String[] dirs = {"p", "c", "1b", "2b", "3b", "ss", "lf", "cf", "rf", "ff"};
//		String[] dirname = {"投手", "捕手", "一垒", "二垒", "三垒", "游击", "左外", "中外", "右外", "自由外"};
//		String[] dirs = {"g", "", "1b", "2b", "3b", "ss", "lf", "cf", "rf", "ff"};
//		String[] dirname = {"投手", "捕手", "一垒", "二垒", "三垒", "游击", "左外", "中外", "右外", "自由外"};
		return s;
	}
}

/**
 * A repository for all pitchers.
 * Give static method to get a pitcher.
 * Statistics of pitchers / batters are accumulated in the whole program execution.
 * @author Robin
 *
 */
class Pitcher {
public int pitches = 0, b1 = 0, b2 = 0, b3 = 0, hr = 0, k = 0, bb = 0,
      fo = 0, gfc = 0, e = 0, er = 0, rbi = 0;
  public String qname;
  protected Pitcher(){}
  protected Pitcher(String name) {
    this.qname = name;
  }

  public static Map<String, Pitcher> _m = new HashMap<String, Pitcher>();

  /*
   * Get the pitcher instance identified by name.
   */
  public static Pitcher getInstance(String name) {
    String qName = name;
    if (!_m.containsKey(qName)) {
      Pitcher ret = new Pitcher(name);
      _m.put(ret.qname, ret);
      return ret;
    }
    return _m.get(qName);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(qname);
    sb.append('\t');
    sb.append(pitches);
    sb.append('\t');
    sb.append(b1);
    sb.append('\t');
    sb.append(b2);
    sb.append('\t');
    sb.append(b3);
    sb.append('\t');
    sb.append(hr);
    sb.append('\t');
    sb.append(k);
    sb.append('\t');
    sb.append(bb);
    sb.append('\t');
    sb.append(fo);
    sb.append('\t');
    sb.append(gfc);
    sb.append('\t');
    sb.append(er);
    sb.append('\t');
    sb.append(rbi);
    sb.append('\t');
    sb.append(e);
    
    return sb.toString();
  }
      
}