package com.mascaradee.grape;

import static java.lang.Math.abs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    public static int sec;
    public static boolean isRunning;
    public static int stage = 1;
    public static int p_num = 3; // 참가인원
    public static int k = 1; // 기준
    List<Float> p_list = new ArrayList<>();

    public static boolean isBlind = false;

    public void start() {
        setContentView(R.layout.activity_start);
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

        TextView tv = findViewById(R.id.tv_pnum);
        TextView tv_t = findViewById(R.id.tv_timer);
        TextView tv_p = findViewById(R.id.tv_point);
        TextView tv_people = findViewById(R.id.tv_people);
        Button btn = findViewById(R.id.btn_start);

        // 랜덤 숫자(초기화 추가) + 타이머 + 시간차(초기화 추가) : 시간 차 적은 사람이 위너
        Random rn = new Random();
        int num = rn.nextInt(201);
        tv.setText(String.valueOf(Float.valueOf(num)/100));
        btn.setText("시작");
        tv_people.setText("참가자" + k);


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

