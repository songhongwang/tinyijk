package com.tangdou.tinyijk.ui;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TableLayout;

import com.tangdou.tinyijk.R;
import com.tangdou.tinyijk.media.widget.media.IRenderView;
import com.tangdou.tinyijk.media.widget.media.IjkVideoView;
import com.tangdou.tinyijk.media.widget.media.controller.SeekBarMediaController;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class PlayerActivity extends AppCompatActivity {

    private IjkVideoView mVideoView;
    private TableLayout mHudView;
    private SeekBarMediaController mSeekBarMediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initPlayer();
    }


    private void initPlayer() {
        String url = "http://aksyun.tangdou.com/6BC9E41AF5AAD7819C33DC5901307461-20.mp4";
//        url = "http://accto.tangdou.com/6B6FB1FBE9E9F9A69C33DC5901307461-20.mp4";
//        url = "http://v6.365yg.com/video/m/220f89ea3ca5c8d472a8fdd0a20854b1e79114632f00002f41f0906ee0/?Expires=1494846019&AWSAccessKeyId=qh0h9TdcEMoS2oPj7aKX&Signature=3dFTg35neuuivNpwadic9r9hbP4%3D";
        url = "http://cm14-ccm1-2.play.bokecc.com/flvs/ca/Qx8NS/urnJ6aykF6-30.mp4?t=1496229360&key=4427BE2EB2C07961F9A0682FAE8EEDFB";
        url = "http://zv.3gv.ifeng.com/live/zhongwen800k.m3u8";
        url = "http://aksyun.tangdou.com/6BC9E41AF5AAD7819C33DC5901307461-20.mp4";


        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");
        // 播放容器
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);

        // 帧率信息
        mHudView = (TableLayout) findViewById(R.id.hud_view);
//        mVideoView.setHudView(mHudView);

        // 播放控制器
//        AndroidMediaController mediaController = new AndroidMediaController(this, false);
//        mVideoView.setMediaController(mediaController);

        mSeekBarMediaController = (SeekBarMediaController) findViewById(R.id.seek_controller);
        mSeekBarMediaController.setMediaPlayer(mVideoView);
        mSeekBarMediaController.setOnCloseListener(new SeekBarMediaController.OnCloseListener() {
            @Override
            public void onClose() {
                finish();
            }
        });



//        HttpProxyCacheServer proxy = App.getProxy(this);
//        String proxyUrl = proxy.getProxyUrl(url);
//
//        if(App.getProxy(this).isCached(url)){
//            Log.d("songxx", "proxy url = " + proxyUrl);
//        }

        mVideoView.setVideoPath(url);
        mVideoView.start();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mVideoView.pause();
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if(mVideoView != null){

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
