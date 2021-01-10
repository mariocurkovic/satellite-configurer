import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class App {

	private static final String destination = "/Users/mariocurkovic/Desktop/satellite-configurer/target";

	public static void main(String[] args) throws IOException {

		List<Satellite> satellites = new ArrayList<>();
		satellites.add(new Satellite("HotBird", 13, "E", "https://en.kingofsat.net/pos-13E.php"));
		satellites.add(new Satellite("Eutelsat", 16, "E", "https://en.kingofsat.net/pos-16E.php"));
		satellites.add(new Satellite("Astra", 19.2, "E", "https://en.kingofsat.net/pos-19.2E.php"));
		satellites.add(new Satellite("Thor", 0.8, "W", "https://en.kingofsat.net/pos-0.8W.php"));

		StringBuilder content = new StringBuilder();

		content.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		content.append("\n <db>");
		content.append("\n <version winsat_editer=\"2.3\"/> \n");

		for (Satellite satellite : satellites) {
			Document doc = Jsoup.connect(satellite.getWeb()).get();

			Elements frequenciesElem = doc.select(".frq");

			List<Frequency> frequencies = new ArrayList<>();

			for (Element frequencyElem : frequenciesElem) {
				Elements properties = frequencyElem.select("td");
				if (Util.isValidFrequency(frequencyElem)) {
					for (int i = 0; i < properties.size(); i++) {
						// System.out.println(properties.get(i));
					}
					Frequency frequency = new Frequency();
					frequency.setSatellite(satellite);
					frequency.setFrequency(properties.get(2).text().substring(0, properties.get(2).text().indexOf(".")));
					frequency.setPolarization(properties.get(3).text());
					frequency.setSymbolRate(properties.get(8).text().split(" ")[0].trim());
					frequency.setFec(properties.get(8).text().split(" ")[1].trim());
					frequency.setNetworkId(properties.get(10).text());
					frequency.setTransponderId(properties.get(11).text());

					frequencies.add(frequency);
				}
			}

			//System.out.println("Found " + frequencies.size() + " frequencies for satelite " + satellite.getFullName() + ":");

			content.append(satellite.getXml());
			for (Frequency frequency : frequencies) {
				content.append(frequency.getXml());
			}
			content.append("</satellite>");
			content.append("\n");
		}

		content.append("</db>");
		Util.writeToFile(destination, content.toString());

	}

}
