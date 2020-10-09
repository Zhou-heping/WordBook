package com.example.wordbook.Model;

public class WordDescription {
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

    //get，set(可以省去)方法
    public String getId() {
        return id;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getWord() {
        return word;
    }

    public String getSample() {
        return sample;
    }
}
