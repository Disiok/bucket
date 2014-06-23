package com.sporkinnovations.bucket;

public class Wish {
	String mMessage;
	
	public Wish(String message) {
		mMessage = message;
	}
	
	@Override
	public String toString() {
		return mMessage;
	}
}
