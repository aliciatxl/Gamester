package com.example.tansuyee.android1;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;


public class BrainTrainerFragment extends Fragment implements View.OnClickListener {
    Button startButton;
    TextView resultTextView, pointsTextView, winnerMessage;
    Button button0, button1, button2, button3;
    TextView sumTextView, timerTextView;
    Button playAgainButton;
    RelativeLayout gameRelativeLayout;
    LinearLayout playAgainLayout;

    ArrayList<Integer> answers = new ArrayList<Integer>();
    int locationOfCorrectAnswer;
    int score = 0;
    int numberOfQuestions = 0;

    public void playAgain() {
        playAgainLayout.setVisibility(View.INVISIBLE);
        gameRelativeLayout.setVisibility(View.VISIBLE);
        score = 0;
        numberOfQuestions = 0;

        timerTextView.setText("30s");
        pointsTextView.setText("0/0");
        resultTextView.setText("");
        playAgainButton.setVisibility(View.INVISIBLE);

        generateQuestion();

        new CountDownTimer(30100, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

                timerTextView.setText(String.valueOf(millisUntilFinished / 1000) + "s");

            }

            @Override
            public void onFinish() {
                gameRelativeLayout.setVisibility(View.INVISIBLE);
                playAgainButton.setVisibility(View.VISIBLE);
                timerTextView.setText("0s");
                playAgainLayout.setVisibility(View.VISIBLE);
                winnerMessage.setText("Your score: " + Integer.toString(score) + "/" + Integer.toString(numberOfQuestions));

            }
        }.start();


    }

    public void generateQuestion() {

        Random rand = new Random();

        int a = rand.nextInt(21);
        int b = rand.nextInt(21);

        sumTextView.setText(Integer.toString(a) + " + " + Integer.toString(b));

        locationOfCorrectAnswer = rand.nextInt(4);

        answers.clear();

        int incorrectAnswer;

        for (int i=0; i<4; i++) {

            if (i == locationOfCorrectAnswer) {

                answers.add(a + b);

            } else {

                incorrectAnswer = rand.nextInt(41);

                while (incorrectAnswer == a + b) {

                    incorrectAnswer = rand.nextInt(41);

                }

                answers.add(incorrectAnswer);

            }

        }

        button0.setText(Integer.toString(answers.get(0)));
        button1.setText(Integer.toString(answers.get(1)));
        button2.setText(Integer.toString(answers.get(2)));
        button3.setText(Integer.toString(answers.get(3)));



    }

    public void chooseAnswer(View view) {

        if (view.getTag().toString().equals(Integer.toString(locationOfCorrectAnswer))) {

            score++;
            resultTextView.setText("Correct!");

        } else {

            resultTextView.setText("Wrong!");

        }

        numberOfQuestions++;
        pointsTextView.setText(Integer.toString(score) + "/" + Integer.toString(numberOfQuestions));
        generateQuestion();


    }

    public void start() {

        startButton.setVisibility(View.INVISIBLE);
        gameRelativeLayout.setVisibility(RelativeLayout.VISIBLE);

        playAgain();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_brain_trainer, container, false);
        playAgainButton = v.findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(this);
        startButton = v.findViewById(R.id.startButton);
        startButton.setOnClickListener(this);
        sumTextView = v.findViewById(R.id.sumTextView);
        button0 = v.findViewById(R.id.button0);
        button0.setOnClickListener(this);
        button1 = v.findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2 = v.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = v.findViewById(R.id.button3);
        button3.setOnClickListener(this);
        resultTextView = v.findViewById(R.id.resultTextView);
        winnerMessage = v.findViewById(R.id.winnerMessage);
        pointsTextView = v.findViewById(R.id.pointsTextView);
        timerTextView = v.findViewById(R.id.timerTextView);
        playAgainLayout = v.findViewById(R.id.playAgainLayout);
        gameRelativeLayout = v.findViewById(R.id.gameRelativeLayout);
        return v;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.playAgainButton:
                playAgain();
                break;
            case R.id.startButton:
                start();
                break;
            default:
                chooseAnswer(view);
        }
    }
}
