package kr.ac.gachon.www;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Member extends AppCompatActivity {
    static int login_count = 0; //로그인 횟수 제한

    @Override
    protected void onCreate(Bundle si) {
        super.onCreate(si);
        setContentView(R.layout.activity_member);

        //각 버튼 매칭
        Button login = (Button) findViewById(R.id.login);
        Button non_member = (Button) findViewById(R.id.non_member);
        Button register = (Button) findViewById(R.id.register);

        non_member.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(Member.this, Main.class);
                Account.current = Account.non_member;
                startActivity(main);
                finish();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login_dialog();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent move_register = new Intent(Member.this, Register.class);
                startActivity(move_register);
            }
        });
    }

    protected void login_dialog() { //로그인 다이얼로그 출력 메서드
        //다이얼로그 생성
        AlertDialog.Builder builder = new AlertDialog.Builder(Member.this);
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_login, null);
        builder.setView(layout);
        //다이얼로그 안의 객체 매칭
        final EditText input_id = (EditText) layout.findViewById(R.id.input_id);
        final EditText input_password = (EditText) layout.findViewById(R.id.input_password);
        final CheckBox keep_login = (CheckBox) layout.findViewById(R.id.keep_login);
        Button confirm = (Button) layout.findViewById(R.id.confirm);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Load액티비티에서 일치하는 객체 확인
                if (input_id.getText().toString().length() == 0)
                    Toast.makeText(getApplicationContext(), "ID를 입력하세요", Toast.LENGTH_SHORT).show();
                else if (input_password.getText().toString().length() == 0)
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력하세요", Toast.LENGTH_SHORT).show();
                else {
                    String id = input_id.getText().toString();
                    String password = input_password.getText().toString();
                    int index = 0;
                    boolean check = false;
                    while (Load.accounts[index] != null && !check) {
                        if (id.equals(Load.accounts[index].ID) && password.equals(Load.accounts[index].password)) { //일치하는 계정 찾기
                            check = true;
                            Account.current = index;
                            //메인 페이지로 이동
                            Intent main = new Intent(Member.this, Main.class);
                            startActivity(main);
                            if (keep_login.isChecked()) {
                                File Logined = new File(getFilesDir() + "logined.dat");
                                try {
                                    BufferedWriter bw = new BufferedWriter(new FileWriter(Logined));
                                    bw.write(Integer.toString(Account.current));
                                    bw.flush();
                                    bw.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            login_count = 0; //로그인 카운트 0으로 초기화
                            finish();
                        }
                        index++;
                    }
                    if (!check) {
                        Toast.makeText(getApplicationContext(), "ID/비밀번호가 다릅니다\n로그인 시도 횟수: " + (++login_count) + "회", Toast.LENGTH_SHORT).show();
                        if (login_count == 10) { //로그인 10회 실패시
                           certification();
                        }
                    }
                }
            }
        });
        //다이얼로그 생성
        AlertDialog dialog = builder.create();
        dialog.show();

    }

    private void certification() { //본인 인증
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.dialog_user_certification, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(Member.this);
        builder.setView(layout)
                .setCancelable(false);
        final EditText phone = (EditText) layout.findViewById(R.id.certification_phone);
        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        Button confirm = (Button) layout.findViewById(R.id.confirm_certification);

        final AlertDialog cdialog = builder.create();
        cdialog.show();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String myNumber = null;
                TelephonyManager mgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String original_phone=phone.getText().toString();
                String[] phone_split=original_phone.split("-");
                String final_phone="";
                for(int i=0; i<phone_split.length; i++)
                    final_phone+=phone_split[i];
                try {
                    if (ActivityCompat.checkSelfPermission(Member.this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Member.this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(Member.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    myNumber = mgr.getLine1Number();
                    myNumber = myNumber.replace("+82", "0");

                }catch(Exception e){}
                if(myNumber.equals(final_phone)) {
                    cdialog.cancel();
                    login_count=0;
                    Toast.makeText(Member.this, "본인인증이 완료되었습니다", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(Member.this, "본인이 아닙니다\n게임을 종료합니다", Toast.LENGTH_SHORT).show();
                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.exit(0);
                        }
                    }, 1000);
                }
            }
        });
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
