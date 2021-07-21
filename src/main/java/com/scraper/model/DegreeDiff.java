package com.scraper.model;

import lombok.Data;

@Data
public class DegreeDiff {
	private String name;
	private boolean reverse;
	private int deg;
	private int min;
	private int sec;
}
