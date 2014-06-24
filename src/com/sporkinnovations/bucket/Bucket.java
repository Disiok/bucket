package com.sporkinnovations.bucket;

import java.util.ArrayList;
import java.util.Collections;

public class Bucket {
	ArrayList<Wish> mWishes;

	public Bucket() {
		mWishes = new ArrayList<Wish>();
	}
	public ArrayList<Wish> getWishes() {
		return mWishes;
	}
	
	public void addWish(Wish wish) {
		mWishes.add(wish);
	}
	@SuppressWarnings("unchecked")
	public void sortWishes() {
		Collections.sort(mWishes);
	}
}
