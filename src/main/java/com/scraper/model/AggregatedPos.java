package com.scraper.model;

import com.scraper.service.MiscellaneousService;

import lombok.Data;

@Data
public class AggregatedPos {

	private int aggrDeg;
	private int deg;
	private int min;
	private int sec;

	
	
	private String getColonSepAggr() {
		return String.valueOf(aggrDeg) + ":" + String.valueOf(min) + ":" + String.valueOf(sec);
	}

	public AggregatedPos(PlanetPosition pos) {
		super();
		String[] parts = pos.getDegree().split("-");
		if (parts.length >= 3) {
			this.deg = Integer.parseInt(parts[0]);
			this.min = Integer.parseInt(parts[1]);
			this.sec = Integer.parseInt(parts[2]);
			this.aggrDeg = MiscellaneousService.getAggrDeg(deg, pos.getRasi());
		}
	}

	public AggregatedPos(int aggrDeg, int deg, int min, int sec) {
		super();
		this.aggrDeg = aggrDeg;
		this.deg = deg;
		this.min = min;
		this.sec = sec;
	}

	public AggregatedPos() {
		super();
	}

}
