package com.example.wordbook.wordModel;

import android.net.Uri;
import android.provider.BaseColumns;

public class Words {

    public Words() {
    }

    //单词列表项
    public static class WordItem {
        public String id;
        public String word;
        public WordItem(String id, String item) {
            this.id = id;
            this.word = word;
        }
        @Override
        public String toString() {
            return word;
        }
    }

    //每个单词的描述
    public static class WordDescription {
        public String id;
        public String word;
        public String meaning;//单词含义
        public String sample;//例子

        public WordDescription(String id, String word, String meaning, String sample) {
            this.id = id;
            this.word = word;
            this.meaning = meaning;
            this.sample = sample;
        }

        public String getSample() {
            return sample;
        }
    }

    public static final String AUTHORITY = "com.example.wordbook.wordsprovider";//URI授权者
    // words表共5个字段：_ID(从接口BaseColumns而来)、word、meaning、sampl,state
    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME = "words";//表名
        public static final String COLUMN_NAME_WORD = "word";//列：单词
        public static final String COLUMN_NAME_MEANING = "meaning";//列：单词含义
        public static final String COLUMN_NAME_SAMPLE = "sample";//单词示例
        public static final String COLUMN_NAME_STATE ="state";//默认为0，如果为1则加入了生词本

        //MIME类型
        public static final String MIME_DIR_PREFIX = "vnd.android.cursor.dir";
        public static final String MIME_ITEM_PREFIX = "vnd.android.cursor.item";
        public static final String MINE_ITEM = "com.example.wordbook.word";

        // 单一数据的MIME类型字符串应该以vnd.android.cursor.item/开头
        public static final String MINE_TYPE_SINGLE = MIME_ITEM_PREFIX + "/" + MINE_ITEM;
        // 数据集的MIME类型字符串则应该以vnd.android.cursor.dir/开头
        public static final String MINE_TYPE_MULTIPLE = MIME_DIR_PREFIX + "/" + MINE_ITEM;

        public static final String PATH_SINGLE = "word/#";//单条数据的路径
        public static final String PATH_MULTIPLE = "word";//多条数据的路径

        //Content Uri
        public static final String CONTENT_URI_STRING = "content://" + AUTHORITY + "/" + PATH_MULTIPLE;
        public static final Uri CONTENT_URI = Uri.parse(CONTENT_URI_STRING);
    }

}