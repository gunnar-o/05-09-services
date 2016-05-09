package edu.uw.servicedemo;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Gunnar on 5/9/2016.
 */
public class CountingService extends IntentService {

    private static final String TAG = "CountingService";

    private Handler handler;

    public CountingService() {
        super("CountingService");
        handler = new Handler();
    }


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public CountingService(String name) {
        super(name);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.v(TAG, " Intent Recieved");
        // Queues up all the services each time the service is started
        // Useful for downloading many items one at a time
        return super.onStartCommand(intent, flags, startId);
    }

    private int count = 0;

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "Handling Intent");

        // stopSelf();  // Service stops itself

        for (count = 0; count < 10; count++) {
            Log.v(TAG, "Count: " + count);

            // Work to show a toast on the UI thread
            // (Handler helps us communicate between threads)
            handler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(CountingService.this, "Count: " + count, Toast.LENGTH_SHORT);
                }
            });


            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

        }
    }
}
