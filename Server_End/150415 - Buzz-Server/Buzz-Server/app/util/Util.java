package util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.codehaus.jackson.node.ObjectNode;
import org.joda.time.DateTime;

import play.libs.Json;

public class Util {
	
	public static void removeDuplicate(List<Long> inputList)
	{
		HashSet<Long> set = new HashSet<Long>(inputList);
		inputList.clear();
		inputList.addAll(set);
	}
	
	public static boolean isValidEmailId(String loginId){
		if(!loginId.contains("@")){
			return false;
		}
		if(!(loginId.endsWith(".com") || loginId.endsWith(".COM"))){
			return false;
		}
		return true;
	}

	public static Integer getLot(Map<Integer, Long> probabilities, Long allProb) {
		
		if (allProb == null) {
			allProb = new Long(0);
			
			for (Integer key: probabilities.keySet()) {
				allProb += probabilities.get(key);
			}
		}
		double lot = Math.random() * allProb.intValue();
		
		for (Integer key: probabilities.keySet()) {
			lot -= probabilities.get(key).doubleValue();

			if (lot < 0) {
				return key;
			}
		}
		return 0;
	}
	
	
	
	public static boolean findProbability(long percentage) {
		Random ran = new Random();
		int n = ran.nextInt(100);
		
		if(n<=percentage) {
			return true;
			
		} else {
			return false;
		}
	}
	public static double getRandomNumberInRange(double a1 , double a2){
		double r = Math.random() * (a2 - a1);
		r += a1;
		return r;
	}
	
	public static long currentTimeInSecs() {
		return System.currentTimeMillis() / 1000;
	}
	
	public static long currentYear() {
		Calendar now = Calendar.getInstance();
		return now.get(Calendar.YEAR);
	}
	
	public static String currentMonth() {
		Calendar now = Calendar.getInstance();
		int month = now.get(Calendar.MONTH);
		switch(month){
		case 0:
			return "January";
		case 1:
			return "February";
		case 2:
			return "March";
		case 3:
			return "April";
		case 4:
			return "May";
		case 5:
			return "June";
		case 6:
			return "July";
		case 7:
			return "August";
		case 8:
			return "September";
		case 9:
			return "October";
		case 10:
			return "November";
		case 11:
			return "December";
		default:
			return "Not Defined";
		}
	}
	
	/////////////////// REMOVE THESE FUNCTIONS AS SOON AS THEIR DEPENDENCIES ARE ELIMINATED //////////////////////
	
	public static String getCommaSeparatedString(List<String> input) {
		StringBuilder output = new StringBuilder();
		
		for (String string : input) {
			output.append(string);
			output.append(",");
		}
		
		if(output.length() > 0) {
			output.deleteCharAt(output.length()-1);
		}
		return output.toString();
	}
	
	
	public static List<Long>  getLongListFromString(String input) {
		List<Long> list = new ArrayList<Long>();
		for (String string : input.split(",")) {
			list.add(Long.parseLong(string));
		}
		return list.isEmpty()?null:list;
	}
	
	public static List<String>  getSpaceSeperatedStrings(String input) {
		List<String> list = new ArrayList<String>();
		for (String string : input.split(" ")) {
			list.add(string);
		}
		return list.isEmpty()?null:list;
	}
	
	public static List<String>  getStringListFromCSString(String input) {
		List<String> list = new ArrayList<String>();
		
		for (String string : input.split(",")) {
			list.add(string);
		}
		return list;
	}
	
	
	public static List<Long>  getLongList(List<String> input) {
		List<Long> list = new ArrayList<Long>();
		
		for (String string : input) {
			list.add(Long.parseLong(string));

		}
		return list;
	}
	
	
	public static List<String>  getStringList(List<Long> input) {
		List<String> list = new ArrayList<String>();
		
		for (Long i : input) {
			list.add(i+"");

		}
		return list;
	}
	
	public static List<Integer>  getIntegerListFromString(String input) {
		List<Integer> list = new ArrayList<Integer>();
		
		for (String string : input.split(",")) {
			list.add(Integer.parseInt(string));
		}
		return list;
	}
	
	public static ObjectNode getMapJsonOfIntLong(HashMap<Integer, Long> map) {
		 ObjectNode mapObj = Json.newObject();

		 for(Integer key : map.keySet()) {
			 mapObj.put(key+"", map.get(key));
		 }
		 return mapObj;
	 }
	
	public static boolean isValidMobageId(String mobageId) {
		
		for(int i = 0, l = mobageId.length(); i < l; i++) {
			
			char ch = mobageId.charAt(i);
			
			if(ch == '-' || ch == '.' || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z') || (ch >= '0' && ch <= '9')) {
				continue;
			}
			
			return false;
		}
		
		return true;
	}
	
	
	 public static long findTimeOfTodayForSpecficeHour(long hours){
	        long hoursToMilli = hours*3600*1000;
	         DateTime newTime = new DateTime();
	         Date day = new Date(System.currentTimeMillis() - newTime.getMillisOfDay()+hoursToMilli);       
	        return day.getTime();
	    }
	     
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
}
