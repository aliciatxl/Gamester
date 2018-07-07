package com.example.tansuyee.android1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class TicTacToeFragment extends Fragment implements View.OnClickListener {

    int player = 0;
    boolean gameIsOver = false;
    String message = "Red";
    View v;

    int[] gameState = {2,2,2,2,2,2,2,2,2};
    int[][] winnerPositions = {{0,1,2}, {3,4,5}, {6,7,8}, {0,3,6}, {1,4,7}, {2,5,8}, {0,4,8}, {2,4,6}};
    RelativeLayout relativeLayout;
    LinearLayout playAgainLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_tic_tac_toe, container, false);
        v.findViewById(R.id.c1).setOnClickListener(this);
        v.findViewById(R.id.c2).setOnClickListener(this);
        v.findViewById(R.id.c3).setOnClickListener(this);
        v.findViewById(R.id.c4).setOnClickListener(this);
        v.findViewById(R.id.c5).setOnClickListener(this);
        v.findViewById(R.id.c6).setOnClickListener(this);
        v.findViewById(R.id.c7).setOnClickListener(this);
        v.findViewById(R.id.c8).setOnClickListener(this);
        v.findViewById(R.id.c9).setOnClickListener(this);
        relativeLayout = v.findViewById(R.id.relativeLayout);
        playAgainLayout = v.findViewById(R.id.playAgainLayout);
        v.findViewById(R.id.playAgain).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playAgain();
            }
        });

        return v;
    }

    public void playAgain() {
        gameIsOver = false;
        relativeLayout.setVisibility(View.VISIBLE);
        playAgainLayout.setVisibility(View.INVISIBLE);
        player = 0;
        for (int j = 0; j < gameState.length; j++) {
            gameState[j] = 2;
        }
        android.support.v7.widget.GridLayout grid = v.findViewById(R.id.gridLayout);
        for (int i = 0; i < grid.getChildCount(); i++) {
            ((ImageView) grid.getChildAt(i)).setImageResource(0);
        }
    }

    @Override
    public void onClick(View view) {
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

                    LinearLayout playAgainLayout = (LinearLayout) v.findViewById(R.id.playAgainLayout);
                    playAgainLayout.setVisibility(View.VISIBLE);
                    relativeLayout.setVisibility(View.INVISIBLE);
                    TextView winnerMessage = (TextView) v.findViewById(R.id.winnerMessage);
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
                        LinearLayout playAgainLayout = (LinearLayout) v.findViewById(R.id.playAgainLayout);
                        playAgainLayout.setVisibility(View.VISIBLE);
                        TextView winnerMessage = (TextView) v.findViewById(R.id.winnerMessage);
                        winnerMessage.setText("Draw!");
                    }
                }
            }
        }
    }
}
