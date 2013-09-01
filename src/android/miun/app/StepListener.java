/**
 * 
 */
package android.miun.app;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

/**
 * @author Qingbao.Guo
 *
 */
public class StepListener implements SensorEventListener{
	
	//TAG
	private static final String TAG = "StepListener";
	private SensorManager sensorManager;
	private Context context;
    //private Database db;
    private long lastTime;
    public static int steps;
    public static int bigSteps;
    private int tempCount;//For counting real steps without external influence. 
   
    //Constructor
	public StepListener(Context context) {
		this.context = context;
	}
	
	
	
    
	//start ACCELEROMETER sensor
	public void start() {
		Log.i(TAG, "[StepListener] started");
		sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		Sensor sonser = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		sensorManager.registerListener(this, sonser, SensorManager.SENSOR_DELAY_FASTEST);
		
	}

	//stop
	public void stop() {
		Log.i(TAG, "[StepListener] stopped");
		sensorManager.unregisterListener(this);
		steps = 0;
		bigSteps = 0;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// ignored
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER) {
			return;
		}
		
		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];
    
        float accelationSquareRoot =  + ((x * x + y * y + z * z)
				/ (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)) - 1.0f;
        long actualTime = System.currentTimeMillis();
        if (actualTime - lastTime > 300) {
        	if(accelationSquareRoot < -0.45f){
            	steps++;
            	tempCount++;
            	if(accelationSquareRoot < -1.0f){
            	  bigSteps++;
            	}
            	if(steps%50==0){
            	  steps++;	
            	}
            	if(tempCount==4){
            	   steps = 0;
            	}
            	 lastTime = actualTime;
            }
		}
			
	}
	
	
	
	
	public  int getSteps(){		
    	return steps;	
	}	
	
	
	public int getBigsteps(){
		return bigSteps;
	}
	
	
}	

