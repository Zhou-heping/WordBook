package com.example.wordbook.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.wordbook.Methods.WordsDB;
import com.example.wordbook.R;
import com.example.wordbook.wordModel.Words;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//生词本，需要实现生词记忆
public class RemberWordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glossary_notebook);
    }

}
