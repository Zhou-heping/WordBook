package com.example.wordbook.Model;

import android.provider.BaseColumns;

// words表共4个字段：_ID(从接口BaseColumns而来)、word、meaning、sample
public class Words {
    public Words() {

    }

    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME = "words";//表名
        public static final String COLUMN_NAME_WORD = "word";//列：单词

        public static final String COLUMN_NAME_MEANING = "meaning";//列：单词含义

        public static final String COLUMN_NAME_SAMPLE = "sample";//单词示例

    }

}
