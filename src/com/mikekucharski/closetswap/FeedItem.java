package com.mikekucharski.closetswap;

import android.graphics.Bitmap;

public class FeedItem {
	public String id;
	public Bitmap thumbnail;
	public Bitmap categoryIcon;
	public String title;
	public String datePosted;
	public String size;
	
	public FeedItem(String id, Bitmap thumbnail, Bitmap categoryIcon, String title, String datePosted, String size) {
		this.id = id;
		this.thumbnail = thumbnail;
		this.categoryIcon = categoryIcon;
		this.title = title;
		this.datePosted = datePosted;
		this.size = size;
	}
}
