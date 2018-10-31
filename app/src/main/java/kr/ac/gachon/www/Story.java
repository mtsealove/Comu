package kr.ac.gachon.www;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
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
                    finish();
                }
            }
        });

    }
    public void go_Edit(View v) {
        Intent editor=new Intent(Story.this, Editor.class);
        startActivity(editor);
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(Story.this, "이 단계에서는 돌아갈 수 없습니다", Toast.LENGTH_SHORT).show();
    }
}
