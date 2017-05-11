package com.example.compress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class TamperActivity extends AppCompatActivity {

    GifImageView attackImage;
    GifImageView attackResultImage;
    Button start;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tamper);

        attackImage = (GifImageView) findViewById(R.id.attack);
        attackResultImage = (GifImageView) findViewById(R.id.attackResult);
        start = (Button) findViewById(R.id.startButton_tamper);
        resultText = (TextView) findViewById(R.id.authenticText_tamper);
    }
}
