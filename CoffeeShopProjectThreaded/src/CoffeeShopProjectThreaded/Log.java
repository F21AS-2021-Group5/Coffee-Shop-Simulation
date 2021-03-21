/**
 * Log.java - class to update and get log 
 * 
 * @author Esther Rayssiguie 
 * @author Jake Marrocco
 * @author Karolina Judzentyte
 * @author Valerio Franchi
 * @version 0.1
 * 
 * Copyright (c) 2021 
 * All rights reserved.
 */

package CoffeeShopProjectThreaded;

public class Log {
	
	private static Log instance = new Log();
	private String log;
	
	private void Log() {
		log ="Coffee Shop Log\n\n"; 
	}
	
	public static Log getInstance() {
		return instance;
	}
	
	public void updateLog(String info) {
		log += info + "\n";
		System.out.println(info);
	}
	
	public String getLog() {
		return log;
	}

}
