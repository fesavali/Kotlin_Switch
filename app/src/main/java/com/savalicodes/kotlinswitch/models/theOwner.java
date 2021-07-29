package com.savalicodes.kotlinswitch.models;


import io.realm.RealmObject;

public class theOwner extends RealmObject {

	private int id;

	private String logincredendial;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getLogincredendial() {
		return logincredendial;
	}

	public void setLogincredendial(String logincredendial) {
		this.logincredendial = logincredendial;
	}
}
