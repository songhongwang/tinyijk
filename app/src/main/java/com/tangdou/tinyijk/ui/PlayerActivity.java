package com.tangdou.tinyijk.ui;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TableLayout;

import com.danikula.videocache.HttpProxyCacheServer;
import com.tangdou.tinyijk.R;
import com.tangdou.tinyijk.media.widget.media.AndroidMediaController;
import com.tangdou.tinyijk.media.widget.media.IRenderView;
import com.tangdou.tinyijk.media.widget.media.IjkVideoView;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerActivity extends AppCompatActivity {

    private IjkVideoView mVideoView;
    private TableLayout mHudView;
    private AndroidMediaController mMediaController;

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
        url = "http://accto.tangdou.com/6B6FB1FBE9E9F9A69C33DC5901307461-20.mp4";

        // 播放容器
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);

        // 帧率信息
        mHudView = (TableLayout) findViewById(R.id.hud_view);
        mVideoView.setHudView(mHudView);

        // 播放控制器
        mMediaController = new AndroidMediaController(this, false);
        mVideoView.setMediaController(mMediaController);



        HttpProxyCacheServer proxy = App.getProxy(this);
        String proxyUrl = proxy.getProxyUrl(url);

        if(App.getProxy(this).isCached(url)){
            Log.d("songxx", "proxy url = " + proxyUrl);
        }
        mVideoView.setVideoPath(proxyUrl);
        mVideoView.start();

    }


    @Override
    protected void onStop() {
        super.onStop();
        mVideoView.pause();
        IjkMediaPlayer.native_profileEnd();
    }

    int ratio = 0;
    @Override
    protected void onRestart() {
        super.onRestart();
        if(mVideoView != null){

            ratio = ++ ratio % 5;

            mVideoView.setAspectRatio(ratio);
            mVideoView.start();
            IjkMediaPlayer.native_profileEnd();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mVideoView != null){
            mVideoView.release(true);
        }
    }
}
