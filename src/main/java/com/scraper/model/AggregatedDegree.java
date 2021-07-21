package com.scraper.model;

import com.scraper.service.MiscellaneousService;

import lombok.Data;

@Data
public class AggregatedDegree {
private int deg;
private int min;
private int sec;
private int aggrDeg;
private String getColonSepAggr() {
	return String.valueOf(aggrDeg)+":"+String.valueOf(min)+":"+String.valueOf(sec);
}
public int setAggregatedDegree(String rasi) {
	this.aggrDeg=MiscellaneousService.getAggrDeg(deg, rasi);
	return aggrDeg;
}

}
