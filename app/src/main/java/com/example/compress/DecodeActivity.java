package com.example.compress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class DecodeActivity extends AppCompatActivity {

    GifImageView decodeImage;
    GifImageView decodeResultImage;
    Button start;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decode);

        decodeImage = (GifImageView) findViewById(R.id.decode);
        decodeResultImage = (GifImageView) findViewById(R.id.decodeResult);
        start = (Button) findViewById(R.id.startButton_decode);
        resultText = (TextView) findViewById(R.id.authenticText_decode);

    }
}
