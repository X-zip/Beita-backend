package utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class UUIDGenerator{
	/**
	 * 获得32位的UUID
	 * 
	 * @return
	 */
	public static String random32UUID() {
		/*UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		// 去掉"-"符号
		String temp = str.substring(0, 8) + str.substring(9, 13)
				+ str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
		return temp.toUpperCase();*/
		return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
		
	}
	
	/**
	 * 获得不重复的 12位
	 * @param args
	 */
	public static String random12UUID(){
		String res=new Date().getTime()-1300000000000L+"";
		
		return res;
	}
	
	public static String random11UUID(){
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		String res=Integer.toString(year)+UUID.randomUUID().toString().replaceAll("-", "").toUpperCase().substring(2, 9);
		return res;
	}
	
	public static  String getnext(String manuMark){		
		int max=999;
		int min=100;
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmss");
		long timeMillis=System.currentTimeMillis();
		String dateStr = dateformat.format(timeMillis);				
		//long secondNow=timeMillis%1000;	
		int secondNow=new Random().nextInt(max)%(max-min+1) + min;
		StringBuffer nextid=new StringBuffer();
		nextid.append(manuMark).append(dateStr).append(secondNow);		
		return nextid.toString();
	}
	public static  String getNextByTime(){		
		SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");//17位
		String dateStr = dateformat.format(new Date());				
		StringBuffer nextid=new StringBuffer();
		nextid.append("GP").append(dateStr);
		return nextid.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis()%1000);
	}
}
