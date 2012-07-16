package com.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;

public class Sudoku extends Activity implements OnClickListener {

	private static final String TAG = "Suduko";

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// set up click listeners for all the buttons
		View continueButton = findViewById(R.id.continue_button);
		continueButton.setOnClickListener(this);
		View newButton = findViewById(R.id.newGame_button);
		newButton.setOnClickListener(this);
		View aboutButton = findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		View exitButton = findViewById(R.id.exit_button);
		exitButton.setOnClickListener(this);

	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.about_button:
			Intent I = new Intent(this, About.class);
			startActivity(I);
			break;
		case R.id.newGame_button:
			Log.d(TAG, "cliclikn");
			openNewGameDialog();
			
			break;
		case R.id.exit_button:
			finish();
			break;
		case R.id.continue_button:
			startGame(Game.DIFFICULTY_CONTINUE);
			break;
		default:
			break;
		}
	}

	private void openNewGameDialog() {
		// TODO Auto-generated method stub
		
		new AlertDialog.Builder(this).setTitle(R.string.new_game_title).setItems(R.array.difficuly, new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startGame(which);
			}

			
		}).show();
	}
		private void startGame(int which) {
			// TODO Auto-generated method stub
			Log.d(TAG, "clicked on"+which);
			Intent i = new Intent(Sudoku.this,Game.class);
			i.putExtra(Game.KEY_DIFFICULTY, which);
			startActivity(i);
			
			//start game here;
		}
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(new Intent(this, Prefs.class));
			return true;
		}
		return false;
	}
}
