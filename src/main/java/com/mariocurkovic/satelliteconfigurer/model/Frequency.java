package com.mariocurkovic.satelliteconfigurer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Frequency {

	private Satellite satellite;
	private String frequency;
	private String polarization;
	private String symbolRate;
	private String fec;
	private String networkId;
	private String transponderId;

	public String getXml() {
		StringBuilder result = new StringBuilder();

		result.append("<transponder original_network_id=\"" + networkId + "\"");
		result.append(" ts_id=\"" + transponderId + "\"");
		result.append(" frequency=\"" + frequency + "000\"");
		result.append(" symbol_rate=\"" + symbolRate + "000\"");
		result.append(" polarisation=\"" + polarization + "\"");
		result.append(" />");

		return result.toString();
	}

}