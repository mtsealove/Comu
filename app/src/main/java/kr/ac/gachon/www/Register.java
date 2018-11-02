package kr.ac.gachon.www;

import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Register extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle si) {
        super.onCreate(si);
        setContentView(R.layout.activity_register);
        final boolean[] is_reuse = {true, true};

        //객체 가져오기
        final EditText name=(EditText)findViewById(R.id.input_name);
        final EditText id=(EditText)findViewById(R.id.input_id);
        final EditText password=(EditText)findViewById(R.id.input_password);
        final EditText password_confirm=(EditText)findViewById(R.id.input_password_confirm);
        final EditText phone=(EditText)findViewById(R.id.input_phone);
        phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        Button check_reuse=(Button)findViewById(R.id.check_reuse);
        Button register=(Button)findViewById(R.id.register);
        Button check_phone_reuse=(Button)findViewById(R.id.check_reuse_phone);

        check_reuse.setOnClickListener(new View.OnClickListener() { //중복 체크
            @Override
            public void onClick(View v) {
                is_reuse[0]=false;
                for(int i=0; i<Account.count; i++) {
                    if(Load.accounts[i].ID.equals(id.getText().toString())) {
                        is_reuse[0] =true;
                    }
                }
                if(is_reuse[0]) Toast.makeText(Register.this, "이미 존재하는 ID입니다", Toast.LENGTH_SHORT).show();
                else Toast.makeText(Register.this, "사용 가능한 ID입니다", Toast.LENGTH_SHORT).show();
            }
        });

        check_phone_reuse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                is_reuse[1]=false;
                for(int i=0; i<Account.count; i++) {
                    if(Load.accounts[i].phone.equals(phone.getText().toString())) {
                        is_reuse[1]=true;
                    }
                }
                if(is_reuse[1]) Toast.makeText(Register.this, "이미 사용되고 있는 전화번호입니다", Toast.LENGTH_SHORT).show();
                else Toast.makeText(Register.this, "사용 가능한 전화번호입니다", Toast.LENGTH_SHORT).show();
            }
        });

        register.setOnClickListener(new View.OnClickListener() { //회원가입 버튼 메서드
            @Override
            public void onClick(View v) {
                String tname=name.getText().toString();
                String tID=id.getText().toString();
                String tpassword=password.getText().toString();
                String tpassword_confirm=password_confirm.getText().toString();
                String tphone=phone.getText().toString();
                if(is_reuse[0]) Toast.makeText(Register.this, "ID중복을 확인하세요", Toast.LENGTH_SHORT).show();
                else if(is_reuse[1]) Toast.makeText(Register.this, "전화번호 중복을 확인하세요", Toast.LENGTH_SHORT).show();
                else if(!tpassword.equals(tpassword_confirm)) Toast.makeText(Register.this, "비밀번호가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                else {
                    Load.accounts[Account.count+1]=new Account(tname, tID, tphone, tpassword, 1, "", "");
                    File account_file=new File(getFilesDir()+"Account.dat");
                    try {
                        BufferedWriter bw=new BufferedWriter(new FileWriter(account_file, true));
                        bw.write(tname);
                        bw.newLine();
                        bw.write(tID);
                        bw.newLine();
                        bw.write(tphone);
                        bw.newLine();
                        bw.write(tpassword);
                        bw.newLine();
                        bw.write("1");
                        bw.newLine();
                        bw.write("");
                        bw.newLine();
                        bw.write("");
                        bw.newLine();
                        bw.flush();
                        bw.close();
                        Toast.makeText(Register.this, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    finish();
                }
            }
        });

    }
}
