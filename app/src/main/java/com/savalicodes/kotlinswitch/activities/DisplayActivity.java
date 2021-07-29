package com.savalicodes.kotlinswitch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.savalicodes.kotlinswitch.R;
import com.savalicodes.kotlinswitch.adapters.DisplayAdapter;
import com.savalicodes.kotlinswitch.models.Repositories;
import com.savalicodes.kotlinswitch.retrofit.GithubAPIService;
import com.savalicodes.kotlinswitch.retrofit.retrofitclient;
import com.sriyank.javatokotlindemo.R;
import com.sriyank.javatokotlindemo.app.Constants;
import com.sriyank.javatokotlindemo.app.Util;
import com.sriyank.javatokotlindemo.models.SearchResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.realm.Realm;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DisplayActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

	private static final String TAG = DisplayActivity.class.getSimpleName();

	private DrawerLayout mDrawerLayout;
	private RecyclerView mRecyclerView;
	private DisplayAdapter mDisplayAdapter;
	private List<Repositories> browsedRepositories;
	private GithubAPIService mService;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(findViewById(R.id.toolbar));
		getSupportActionBar().setTitle("Showing Browsed Results");

		mRecyclerView = findViewById(R.id.recyclerView);
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		mRecyclerView.setLayoutManager(layoutManager);

		mService = retrofitclient.getGithubAPIService();

		NavigationView navigationView = findViewById(R.id.navigationView);
		navigationView.setNavigationItemSelectedListener(DisplayActivity.this);

		mDrawerLayout = findViewById(R.id.drawerLayout);
		ActionBarDrawerToggle drawerToggle
				= new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
		mDrawerLayout.addDrawerListener(drawerToggle);
		drawerToggle.syncState();

		Intent intent = getIntent();
		if (intent.getIntExtra(Constants.KEY_QUERY_TYPE, -1) == Constants.SEARCH_BY_REPO) {
			String queryRepo = intent.getStringExtra(Constants.KEY_REPO_SEARCH);
			String repoLanguage = intent.getStringExtra(Constants.KEY_LANGUAGE);
			fetchRepositories(queryRepo, repoLanguage);
		} else {
			String githubUser = intent.getStringExtra(Constants.KEY_GITHUB_USER);
			fetchUserRepositories(githubUser);
		}
	}

	private void fetchUserRepositories(String githubUser) {

		mService.searchRepositoriesByUser(githubUser).enqueue(new Callback<List<Repositories>>() {
			@Override
			public void onResponse(@NonNull Call<List<Repositories>> call, Response<List<Repositories>> response) {
				if (response.isSuccessful()) {
					Log.i(TAG, "posts loaded from API " + response);

					browsedRepositories = response.body();

					if (browsedRepositories != null && browsedRepositories.size() > 0)
						setupRecyclerView(browsedRepositories);
					else
						Util.showMessage(DisplayActivity.this, "No Items Found");

				} else {
					Log.i(TAG, "Error " + response);
					Util.showErrorMessage(DisplayActivity.this, response.errorBody());
				}
			}

			@Override
			public void onFailure(Call<List<Repositories>> call, Throwable t) {
				Util.showMessage(DisplayActivity.this, t.getMessage());
			}
		});
	}

	private void fetchRepositories(String queryRepo, String repoLanguage) {

		Map<String, String> query = new HashMap<>();

		if (repoLanguage != null && !repoLanguage.isEmpty())
			queryRepo += " language:" + repoLanguage;
		query.put("q", queryRepo);

		mService.searchRepositories(query).enqueue(new Callback<SearchResponse>() {
			@Override
			public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
				if (response.isSuccessful()) {
					Log.i(TAG, "posts loaded from API " + response);

					browsedRepositories = response.body().getItems();

					if (browsedRepositories.size() > 0)
						setupRecyclerView(browsedRepositories);
					else
						Util.showMessage(DisplayActivity.this, "No Items Found");

				} else {
					Log.i(TAG, "error " + response);
					Util.showErrorMessage(DisplayActivity.this, response.errorBody());
				}
			}

			@Override
			public void onFailure(Call<SearchResponse> call, Throwable t) {
				Util.showMessage(DisplayActivity.this, t.toString());
			}
		});
	}

	private void setupRecyclerView(List<Repositories> items) {
		mDisplayAdapter = new DisplayAdapter(this, items);
		mRecyclerView.setAdapter(mDisplayAdapter);
	}

	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

		menuItem.setChecked(true);
		closeDrawer();

		switch (menuItem.getItemId()) {

			case R.id.item_bookmark:
				showBookmarks();
				getSupportActionBar().setTitle("Showing Bookmarks");
				break;

			case R.id.item_browsed_results:
				showBrowsedResults();
				getSupportActionBar().setTitle("Showing Browsed Results");
				break;
		}

		return true;
	}

	private void showBrowsedResults() {
		mDisplayAdapter.swap(browsedRepositories);
	}


	private void closeDrawer() {
		mDrawerLayout.closeDrawer(GravityCompat.START);
	}

	@Override
	public void onBackPressed() {
		if (mDrawerLayout.isDrawerOpen(GravityCompat.START))
			closeDrawer();
		else {
			super.onBackPressed();
			mRealm.close();
		}
	}
}
