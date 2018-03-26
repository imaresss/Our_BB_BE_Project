package ieee.donn.Services;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import ieee.donn.R;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Pulkit on 05-03-2018.
 */


    public class ResultActivity extends AppCompatActivity {
        private TextView textView ;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            textView = (TextView)findViewById(R.id.textView1);
            textView.setText("Welcome to the Result Activity");
        }
    }
