package com.scraper.restcontroller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.scraper.model.DegreeDiff;
import com.scraper.model.GetDegreeRequestBody;
import com.scraper.model.GetDegreeResponseBody;
import com.scraper.model.GetDiffResponseBody;
import com.scraper.model.PlanetPosition;
import com.scraper.service.MiscellaneousService;
import com.scraper.service.ScraperService;

@RestController
public class PlanetController {
	
	@Autowired
	ScraperService service;
	@Autowired 
	MiscellaneousService calcService;
	
	@PostMapping(value = "/getdegree")
	public GetDegreeResponseBody getDegree(@RequestBody GetDegreeRequestBody reqbody) {
		List<PlanetPosition> listOfPlanets=service.getDegree(reqbody);
		GetDegreeResponseBody response=new GetDegreeResponseBody();
		response.setPositionList(listOfPlanets);
		return response;
	}
	
	@PostMapping(value = "/getDiff")
	public GetDiffResponseBody getDifference(@RequestBody GetDegreeRequestBody reqBody) {
		return calcService.getDifferenceOfPlanets(reqBody);
	}
	
	@PostMapping(value = "/getDiffAlone")
	public List<DegreeDiff> getDifferenceAlone(@RequestBody GetDegreeRequestBody reqBody) {
		GetDiffResponseBody withOthers= calcService.getDifferenceOfPlanets(reqBody);
		List<DegreeDiff> diff=withOthers.getDiff();
		return diff;
	}
}
