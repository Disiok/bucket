package com.sporkinnovations.bucket.model;

public class Vote {
	private int mInstances;
	
	public Vote() {
		mInstances = 0;
	}

	public void addInstance() {
		mInstances ++;		
	}
	
	public int getInstances() {
		return mInstances;
	}
}
