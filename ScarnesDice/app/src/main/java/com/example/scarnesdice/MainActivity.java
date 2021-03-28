package com.example.scarnesdice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    private final int DELAY = 1500;
    private TextView score;
    private ImageView diceImage;
    private Button btnHold;
    private Button btnRoll;
    private int userOverall;
    private int userCurrent;
    private int computerOverall;
    private int computerCurrent;
    private String message;
    private Random random;
    int diceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score = findViewById(R.id.score);
        diceImage = findViewById(R.id.diceImage);
        btnHold = findViewById(R.id.button_hold);
        btnRoll = findViewById(R.id.button_roll);
        
        random = new Random();
        message = "It's your turn";

        userCurrent = 0;
        userOverall = 0;
        computerCurrent = 0;
        computerOverall = 0;
    }

    public int rollDice() {
        int number = random.nextInt(6) + 1;
        //we save the image's id
        switch (number) {
            case 1:
                diceID = R.drawable.dice1;
                break;
            case 2:
                diceID = R.drawable.dice2;
                break;
            case 3:
                diceID = R.drawable.dice3;
                break;
            case 4:
                diceID = R.drawable.dice4;
                break;
            case 5:
                diceID = R.drawable.dice5;
                break;
            case 6:
                diceID = R.drawable.dice6;
                break;
        }
        return number;
    }


    private void updateScore() {
        String text = "Your score: " + userOverall + " Computer score: " + computerOverall + "\n" + message;
        score.setText(text);
    }

    public void roll(View view) throws InterruptedException {
        int number = rollDice();
        diceImage.setImageResource(diceID);
        String text;
        if (number != 1) {
            userCurrent += number;
            if (userOverall + userCurrent >= 100) {
                userOverall += userCurrent;
                message = "You won! Congratulations!";
                btnHold.setEnabled(false);
                btnRoll.setEnabled(false);
            } else {
                message = "Your turn score: " + userCurrent;
            }
            updateScore();
        } else {
            userCurrent = 0;
            message = "You rolled a one, it's the computer's turn..";
            updateScore();
            btnRoll.setEnabled(false);
            btnHold.setEnabled(false);
            computerTurn();
        }
    }

    public void hold(View view) throws InterruptedException {
        userOverall += userCurrent;
        userCurrent = 0;
        message = "It's the computer's turn..";
        updateScore();
        btnHold.setEnabled(false);
        btnRoll.setEnabled(false);
        computerTurn();
    }

    public void reset(View view) {
        userOverall = 0;
        userCurrent = 0;
        computerOverall = 0;
        computerCurrent = 0;
        message = "It's your turn";
        btnRoll.setEnabled(true);
        btnHold.setEnabled(true);
        updateScore();
    }

    public void computerTurn() {
        String text;
        int number = rollDice();

        if (number == 1) {
            computerCurrent = 0;
            message = "Computer rolled a one";
            diceImage.setImageResource(diceID);
            updateScore();
            btnHold.setEnabled(true);
            btnRoll.setEnabled(true);
        } else if (computerCurrent >= 20) {
            computerOverall += computerCurrent;
            computerCurrent = 0;
            message = "Computer holds";
            diceImage.setImageResource(diceID);
            updateScore();
            btnHold.setEnabled(true);
            btnRoll.setEnabled(true);
        } else {
            computerCurrent += number;
            if (computerOverall + computerCurrent >= 100) {
                computerOverall += computerCurrent;
                message = "Computer won!";
            } else {
                message = "Computer's turn score: " + computerCurrent;
            }
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    updateScore();
                    diceImage.setImageResource(diceID);
                    if (computerOverall < 100)
                        computerTurn();
                }
            }, DELAY);
        }
    }
}