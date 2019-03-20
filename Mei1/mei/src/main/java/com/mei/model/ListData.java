package com.mei.model;

import java.io.Serializable;
import java.util.List;

public class ListData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private List<item> jokeItems;

	public List<item> getJokeItems() {
		return jokeItems;
	}

	public void setJokeItems(List<item> jokeItems) {
		this.jokeItems = jokeItems;
	}
}
