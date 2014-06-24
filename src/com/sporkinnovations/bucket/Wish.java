package com.sporkinnovations.bucket;

import java.util.ArrayList;

import android.content.Context;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class Wish implements Comparable{
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

	public String getMessage() {
		return mMessage;
	}
	@Override
	public int compareTo(Object another) {
		Wish otherWish = (Wish) another;
		int compareScore = (int) ((otherWish.getTotalVoteValue() - getTotalVoteValue()) * 1000);
		System.out.println(compareScore);
		return compareScore;
	}
	
	public void save(final Context context) {
		ParseObject parseWish = new ParseObject("Wish");
		parseWish.put("message", mMessage);
		parseWish.put("user", ParseUser.getCurrentUser());
		
		parseWish.saveInBackground(new SaveCallback() {

			@Override
			public void done(ParseException error) {
				if (error == null) {
					Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Error saving: " + error.getMessage(), Toast.LENGTH_SHORT).show();
				}
				
			}
			
		});
	}
}
