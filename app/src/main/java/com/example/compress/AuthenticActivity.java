package com.example.compress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class AuthenticActivity extends AppCompatActivity {

    Button choose;
    Button start;
    ImageView authentic;
    GifImageView twoDataAuthenticImage;
    GifImageView authenticResultImage;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentic);

        choose = (Button) findViewById(R.id.changeButton);
        start = (Button) findViewById(R.id.startButton);
        authentic = (ImageView) findViewById(R.id.authenticImage);
        twoDataAuthenticImage = (GifImageView) findViewById(R.id.twoDataAuthenticationImage);
        authenticResultImage = (GifImageView) findViewById(R.id.authenticResult);
        resultText = (TextView) findViewById(R.id.authenticText);

    }
}
