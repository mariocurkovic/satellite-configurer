import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Util {

	// dvbchannel_v2.3_2020_12_10_17_40_50.xml
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

	public static boolean isValidFrequency(Element frequencyElement) {
		Elements properties = frequencyElement.select("td");
		if (properties == null || properties.get(0) == null) {
			return false;
		}
		if ("Pos".equals(properties.get(0).text().trim())) {
			return false;
		}
		if (!"V".equals(properties.get(3).text().trim()) && !"H".equals(properties.get(3).text().trim())) {
			return false;
		}
		return true;
	}

	public static void writeToFile(String destination, String content) {
		try {
			// dvbchannel_v2.3_2020_12_10_17_40_50.xml
			String filename = destination + "/dvbchannel_v2.3_" + getCurrentTimestamp() + ".xml";
			FileWriter myWriter = new FileWriter(filename);
			myWriter.write(content);
			myWriter.close();
			System.out.println("Created file " + filename + ".");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	private static String getCurrentTimestamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return (sdf.format(timestamp));
	}

}
