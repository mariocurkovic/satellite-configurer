package com.mariocurkovic.satelliteconfigurer.util;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {

	// dvbchannel_v2.3_2020_12_10_17_40_50.xml
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");

	public static boolean isValidFrequency(Element frequencyElement) {
		Elements properties = frequencyElement.select("td");
		if (properties == null || properties.get(0) == null || properties.size() < 4) {
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

	public static boolean isValidChannel(Element channelElement) {
		Elements properties = channelElement.select("td");
		return properties != null && properties.size() > 13;
	}

	public static void writeToFile(String filename, String content) {
		try {
			FileWriter myWriter = new FileWriter(filename);
			myWriter.write(content);
			myWriter.close();
			System.out.println("Created file " + filename + ".");
		} catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		}
	}

	public static String getCurrentTimestamp() {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		return (sdf.format(timestamp));
	}

	public static String getXmlHeader() {
		StringBuilder header = new StringBuilder();

		header.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
		header.append("\n <db>");
		header.append("\n <version winsat_editer=\"2.3\"/> \n");

		return header.toString();
	}

	public static String getXmlFooter() {
		return "</db>";
	}

	public static List<String> getRemainingPrograms(String filePath) throws FileNotFoundException {
		List<String> list = new ArrayList<>();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(filePath));
			String line = reader.readLine();
			while (line != null) {
				list.add(line.toLowerCase());
				line = reader.readLine();
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

}
