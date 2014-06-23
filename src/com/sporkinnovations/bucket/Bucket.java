package com.sporkinnovations.bucket;

import java.util.ArrayList;

public class Bucket {
	ArrayList<Wish> mWishes;

	public Bucket() {
		mWishes = new ArrayList<Wish>();
		// Temporary wishes for testing
		mWishes.add(new Wish("Guu Izakaya"));
		mWishes.add(new Wish("Sky diving"));
		mWishes.add(new Wish("Lamb Kabob"));
		
	}
	public ArrayList<Wish> getWishes() {
		return mWishes;
	}
	
	public void addWish(Wish wish) {
		mWishes.add(wish);
	}
}
