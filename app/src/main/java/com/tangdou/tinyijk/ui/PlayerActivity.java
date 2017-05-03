package com.tangdou.tinyijk.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TableLayout;

import com.tangdou.tinyijk.R;
import com.tangdou.tinyijk.media.widget.media.AndroidMediaController;
import com.tangdou.tinyijk.media.widget.media.IRenderView;
import com.tangdou.tinyijk.media.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerActivity extends AppCompatActivity {

    private IjkVideoView mVideoView;
    private TableLayout mHudView;
    private AndroidMediaController mMediaController;
    private boolean mBackPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initPlayer();
        initLogic();
    }

    private void initLogic(){
        findViewById(R.id.btn_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.start();
            }
        });

        findViewById(R.id.btn_pause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoView.pause();
            }
        });
    }

    private void initPlayer() {
        String url = "http://aksyun.tangdou.com/6BC9E41AF5AAD7819C33DC5901307461-20.mp4";
//        url = "http://zv.3gv.ifeng.com/live/zhongwen800k.m3u8";

        // 播放容器
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);

        // 帧率信息
        mHudView = (TableLayout) findViewById(R.id.hud_view);
        mVideoView.setHudView(mHudView);

        // 播放控制器
        mMediaController = new AndroidMediaController(this, false);
        mVideoView.setMediaController(mMediaController);

        mVideoView.setVideoURI(Uri.parse(url));
        mVideoView.start();
    }


    @Override
    protected void onStop() {
        super.onStop();

        if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
        }
        IjkMediaPlayer.native_profileEnd();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mBackPressed = true;
        if(mVideoView != null){
            mVideoView.release(true);
        }
    }
}
