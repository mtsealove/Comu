package kr.ac.gachon.www;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Load extends AppCompatActivity {
    static Account[] accounts=new Account[Account.non_member+2]; //계정정보를 저장할 인스턴스 배열
    final String manager_id="manager", manager_pw="password"; //관리자 계정

    //정보를 로딩할 액티비티, 잠시 애플리케이션 로고 출력
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //전체화면 활성화
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_load);

        //계정 정보 저장
        int count=0;
        File account_file=new File(getFilesDir()+"Account.dat");
        try {
            BufferedReader br=new BufferedReader(new FileReader(account_file));
            String name, id, password, fc, fs, phone;
            int level;
            String tmp="";
            while((tmp=br.readLine())!=null) {
                name=tmp;
                tmp=br.readLine();
                id=tmp;
                tmp=br.readLine();
                phone=tmp;
                tmp=br.readLine();
                password=tmp;
                tmp=br.readLine();
                level=Integer.parseInt(tmp);
                tmp=br.readLine();
                fc=tmp;
                tmp=br.readLine();
                fs=tmp;
                //인스턴스로 생성
                accounts[count++]=new Account(name, id, phone, password, level, fc, fs);
            }
            Account.count=count;
            br.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //비회원 계정
        accounts[Account.non_member]=new Account("비회원", "", "","", 1, "", "");
        //관리자 계정
        accounts[Account.manager]=new Account("관리자", manager_id, "", manager_pw, 100, "","");

        //로그인 유지 확인
        File Logined=new File(getFilesDir()+"logined.dat");
        int Logined_index=100;
        try {
            BufferedReader br=new BufferedReader(new FileReader(Logined));
            String tmp="";
            if((tmp=br.readLine())!=null) Logined_index=Integer.parseInt(tmp);
            br.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //0.5초 후에 로그인 페이지로 이동, 로고를 잠시 출력하기 위함
        Handler handler=new Handler();
        final int finalLogin_index =Logined_index;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                move_login(finalLogin_index);
            }
        }, 500);
    }

    protected void move_login(int Login_index) { //로그인 인덱스를 받아 로그인 페이지로 이동하거나 메인 페이지로 이동
            Intent intent;
            if(Login_index==Account.non_member) intent=new Intent(Load.this, Member.class);
            else intent=new Intent(Load.this, Main.class);
            startActivity(intent);
            finish();
    }
}
