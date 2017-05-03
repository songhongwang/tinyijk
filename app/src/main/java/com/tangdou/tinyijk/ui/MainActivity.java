package com.tangdou.tinyijk.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;

import com.tangdou.tinyijk.R;
import com.tangdou.tinyijk.media.widget.media.AndroidMediaController;
import com.tangdou.tinyijk.media.widget.media.IRenderView;
import com.tangdou.tinyijk.media.widget.media.IjkVideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLogic();
    }

    private void initLogic(){
        findViewById(R.id.to_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
                startActivity(intent);
            }
        });
    }

}
