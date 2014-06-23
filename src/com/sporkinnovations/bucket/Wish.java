package com.sporkinnovations.bucket;

import java.util.ArrayList;

public class Wish {
	String mMessage;
	ArrayList<Vote> mVotes;
	
	public Wish(String message) {
		mMessage = message;
		mVotes = new ArrayList<Vote>();
	}
	
	@Override
	public String toString() {
		return mMessage;
	}
	
	public boolean hasVote() {
		return !mVotes.isEmpty();
	}
	public void addVote(Vote vote) {
		mVotes.add(vote);
	}
	public float getTotalVoteValue() {
		float sum = 0;
		for (Vote vote: mVotes) {
			sum += 1f / vote.getInstances();
		}
		return sum;
	}
}
