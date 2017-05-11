package com.example.compress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import pl.droidsonroids.gif.GifImageView;

public class EncodeActivity extends AppCompatActivity {

    Button choose;
    Button start;
    ImageView originImage;
    GifImageView resultImage;
    TextView resultText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encode);

        choose = (Button) findViewById(R.id.changeButton_encode);
        start = (Button) findViewById(R.id.startButton_encode);
        originImage = (ImageView) findViewById(R.id.originImage);
        resultImage = (GifImageView) findViewById(R.id.encodeResult);
        resultText = (TextView) findViewById(R.id.authenticText_encode);
    }
}
