package com.tangdou.tinyijk.media.widget.media.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tangdou.tinyijk.R;
import com.tangdou.tinyijk.media.widget.media.IjkVideoView;

import java.util.Formatter;
import java.util.Locale;

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
    private boolean mShowing;
    private boolean mDragging;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private TextView mPauseButton;

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
        setProgress();
    }

    protected void initView() {
        ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.layout_seek_controller, this);

        mMenu = findViewById(R.id.rl_menu);
        findViewById(R.id.rl_root).setOnTouchListener(this);
        mPauseButton = (TextView) findViewById(R.id.tv_pause);
        mPauseButton.setOnClickListener(mPauseListener);

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

    /**
     * Remove the controller from the screen.
     */
    public void hide() {
//        if (mShowing) {
//            try {
//                removeCallbacks(mShowProgress);
//                mMenu.setVisibility(INVISIBLE);
//            } catch (IllegalArgumentException ex) {
//                Log.w("MediaController", "already removed");
//            }
//            mShowing = false;
//        }
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

                Log.d("hongxx", pos + " " +(1000 - (pos % 1000)));
                postDelayed(mShowProgress, 1000);
            }
        }
    };

    private int setProgress() {
        Log.d("songxx6", "******" + mVideoView.getCurrentPosition() + " " + mVideoView.getDuration());


        if (mVideoView == null || mDragging) {
            return 0;
        }
        Log.d("songxx6", "………………………………………………" + mVideoView.getCurrentPosition() + " " + mVideoView.getDuration());

        int position = mVideoView.getCurrentPosition();
        int duration = mVideoView.getDuration();



        if (duration > 0) {
            // use long to avoid overflow
            long pos = 1000L * position / duration;
            mProgress.setProgress( (int) pos);

            Log.d("songxx2", pos + " " + position + " " + duration);
        }
        int percent = mVideoView.getBufferPercentage();
        mProgress.setSecondaryProgress(percent * 10);



        if (mEndTime != null){
            mEndTime.setText(stringForTime(duration));
        }
        if (mCurrentTime != null){
            mCurrentTime.setText(stringForTime(position));
            Log.d("songxx4",  " "+ stringForTime(position));

        }
        return position;
    }


    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int keyCode = event.getKeyCode();
        final boolean uniqueDown = event.getRepeatCount() == 0
                && event.getAction() == KeyEvent.ACTION_DOWN;
        if (keyCode ==  KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE
                || keyCode == KeyEvent.KEYCODE_SPACE) {
            if (uniqueDown) {
                doPauseResume();
                show(sDefaultTimeout);
                if (mPauseButton != null) {
                    mPauseButton.requestFocus();
                }
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_PLAY) {
            if (uniqueDown && !mVideoView.isPlaying()) {
                mVideoView.start();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP
                || keyCode == KeyEvent.KEYCODE_MEDIA_PAUSE) {
            if (uniqueDown && mVideoView.isPlaying()) {
                mVideoView.pause();
                updatePausePlay();
                show(sDefaultTimeout);
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN
                || keyCode == KeyEvent.KEYCODE_VOLUME_UP
                || keyCode == KeyEvent.KEYCODE_VOLUME_MUTE
                || keyCode == KeyEvent.KEYCODE_CAMERA) {
            // don't show the controls for volume adjustment
            return super.dispatchKeyEvent(event);
        } else if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_MENU) {
            if (uniqueDown) {
                hide();
            }
            return true;
        }

        show(sDefaultTimeout);
        return super.dispatchKeyEvent(event);
    }

    private final View.OnClickListener mPauseListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doPauseResume();
            show(sDefaultTimeout);
        }
    };

    private void updatePausePlay() {
        if (mMenu == null || mPauseButton == null)
            return;

        if (mVideoView.isPlaying()) {
            mPauseButton.setBackgroundResource(R.drawable.video_pause);
        } else {
            mPauseButton.setBackgroundResource(R.drawable.video_play);
        }
    }

    private void doPauseResume() {
        if (mVideoView.isPlaying()) {
            mVideoView.pause();
        } else {
            mVideoView.start();
        }
        updatePausePlay();
    }

    private final SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onStartTrackingTouch(SeekBar bar) {
            show(3600000);

            mDragging = true;
            removeCallbacks(mShowProgress);

            Log.d("songxx", "start-----------");

        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser) {
                return;
            }

            long duration = mVideoView.getDuration();
            long newposition = (duration * progress) / 1000L;

            Log.d("songxx", duration + " "+ newposition);

            mVideoView.seekTo( (int) newposition);
            if (mCurrentTime != null)
                mCurrentTime.setText(stringForTime( (int) newposition));
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {
            mDragging = false;
            setProgress();
            updatePausePlay();
            show(sDefaultTimeout);

            post(mShowProgress);
            Log.d("songxx", "----------- stop");

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
