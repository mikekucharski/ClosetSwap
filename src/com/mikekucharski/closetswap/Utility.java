package com.mikekucharski.closetswap;

import java.text.DateFormat;
import java.util.Date;

public class Utility {
	
	// Converts a Date object to the desired string format
	public static String dateFormat(Date d) {
		return DateFormat.getInstance().format(d);
	}
	
	// returns the image resource filename from the category
	// Ex: T-Shirt -> 't_shirt.png'
	public static String getImageName(String category) {
		category = category.trim().replace('-', '_').replace(' ', '_').toLowerCase();
		return "clothing_" + category;
	}
}
