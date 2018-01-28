package com.almundo.app.config;

import java.util.Random;

/**
 * This class alows to configure some values
 * 
 */
public class Config {
	
	public static int TIME_MIN = 5000;
	public static int TIME_MAX = 10000;
	
	/**
	 * This method generates a random value 
	 * between 5 and 10 seconds
	 *
	 * @return the long => random time
	 */
	public static Long asignRandomTimeToTask(){
		Random randomGenerator = new Random();
		int value = randomGenerator.nextInt(TIME_MAX - TIME_MIN + 1) + TIME_MIN;
		return new Long(value);
	}

}
