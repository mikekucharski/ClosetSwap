package com.mikekucharski.closetswap;

import android.graphics.Bitmap;

public class FeedItem {
	public Bitmap image;
	public String name;
	public String itemType;
	public String itemSize;
	
	public FeedItem(Bitmap image, String name, String itemType, String itemSize) {
		this.image = image;
		this.name = name;
		this.itemType = itemType;
		this.itemSize = itemSize;
	}
}
