/**
 * 
 */
package android.miun.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author Qingbao.Guo
 *
 */
public class Launcher extends Activity{
	/**

     * The thread to process loading logo image

     */

    private Thread logoThread;    
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo);
        
        final Launcher logo = this;   

        // The thread to wait for splash screen events
        
        logoThread =  new Thread(){

        	@Override
            public void run(){

                try {

                    synchronized(this){
                        // Wait given period of time
                        wait(3000);
                    }

                }catch(InterruptedException ex){                    

                }

               //Finish this activity
                finish();

                // Run Main activity
                Intent intent = new Intent();
                intent.setClass(logo, MainActivity.class);
                startActivity(intent);
                                 
                //Stop this thread
                interrupt();
            }

        };

        logoThread.start();      

    }
}
