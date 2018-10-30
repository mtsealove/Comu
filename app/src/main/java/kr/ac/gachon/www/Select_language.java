package kr.ac.gachon.www;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Select_language extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle si) {
        super.onCreate(si);
        setContentView(R.layout.activity_select_language);

        if(!Load.accounts[Account.current].name.equals("비회원")) //비회원이 아니라면 환영 토스트 출력
            Toast.makeText(Select_language.this, Load.accounts[Account.current].name+"님 환영합니다", Toast.LENGTH_SHORT).show();

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

    }

    protected void move_chapter(String lang) {//챕터 선택으로 이동하는 메서드
        Intent chapter=new Intent(Select_language.this, Select_chapter.class);
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
        Intent memeber=new Intent(Select_language.this, Member.class);
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
