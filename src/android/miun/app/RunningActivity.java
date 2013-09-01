/**
 * 
 */
package android.miun.app;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.xmlpull.v1.XmlSerializer;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * @author Qingbao.Guo
 *
 */
public class RunningActivity extends Activity{
	
	protected static final CharSequence DISPLAY_NO_DATA = "- -";
	//Application mode walking and running have different values default mode is  Moderately active-1.55
	private static float ACTIVITY_COEFFICIENT = 1.55F;
	//TextViews
	private TextView CurrentSteps;
	private TextView CurrentSpeed;
	private TextView CurrentTime;
	private TextView CurrentCalories;
	//For counting time
	private Timer timer;
	private int second;
	private int minute;
	private int hour;
	//Buttons
	private Button mStopButton;
	private Intent serviceIntent;
	//Calories
	private static int calories;
	//Steps and Speed
	private int speed;
	private static int steps;
	//Some parameters for calculating calories
	private static int gender;
	private static int age;
	private static int height;
	private static int weight;
	//Database
	private Database mDatabase;
	private SQLiteDatabase getMyDB;
	
	//Service connection
	private final ServiceConnection serviceConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder binder) {
			((BackService.LocalBinder) binder).gimmeHandler(updateDisplay);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// do nothing
		}
	};
   //Message hander
	private final Handler updateDisplay = new Handler() {
		@Override
		public void handleMessage(Message message) {
			steps = message.arg1;
			int bigSteps = message.arg2;
			if (steps > 0) {
				CurrentSteps.setText(String.valueOf(steps));
			}
			
			//Change mode walking or running
			if(bigSteps >= (int)(steps/2)){
				 ACTIVITY_COEFFICIENT = 1.725f;
			}else{
				ACTIVITY_COEFFICIENT = 1.55f;
				}
		}
	};
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running);
        
        //Initialize TextViews
        CurrentSteps = (TextView) findViewById(R.id.step);//Display steps 
        CurrentSpeed = (TextView) findViewById(R.id.speed);//Display speed 
        CurrentTime = (TextView) findViewById(R.id.time);//Display counting time for 1 sec.
        CurrentCalories = (TextView) findViewById(R.id.calorie);//Display calories
        
        CurrentSpeed.setText(DISPLAY_NO_DATA);
        CurrentTime.setText("00"+" : " + "00" + " : " + "00");
        CurrentCalories.setText("0");
        
        //Timer
        timer = new Timer();
        timer.schedule(task, 1000, 1000);
        //Preference
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //If the values do not exist, return -1 
        gender = Integer.parseInt(prefs.getString("gender", "-1"));
        age = Integer.parseInt(prefs.getString("age", "-1"));
        height = Integer.parseInt(prefs.getString("height", "-1"));
        weight = Integer.parseInt(prefs.getString("weight", "-1"));
        //Initialize database
        mDatabase = new Database(this, "history.db", null, 1);
        
        //Button listener
        mStopButton = (Button) findViewById(R.id.stopButton);
		mStopButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				getMyDB = mDatabase.getWritableDatabase();
				getMyDB.execSQL("INSERT INTO showhistory (steps, calories, time) VALUES ('"+steps+"','"+calories+"', datetime())");
				getMyDB.close();
				stopService(new Intent(RunningActivity.this, BackService.class));
				finish();
		
			}
		});
		serviceIntent = new Intent(RunningActivity.this, BackService.class);
		startService(serviceIntent);
    }

	@Override
	protected void onStop() {
		super.onStop();
		unbindService(serviceConnection);
		steps = 0;
		calories = 0;
	}

	@Override
	protected void onStart() {
		super.onStart();
		bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onBackPressed() {
		// do nothing, disable returning to previous activity
	}
	
	//This method for counting time and calculating some parameters
	private TimerTask task = new TimerTask() {  
        @Override  
        public void run() {  
  
            runOnUiThread(new Runnable() {      // UI thread  
                @Override  
                public void run() { 
                                         
                    second++;
                    if(second == 60){
                       second=0;
                       minute++;                   	
                    }
                    if(minute == 60){
                    	minute=0;
                    	hour++;
                    }
                    if(minute%10==0&&second==0){
                    	writeXML();
                    }else{
                    	//do nothing
                    }
                    
                    //calculate speed (time/steps) = speed (by minutes)
                    if(steps > 0){
                    speed = (int)((((hour/3600)+(minute/60)+second)/steps)*60);
                    }
                    //Calculate calories by using 
                    //BRM(Basal Metabolic Rate)+ activity coefficient﹞BRM
                    /**
                      For men: BMR = 66 + (13.7 X weight in kg) + (5 X height in cm) - (6.8 X age in years)
					  For women: BMR = 655 + (9.6 X weight in kg) + (1.8 X height in cm) - (4.7 X age in years)
                      
                      Sedentary 每 BMR x 1.2
					  Lightly active  每 BMR x 1.375
					  Moderately active  每 BMR x 1.55
					  Very active  每 BMR x 1.725
					  Extremely active  每 BMR x 1.9
                      In this case we choose  Moderately active parameter
                     **/
                    //Women
                    if(gender == 0){
                    calories += (int)(((((655+(9.6*weight)+(1.8*height)-(4.7*age))*ACTIVITY_COEFFICIENT)/24)/3600)
                    					*(second));
                    }//men
                    else{
                    calories += (int)(((((66+(13.7*weight)+(5*height)-(6.8*age))*ACTIVITY_COEFFICIENT)/24)/3600)
        								*(second));
                    }
                    
                    
                    //Display
                    CurrentSpeed.setText(String.valueOf(speed));
                    
                    CurrentCalories.setText(String.valueOf(calories));
                    
                    CurrentTime.setText(String.valueOf(hour)+" : "+
                    					String.valueOf(minute)+" : "+
                    					String.valueOf(second));   
                     
                }  
            });  
        }  
    };  
   
    //This method for writing XML file
    private void writeXML(){
    	//Get system path = "data/data/...."
    	String appDir = this.getApplicationContext().getFilesDir().getAbsolutePath();
    	//Get current data and time 
    	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");     
    	Date curDate = new Date(System.currentTimeMillis());    
    	String datatime = formatter.format(curDate);
    	//File name
        String xmlFileName = appDir + "/" + datatime + ".xml";
        File xmlFile = new File(xmlFileName);
        try {
        xmlFile.createNewFile();
        }catch (IOException e){
         Log.e("IOException", "exception in createNewFile() method");
       }
       //Create file
       FileOutputStream fileos = null; 
       try {
         fileos = new FileOutputStream(xmlFile);
       }catch (FileNotFoundException e){
      Log.e("FileNotFoundException", "can't create FileOutputStream");
      }
      //Using XmlPull API
      XmlSerializer serializer = Xml.newSerializer();
      try
     {
     serializer.setOutput(fileos, "UTF-8");
     serializer.startDocument(null, Boolean.valueOf(true));
     serializer.setFeature("http://xmlpull.org/v1/doc/features.html#indent-output",true);
     //Root tag
     serializer.startTag(null, "root");
     //sub tag1
     serializer.startTag(null, "Steps");
     serializer.text(String.valueOf(steps));
     serializer.endTag(null, "Steps");
     //sub tag2
     serializer.startTag(null, "Calories");
     serializer.text(String.valueOf(calories));
     serializer.endTag(null, "Calories");
     //sub tag3
     serializer.startTag(null, "EndTime");
     serializer.text(datatime);
     serializer.endTag(null, "EndTime");
     //end root tag
     serializer.endTag(null, "root");
     serializer.endDocument();
     serializer.flush();
     fileos.close();
     }catch (Exception e){
       Log.e("Exception", "error occurred while creating xml file");    
    }
  }
   
}
