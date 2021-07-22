package com.scraper.service;

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
import org.springframework.stereotype.Service;

import com.scraper.model.AggregatedPos;
import com.scraper.model.GetDegreeRequestBody;
import com.scraper.model.GetDegreeResponseBody;
import com.scraper.model.PlanetPosition;

@Service
public class ScraperService {

	final String OldCred = "bemolek452@dedatre.com";
	
	
	public List<PlanetPosition> getDegree(GetDegreeRequestBody reqbody) {
		final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36";
		final String LOGIN_FORM_URL = "https://www.astrosage.com/";
		final String LOGIN_ACTION_URL = "https://ascloud.astrosage.com/LoginServlet";
		final String callback = "myCallback";
		final String email = "hotebeg120@dmsdmg.com";
		final String password = "hotebeg120@dmsdmg.com";
		final String uname = "";
		final String code = "login";
		final String savecookie = "false";
		final String fromastrosage = "1";
		final String newChartURL = "https://ascloud.astrosage.com/cloud/session-destroy-for-newchart.asp?methodName=newChart";
		final String newChart = "https://ascloud.astrosage.com/cloud/newchart-session.asp";
		final String position = "https://ascloud.astrosage.com/cloud/planetposition.asp";
		try {

			Connection.Response loginForm = Jsoup.connect(LOGIN_FORM_URL).method(Connection.Method.GET)
					.userAgent(USER_AGENT).execute();
			Document loginDoc = loginForm.parse();
			HashMap<String, String> cookies = new HashMap<>(loginForm.cookies());
			HashMap<String, String> formData = new HashMap<>();
			formData.put("callback", callback);
			formData.put("email", email);
			formData.put("password", password);
			formData.put("uname", uname);
			formData.put("code", code);
			formData.put("savecookie", savecookie);
			formData.put("fromastrosage", fromastrosage);

			Connection.Response homePage = Jsoup.connect(LOGIN_ACTION_URL).cookies(cookies).data(formData)
					.method(Connection.Method.GET).header("referer", "https://www.astrosage.com/").userAgent(USER_AGENT)
					.execute();

			Connection.Response dobPage = Jsoup.connect(newChartURL).cookies(homePage.cookies())
					.method(Connection.Method.GET).userAgent(USER_AGENT).execute();
			/*
			 * System.out.println(homePage.parse().html());
			 * System.out.println("======================================");
			 * System.out.println(dobPage.parse().html());
			 */

			formData.clear();
			String name = "nameisthere";
			String sex = "male";
			formData.put("name", name);
			formData.put("sex", sex);
			formData.put("day", reqbody.getDay());
			formData.put("month", reqbody.getMonth());
			formData.put("year", reqbody.getYear());
			formData.put("hrs", reqbody.getHrs());
			formData.put("min", reqbody.getMin());
			formData.put("sec", reqbody.getSec());
			formData.put("place", "erode");
			formData.put("longdeg", "77");
			formData.put("longmin", "33");
			formData.put("longew", "E");
			formData.put("latdeg", "11");
			formData.put("latmin", "20");
			formData.put("latns", "N");
			formData.put("timezone", "5.5");
			formData.put("kphn", "0");
			formData.put("dst", "0");
			formData.put("ayanamsa", "0");
			formData.put("charting", "0");
			formData.put("timezoneid","Asia/Kolkata");
			formData.put("accuracy","");
			formData.put("languagecode","0");
			formData.put("methodName", "newChartDone");
			Connection.Response afterDOB = Jsoup.connect(newChart).cookies(dobPage.cookies()).data(formData)
					.method(Connection.Method.POST).userAgent(USER_AGENT).execute();

			Connection.Response kochara = Jsoup.connect(position).cookies(afterDOB.cookies()).data(formData)
					.method(Connection.Method.GET).userAgent(USER_AGENT).execute();

			Document doc = kochara.parse();
			
			Element dateTime=doc.select("div.collapsible-body").first();
			
			System.out.println(dateTime);
			
			
			Element table = doc.selectFirst("#ui-table>tbody");
			Iterator<Element> row = table.select("tr").iterator();
			
			//row.next(); // Skipping the <th>
			List<PlanetPosition> listOfPlanets=new ArrayList<PlanetPosition>();
			while (row.hasNext()) {
				PlanetPosition thisPlanet=new PlanetPosition();
				Iterator<Element> ite = ((Element) row.next()).select("td").iterator();
				//Element text=((Element) ite).selectFirst("div:eq(1)");
				List<Node> textList=((Element) ite.next()).childNodes();
				String text=textList.get(textList.size()-1).toString();
				if(text==null)
				text= ite.next().text();
				//System.out.println("name:"+text);
				
				//important line
				System.out.println(ite.next());System.out.println(ite.next());
				//above line important
				
				
				String rasi=ite.next().text();
				//System.out.println("rasi"+rasi);
				//System.out.println(ite.next());
				String degree=ite.next().text();
				thisPlanet.setName(text);
				thisPlanet.setDegree(degree);
				thisPlanet.setRasi(rasi);
				thisPlanet.setAggrPos(new AggregatedPos(thisPlanet));
				listOfPlanets.add(thisPlanet);
			}
//			for(PlanetPosition planet:listOfPlanets) {
//				//System.out.println(""+planet.getName()+"= "+planet.getDegree());
//			}
//			while (row.hasNext()) {
//				Iterator<Element> ite = ((Element) row.next()).select("div:eq(1)").iterator();
//				
//				Element elem= ite.next();
//				//Element text= elem.selectFirst("div+");
//				System.out.print("name: " + elem.text());
//				
//				break;
//			}
			
			//System.out.println(kochara.parse().html());
			return listOfPlanets;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new ArrayList<PlanetPosition>();
		}
	}
	
	
	
	public GetDegreeRequestBody yesterdayThisTime(GetDegreeRequestBody today) {
		final long MILLIS_IN_A_DAY = 1000 * 60 * 60 * 24;
		Date todaysDate=new Date();
		todaysDate.setDate(Integer.parseInt(today.getDay()));
		todaysDate.setMonth((Integer.parseInt(today.getMonth())));
		todaysDate.setYear(Integer.parseInt(today.getYear()));
		
		Date yesterdaysDate = new Date(todaysDate.getTime() - MILLIS_IN_A_DAY);
		GetDegreeRequestBody yesterday=new GetDegreeRequestBody();
		yesterday.setDay(String.valueOf(yesterdaysDate.getDate()));
		yesterday.setMonth(String.valueOf(yesterdaysDate.getMonth()));;
		yesterday.setYear(String.valueOf(yesterdaysDate.getYear()));
		yesterday.setHrs(today.getHrs());
		yesterday.setMin(today.getMin());
		yesterday.setSec(today.getSec());
		
		return yesterday;
	}
}
