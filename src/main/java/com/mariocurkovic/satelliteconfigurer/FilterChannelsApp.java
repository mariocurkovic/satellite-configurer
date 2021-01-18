package com.mariocurkovic.satelliteconfigurer;

import com.mariocurkovic.satelliteconfigurer.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FilterChannelsApp {

	private static final Logger logger = LogManager.getLogger(FilterChannelsApp.class);

	private static final String resourcesPath = "/Users/mariocurkovic/workspace/mcurkovic/satellite-configurer/src/main/resources/";

	private static final String templateFilePath = resourcesPath + "input/dvbchannel_v2.3_initial_scan.xml";
	private static final String outputFilePath = resourcesPath + "output/dvbchannel_v2.3_filtered_config.xml";

	private static final String channelNamesFile = resourcesPath + "input/channel_names.txt";
	private static final String remainingProgramsFile = resourcesPath + "/remaining_programs.txt";

	public static List<String> remainingPrograms = new ArrayList<>();

	public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {

		remainingPrograms = Util.getRemainingPrograms(remainingProgramsFile);

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(templateFilePath);

		NodeList programList = doc.getElementsByTagName("program");
		int length = programList.getLength();

		List<Node> forDeletion = new ArrayList<>();

		for (int j = 0; j < length; j++) {

			Node program = programList.item(j);

			if (shouldDeleteProgram(program)) {
				forDeletion.add(program);
			}

		}

		logger.info("Deleted " + deletePrograms(forDeletion) + " programs.");

		printRemainingPrograms(doc.getElementsByTagName("program"));
		buildChannelNames(doc.getElementsByTagName("program"));

		StreamResult xmlOutput = new StreamResult(new File(outputFilePath));
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		transformerFactory.setAttribute("indent-number", 1);
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(doc), xmlOutput);

	}

	private static int deletePrograms(List<Node> programs) {
		int result = 0;
		for (Node program : programs) {
			program.getParentNode().removeChild(program);
			result++;
			// logger.info("Removed channel " + program.getAttributes().getNamedItem("name").getTextContent());
		}
		return result;
	}

	private static void printRemainingPrograms(NodeList programList) {
		logger.info("Remaining programs:");
		for (int i = 0; i < programList.getLength(); i++) {
			logger.info(programList.item(i).getAttributes().getNamedItem("name").getTextContent());
		}
	}

	private static void buildChannelNames(NodeList programList) {
		StringBuilder channelNames = new StringBuilder();
		for (int i = 0; i < programList.getLength(); i++) {
			String programName = programList.item(i).getAttributes().getNamedItem("name").getTextContent();
			channelNames.append(programName).append(" = ").append(programName).append("\n");
		}
		Util.writeToFile(channelNamesFile, channelNames.toString());
	}

	private static boolean shouldDeleteProgram(Node program) {
		if (program != null && program.getAttributes() != null && program.getAttributes().getNamedItem("name") != null && program.getAttributes().getNamedItem("name").getTextContent() != null) {
			String name = program.getAttributes().getNamedItem("name").getTextContent().trim().toLowerCase();
			name = name.startsWith("xxx") ? name.substring(3) : name;
			for (String remainingProgram : remainingPrograms) {
				if (remainingProgram.equals(name) || (remainingProgram.endsWith("*") && name.startsWith(remainingProgram.substring(0, remainingProgram.length() - 1)))) {
					return false;
				}
			}
		}
		return true;
	}

}