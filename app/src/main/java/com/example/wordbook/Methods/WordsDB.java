package com.example.wordbook.Methods;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.wordbook.wordModel.Words;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//实现对单词的增删改查
public class WordsDB {
    private static final String TAG = "tag";

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
        if (wordsDBHelper == null) {
            wordsDBHelper = new WordsDBHelper(WordsApplication.getContext());
        }
    }

    public void close() {
        if (wordsDBHelper != null)
            wordsDBHelper.close();
    }

    //获得某个单词的全部信息
    public Words.WordDescription getSingleWord(String id) {
        //通过id索引从数据库中得到单词的全部信息，然后将其赋值给WordDescription。
        sql = "select * from words where _id = '" + id + "'";
        db = wordsDBHelper.getReadableDatabase();
        cursor = db.rawQuery(sql, null);
        if (cursor.moveToNext()) {
            //  由于此处只可能得到一行，即一组的数据，因此不需要循环取出cursor中的值
            String getId = cursor.getString(cursor.getColumnIndex(Words.Word._ID));
            String word = cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD));
            String meaning = cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING));
            String sample = cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE));

            return new Words.WordDescription(getId, word, meaning, sample);
        }
        return new Words.WordDescription("", "", "", "");
    }

    //得到单词列表
    public ArrayList<Map<String, String>> getAllWords() {
        sql = "select * from words ";
        db = wordsDBHelper.getReadableDatabase();
        cursor = db.rawQuery(sql, null);
        return ConvertCursor2WordList(cursor);
    }
    //得到所有生词
    public ArrayList<Map<String, String>> getRemenberAllWords(){
        sql = "select * from words where state = '1' ";
        db = wordsDBHelper.getReadableDatabase();
        cursor = db.rawQuery(sql, null);
        return ConvertCursor2WordList(cursor);
    }

    //将游标转化为单词列表(即解析游标)
    private ArrayList<Map<String, String>> ConvertCursor2WordList(Cursor cursor) {
        ArrayList<Map<String, String>> array = new ArrayList<Map<String, String>>();

        while (cursor.moveToNext()) {//循环取出游标存储的数据
            Map<String, String> info = new HashMap<String, String>();
            info.put(Words.Word._ID, cursor.getString(cursor.getColumnIndex(Words.Word._ID)));
            info.put(Words.Word.COLUMN_NAME_WORD, cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)));
            info.put(Words.Word.COLUMN_NAME_MEANING, cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_MEANING)));
            info.put(Words.Word.COLUMN_NAME_SAMPLE, cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_SAMPLE)));
            info.put(Words.Word.COLUMN_NAME_STATE, cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_STATE)));
            array.add(info);
        }
        return array;
    }

    //增加单词方法1
    public void InsertUserSql(String strWord, String strMeaning, String strSample) {
        sql = "insert into words(_id,word,meaning,sample) values(?,?,?,?)";

        String id = GUID.getGUID();
        db = wordsDBHelper.getWritableDatabase();
        db.execSQL(sql, new String[]{id, strWord, strMeaning, strSample});

    }

    //插入数据方法2
    public void Insert(String strWord, String strMeaning, String strSample) {
        db = wordsDBHelper.getWritableDatabase();
        String id = GUID.getGUID();
        ContentValues values = new ContentValues();
        values.put(Words.Word._ID, id);
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);
        //插入数据，并返回插入数据的主键id
        long rowId = db.insert(Words.Word.TABLE_NAME, null, values);

    }

    //删除单词方法1
    public void DeleteUseSql(String strId) {
        sql = "delete from words where  _id = '" + strId + "'";
        db = wordsDBHelper.getReadableDatabase();
        db.execSQL(sql);

    }

    //删除单词方法2
    public void Delete(String strId) {
        db = wordsDBHelper.getReadableDatabase();
        // 定义where子句
        String selection = Words.Word._ID + "=?";
        // 指定占位符对应的实际参数
        String[] selectionArgs = {strId};
        db.delete(Words.Word.TABLE_NAME, selection, selectionArgs);
    }

    //更新单词方法1
    public void UpdateUseSql(String strId, String strWord, String strMeaning, String strSample) {
        db = wordsDBHelper.getReadableDatabase();
        sql = "update words set word=?,meaning=?,sample=? where _id=?";
        db.execSQL(sql, new String[]{strWord, strMeaning, strSample, strId});

    }

    //更新单词方法2
    public void Update(String strId, String strWord, String strMeaning, String strSample) {
        db = wordsDBHelper.getReadableDatabase();
        //将要更新的值存入values
        ContentValues values = new ContentValues();
        values.put(Words.Word.COLUMN_NAME_WORD, strWord);
        values.put(Words.Word.COLUMN_NAME_MEANING, strMeaning);
        values.put(Words.Word.COLUMN_NAME_SAMPLE, strSample);
        String selection = Words.Word._ID + " = ?";
        String[] selectionArgs = {strId};
        int count = db.update(Words.Word.TABLE_NAME, values, selection, selectionArgs);
    }
    //更新单词状态
    public void UpdateState(String strId){
        db = wordsDBHelper.getReadableDatabase();
        sql = "update words set state=? where _id=?";
        String state = "1";
        db.execSQL(sql, new String[]{state, strId});
    }
    //查找方法1
    public ArrayList<Map<String, String>> SearchUseSql(String strWordSearch) {
        db = wordsDBHelper.getReadableDatabase();
        sql = "select * from words where word like ? order by word desc";//按照降序排序查询 asc升序
        cursor = db.rawQuery(sql, new String[]{"%" + strWordSearch + "%"});
        return ConvertCursor2WordList(cursor);
    }

    //查找方法2
    public ArrayList<Map<String, String>> Search(String strWordSearch) {
        db = wordsDBHelper.getReadableDatabase();
        String[] projection = {
                Words.Word._ID,
                Words.Word.COLUMN_NAME_WORD,
                Words.Word.COLUMN_NAME_MEANING,
                Words.Word.COLUMN_NAME_SAMPLE
        };
        String sortOrder = Words.Word.COLUMN_NAME_WORD + " DESC";
        String selection = Words.Word.COLUMN_NAME_WORD + " LIKE ?";
        String[] selectionArgs = {"%" + strWordSearch + "%"};
        cursor = db.query(Words.Word.TABLE_NAME, projection, selection, selectionArgs,
                null, null, sortOrder);
        return ConvertCursor2WordList(cursor);
    }
}
