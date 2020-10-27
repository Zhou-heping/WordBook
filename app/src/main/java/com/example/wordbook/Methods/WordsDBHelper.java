package com.example.wordbook.Methods;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.wordbook.wordModel.Words;


//数据库的创建和删除
public class WordsDBHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME = "wordsdb";//数据库名字
    private final static int DATABASE_VERSION = 2;//数据库版本
    SQLiteDatabase sqLiteDatabase;

    public WordsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //Log.i("数据库创建成功","");
    }

    //创建数据库语句
    private final static String SQL_CREATE_DATABASE = "CREATE TABLE " + Words.Word.TABLE_NAME + " (" +
            Words.Word._ID + " VARCHAR(32) PRIMARY KEY NOT NULL," +
            Words.Word.COLUMN_NAME_WORD + " TEXT UNIQUE NOT NULL," +
            Words.Word.COLUMN_NAME_MEANING + " TEXT," +
            Words.Word.COLUMN_NAME_SAMPLE + " TEXT," +
            Words.Word.COLUMN_NAME_STATE + " VARCHER(2) DEFAULT('0')," +
            " CHECK( "+ Words.Word.COLUMN_NAME_STATE +" = '0' OR "+Words.Word.COLUMN_NAME_STATE + " = '1' "+" ) )";

    //删除SQL语句
    private final static String SQL_DELETE_DATABASE = "DROP TABLE IF EXISTS " + Words.Word.TABLE_NAME;

    @Override
    //创建数据库
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        sqLiteDatabase.execSQL(SQL_CREATE_DATABASE);
        Log.i("建表成功？", sqLiteDatabase.toString());
    }
    //删除数据库
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //当数据库升级时被调用，首先删除旧表，然后调用OnCreate()创建新表
        sqLiteDatabase.execSQL(SQL_DELETE_DATABASE);//删除数据库表
        onCreate(sqLiteDatabase);
    }
}
