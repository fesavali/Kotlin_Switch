package com.savalicodes.kotlinswitch.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.sriyank.javatokotlindemo.R;
import com.sriyank.javatokotlindemo.app.Constants;


public class MainActivity extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	private EditText etName, etGithubRepoName, etLanguage, etGithubUser;
	private TextInputLayout inputName, inputLayoutRepoName, inputLayoutGithubUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		etName			 = findViewById(R.id.etName);
		etGithubRepoName = findViewById(R.id.etRepoName);
		etLanguage 		 = findViewById(R.id.etLanguage);
		etGithubUser 	 = findViewById(R.id.etGithubUser);

		inputName = findViewById(R.id.inputLayoutName);
		inputLayoutRepoName   = findViewById(R.id.inputLayoutRepoName);
		inputLayoutGithubUser = findViewById(R.id.inputLayoutGithubUser);
	}

	/** Save app username in SharedPreferences */
	public void saveName(View view) {

		if (isNotEmpty(etName, inputName)) {
			String personName = etName.getText().toString();

			SharedPreferences sp = getSharedPreferences(Constants.APP_SHARED_PREFERENCES, MODE_PRIVATE);
			SharedPreferences.Editor editor = sp.edit();
			editor.putString(Constants.KEY_PERSON_NAME, personName);

			editor.apply();
		}
	}

	/** Search repositories on github */
	public void listRepositories(View view) {

		if (isNotEmpty(etGithubRepoName, inputLayoutRepoName)) {

			String queryRepo = etGithubRepoName.getText().toString();
			String repoLanguage = etLanguage.getText().toString();

			Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
			intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_REPO);
			intent.putExtra(Constants.KEY_REPO_SEARCH, queryRepo);
			intent.putExtra(Constants.KEY_LANGUAGE, repoLanguage);
			startActivity(intent);
		}
	}

	/** Search repositories of a particular github user */
	public void listUserRepositories(View view) {

		if (isNotEmpty(etGithubUser, inputLayoutGithubUser)) {

			String githubUser = etGithubUser.getText().toString();

			Intent intent = new Intent(MainActivity.this, DisplayActivity.class);
			intent.putExtra(Constants.KEY_QUERY_TYPE, Constants.SEARCH_BY_USER);
			intent.putExtra(Constants.KEY_GITHUB_USER, githubUser);
			startActivity(intent);
		}
	}

	/** Validation */
	private boolean isNotEmpty(EditText editText, TextInputLayout textInputLayout) {
		if (editText.getText().toString().isEmpty()) {
			textInputLayout.setError("Cannot be blank !");
			return false;
		} else {
			textInputLayout.setErrorEnabled(false);
			return true;
		}
	}
}
