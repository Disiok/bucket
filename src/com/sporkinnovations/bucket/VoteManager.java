package com.sporkinnovations.bucket;

public class VoteManager {
	Vote mVote;
	
	public VoteManager() {
		mVote = new Vote();
	}
	public Vote getVote() {
		mVote.addInstance();
		return mVote;
	}
	public float getVotePower() {
		
		return 1f / (mVote.getInstances() + 1);
	}
}
