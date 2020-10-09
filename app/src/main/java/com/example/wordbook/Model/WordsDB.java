package com.example.wordbook.Model;

public class WordsDB {
    private static final String TAG = "myTag: ";

    private static WordsDBHelper wordsDBHelper;

    private static WordsDB instance = new WordsDB();

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
    //获得单词的全部信息
    public WordDescription getSingleWord(String id) {
        //通过id索引找到对应单词的全部信息
        WordDescription wd = new WordDescription();
        if()
        return
    }
    //得到单词列表
    //将游标转化为单词列表


}
