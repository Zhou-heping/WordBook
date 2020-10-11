package com.example.wordbook.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        //相关组件的注册和使用（button，EextView,EditText）已经在fragment中实现，只需要加载主页面

        //创建SQLiteOpenHelper对象，注意第一次运行时，此时数据库并没有被创建
        wordsDBHelper = new WordsDBHelper(this);

    }

    //选项菜单的创建和其选项的动作
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_search: //查找
                SearchDialog();
                return true;

            case R.id.action_insert://新增单词
                InsertDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    //当用户在单词详细Fragment中单击时回调此函数,空实现
    public void onWordDetailClick(Uri uri) {

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        wordsDBHelper.close();//关闭数据库
    }

    //当用户在单词列表Fragment中单击某个单词时回调此函数,判断如果横屏的话，则需要在右侧单词详细Fragment中显示
    @Override
    public void onWordItemClick(String id) {
        if (isLand()) {
            ChangeWordDetailFragment(id);//如果是横屏，则将单词详细fragment加入该activity，并通过id显示其内容
        }
        else {//为竖屏，当点击单词后，需要进行页面的跳转，即跳到新的WordDetailActivity，并将id传过去，用于显示该id下的单词详细信息
            Intent intent = new Intent(MainActivity.this, WordDetailActivity.class);
            intent.putExtra(WordDetailFragment.ARG_ID, id);//传递数据，将id传给WordDetailActivity并显示内容
            startActivity(intent);
        }
    }

    @Override
    public void onDeleteDialog(String strId) {
        DeleteDialog(strId);
    }

    @Override
    public void onUpdateDialog(String strId) {
        WordsDB wordsDB = WordsDB.getWordsDB();//能否实例化成功，如果能，则继续下面的操作
        if (wordsDB != null && strId != null) {
            WordDescription wordDescription = wordsDB.getSingleWord(strId);
            if (wordDescription != null) {
                UpdateDialog(strId, wordDescription.word, wordDescription.meaning, wordDescription.sample);
            }
        }
    }

    private boolean isLand() {//判断是否为横屏
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            return true;
        }
        return false;
    }

    private void ChangeWordDetailFragment(String id) {
        Bundle arguments = new Bundle();
        arguments.putString(WordDetailFragment.ARG_ID, id);//传递数据id
        Log.v("查看id:", id);
        WordDetailFragment fragment = new WordDetailFragment();
        fragment.setArguments(arguments);
        //getFragmentManager()报错--》解决方案改为 getSupportFragmentManager()
        getSupportFragmentManager().beginTransaction().replace(R.id.wordDetail, fragment).commit();

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
                        String txtSearchWord = ((EditText) tableLayout.findViewById(R.id.txtSearchWord))//从searchterm中获得txtSearchWord
                                .getText().toString();
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
        WordItemFragment wordItemFragment = (WordItemFragment) getSupportFragmentManager()
                .findFragmentById(R.id.wordsList);//R.id.wordslist即WordItemFragment的总布局id
        wordItemFragment.refreshWordsList();
    }

    //更新单词列表

    private void RefreshWordItemFragment(String strWord) {
        WordItemFragment wordItemFragment = (WordItemFragment) getSupportFragmentManager()//增加了Support，根据实际情况修改
                .findFragmentById(R.id.wordsList);//R.id.wordslist即WordItemFragment的总布局id
        wordItemFragment.refreshWordsList(strWord);
    }
}