package com.example.wordbook.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
//实现对单词的增删改查
public class WordsDB {
    private static final String TAG = "myTag: ";

    private static WordsDBHelper wordsDBHelper;
    private static SQLiteDatabase db;
    private static Cursor cursor;
    private static WordsDB instance = new WordsDB();
    private String sql;

    // 创建WordsDB实例
    public static WordsDB getWordsDB() {
        return WordsDB.instance;
    }

    private WordsDB() {//不能通过外部的new实例化该类，只能通过该类的方法getWordsDB创建新的实例
        if (wordsDBHelper != null) {
            wordsDBHelper = new WordsDBHelper(WordsApplication.getContext());
        }
    }
    public void close(){
        if(wordsDBHelper != null)
            wordsDBHelper.close();
    }
    //获得某个单词的全部信息
    public WordDescription getSingleWord(String id) {
        //通过id索引从数据库中得到单词的全部信息，然后将其赋值给WordDescription。
          sql = "select * from words where _ID = '"+ id+"'";
         db = wordsDBHelper.getReadableDatabase();
        cursor = db.rawQuery(sql,null);
        //  由于此处只可能得到一行，即一组的数据，因此不需要循环取出cursor中的值
        String  getId = cursor.getString(cursor.getColumnIndex(Words.Word._ID));
        String  word = cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD));
        String  meaning = cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING));
        String  sample = cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE));

        return  new WordDescription(getId,word,meaning,sample);
    }
    //得到单词列表
    public ArrayList<Map<String, String>> getAllWords() {
        sql = "select * from words ";
        db = wordsDBHelper.getReadableDatabase();
        cursor = db.rawQuery(sql,null);

        return ConvertCursor2WordList(cursor);
    }
    //将游标转化为单词列表(即解析游标)
    private ArrayList<Map<String, String>> ConvertCursor2WordList(Cursor cursor) {
        ArrayList<Map<String, String>> array = new ArrayList<Map<String, String>>();
        Map<String, String> info = new HashMap<String,String>();
        while(cursor.moveToNext()){//循环取出游标存储的数据
            info.clear();
            info.put(Words.Word._ID,cursor.getString(cursor.getColumnIndex(Words.Word._ID)));
            info.put(Words.Word.COLUMN_NAME_WORD,cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)));
            info.put(Words.Word.COLUMN_NAME_MEANING,cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)));
            info.put(Words.Word.COLUMN_NAME_SAMPLE,cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE)));
            array.add(info);
        }
        return array;
    }
    //增加单词方法1
    public void InsertUserSql(String strWord, String strMeaning, String strSample) {
        sql = "insert into words(word,meaning,sample) values(?,?,?)";
        db =wordsDBHelper.getWritableDatabase();
        db.execSQL(sql,new String[]{strWord,strMeaning,strSample});
    }
    //插入数据方法2
    public void Insert(String strWord, String strMeaning, String strSample) {
        db =wordsDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);
        //插入数据，并返回插入数据的主键id
        long rowId = db.insert(Words.Word.TABLE_NAME,null,values);
    }
    //删除单词方法1
    public void DeleteUseSql(String strId) {
        sql = "delete from words where  _id = '" +strId+"'";
        db = wordsDBHelper.getReadableDatabase();
        db.execSQL(sql);
    }
    //删除单词方法2
    public void Delete(String strId) {

    }
    //更新单词方法1
    public void UpdateUseSql(String strId, String strWord, String strMeaning, String strSample) {

    }
    //更新单词方法2
    public void Update(String strId, String strWord, String strMeaning, String strSample) {

    }
    //查找方法1
    //public ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {


   // }
    //查找方法2
    //public ArrayList<Map<String, String>> Search(String strWordSearch) {

   // }
}
