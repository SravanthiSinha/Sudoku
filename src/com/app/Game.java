package com.app;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.Toast;

public class Game extends Activity {

	private static final String TAG = "Sudoku";

	static final String KEY_DIFFICULTY = "com.app.sudoku.difficulty";

	private static final int DIFFICULTY_EASY = 0;
	private static final int DIFFICULTY_MEDIUM = 1;
	private static final int DIFFICULTY_HARD = 2;

	private static final String PREF_PUZZLE = "puzzle";

	protected static final int DIFFICULTY_CONTINUE = -1;
	private int puzzle[] = new int[9 * 9];

	private PuzzleView puzzleView;

	private int[][][] used = new int[9][9][];

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Music.stop(this);
		
		//save current puzzle
		
		getPreferences(MODE_PRIVATE).edit().putString(PREF_PUZZLE, toPuzzleString(puzzle)).commit();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Music.play(this,R.raw.game);
	}

	private final String easyPuzzle = "360000000004230800000004200"
			+ "070460003820000014500013020" + "001900000007048300000000045";
	private final String mediumPuzzle = "360056000004230800000004200"
			+ "070460003820000014500013020" + "001900000007048300000000045";
	private final String hardPuzzle = "360000000004230800000004200"
			+ "070460003820000014500013020" + "001900000007048300000000045";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d(TAG, "onCreate");
		int diff = getIntent().getIntExtra(KEY_DIFFICULTY, DIFFICULTY_EASY);
		getIntent().putExtra(KEY_DIFFICULTY, DIFFICULTY_CONTINUE);
		puzzle = getPuzzle(diff);
		calculateUsedTiles();

		puzzleView = new PuzzleView(this);
		setContentView(puzzleView);
		puzzleView.requestFocus();

	}

	private int[] getPuzzle(int diff) {
		// TODO Auto-generated method stub
		String puz;
		switch (diff) {
		case DIFFICULTY_CONTINUE:
			puz=getPreferences(MODE_PRIVATE).getString(PREF_PUZZLE, easyPuzzle);
			break;
		case DIFFICULTY_HARD:
			puz = hardPuzzle;
			break;
		case DIFFICULTY_MEDIUM:
			puz = mediumPuzzle;
			break;
		case DIFFICULTY_EASY:
			puz = easyPuzzle;
			break;
		default:
			puz = easyPuzzle;
			break;
		}
		return fromPuzzleString(puz);
	}

	static protected int[] fromPuzzleString(String string) {
		// TODO Auto-generated method stub

		int[] puz = new int[string.length()];
		for (int i = 0; i < puz.length; i++) {
			puz[i] = string.charAt(i) - '0';

		}
		return puz;
	}

	private void calculateUsedTiles() {
		// TODO Auto-generated method stub
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				used[i][j] = calculateUsedTiles(i, j);
			}
		}
	}

	private int[] calculateUsedTiles(int x, int y) {
		// TODO Auto-generated method stub
		int c[] = new int[9];

		for (int i = 0; i < 9; i++) {
			if (i == y)
				continue;
			int t = getTile(x, i);
			if (t != 0)
				c[t - 1] = t;
		}

		for (int i = 0; i < 9; i++) {
			if (i == x)
				continue;
			int t = getTile(i, y);
			if (t != 0)
				c[t - 1] = t;

		}
		int startx = (x / 3) * 3;
		int starty = (y / 3) * 3;

		for (int i = startx; i < startx + 3; i++) {
			for (int j = starty; j < starty + 3; j++) {
				if (i == x && j == y) 
					continue;
					int t = getTile(i, j);
					if (t != 0) 
						c[t - 1] = t;
					
				}
			
		}

		int nused = 0;

		for (int t : c) {
			if (t != 0) {
				nused++;
			}

		}
		int c1[] = new int[nused];
		nused = 0;
		for (int t : c) {
			if (t != 0) {
				c1[nused++] = t;
			}

		}
		return c1;
	}

	private int getTile(int x, int y) {
		// TODO Auto-generated method stub
		return puzzle[y * 9 + x];

	}

	private void setTile(int x, int y, int value) {
		// TODO Auto-generated method stub
		puzzle[y * 9 + x] = value;
		;

	}

	protected void showKeypadOrError(int x, int y) {
		int tiles[] = getUsedTiles(x, y);
		if (tiles.length == 9) {
			Toast toast = Toast.makeText(this, R.string.no_moves_label,
					Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
		} else {
			Log.d(TAG, "showkeypad:used=" + toPuzzleString(tiles));
			Dialog v = new Keypad(this, tiles, puzzleView);
			v.show();
		}
	}

	static protected String toPuzzleString(int[] puz) {
		// TODO Auto-generated method stub
		StringBuilder buf = new StringBuilder();
		for (int element : puz) {
			buf.append(element);
		}
		return buf.toString();
	}

	protected boolean setTileIfValid(int x, int y, int value) {
		int tiles[] = getUsedTiles(x, y);
		if (value != 0) {
			for (int t : tiles) {
				if (t == value) {
					return false;
				}
			}
		}
		setTile(x, y, value);
		calculateUsedTiles();
		return true;
	}

	protected int[] getUsedTiles(int x, int y) {
		// TODO Auto-generated method stub
		return used[x][y];
	}

	protected String getTileString(int x, int y) {
		int v = getTile(x, y);
		if (v == 0) {
			return "";
		} else
			return String.valueOf(v);
	}
}
