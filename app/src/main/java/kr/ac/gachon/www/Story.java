package kr.ac.gachon.www;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class Story extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle si) {
        super.onCreate(si);
        setContentView(R.layout.activity_story);

    }
    public void go_Edit(View v) {
        Intent editor=new Intent(Story.this, Editor.class);
        startActivity(editor);
    }
}
