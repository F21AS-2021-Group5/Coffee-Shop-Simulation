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

//Log implemented as Singleton
public class Log {
	
	private static Log instance; //Volatile modifier
	private String log;
	
	private void Log() {
		log ="Coffee Shop Log\n\n"; 
	}
	
	public static Log getInstance() {
		//Using Double-checked locking
		if(instance == null) { //Read
			synchronized(Log.class) {
				if(instance == null) { //Read
					instance = new Log(); //Write
				}
			}
		}
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
