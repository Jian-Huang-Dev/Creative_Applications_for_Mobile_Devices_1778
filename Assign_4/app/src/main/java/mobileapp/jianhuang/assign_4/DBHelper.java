package mobileapp.jianhuang.assign_4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;

/**
 * Created by jianhuang on 16-02-14.
 */
public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Assign4.db";
    public static final String PEOPLE_INFO_TABLE_NAME = "people_info";
    public static final String PEOPLE_INFO_COLUMN_NAME = "name";
    public static final String PEOPLE_INFO_COLUMN_BIO = "bio";
    public static final String PEOPLE_INFO_COLUMN_PIC_PATH = "path";

    private HashMap hp;

    public DBHelper(Context context)
    {
        super(context, DATABASE_NAME , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table people_info " +
                        "(id integer primary key, name text, bio text, path text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS people_info");
        onCreate(db);
    }

    public boolean insertInfo(String name, String bio, String path)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("bio", bio);
        contentValues.put("path", path);
        db.insert("people_info", null, contentValues);
        return true;
    }

    public Cursor getCursor(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "select * from people_info", null );
        return res;
    }

//    public int numberOfRows(){
//        SQLiteDatabase db = this.getReadableDatabase();
//        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
//        return numRows;
//    }
//
//    public boolean updateContact (Integer id, String name, String phone, String email, String street,String place)
//    {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("name", name);
//        contentValues.put("phone", phone);
//        contentValues.put("email", email);
//        contentValues.put("street", street);
//        contentValues.put("place", place);
//        db.update("contacts", contentValues, "id = ? ", new String[] { Integer.toString(id) } );
//        return true;
//    }

    public Integer deleteTable()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("people_info", null, null);
    }

    public Integer deleteTableRow(Integer id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("people_info",
                "id = ? ",
                new String[] { Integer.toString(id) });
    }

//    public ArrayList<String> getAllCotacts()
//    {
//        ArrayList<String> array_list = new ArrayList<String>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res =  db.rawQuery( "select * from contacts", null );
//        res.moveToFirst();
//
//        while(res.isAfterLast() == false){
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
//            res.moveToNext();
//        }
//        return array_list;
//    }
}