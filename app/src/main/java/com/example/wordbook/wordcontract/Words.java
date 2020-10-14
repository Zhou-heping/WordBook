package com.example.wordbook.wordcontract;

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

    // words表共4个字段：_ID(从接口BaseColumns而来)、word、meaning、sampl
    public static abstract class Word implements BaseColumns {
        public static final String TABLE_NAME = "words";//表名
        public static final String COLUMN_NAME_WORD = "word";//列：单词

        public static final String COLUMN_NAME_MEANING = "meaning";//列：单词含义

        public static final String COLUMN_NAME_SAMPLE = "sample";//单词示例

    }

}