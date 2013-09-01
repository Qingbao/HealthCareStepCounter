/**
 * 
 */
package android.miun.app;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * @author Qingbao.Guo
 *
 */
public class Database extends SQLiteOpenHelper  {
	
    //Constructor
	public Database(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		//Create table
		String sql = "CREATE TABLE showhistory (_id INTEGER PRIMARY KEY   AUTOINCREMENT, steps verchar, calories verchar, time datetime);";
		db.execSQL(sql);		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 //do nothing		
	}
}
