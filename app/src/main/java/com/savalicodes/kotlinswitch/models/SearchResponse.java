package com.savalicodes.kotlinswitch.models;


import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;

public class SearchResponse extends RealmObject {

	@SerializedName("total_counting")
	private int totalCount;

	private RealmList<Repositories> items;

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<Repositories> getItems() {
		return items;
	}

	public void setItems(RealmList<Repositories> items) {
		this.items = items;
	}
}
