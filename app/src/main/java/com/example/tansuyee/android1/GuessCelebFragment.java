package com.example.tansuyee.android1;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class GuessCelebFragment extends Fragment implements View.OnClickListener{

    ArrayList<String> celebNames = new ArrayList<String>();
    ArrayList<String> celebURLs = new ArrayList<String>();
    int chosen;
    ImageView imageView;
    TextView scoreTextView, qnTextView, winnerMessage;
    int correct;
    String[] answers = new String[4];
    Button button0, button1, button2, button3, playAgainButton;
    int score = 0;
    int qnNum = 1;
    LinearLayout playAgainLayout;
    RelativeLayout relativeLayout;

    private void reset() {
        playAgainLayout.setVisibility(View.INVISIBLE);
        relativeLayout.setVisibility(View.VISIBLE);
        score = 0;
        qnNum = 0;
        scoreTextView.setText("0/10");
        qnTextView.setText("Qn 1");
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.playAgainButton) {
            reset();
        } else if (view.getTag().toString().equals(Integer.toString(correct))) {
            Toast.makeText(getActivity(), "Correct!", Toast.LENGTH_SHORT).show();
            if (qnNum < 10) {
                qnNum++;
                score++;
                qnTextView.setText("Qn " + qnNum);
                scoreTextView.setText(score + "/10");
            } else {
                playAgainLayout.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
                winnerMessage.setText("You scored " + scoreTextView.getText() + " !");
            }
        } else {
            Toast.makeText(getActivity(), "Wrong, the answer is " + celebNames.get(chosen), Toast.LENGTH_SHORT).show();
            if (qnNum < 10) {
                qnNum++;
                qnTextView.setText("Qn " + qnNum);
            } else {
                playAgainLayout.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.INVISIBLE);
                winnerMessage.setText("You scored " + scoreTextView.getText() + " !");
            }
        }

        generateQn();
    }

    public void generateQn() {
        Random rand = new Random();
        ImageDownloader imageTask = new ImageDownloader();
        chosen = rand.nextInt(celebNames.size());
        Bitmap image = null;
        try {
            image = imageTask.execute(celebURLs.get(chosen)).get();
            imageView.setImageBitmap(image);

            correct = rand.nextInt(4);
            int incorrect;
            for (int i = 0; i < 4; i++) {
                if (i == correct) {
                    answers[i] = celebNames.get(chosen);
                } else {
                    incorrect = rand.nextInt(celebURLs.size());
                    while (incorrect == correct) {
                        incorrect = rand.nextInt(celebURLs.size());
                    }
                    answers[i] = celebNames.get(incorrect);

                }
            }

            button0.setText(answers[0]);
            button1.setText(answers[1]);
            button2.setText(answers[2]);
            button3.setText(answers[3]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url;
            HttpURLConnection connection;
            InputStream is;

            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                is = connection.getInputStream();
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                return bitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strings) {
            URL url;
            HttpURLConnection urlConnection = null;
            String result = "";
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_guess_celeb, container, false);
        imageView = (ImageView) v.findViewById(R.id.imageView);
        button0 = v.findViewById(R.id.button0);
        button0.setOnClickListener(this);
        button1 = v.findViewById(R.id.button1);
        button1.setOnClickListener(this);
        button2 = v.findViewById(R.id.button2);
        button2.setOnClickListener(this);
        button3 = v.findViewById(R.id.button3);
        button3.setOnClickListener(this);
        scoreTextView = v.findViewById(R.id.scoreTextView);
        qnTextView = v.findViewById(R.id.qnTextView);
        playAgainLayout =  v.findViewById(R.id.playAgainLayout);
        relativeLayout = v.findViewById(R.id.relativeLayout);
        winnerMessage = v.findViewById(R.id.winnerMessage);
        playAgainButton = v.findViewById(R.id.playAgainButton);
        playAgainButton.setOnClickListener(this);

        DownloadTask task = new DownloadTask();
        String result = null;
        try {
            result = task.execute("http://www.posh24.se/kandisar").get();
            String[] split = result.split("<div class=\"sidebarContainer\">");
            Pattern p = Pattern.compile("<img src=\"(.*?)\"");
            Matcher matcher = p.matcher(split[0]);

            while (matcher.find()) {
                celebURLs.add(matcher.group(1));
            }

            p = Pattern.compile("alt=\"(.*?)\"");
            matcher = p.matcher(split[0]);

            while (matcher.find()) {
                celebNames.add(matcher.group(1));
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        generateQn();

        return v;
    }
}
