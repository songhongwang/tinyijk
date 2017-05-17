package com.tangdou.tinyijk.media.widget.media.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tangdou.tinyijk.R;
import com.tangdou.tinyijk.media.widget.media.IjkVideoView;

import java.util.Formatter;
import java.util.Locale;

import tv.danmaku.ijk.media.player.IMediaPlayer;

/**
 * Created by vigorous on 17/5/15.
 */

public class SeekController extends FrameLayout implements View.OnTouchListener{

    private static final int sDefaultTimeout = 3000;
    private IjkVideoView mVideoView;

    private Context mContext;
    private ProgressBar mProgress;
    private View mMenu;
    private TextView mEndTime, mCurrentTime;
    private boolean mShowing = true;
    private boolean mDragging;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private ImageView mPauseButton;

    public SeekController(Context context) {
        super(context);
        initView();
    }

    public SeekController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
    }

    public SeekController(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void setMediaPlayer(IjkVideoView videoView) {
        mVideoView = videoView;
        mVideoView.setKeepScreenOn(true);
        mVideoView.setOnCompletionListener(new IMediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(IMediaPlayer iMediaPlayer) {
                Log.d("onCompletion", iMediaPlayer.getCurrentPosition() + " #$$$");
                mLastPos = iMediaPlayer.getDuration();
                updatePausePlay();
            }
        });
        mVideoView.setOnPreparedListener(new IMediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer iMediaPlayer) {
                post(mShowProgress);
            }
        });
    }

    protected void initView() {
        ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_seek_controller, this);

        mMenu = findViewById(R.id.rl_menu);
        findViewById(R.id.rl_root).setOnTouchListener(this);
        mPauseButton = (ImageView) findViewById(R.id.iv_pause);
        mPauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mVideoView.isPlaying()) {
                    mVideoView.pause();
                } else {
                    mVideoView.start();
                    if(mLastPos == mVideoView.getDuration()){ // 播放完毕 重新点击播放 seek到0
                        mLastPos = 0;
                    }
                    mVideoView.seekTo((int) mLastPos);
                }
                updatePausePlay();

                show(sDefaultTimeout);
            }
        });

        mProgress = (SeekBar) findViewById(R.id.sb_progress);
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
            }
            mProgress.setMax(1000);
        }

        mEndTime = (TextView) findViewById(R.id.time_total);
        mCurrentTime = (TextView) findViewById(R.id.time_current);
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
    }


    public void show(int timeout) {
        if (!mShowing) {
            setProgress();
            if (mPauseButton != null) {
                mPauseButton.requestFocus();
            }
            mMenu.setVisibility(VISIBLE);

            mShowing = true;
        }
        updatePausePlay();

        post(mShowProgress);

        if (timeout != 0) {
            removeCallbacks(mFadeOut);
            postDelayed(mFadeOut, timeout);
        }
    }

    public void hide() {
        if (mShowing) {
            try {
                removeCallbacks(mShowProgress);
                mMenu.setVisibility(INVISIBLE);
            } catch (IllegalArgumentException ex) {
                Log.w("MediaController", "already removed");
            }
            mShowing = false;
        }
    }

    private String stringForTime(int timeMs) {
        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }


    private final Runnable mFadeOut = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };

    private final Runnable mShowProgress = new Runnable() {

        @Override
        public void run() {
            int pos = setProgress();
            if (!mDragging && mShowing && mVideoView.isPlaying()) {

//                Log.d("hongxx", pos + " " +(1000 - (pos % 1000)));
                postDelayed(mShowProgress, (1000 - (pos % 1000)));
            }
        }
    };

    private long mLastPos;
    private int setProgress() {
        if (mVideoView == null || mDragging) {
            return 0;
        }
        if(mVideoView.getDuration() == -1){
            postDelayed(mShowProgress, 500);

        }
        int position = mVideoView.getCurrentPosition();
        int duration = mVideoView.getDuration();

        Log.d("setProgress1" , position + " " + mLastPos + " " + duration);

        if(position < mLastPos){
            position = (int) mLastPos;
        }else{
            mLastPos = position;
        }

        if (duration > 0) {
            // use long to avoid overflow
            long pos = 1000L * position / duration;
            mProgress.setProgress( (int) pos);
        }
        int percent = mVideoView.getBufferPercentage();
        mProgress.setSecondaryProgress(percent * 10);

        if (mEndTime != null){
            mEndTime.setText(stringForTime(duration));
        }
        if (mCurrentTime != null){
            mCurrentTime.setText(stringForTime(position));
        }
        return position;
    }

    private void updatePausePlay() {
        if (mMenu == null || mPauseButton == null)
            return;

        if (mVideoView.isPlaying()) {
            mPauseButton.setImageResource(R.drawable.video_pause);
        } else {
            mPauseButton.setImageResource(R.drawable.video_play);
        }
    }

    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;
            removeCallbacks(mShowProgress);
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                return;
            }

            long duration = mVideoView.getDuration();
            long newposition = (duration * progress) / 1000L;

            mLastPos = newposition;
            Log.d("onProgressChanged", "seekTo " + mLastPos + " duration " + mVideoView.getDuration());

            mVideoView.seekTo( (int) newposition);
            if (mCurrentTime != null){
                mCurrentTime.setText(stringForTime( (int) newposition));
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;

            mLastPos = 0;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);

            post(mShowProgress);
        }
    };

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                show(0); // show until hide is called
                break;
            case MotionEvent.ACTION_UP:
                show(sDefaultTimeout); // start timeout
                break;
            case MotionEvent.ACTION_CANCEL:
                hide();
                break;
            default:
                break;
        }
        return true;
    }
}
