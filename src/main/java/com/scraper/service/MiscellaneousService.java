package com.scraper.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.scraper.model.AggregatedPos;
import com.scraper.model.DegreeDiff;
import com.scraper.model.GetDegreeRequestBody;
import com.scraper.model.GetDiffResponseBody;
import com.scraper.model.PlanetPosition;

@Service
public class MiscellaneousService {
	
	@Autowired
	ScraperService service;
	
	public static int getAggrDeg(int deg, String rasi) {
		int extraDeg = -9999;
		if (rasi.equals("Aries")) {
			extraDeg = 0;
		} else if (rasi.equals("Taurus")) {
			extraDeg = 30;
		} else if (rasi.equals("Gemini")) {
			extraDeg = 60;
		} else if (rasi.equals("Cancer")) {
			extraDeg = 90;
		} else if (rasi.equals("Leo")) {
			extraDeg = 120;
		} else if (rasi.equals("Virgo")) {
			extraDeg = 150;
		} else if (rasi.equals("Libra")) {
			extraDeg = 180;
		} else if (rasi.equals("Scorpion")) {
			extraDeg = 210;
		} else if (rasi.equals("Sagittarius")) {
			extraDeg = 240;
		} else if (rasi.equals("Capricorn")) {
			extraDeg = 270;
		} else if (rasi.equals("Aquarius")) {
			extraDeg = 300;
		} else if (rasi.equals("Pisces")) {
			extraDeg = 330;
		}

		return extraDeg + deg;
	}
	
	public static DegreeDiff posDiff(PlanetPosition today,PlanetPosition yesterday) {
		AggregatedPos tod=today.getAggrPos();
		AggregatedPos yes=yesterday.getAggrPos();
		System.out.println("today: "+tod);
		System.out.println("yesterday: "+yes);
		DegreeDiff diff=new DegreeDiff();
		long todAggrSec=tod.getSec()+tod.getMin()*60+tod.getAggrDeg()*60*60;
		long yesAggrSec=yes.getSec()+yes.getMin()*60+yes.getAggrDeg()*60*60;
		if(today.getRasi().equals("Aries") && yesterday.getRasi().equals("Pisces")) {
			todAggrSec+=360*60*60;
		}
		if(yesterday.getRasi().equals("Aries") && today.getRasi().equals("Pisces")) {
			 yesAggrSec+=360*60*60;
		}
		long differenceInSec=todAggrSec-yesAggrSec;
		if(differenceInSec<0) {
			diff.setReverse(true);
			differenceInSec*=-1;
		}else {
			diff.setReverse(false);
		}
		int secDiff=(int) (differenceInSec%60);
		differenceInSec/=60;
		int minDiff=(int) (differenceInSec%60);
		differenceInSec/=60;
		int degDiff=(int) (differenceInSec%60);
		diff.setDeg(degDiff);
		diff.setMin(minDiff);
		diff.setSec(secDiff);
		/*
		 * int diffInSec=tod.getSec()-yes.getSec(); int
		 * diffInMin=tod.getMin()-yes.getMin(); int
		 * diffInDeg=tod.getAggrDeg()-yes.getAggrDeg(); if(diffInSec<0) {
		 * tod.setMin(tod.getMin()-1); diff.setSec(diffInSec+60); }else
		 * diff.setSec(diffInSec);
		 * 
		 * if(diffInMin<0) { tod.setAggrDeg(tod.getAggrDeg()-1);
		 * diff.setMin(diffInMin+60); }else diff.setMin(diffInMin);
		 * 
		 * if(diffInDeg<0) { diff.setAggrDeg(diffInDeg+360); }else
		 * diff.setAggrDeg(diffInDeg);
		 */
		//System.out.println(diffInDeg+":"+diffInMin+":"+diffInSec+"==="+diff.toString());
		return diff;
	}
	
	public static List<DegreeDiff> diffList(List<PlanetPosition> todayPlanets,List<PlanetPosition> yesterdayPlanets){
		List<DegreeDiff> diff=new ArrayList<DegreeDiff>();
		for(int i=0;i<todayPlanets.size();i++) {
			PlanetPosition today=todayPlanets.get(i);
			PlanetPosition yesterday=yesterdayPlanets.get(i);
			
			
			DegreeDiff thisDiff=posDiff(today, yesterday);
			thisDiff.setName(today.getName());
			
			
			diff.add(thisDiff);
		}
		return  diff;
	}
	
	public GetDiffResponseBody getDifferenceOfPlanets(@RequestBody GetDegreeRequestBody reqBody) {
		GetDegreeRequestBody today=reqBody;
		List<PlanetPosition> todayPlanets=service.getDegree(today);
		System.out.println("today:"+todayPlanets);
		GetDegreeRequestBody yesterday=service.yesterdayThisTime(today);
		System.out.println("today date:"+today);
		
		GetDegreeRequestBody fakeAddition=new GetDegreeRequestBody();
		fakeAddition.setDay(yesterday.getDay());
		fakeAddition.setMonth(yesterday.getMonth());
		fakeAddition.setYear(yesterday.getYear());
		fakeAddition.setHrs(yesterday.getHrs());
		fakeAddition.setMin(yesterday.getMin());
		fakeAddition.setSec(yesterday.getSec());
		
		System.out.println("yesterday date:"+fakeAddition);
		List<PlanetPosition> yesterdayPlanets=service.getDegree(fakeAddition);
		
		System.out.println("yesterday:"+yesterdayPlanets);
		
		List<DegreeDiff> diff=MiscellaneousService.diffList(todayPlanets, yesterdayPlanets);
		GetDiffResponseBody response=new GetDiffResponseBody();
		response.setToday(todayPlanets);
		response.setYesterday(yesterdayPlanets);
		response.setDiff(diff);
		return response; 
	}
	
	
}
