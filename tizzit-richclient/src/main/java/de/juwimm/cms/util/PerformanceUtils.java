package de.juwimm.cms.util;

import org.apache.log4j.Logger;

public class PerformanceUtils {
	private static Logger log = Logger.getLogger(PerformanceUtils.class);
	
	private static long timestamp=-1;
	
	
	public static void start(){
		if(timestamp!=-1){
			throw new RuntimeException("Timer already started");
		}
		timestamp=System.currentTimeMillis();
	}
	
	public static long mark(){
		if(timestamp==-1){
			throw new RuntimeException("Timer not started");
		}
		long duration=System.currentTimeMillis()-timestamp;
		timestamp=System.currentTimeMillis();
		return duration;
	}

	public static long mark(String operation){
		if(timestamp==-1){
			throw new RuntimeException("Timer not started");
		}
		long duration=System.currentTimeMillis()-timestamp;
		Double d=new Double((double)duration/1000);
		if(log!=null){
			log.info("PERFORMANCE - "+operation+": "+d.doubleValue()+" seconds");
		} else {
			System.out.println("PERFORMANCE - "+operation+": "+d.doubleValue()+" seconds");
		}
		
		timestamp=System.currentTimeMillis();
		return duration;
	}

	public static void stop(){
		if(timestamp==-1){
			throw new RuntimeException("Timer not started");
		}
		timestamp=-1;
	}
}
