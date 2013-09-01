package android.miun.app;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

/**
 * @author Qingbao.Guo
 *
 */

public class BackService extends Service {

	/**
	 * This service always runs for RunningActivity which is client. 
	 */
	public class LocalBinder extends Binder {
		public void gimmeHandler(Handler handler) {
			clientHandler = handler;
		}
	}

	private static final String TAG = "BackService";
	private final int NOTIFICATION = R.string.service_running;
	private StepListener stepListener;
	private Handler clientHandler;

	private final IBinder mBinder = new LocalBinder();
	private final Handler serviceHandler = new Handler();
	private final Runnable UpdateSteps = new Runnable() {
		@Override
		public void run() {
			Log.i(TAG, "UpdateSteps is running.");
			int step = stepListener.getSteps();
			int bigSteps = stepListener.getBigsteps();
			if (clientHandler != null) {
				Log.i(TAG, "Update steps.");
				Message message = Message.obtain();
				message.arg1 = step;
				message.arg2 = bigSteps;
				clientHandler.sendMessage(message);
			}
			serviceHandler.postDelayed(this, 1000);
		}
	};

	@Override
	public IBinder onBind(Intent intent) {
		Log.i(TAG, "onBind");
		return mBinder;
	}

	@Override
	public boolean onUnbind(Intent intent) {
		Log.i(TAG, "onUnbind");
		clientHandler = null;
		return true;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		serviceHandler.removeCallbacks(UpdateSteps);
		stepListener.stop();	
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
		stepListener = new StepListener(this);
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStart(intent, startId);
		Log.v(TAG, "onStart");
		start();
		return START_STICKY;
	}

	private void start() {
		stepListener.start();
		showNotification();
		serviceHandler.post(UpdateSteps);
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification() {
		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.ic_notification, null, System.currentTimeMillis());

		// The PendingIntent to launch the activity if the user selects this notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, RunningActivity.class), 0);

		// Set the information for the views that show in the notification panel.
		notification.setLatestEventInfo(this, getText(R.string.app_name), getText(R.string.service_running),
				contentIntent);

		startForeground(NOTIFICATION, notification);
	}

	
}

