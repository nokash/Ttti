package com.example.noka.a42translate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.List;

/**
 * Created by ben on 5/30/2016.
 */


public class DatabaseHelper extends SQLiteOpenHelper {
    //DATABASE VARIABLES
    public static final String DATABASE_NAME = "db_Bets_Alpa";
    public static final String DEVICE_INFO_TABLE = "user_table";
    public static final String ADMIN = "admin_table";
    public String username;
    public String boss;


    //DEVICE REGISTRATION VARIABLES
    public static final String COL_1DEVICE = "user_id";
    public static final String COL_2DEVICE = "name1";
    public static final String COL_ID_ADMIN = "user_id";
    public static final String COL_NAME_ADMIN = "name";
//    public static final String COL_3DEVICE = "name2";
//    public static final String COL_4DEVICE = "photo";
//    public static final String COL_5DEVICE = "username";
//    public static final String COL_6DEVICE = "access_code";




    Context ctx;
    int print_count;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.ctx = context;


    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + DEVICE_INFO_TABLE + " (user_id INTEGER,name1 TEXT)");
        db.execSQL("create table " +  ADMIN + " (user_id INTEGER, name TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DEVICE_INFO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + ADMIN);
        onCreate(db);

    }
//    public boolean updateGroup(String idmobile_itinerary_routes, String itinerary_name) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COL_2GROUP, itinerary_name);
////        db.update(TABLE_GROUP, contentValues,COL_2GROUP +" = ?", new String[] {itinerary_name});
//
//
//        String where = "idmobile_itinerary_routes=?";
//        String[] whereArgs = new String[]{String.valueOf(idmobile_itinerary_routes)};
//
//        db.update(TABLE_GROUP, contentValues, where, whereArgs);
//        db.close();
//
//        return true;
//    }

    public boolean insertUser(String user_id, String name) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID_ADMIN, user_id);
        contentValues.put(COL_NAME_ADMIN, name);
//        contentValues.put(COL_3DEVICE, name2);
//        contentValues.put(COL_4DEVICE, username);
//        contentValues.put(COL_5DEVICE, photo);
        long result = db.insert(ADMIN, null, contentValues);
        db.close();

        if (result == -1)
            return false;
        else
            return true;
    }

    public boolean insertUser_login(String user_id, String name1) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM "+DEVICE_INFO_TABLE);
        db.execSQL("VACUUM");

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1DEVICE, user_id);
        contentValues.put(COL_2DEVICE, name1);
//        contentValues.put(COL_3DEVICE, name2);
//        contentValues.put(COL_4DEVICE, photo);
//        contentValues.put(COL_5DEVICE, username);
//        contentValues.put(COL_6DEVICE, access_code);

        long result = db.insert(DEVICE_INFO_TABLE, null, contentValues);
        db.close();

        if (result == -1)
            return false;
        else
            return true;
    }


//    public void delete_everything() {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.execSQL("DELETE FROM "+TABLE_ITEM);
//        db.execSQL("DELETE FROM "+TABLE_GROUP);
//        db.execSQL("DELETE FROM "+UPDATED_ITEMS_TABLE);
//        db.execSQL("DELETE FROM "+TABLE_BILLS);
//        db.execSQL("DELETE FROM "+TABLE_BILL_lOGS);
//
//        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+TABLE_ITEM+"';");
//        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+TABLE_GROUP+"';");
//        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+UPDATED_ITEMS_TABLE+"';");
//        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+TABLE_BILLS+"';");
//        db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE name='"+TABLE_BILL_lOGS+"';");
//        db.execSQL("VACUUM");
//        db.close();
//
//    }

    public Cursor select_user() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT user_id,name1 FROM  user_table LIMIT 1",null);
        return c;
    }
    public Cursor select_post_user() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor c = db.rawQuery("SELECT user_id FROM  user_table LIMIT 1",null);
        return c;
    }

    public void deleteUser() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM user_table");
        db.execSQL("VACUUM");
        db.close();

    }

    public void deleteAdmin() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM admin_table");
        db.execSQL("VACUUM");
        db.close();

    }

    public String getUserName(){
        final String userTable = "user_table";
        String selecQuery = "SELECT name1 FROM " + userTable;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selecQuery, null);
        String data = null;
        if(cursor.moveToFirst()){
            do{
                //get data into array or class variable
                username = data;
            }while (cursor.moveToNext());
        }
        return data;
    }

    public String getAdminName(){
        final String adminTable = "admin_table";
        String selecQuery = "SELECT name1 FROM " + adminTable;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selecQuery, null);
        String data = null;
        if(cursor.moveToFirst()){
            do{
                //get data into array or class variable
                boss = data;
            }while (cursor.moveToNext());
        }
        return data;
    }



}
