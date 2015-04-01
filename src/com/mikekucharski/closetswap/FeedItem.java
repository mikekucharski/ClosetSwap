package com.mikekucharski.closetswap;

import android.graphics.Bitmap;

public class FeedItem {
	public String id;
	public Bitmap image;
	public String name;
	public String itemType;
	public String itemSize;
	
	public FeedItem(String id, Bitmap image, String name, String itemType, String itemSize) {
		this.id = id;
		this.image = image;
		this.name = name;
		this.itemType = itemType;
		this.itemSize = itemSize;
	}
}
