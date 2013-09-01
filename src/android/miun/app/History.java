/**
 * 
 */
package android.miun.app;

import android.app.ListActivity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.SimpleCursorAdapter;

/**
 * @author Qingbao.Guo
 *
 */

/**
 * This class shows data from database as a list
 *
 */
public class History extends ListActivity{
	private SQLiteDatabase  myDB = null;	
	private Cursor cursor = null;   
    private SimpleCursorAdapter adapter = null;
	
	 @Override
	 protected void onCreate(Bundle savedInstanceState) {
	      super.onCreate(savedInstanceState);
	    //Open database
		 myDB = (new Database(this,"history.db",null,1)).getReadableDatabase();
		 //Query data  inverted order
		 cursor =myDB.rawQuery("SELECT * from showhistory order by _id DESC", null);
		 //Put data into adapter
		 adapter = new SimpleCursorAdapter(this,
	                R.layout.history,
	                cursor,
	                new String[]{"steps","calories","time"},
	                new int[]{R.id.steps,R.id.calories,R.id.datetime});
	     //Show data
		 setListAdapter(adapter);  
	        
	 }
	 
	 
	 @Override
	 protected void onDestroy() {
	        super.onDestroy();
	        cursor.close(); 
	        myDB.close(); 
	    }
}
