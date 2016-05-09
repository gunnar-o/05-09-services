package edu.uw.servicedemo;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import edu.uw.servicedemo.R;

/**
 * Created by Gunnar on 5/9/2016.
 */
public class MusicService extends Service {

    private static final String TAG = "MusicService";
    private String songName = "The Entertainer";
    private MediaPlayer player;

    @Override
    public void onCreate() {
        Log.v(TAG, "Music Service Started");
        super.onCreate();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.scott_joplin_the_entertainer_1902);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    player.stop();
                    player.release();
                    player = null;
                }
            });
        }
        player.start();

        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(), 0,
                new Intent(getApplicationContext(), MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);

        // Make foreground service (notification panel to act as UI)
        Notification n = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.ic_media_play)
                .setContentTitle("Music Player")
                .setContentText("Now playing " + songName)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, n); // show notification (1 can be any id)
        return START_NOT_STICKY;
    }

    public void pause() {
        if (player != null) {
            player.pause();
        }
    }

    public void stop() {
        if (player != null) {
            player.stop();
            player.release();   // Free memory used by the player
            player = null;      // For garbage collection
        }
        stopForeground(true);
    }

    @Override
    public void onDestroy() {
        stop();
        super.onDestroy();
    }

    private final IBinder binder = new LocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {

        public String getSong() {
            return songName;
        }

        public MusicService getMusicService() {
            return MusicService.this; // Get access to the service to call its methods
        }
    }
}
