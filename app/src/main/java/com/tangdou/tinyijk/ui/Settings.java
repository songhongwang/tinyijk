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

public class Settings {

    public static final int PV_PLAYER__AndroidMediaPlayer = 1;
    public static final int PV_PLAYER__IjkMediaPlayer = 2;
    public static final int PV_PLAYER__IjkExoMediaPlayer = 3;

    public Settings() {}

    // 1: mediaplayer 2:ijkplayer 3:exoplayer
    public int getPlayer() {
        return PV_PLAYER__IjkMediaPlayer;
    }

    // true : 开启硬解码 false ：关闭硬解码
    public boolean getUsingMediaCodec() {
        return true;
    }

    // true : 开启自动旋转  false : 禁用自动旋转
    public boolean getUsingMediaCodecAutoRotate() {
        return true;
    }

    // true : 硬解码旋转角度处理 false : 不处理角度旋转
    public boolean getMediaCodecHandleResolutionChange() {
        return true;
    }

    // true : 使用oenSLES同步输出影视频 false ： 使用audiotrack作为音频输出
    public boolean getUsingOpenSLES() {
        return false;
    }

    // 解码后的数据回传格式
    public String getPixelFormat() {
//        return "fcc-i420";
        return null;
    }

    // 是否允许 不渲染画布
    public boolean getEnableNoView() {
        return true;
    }

    // 启用surfaceview 后台切换容易花屏
    public boolean getEnableSurfaceView() {
        return false;
    }

    // true : 使用TextureView 渲染
    public boolean getEnableTextureView() {
        return true;
    }

    // 是否可以分离surfaceview (后台切前台不会黑屏或花屏)
    public boolean getEnableDetachedSurfaceTextureView() {
        return true;
    }

    // 低版本不启用缓存
    public boolean getUsingMediaDataSource() {
        return true;
    }


    public boolean getEnableBackgroundPlay() {
        return true;
    }
}
