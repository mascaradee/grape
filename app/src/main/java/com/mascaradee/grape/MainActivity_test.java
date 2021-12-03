package com.mascaradee.grape;

import static java.lang.Math.abs;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity_test extends AppCompatActivity {

    public static int sec;
    public static boolean isRunning;
    public static int stage = 1;

    public void main() {
        setContentView(R.layout.activity_main_test);

        // 버튼 클릭 시 텍스트 변경
        TextView tv = findViewById(R.id.tv_hello);
        Button btn = findViewById(R.id.btn_enter);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv.setText("넌 바보");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main();
    }
}

