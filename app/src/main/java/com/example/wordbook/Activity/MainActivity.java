package com.example.wordbook.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;

import com.example.wordbook.Fragment.WordDetailFragment;
import com.example.wordbook.Fragment.WordItemFragment;
import com.example.wordbook.Methods.WordsDB;
import com.example.wordbook.Methods.WordsDBHelper;
import com.example.wordbook.Model.WordDescription;
import com.example.wordbook.R;

import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements WordItemFragment.OnFragmentInteractionListener,
        WordDetailFragment.OnFragmentInteractionListener {
    WordsDBHelper wordsDBHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //相关组件的注册和使用（button，EextView,EditText）
        //展示省略
        //对话框注册
        //暂时省略
        //菜单注册，上下文菜单，
        ListView list = (ListView) findViewById(R.id.lstWords);
        registerForContextMenu(list);
        //创建SQLiteOpenHelper对象，注意第一次运行时，此时数据库并没有被创建
        wordsDBHelper = new WordsDBHelper(this);
        //在列表显示全部单词


        ArrayList<Map<String, String>> items=getAll();
        setWordsListView(items);

    }

    //当用户在单词详细Fragment中单击时回调此函数
    public void onWordDetailClick(Uri uri) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        wordsDBHelper.close();
    }
    //当用户在单词列表Fragment中单击某个单词时回调此函数,判断如果横屏的话，则需要在右侧单词详细Fragment中显示
    @Override
    public void onWordItemClick(String id) {
        if (isLand()) {
            ChangeWordDetailFragment(id);
        } else {
            Intent intent = new Intent(MainActivity.this, WordDetailActivity.class);
            intent.putExtra(WordDetailFragment.ARG_ID, id);
            startActivity(intent);
        }
    }

    @Override
    public void onDeleteDialog(String strId) {
        DeleteDialog(strId);
    }

    @Override
    public void onUpdateDialog(String strId) {
        WordsDB wordsDB = WordsDB.getWordsDB();
        if (wordsDB != null && strId != null) {
            WordDescription wordDescription = wordsDB.getSingleWord(strId);
            if (wordDescription != null) {
                UpdateDialog(strId, wordDescription.word, wordDescription.meaning, wordDescription.sample);
            }
        }
    }

    private boolean isLand() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        }
        return false;
    }

    private void ChangeWordDetailFragment(String id) {
        Bundle arguments = new Bundle();
        arguments.putString(WordDetailFragment.ARG_ID, id);
        Log.v("查看id:", id);
        WordDetailFragment fragment = new WordDetailFragment();
        fragment.setArguments(arguments);
        getFragmentManager().beginTransaction().replace(R.id.worddetail, fragment).commit();

    }

    //新增对话框
    private void InsertDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        new AlertDialog.Builder(this).setTitle("新增单词")
                .setView(tableLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();
                        //使用Sql语句插入
                        WordsDB wordsDB = WordsDB.getWordsDB();
                        wordsDB.Insert(strWord, strMeaning, strSample);
                        //单词已经插入到数据库，更新显示列表
                        RefreshWordItemFragment();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }

        }).create()//创建对话框
                .show();//显示对话框
    }
    //删除对话框
    private void DeleteDialog(final String strId) {
        new AlertDialog.Builder(this).setTitle("删除单词").setMessage("是否真的删除单词?")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //使用Sql语句删除
                        WordsDB wordsDB=WordsDB.getWordsDB();
                        wordsDB.DeleteUseSql(strId);
                        //单词已经删除，更新显示列表
                        RefreshWordItemFragment();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { }
        }).create().show();
    }
    //修改对话框
    private void UpdateDialog(final String strId, final String strWord, final String strMeaning, final String strSample) {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert, null);
        ((EditText) tableLayout.findViewById(R.id.txtWord)).setText(strWord);
        ((EditText) tableLayout.findViewById(R.id.txtMeaning)).setText(strMeaning);
        ((EditText) tableLayout.findViewById(R.id.txtSample)).setText(strSample);
        new AlertDialog.Builder(this).setTitle("修改单词").setView(tableLayout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String strNewWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                        String strNewMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                        String strNewSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();
                        //更新数据库的信息
                        WordsDB wordsDB=WordsDB.getWordsDB();
                        wordsDB.UpdateUseSql(strId, strWord, strNewMeaning, strNewSample);
                        //单词已经更新，更新显示列表
                        RefreshWordItemFragment();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) { }
                }).create().show();
    }
    //查找对话框
    private void SearchDialog() {
        final TableLayout tableLayout = (TableLayout) getLayoutInflater()
                .inflate(R.layout.searchterm, null);
        new AlertDialog.Builder(this)
                .setTitle("查找单词")//标题
                .setView(tableLayout)//设置视图
                //确定按钮及其动作
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String txtSearchWord = ((EditText) tableLayout.findViewById(R.id.txtSearchWord))
                                .getText().toString();
                        //单词已经插入到数据库，更新显示列表
                        RefreshWordItemFragment(txtSearchWord);
                    }
                })
                //取消按钮及其动作
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .create()//创建对话框
                .show();//显示对话框
    }
    private void RefreshWordItemFragment() {
        WordItemFragment wordItemFragment = (WordItemFragment) getFragmentManager()
                .findFragmentById(R.id.wordslist);
        wordItemFragment.refreshWordsList();
    }

    //更新单词列表

    private void RefreshWordItemFragment(String strWord) {
        WordItemFragment wordItemFragment = (WordItemFragment) getFragmentManager()
                .findFragmentById(R.id.wordslist);
        wordItemFragment.refreshWordsList(strWord);
    }
}