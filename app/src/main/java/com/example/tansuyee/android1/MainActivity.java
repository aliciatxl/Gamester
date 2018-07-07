package com.example.tansuyee.android1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    int player = 0;
    boolean gameIsOver = false;
    String message = "Red";

    int[] gameState = {2,2,2,2,2,2,2,2,2};
    int[][] winnerPositions = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};

    public void dropIn(View view) {
        ImageView counter = (ImageView) view;

        int index = Integer.parseInt(counter.getTag().toString())-1;

        if ((!gameIsOver) && (gameState[index] == 2)) {
            counter.setTranslationY(-1000f);
            gameState[index] = player;
            if (player == 0) {
                counter.setImageResource(R.drawable.red);
                player = 1;
            } else if (player == 1) {
                counter.setImageResource(R.drawable.yellow);
                player = 0;
            }

            counter.animate().translationYBy(1000f).rotation(360).setDuration(300);

            for (int[] winnerPosition: winnerPositions) {
                if (gameState[winnerPosition[0]] == gameState[winnerPosition[1]] &&
                        gameState[winnerPosition[1]] == gameState[winnerPosition[2]] &&
                        gameState[winnerPosition[0]] != 2) {

                    LinearLayout playAgainLayout = (LinearLayout) findViewById(R.id.playAgainLayout);
                    playAgainLayout.setVisibility(View.VISIBLE);
                    TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);
                    if (gameState[winnerPosition[0]] == 1) {
                        message = "Yellow";
                    }
                    winnerMessage.setText(message + " is the winner!");
                    gameIsOver = true;
                } else {
                    gameIsOver = true;

                    for (int count : gameState) {
                        if (count == 2) {
                            gameIsOver = false;
                        }
                    }

                    if (gameIsOver) {
                        LinearLayout playAgainLayout = (LinearLayout) findViewById(R.id.playAgainLayout);
                        playAgainLayout.setVisibility(View.VISIBLE);
                        TextView winnerMessage = (TextView) findViewById(R.id.winnerMessage);
                        winnerMessage.setText("Draw!");
                    }
                }
            }
        }
    }

    public void playAgain(View view) {
        gameIsOver = false;
        Log.i("Info", "Pressed");
        LinearLayout playAgainLayout = (LinearLayout) findViewById(R.id.playAgainLayout);
        playAgainLayout.setVisibility(View.INVISIBLE);
        player = 0;
        for (int j = 0; j < gameState.length; j++) {
            gameState[j] = 2;
        }
        android.support.v7.widget.GridLayout grid = (android.support.v7.widget.GridLayout) findViewById(R.id.gridLayout);
        for (int i = 0; i < grid.getChildCount(); i++) {
            ((ImageView) grid.getChildAt(i)).setImageResource(0);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
