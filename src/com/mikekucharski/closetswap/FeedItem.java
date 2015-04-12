package com.mikekucharski.closetswap;

import android.graphics.Bitmap;

public class FeedItem {
	public Bitmap thumbnail;
	public Bitmap categoryIcon;
	public String id, title, datePosted, size, description, itemType, color;
	
	public FeedItem(String id, Bitmap thumbnail, Bitmap categoryIcon, String title, 
					String datePosted, String size, String description, String itemType, String color) {
		this.id = id;
		this.thumbnail = thumbnail;
		this.categoryIcon = categoryIcon;
		this.title = title;
		this.datePosted = datePosted;
		this.size = size;
		this.description = description;
		this.itemType = itemType;
		this.color = color;
	}
}
