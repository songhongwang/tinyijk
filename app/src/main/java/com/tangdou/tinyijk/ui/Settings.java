/*
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tangdou.tinyijk.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


public class Settings {
    private Context mAppContext;
    private SharedPreferences mSharedPreferences;

    public static final int PV_PLAYER__Auto = 0;
    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    public static final int PV_PLAYER__IjkExoMediaPlayer = 3;

    public Settings(Context context) {
        mAppContext = context.getApplicationContext();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mAppContext);
    }

    public boolean getEnableBackgroundPlay() {
//        String key = mAppContext.getString(R.string.pref_key_enable_background_play);
        String key = "getEnableBackgroundPlay";
        return mSharedPreferences.getBoolean(key, false);
    }

    public int getPlayer() {
//        String key = mAppContext.getString(R.string.pref_key_player);
//        String value = mSharedPreferences.getString(key, "");
        String value = "PV_PLAYER__IjkMediaPlayer";
        try {
            return Integer.valueOf(value).intValue();
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public boolean getUsingMediaCodec() {
//        String key = mAppContext.getString(R.string.pref_key_using_media_codec);
        String key = "getUsingMediaCodec";
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getUsingMediaCodecAutoRotate() {
//        String key = mAppContext.getString(R.string.pref_key_using_media_codec_auto_rotate);
        String key = "getUsingMediaCodecAutoRotate";
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getMediaCodecHandleResolutionChange() {
//        String key = mAppContext.getString(R.string.pref_key_media_codec_handle_resolution_change);
        String key = "getMediaCodecHandleResolutionChange";
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getUsingOpenSLES() {
//        String key = mAppContext.getString(R.string.pref_key_using_opensl_es);
        String key = "getUsingOpenSLES";
        return mSharedPreferences.getBoolean(key, false);
    }

    public String getPixelFormat() {
//        String key = mAppContext.getString(R.string.pref_key_pixel_format);
        String key = "getPixelFormat";
        return mSharedPreferences.getString(key, "");
    }

    public boolean getEnableNoView() {
//        String key = mAppContext.getString(R.string.pref_key_enable_no_view);
        String key = "getEnableNoView";
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getEnableSurfaceView() {
//        String key = mAppContext.getString(R.string.pref_key_enable_surface_view);
        String key = "getEnableSurfaceView";
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getEnableTextureView() {
//        String key = mAppContext.getString(R.string.pref_key_enable_texture_view);
        String key = "getEnableTextureView";
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getEnableDetachedSurfaceTextureView() {
//        String key = mAppContext.getString(R.string.pref_key_enable_detached_surface_texture);
        String key = "getEnableDetachedSurfaceTextureView";
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getUsingMediaDataSource() {
//        String key = mAppContext.getString(R.string.pref_key_using_mediadatasource);
        String key = "getUsingMediaDataSource";
        return mSharedPreferences.getBoolean(key, false);
    }

    public String getLastDirectory() {
//        String key = mAppContext.getString(R.string.pref_key_last_directory);
        String key = "getLastDirectory";
        return mSharedPreferences.getString(key, "/");
    }

    public void setLastDirectory(String path) {
//        String key = mAppContext.getString(R.string.pref_key_last_directory);
        String key = "setLastDirectory";
        mSharedPreferences.edit().putString(key, path).apply();
    }
}
