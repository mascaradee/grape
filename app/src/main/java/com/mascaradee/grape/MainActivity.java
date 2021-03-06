package com.mascaradee.grape;

import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static int sec;
    private static boolean isRunning;
    private static int stage = 1;
    private static int p_num = 3; // 참가인원
    private static int k = 1; // 기준
    List<Float> p_list = new ArrayList<>();
    private static boolean isBlind = false;
    private AdView adView;
    private AdView adView2;
    private AdView adView3;


    public void start() {
        setContentView(R.layout.activity_start);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);


        TextView tv_pnum = findViewById(R.id.tv_pnum);
        Button btn_minus = findViewById(R.id.btn_minus);
        Button btn_plus = findViewById(R.id.btn_plus);
        Button btn_start = findViewById(R.id.btn_start);
        Button btn_blind = findViewById(R.id.btn_blind);

        tv_pnum.setText(String.valueOf(p_num));
        btn_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p_num--;
                if (p_num == 0) {
                    p_num = 1;
                }
                tv_pnum.setText(String.valueOf(p_num));
            }
        });
        btn_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p_num++;
                tv_pnum.setText(String.valueOf(p_num));
            }
        });
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main();
            }
        });

        btn_blind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isBlind = !isBlind;
                if (isBlind == true) {
                    btn_blind.setText("Blind 모드 ON");
                } else {
                    btn_blind.setText("Blind 모드 OFF");
                }
            }
        });
    }

    public void end() {
        setContentView(R.layout.activity_end);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView3= findViewById(R.id.adView3);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView3.loadAd(adRequest);

        TextView tv_lpoint = findViewById(R.id.tv_lpoint);
        TextView tv_last = findViewById(R.id.tv_last);
        Button btn_init = findViewById(R.id.btn_init);

        Float max = p_list.isEmpty() ? Float.valueOf(0) : Collections.max(p_list);
        tv_lpoint.setText(String.valueOf(max));

        int idx = p_list.indexOf(max);
        tv_last.setText("참가자" + (idx + 1));

        btn_init.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p_list.clear();
                k = 1;
                start();
            }
        });
    }

    public void main() {
        setContentView(R.layout.activity_main);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        adView2= findViewById(R.id.adView2);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView2.loadAd(adRequest);

        TextView tv = findViewById(R.id.tv_pnum);
        TextView tv_t = findViewById(R.id.tv_timer);
        TextView tv_p = findViewById(R.id.tv_point);
        TextView tv_people = findViewById(R.id.tv_people);
        Button btn = findViewById(R.id.btn_start);
        ConstraintLayout bg_main = findViewById(R.id.bg_main);

        String[] colorList = {"#673AB7","#3F51B5","#2196F3","#009688","#FFEB3B","#FF5722","#F44336"};
        int color_index = k % 7 - 1;
        if(color_index == -1) {
            color_index = 6;
        }
        String selectedColor = colorList[color_index];
        bg_main.setBackgroundColor(Color.parseColor(selectedColor));

        // 랜덤 숫자(초기화 추가) + 타이머 + 시간차(초기화 추가) : 시간 차 적은 사람이 위너
        Random rn = new Random();
        int num = rn.nextInt(201);
        tv.setText(String.valueOf(Float.valueOf(num)/100));
        btn.setText("시작");
        tv_people.setText("참가자 " + k);

        sec = 0;
        isRunning = false;

        LinkedList<Timer> list = new LinkedList<>();
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stage++;
                if (stage == 2) {
                    Timer timer = new Timer();
                    list.add(timer);
                    TimerTask tt = new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread( new Runnable(){ // 메인쓰레드만 쓸 수 있다.
                                @Override
                                public void run() {
                                    sec++;
                                    if (!isBlind) {
                                        tv_t.setText(String.valueOf(Float.valueOf(sec)/100));
                                    } else if (isBlind && stage == 2){
                                        tv_t.setText("???");
                                    }
                                }
                            });
                        }
                    };
                    timer.schedule(tt, 0, 10);
                    btn.setText("정지");
                } else if (stage == 3) {
                    tv_t.setText(String.valueOf(Float.valueOf(sec)/100)); // 현재
                    list.pop().cancel();
                    Float point = abs(Float.valueOf(sec) - Float.valueOf(num))/100; // 목표(랜덤)
                    tv_p.setText(String.valueOf(point));
                    p_list.add(point);
                    btn.setText("다음");
                    stage = 0;
                } else if (stage == 1) {
                    if (k < p_num) {
                        k++;
                        main();
                    } else {
                        System.out.println(p_list);
//                        stage--;
                        end();
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        start();
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // 버튼 클릭 시 텍스트 변경
//        TextView tv = findViewById(R.id.tv_random);
//        TextView tv_t = findViewById(R.id.tv_timer);
//        TextView tv_p = findViewById(R.id.tv_point);
//        Button btn = findViewById(R.id.btn_main);
////        btn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                tv.setText("넌 바보");
////            }
////        });
//
//        // 타이머
////        Timer timer = new Timer();
////        timer.schedule(new TimerTask() {
////            int sec = 0;
////            @Override
////            public void run() {
////                runOnUiThread( new Runnable(){ // 메인쓰레드만 쓸 수 있다.
////                    @Override
////                    public void run() {
////                        if (sec < 5) {
////                            sec++;
////                            tv.setText(String.valueOf(sec));
////                        } else {
////                            timer.cancel();
////                        }
////                    }
////                });
////            }
////        }, 0, 1000);
//
//        // 버튼 결합
////        // TODO timer 인스턴스를 계속 사용할 수 있는 방법 찾기, 여기서는 임의로 list에 담아썼지만...
////        sec = 0;
////        isRunning = false;
////
////        LinkedList<Timer> list = new LinkedList<>();
////
////        btn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (!isRunning) {
////                    isRunning = true;
////                    Timer timer = new Timer();
////                    list.add(timer);
////                    TimerTask tt = new TimerTask() {
////                        @Override
////                        public void run() {
////                            runOnUiThread( new Runnable(){ // 메인쓰레드만 쓸 수 있다.
////                                @Override
////                                public void run() {
////                                    sec++;
////                                    tv.setText(String.valueOf(sec));
////                                }
////                            });
////                        }
////                    };
////                    timer.schedule(tt, 0, 1000);
////                } else {
////                    isRunning = false;
////                    list.pop().cancel();
////                }
////            }
////        });
//
//        // 랜덤 숫자 + 타이머 + 시간차 : 시간 차 적은 사람이 위너
////        Random rn = new Random();
////        int num = rn.nextInt(1001);
////        tv.setText(String.valueOf(Float.valueOf(num)/100));
////
////        sec = 0;
////        isRunning = false;
////
////        LinkedList<Timer> list = new LinkedList<>();
////
////        btn.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View v) {
////                if (!isRunning) {
////                    isRunning = true;
////                    Timer timer = new Timer();
////                    list.add(timer);
////                    TimerTask tt = new TimerTask() {
////                        @Override
////                        public void run() {
////                            runOnUiThread( new Runnable(){ // 메인쓰레드만 쓸 수 있다.
////                                @Override
////                                public void run() {
////                                    sec++;
////                                    tv_t.setText(String.valueOf(Float.valueOf(sec)/100));
////                                }
////                            });
////                        }
////                    };
////                    timer.schedule(tt, 0, 10);
////                } else {
////                    isRunning = false;
////                    list.pop().cancel();
////                    tv_p.setText(String.valueOf((abs(Float.valueOf(sec) - Float.valueOf(num)))/100));
////                }
////            }
////        });
//
//        // 랜덤 숫자(초기화 추가) + 타이머 + 시간차(초기화 추가) : 시간 차 적은 사람이 위너
//        Random rn = new Random();
//        int num = rn.nextInt(1001);
//        tv.setText(String.valueOf(Float.valueOf(num)/100));
//
//        sec = 0;
//        isRunning = false;
//
//        LinkedList<Timer> list = new LinkedList<>();
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isRunning) {
//                    isRunning = true;
//                    Timer timer = new Timer();
//                    list.add(timer);
//                    TimerTask tt = new TimerTask() {
//                        @Override
//                        public void run() {
//                            runOnUiThread( new Runnable(){ // 메인쓰레드만 쓸 수 있다.
//                                @Override
//                                public void run() {
//                                    sec++;
//                                    tv_t.setText(String.valueOf(Float.valueOf(sec)/100));
//                                }
//                            });
//                        }
//                    };
//                    timer.schedule(tt, 0, 10);
//                } else {
//                    isRunning = false;
//                    list.pop().cancel();
//                    tv_p.setText(String.valueOf((abs(Float.valueOf(sec) - Float.valueOf(num)))/100));
//                }
//            }
//        });
//
//    }
}

