package com.example.wordbook.Model;

//单词列表项
public class WordItem {
    public String id;
    public String word;

    public WordItem(String id, String item) {
        this.id = id;
        this.word = word;
    }

    public String getId() {
        return id;
    }

    public String getWord() {
        return word;
    }

    @Override
    public String toString() {
        return word;
    }
}
