package me.wcy.music.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import me.wcy.music.application.Notifier;

/**
 * 音乐播放后台服务
 * Created by wcy on 2015/11/27.
 */
public class PlayService extends Service {
    private static final String TAG = "Service";

    private OnPlayerEventListener listener;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG, "onCreate: " + getClass().getSimpleName());
        AudioPlayer.get().init(this);
        MediaSessionManager.get().init(this);
        Notifier.get().init(this);
        QuitTimer.getInstance().init(this, aLong -> {
            if (listener != null) {
                listener.onTimer(aLong);
            }
        });
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new PlayBinder();
    }

    public void setOnPlayEventListener(OnPlayerEventListener listener) {
        this.listener = listener;
        AudioPlayer.get().setOnPlayEventListener(listener);
    }

    public void stop() {
        AudioPlayer.get().stopPlayer();
        QuitTimer.getInstance().stop();
        Notifier.get().cancelAll();
    }

    public class PlayBinder extends Binder {
        public PlayService getService() {
            return PlayService.this;
        }
    }
}
