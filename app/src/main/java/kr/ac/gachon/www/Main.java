package kr.ac.gachon.www;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Main extends AppCompatActivity {
    Button play_btn, friends_btn, setting_btn, info_btn, notice_btn;

    @Override
    protected void onCreate(Bundle si) {
        super.onCreate(si);
        setContentView(R.layout.activity_main);

        if(!Load.accounts[Account.current].name.equals("비회원")) //비회원이 아니라면 환영 토스트 출력
            Toast.makeText(Main.this, Load.accounts[Account.current].name+"님 환영합니다", Toast.LENGTH_SHORT).show();

        //플레이 기능|챕터 선택으로 이동
        Button c=(Button)findViewById(R.id.c);
        Button java=(Button)findViewById(R.id.java);
        Button cplus=(Button)findViewById(R.id.cplus);

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_chapter("c");
            }
        });
        java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_chapter("java");
            }
        });
        cplus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                move_chapter("cplus");
            }
        });
        //하단 버튼들 매칭
        play_btn=(Button)findViewById(R.id.play_btn);
        friends_btn=(Button)findViewById(R.id.friend_btn);
        notice_btn=(Button)findViewById(R.id.notice_btn);
        setting_btn=(Button)findViewById(R.id.setting_btn);
        info_btn=(Button)findViewById(R.id.info_btn);

        play_btn.setBackground(ContextCompat.getDrawable(Main.this, R.drawable.menu_button_focus));
    }


    private void hide_layout() { //레이아웃 숨김 메서드
        //버튼 포커스 해제
        play_btn.setBackground(ContextCompat.getDrawable(Main.this, R.drawable.menu_button));
        friends_btn.setBackground(ContextCompat.getDrawable(Main.this, R.drawable.menu_button));
        notice_btn.setBackground(ContextCompat.getDrawable(Main.this, R.drawable.menu_button));
        setting_btn.setBackground(ContextCompat.getDrawable(Main.this, R.drawable.menu_button));
        info_btn.setBackground(ContextCompat.getDrawable(Main.this, R.drawable.menu_button));

        //레이아웃 매칭
        LinearLayout play=(LinearLayout)findViewById(R.id.play);
        LinearLayout setting=(LinearLayout)findViewById(R.id.setting);
        //레이아웃 소멸
        play.setVisibility(View.GONE);
        setting.setVisibility(View.GONE);
    }

    public void set_play(View v) {
        hide_layout();
        LinearLayout layout=(LinearLayout)findViewById(R.id.play);
        layout.setVisibility(View.VISIBLE);
        play_btn.setBackground(ContextCompat.getDrawable(Main.this, R.drawable.menu_button_focus));
    }

    public void set_friends(View v) {
        hide_layout();
        friends_btn.setBackground(ContextCompat.getDrawable(Main.this, R.drawable.menu_button_focus));
    }

    public void set_notice(View v) {
        hide_layout();
        notice_btn.setBackground(ContextCompat.getDrawable(Main.this, R.drawable.menu_button_focus));
    }

    public void set_info(View v) {
        hide_layout();
        info_btn.setBackground(ContextCompat.getDrawable(Main.this, R.drawable.menu_button_focus));
    }

    public void set_setting(View v) {
        hide_layout();
        LinearLayout layout=(LinearLayout)findViewById(R.id.setting);
        layout.setVisibility(View.VISIBLE);
        Button logout=(Button)findViewById(R.id.logout);
        if(Account.current==Account.non_member) logout.setText("로그인 화면으로 이동");
        setting_btn.setBackground(ContextCompat.getDrawable(Main.this, R.drawable.menu_button_focus));
    }


    protected void move_chapter(String lang) {//챕터 선택으로 이동하는 메서드
        Intent chapter=new Intent(Main.this, Select_chapter.class);
        chapter.putExtra("lang", lang);
        startActivity(chapter);
    }

    public void logout(View v) { // 로그아웃
        File Logined=new File(getFilesDir()+"logined.dat");
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(Logined));
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Intent memeber=new Intent(getApplicationContext(), Member.class);
        startActivity(memeber);
        finish();
    }

    //뒤로가기 2번 눌러 종료
    private final long FINISH_INTERVAL_TIME = 2000;
    private long   backPressedTime = 0;
    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            System.exit(0);
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "뒤로 버튼을 한 번 더 누르면 종료합니다", Toast.LENGTH_SHORT).show();
        }
    }
}
