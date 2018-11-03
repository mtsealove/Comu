package kr.ac.gachon.www;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

public class Story extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle si) {
        super.onCreate(si);
        setContentView(R.layout.activity_story);
        final int max_script=1000;
        final Conversation[] conversations=new Conversation[max_script];
        int script_count=0; //대화의 개수

        String file_name="java_variable.dat";
        try {
            BufferedReader br=new BufferedReader(new InputStreamReader(getAssets().open(file_name)));
            String tmp="";
            String character, comment;
            while((tmp=br.readLine())!=null) {
                character=tmp;
                tmp=br.readLine();
                comment=tmp;
                conversations[script_count]=new Conversation(character, comment);
                script_count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //대화 내용 설정
        final TextView name = (TextView) findViewById(R.id.name);
        final TextView comment = (TextView) findViewById(R.id.comment);
        name.setText(conversations[0].character);
        comment.setText(conversations[0].comment);

        //대화 터치시 다음 대화로 이동
        final int[] ing = {1};
        final Bundle final_si=si;
        RelativeLayout script=(RelativeLayout)findViewById(R.id.script);
        script.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    name.setText(conversations[ing[0]].character);
                    comment.setText(conversations[ing[0]].comment);
                    ing[0]++;
                } catch (NullPointerException e) {
                    Toast.makeText(Story.this, "대화가 종료되었습니다", Toast.LENGTH_SHORT).show();
                    Intent intent=getIntent();
                    String chapter=intent.getStringExtra("chapter");
                    Load.accounts[Account.current].exp+=10;
                    boolean already_exist=false;
                    for(int i=0; i<Load.accounts[Account.current].finished_chapeter.length; i++) //이미 했는지 확인
                        if(chapter.equals(Load.accounts[Account.current].finished_chapeter[i])) already_exist=true;
                    if(!already_exist) //존재하지 않으면 추가
                        Load.accounts[Account.current].fc+=chapter+",";
                    Load.accounts[Account.current].re_split_chapter();
                    rewrite_Accountfile();
                    if(Load.accounts[Account.current].exp>=100) {
                        Load.accounts[Account.current].exp-=100;
                        Load.accounts[Account.current].level++;
                        rewrite_Accountfile();
                    }
                    finish();
                }
            }
        });

    }
    public void go_Edit(View v) {
        Intent editor=new Intent(Story.this, Editor.class);
        startActivity(editor);
    }
    public void rewrite_Accountfile() { //계정 파일 재작성 메서드
        File account_file=new File(getFilesDir()+"Account.dat");
        try {
            BufferedWriter bw=new BufferedWriter(new FileWriter(account_file, false));
            for(int i=0; i<Account.count; i++) {
                bw.write(Load.accounts[i].name);
                bw.newLine();
                bw.write(Load.accounts[i].ID);
                bw.newLine();
                bw.write(Load.accounts[i].phone);
                bw.newLine();
                bw.write(Load.accounts[i].password);
                bw.newLine();
                bw.write(Integer.toString(Load.accounts[i].level));
                bw.newLine();
                bw.write(Integer.toString(Load.accounts[i].exp));
                bw.newLine();
                bw.write(Load.accounts[i].fc);
                bw.newLine();
                bw.write(Load.accounts[i].friends_split);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(Story.this, "이 단계에서는 돌아갈 수 없습니다", Toast.LENGTH_SHORT).show();
    }
}
