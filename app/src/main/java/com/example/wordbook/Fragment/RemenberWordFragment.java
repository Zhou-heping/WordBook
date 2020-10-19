package com.example.wordbook.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wordbook.Methods.WordsDB;
import com.example.wordbook.R;
import com.example.wordbook.wordModel.Words;

import java.util.ArrayList;
import java.util.Map;

import androidx.appcompat.app.AlertDialog;

public class RemenberWordFragment extends Fragment {

    TextView textView;
    EditText editText;
    Button button_ensure, button_meaning, button_next;
    int wordscount = 0;
    int number = 0;
    int count = 0;
    WordsDB wordsDB = WordsDB.getWordsDB();
    ArrayList<Map<String, String>> items = wordsDB.getRemenberAllWords();
    Map<String, String> info;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remenber_word, container,false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = view.findViewById(R.id.tv_word_1);
        editText = view.findViewById(R.id.et_translate);
        button_meaning = view.findViewById(R.id.button_meaning);//单词正确含义
        button_meaning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button_meaning.setText(info.get(Words.Word.COLUMN_NAME_MEANING));
            }
        });
        button_next = view.findViewById(R.id.button_next);//下一题
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("");
                editText.setText("");
                button_meaning.setText("查看答案");
                Remenber();
            }
        });
        button_ensure = view.findViewById(R.id.button_ensure);//确认，验证结果是否正确
        button_ensure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (info.get(Words.Word.COLUMN_NAME_MEANING).equals(editText.getText().toString())) {
                    Toast.makeText(getActivity(), "回答正确", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "回答错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Remenber();//默认加载，后续加载需要在点击下一次后才可以继续加载
    }

    public void Remenber() {
        if (count < 4) {

            wordscount = items.size();
            Log.e("生词数量：", wordscount + "");
            number = (int) (wordscount * Math.random());
            info = items.get(number);

            Log.e("info",info.get(Words.Word.COLUMN_NAME_WORD)+"");
            textView.setText(info.get(Words.Word.COLUMN_NAME_WORD));
            Log.e("items",items+"");
            count++;
        } else {
            StartOrEnd();
        }
    }

    public void StartOrEnd() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("已经完成记忆").setMessage("是否再来一组？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int j) {
                        count = 0;
                        Remenber();
                    }
                }).setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(getActivity(), "您离成功又进了一步了", Toast.LENGTH_SHORT).show();
                editText.setText("");
            }
        }).show();
    }
}
