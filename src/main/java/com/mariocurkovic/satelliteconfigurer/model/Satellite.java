package com.mariocurkovic.satelliteconfigurer.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Satellite {

	private String name;
	private double position;
	private String orientation;
	private String web;

	public Satellite(String name, double position, String orientation, String web) {
		this.name = name;
		this.position = position;
		this.orientation = orientation;
		this.web = web;
	}

	public String getFullName() {
		return name + " " + position + orientation;
	}

	/*
	    <satellite name="Thor 5/6/7/Intelsat 10-02  Ku-band"
               longitude="-9"
               lof_hi="10600000"
               lof_lo="9750000"
               lof_threshold="11700000"
               lnb_power="13/18v"
               signal_22khz="auto"
               toneburst="none"
               diseqc1_0="none"
               diseqc1_1="none"
               motor="none">
    	</satellite>
	 */
	public String getXml() {
		StringBuilder result = new StringBuilder();

		result.append("<satellite name=\"" + getFullName() + "\"");
		result.append(" longitude=\"" + (orientation.equals("E") ? (int) (position * 10) : (int) ((0 - position) * 10)) + "\"");
		result.append(
				" lof_hi=\"10600000\" lof_lo=\"9750000\" lof_threshold=\"11700000\" lnb_power=\"13/18v\" signal_22khz=\"auto\" toneburst=\"none\" diseqc1_0=\"none\" diseqc1_1=\"none\" motor=\"none\">");

		return result.toString();
	}

}