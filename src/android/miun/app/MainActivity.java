package android.miun.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * @author Qingbao.Guo
 *
 */

public class MainActivity extends Activity {
	
	private Button mStartButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        //Start button
        mStartButton = (Button) findViewById(R.id.startButton);
		mStartButton.setOnClickListener(new Button.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, RunningActivity.class));
			}
		});
        
    }
    
    /**
     * Menu bar
     **/ 
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater=getMenuInflater();
		inflater.inflate(R.layout.menu,menu);
		return true;
	}
    /**
     * Four option items
     * **/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int item_id=item.getItemId();
		
		switch(item_id){
		//About item
		case R.id.about:
			Dialog dialog1=new AlertDialog.Builder(MainActivity.this)		
			.setTitle("About")
			.setMessage("Health-care App by Qingbao Guo\nVersion 1.0")
			.setPositiveButton("OK", new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialog, int which){
					//do nothing					
				}
			}).create();			
			dialog1.show();
			break;
			
		//Exit item	
		case R.id.exit:			
			Dialog dialog2=new AlertDialog.Builder(MainActivity.this)
			.setTitle("Exit")
			.setMessage("Sure you want to exit?")
			.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialog, int which){
					// TODO Auto-generated method stub
					MainActivity.this.finish();
				}
			}).setNegativeButton("No", new DialogInterface.OnClickListener(){
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub					
				}
			}).create();			
			dialog2.show();	
			break;  
			
			//History item
		case R.id.histroy:
			ProgressDialog mypDialog1=new ProgressDialog(this);
            mypDialog1.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //ProgressDialog title
            mypDialog1.setTitle("History");
            //ProgressDialog message     
            mypDialog1.setMessage("Loading....");
            //ProgressDialog indeterminate
            mypDialog1.setIndeterminate(false);
           //ProgressDialog cancelable
            mypDialog1.setCancelable(true);
            //show ProgressDialog
            mypDialog1.show();
            //start history activity 
			startActivity(new Intent(MainActivity.this, History.class));
			break;
			
			//Setting item
		case R.id.setting:		
			ProgressDialog mypDialog2=new ProgressDialog(this);
            mypDialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            //ProgressDialog title
            mypDialog2.setTitle("Setting");
            //ProgressDialog message     
            mypDialog2.setMessage("Loading....");
            //ProgressDialog indeterminate
            mypDialog2.setIndeterminate(false);
           //ProgressDialog cancelable
            mypDialog2.setCancelable(true);
            //show ProgressDialog
            mypDialog2.show();
            //start setting activity 
            startActivity(new Intent(MainActivity.this, Setting.class));
			break;
		}		
		return true;
	}
    
}