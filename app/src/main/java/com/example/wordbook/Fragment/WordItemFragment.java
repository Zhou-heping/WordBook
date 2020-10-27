package com.example.wordbook.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.ListFragment;
import com.example.wordbook.Methods.WordsDB;
import com.example.wordbook.R;
import com.example.wordbook.wordModel.Words;

import java.util.ArrayList;
import java.util.Map;

//单词项显示
public class WordItemFragment extends ListFragment {
    private OnFragmentInteractionListener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //刷新单词列表
        refreshWordsList();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener) getActivity();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //view页面注册
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        //为列表注册上下文菜单
        ListView mListView = (ListView) view.findViewById(android.R.id.list);
        //mListView.setOnCreateContextMenuListener(this);
        registerForContextMenu(mListView);
        return view;
    }

    //Fragment所在的Activity必须实现该接口，通过该接口Fragment和Activity可以进行通信
    public interface OnFragmentInteractionListener {

        public void onWordItemClick(String strId);

        public void onDeleteDialog(String strId);

        public void onUpdateDialog(String strId);

         public void onAddToNotebook(String strId);
    }

    //更新单词列表，从数据库中找到所有单词，然后在列表中显示出来
    public void refreshWordsList() {
        WordsDB wordsDB = WordsDB.getWordsDB();
        if (wordsDB != null) {
            ArrayList<Map<String, String>> items = wordsDB.getAllWords();
            if (items.isEmpty()) {
                wordsDB.InsertUserSql("Apple", "苹果", "THSI IS A APPLE");
                items = wordsDB.getAllWords();
                Log.e("items-->", items + "");
            }
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), items, R.layout.item,
                    new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD},
                    new int[]{R.id.textId, R.id.textViewWord});
            setListAdapter(adapter);
        }
    }


    //更新单词列表，从数据库中找到同strWord匹配的单词，然后在设置适配器并显示
    public void refreshWordsList(String strWord) {
        WordsDB wordsDB = WordsDB.getWordsDB();
        if (wordsDB != null) {
            ArrayList<Map<String, String>> items = wordsDB.SearchUseSql(strWord);
            if (items.size() > 0) {
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), items, R.layout.item,//布局文件id，即布局文件在item.xml中
                        new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD},//数组对象
                        new int[]{R.id.textId, R.id.textViewWord});//存放布局的id
                setListAdapter(adapter);
            } else {
                Toast.makeText(getActivity(), "Not found", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onListItemClick(ListView listView, View view, int position, long id) {
        super.onListItemClick(listView, view, position, id);
        if (null != mListener) {
            //通知Fragment所在的Activity，用户单击了列表的position项
            TextView textView = (TextView) view.findViewById(R.id.textId);
            if (textView != null) {
                //将单词ID传过去
                mListener.onWordItemClick(textView.getText().toString());
            }
        }
    }

    //创建上下文菜单，来源contextmenu_wordslistview，并添加菜单项删除和更新
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getActivity().getMenuInflater().inflate(R.menu.contextmenu_wordslistview, menu);
    }

    //对上下文菜单项设置点击动作
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        TextView textId = null;
        TextView textWord = null;
        TextView textMeaning = null;
        TextView textSample = null;
        AdapterView.AdapterContextMenuInfo info = null;
        View itemView = null;
        switch (item.getItemId()) {

            case R.id.action_delete:
                //删除单词
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                itemView = info.targetView;//设置该view在哪个蒙层上显示
                textId = (TextView) itemView.findViewById(R.id.textId);
                if (textId != null) {
                    String strId = textId.getText().toString();
                    mListener.onDeleteDialog(strId);
                }
                break;
            case R.id.action_update:
                //修改单词
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                itemView = info.targetView;//引导蒙层在哪个view上显示
                textId = (TextView) itemView.findViewById(R.id.textId);
                if (textId != null) {
                    String strId = textId.getText().toString();
                    mListener.onUpdateDialog(strId);
                }
                break;

            case R.id.action_remenber://修改数据库，在
                //添加到生词本
                info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
                itemView = info.targetView;//引导蒙层在哪个view上显示
                textId = (TextView) itemView.findViewById(R.id.textId);
                if (textId != null) {
                    String strId = textId.getText().toString();
                    mListener.onAddToNotebook(strId);
                }
                break;
        }
        return true;
    }
}