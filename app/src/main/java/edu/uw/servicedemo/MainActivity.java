package edu.uw.servicedemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ServiceConnection {

    private static final String TAG = "Main";

    private boolean bound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {

        bindService(new Intent(MainActivity.this, MusicService.class), this, Context.BIND_AUTO_CREATE);
        super.onStart();
    }

    @Override
    protected void onStop() {

        unbindService(this);
        super.onStop();
    }

    //when "Start" button is pressed
    public void handleStart(View v){
        Log.i(TAG, "Start pressed");

        // Send an intent to our CountingService
        Intent intent = new Intent(MainActivity.this, CountingService.class);
        startService(intent);

    }

    //when "Stop" button is pressed
    public void handleStop(View v){
        Log.i(TAG, "Stop pressed");
        if (bound) {
            service.stop();
        }
        // Finishes current service, but removes all other services from the queue
       // stopService(new Intent(MainActivity.this, CountingService.class));

    }


    private MediaPlayer player;

    /* Media controls */
    public void playMedia(View v){
        startService(new Intent(MainActivity.this, MusicService.class));

    }

    public void pauseMedia(View v){
        if (bound) {
            service.pause();
        }

    }

    public void stopMedia(View v) {
        //stopService(new Intent(MainActivity.this, MusicService.class));
        if (bound) {
            unbindService(this);
        }

        super.onStop();
    }


    //when "Quit" button is pressed
    public void handleQuit(View v){
        finish(); //end the Activity
    }

    @Override
    protected void onDestroy() {
        Log.v(TAG, "Activity destroyed");
        super.onDestroy();
    }

    private MusicService service;
    @Override
    public void onServiceConnected(ComponentName name, IBinder iBinder) {
        MusicService.LocalBinder binder = (MusicService.LocalBinder) iBinder;
        String songName = binder.getSong();

        ((TextView)findViewById(R.id.txtSongTitle)).setText(songName);

        service = binder.getMusicService();
        bound = true;
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        bound = false;
    }
}
