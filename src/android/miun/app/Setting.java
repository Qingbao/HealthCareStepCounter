/**
 * 
 */
package android.miun.app;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * @author Qingbao.Guo
 *
 */
public class Setting extends PreferenceActivity{
	

	@Override  
	public void onCreate(Bundle savedInstanceState) {  
		
	super.onCreate(savedInstanceState);  
    //Got Preference items form XML.
    addPreferencesFromResource(R.xml.preference);  
    
	 }

}
