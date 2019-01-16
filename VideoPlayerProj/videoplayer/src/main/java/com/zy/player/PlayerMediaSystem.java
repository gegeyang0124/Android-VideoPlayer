package com.zy.player;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.PlaybackParams;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Surface;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * Created by 周阳 on 2017/11/8.
 * 实现系统的播放引擎
 */
public class PlayerMediaSystem extends PlayerMediaInterface implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnSeekCompleteListener, MediaPlayer.OnErrorListener, MediaPlayer.OnInfoListener, MediaPlayer.OnVideoSizeChangedListener {

    public MediaPlayer mediaPlayer;

    @Override
    public void start() {
        mediaPlayer.start();
    }

    @Override
    public void prepare() {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setLooping(jzDataSource.looping);
            mediaPlayer.setOnPreparedListener(PlayerMediaSystem.this);
            mediaPlayer.setOnCompletionListener(PlayerMediaSystem.this);
            mediaPlayer.setOnBufferingUpdateListener(PlayerMediaSystem.this);
            mediaPlayer.setScreenOnWhilePlaying(true);
            mediaPlayer.setOnSeekCompleteListener(PlayerMediaSystem.this);
            mediaPlayer.setOnErrorListener(PlayerMediaSystem.this);
            mediaPlayer.setOnInfoListener(PlayerMediaSystem.this);
            mediaPlayer.setOnVideoSizeChangedListener(PlayerMediaSystem.this);
            Class<MediaPlayer> clazz = MediaPlayer.class;
            Method method = clazz.getDeclaredMethod("setDataSource", String.class, Map.class);
//            if (dataSourceObjects.length > 2) {
            method.invoke(mediaPlayer, jzDataSource.getCurrentUrl().toString(), jzDataSource.headerMap);
//            } else {
//                method.invoke(mediaPlayer, currentDataSource.toString(), null);
//            }
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    @Override
    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    @Override
    public void seekTo(long time) {
        try {
            mediaPlayer.seekTo((int) time);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void release() {
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

    @Override
    public long getCurrentPosition() {
        if (mediaPlayer != null) {
            return mediaPlayer.getCurrentPosition();
        } else {
            return 0;
        }
    }

    @Override
    public long getDuration() {
        if (mediaPlayer != null) {
            return mediaPlayer.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public void setSurface(Surface surface) {
        mediaPlayer.setSurface(surface);
    }

    @Override
    public void setVolume(float leftVolume, float rightVolume) {
        mediaPlayer.setVolume(leftVolume, rightVolume);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void setSpeed(float speed) {
        PlaybackParams pp = mediaPlayer.getPlaybackParams();
        pp.setSpeed(speed);
        mediaPlayer.setPlaybackParams(pp);
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
//        mediaPlayer.start();
        start();
        if (jzDataSource.getCurrentUrl().toString().toLowerCase().contains("mp3") ||
                jzDataSource.getCurrentUrl().toString().toLowerCase().contains("wav")) {
            PlayerMediaMgr.instance().mainThreadHandler.post(() -> {
                if (PlayerMgr.getCurrentJzvd() != null) {
                    PlayerMgr.getCurrentJzvd().onPrepared();
                }
            });
        }
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        PlayerMediaMgr.instance().mainThreadHandler.post(() -> {
            if (PlayerMgr.getCurrentJzvd() != null) {
                PlayerMgr.getCurrentJzvd().onAutoCompletion();
            }
        });
    }

    @Override
    public void onBufferingUpdate(MediaPlayer mediaPlayer, final int percent) {
        PlayerMediaMgr.instance().mainThreadHandler.post(() -> {
            if (PlayerMgr.getCurrentJzvd() != null) {
                PlayerMgr.getCurrentJzvd().setBufferProgress(percent);
            }
        });
    }

    @Override
    public void onSeekComplete(MediaPlayer mediaPlayer) {
        PlayerMediaMgr.instance().mainThreadHandler.post(() -> {
            if (PlayerMgr.getCurrentJzvd() != null) {
                PlayerMgr.getCurrentJzvd().onSeekComplete();
            }
        });
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, final int what, final int extra) {
        PlayerMediaMgr.instance().mainThreadHandler.post(() -> {
            if (PlayerMgr.getCurrentJzvd() != null) {
                PlayerMgr.getCurrentJzvd().onError(what, extra);
            }
        });
        return true;
    }

    @Override
    public boolean onInfo(MediaPlayer mediaPlayer, final int what, final int extra) {
        PlayerMediaMgr.instance().mainThreadHandler.post(() -> {
            if (PlayerMgr.getCurrentJzvd() != null) {
                if (what == MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
                    if (PlayerMgr.getCurrentJzvd().currentState == Player.CURRENT_STATE_PREPARING
                            || PlayerMgr.getCurrentJzvd().currentState == Player.CURRENT_STATE_PREPARING_CHANGING_URL) {
                        PlayerMgr.getCurrentJzvd().onPrepared();
                    }
                } else {
                    PlayerMgr.getCurrentJzvd().onInfo(what, extra);
                }
            }
        });
        return false;
    }

    @Override
    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int width, int height) {
        PlayerMediaMgr.instance().currentVideoWidth = width;
        PlayerMediaMgr.instance().currentVideoHeight = height;
        PlayerMediaMgr.instance().mainThreadHandler.post(() -> {
            if (PlayerMgr.getCurrentJzvd() != null) {
                PlayerMgr.getCurrentJzvd().onVideoSizeChanged();
            }
        });
    }
}
