package com.example.wordbook.Fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.wordbook.WordsDB;
import com.example.wordbook.R;
import com.example.wordbook.wordcontract.Words;

public class WordDetailFragment extends Fragment {
    public static final String ARG_ID = "id";
    private String wordsID;//单词主键
    private OnFragmentInteractionListener mListener;//本Fragment所在的Activity

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (OnFragmentInteractionListener) getActivity();//得到所在activity
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        public void onWordDetailClick(Uri uri);//在WordDetailActivituy中实现
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wordsID = getArguments().getString(ARG_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_detail, container, false);
        WordsDB wordsDB = WordsDB.getWordsDB();
        if (wordsDB != null && wordsID != null) {
            TextView textViewWord = (TextView) view.findViewById(R.id.word);
            TextView textViewWordMeaning = (TextView) view.findViewById(R.id.wordmeaning);
            TextView textViewWordSample = (TextView) view.findViewById(R.id.wordsample);

            Words.WordDescription item = wordsDB.getSingleWord(wordsID);//通过id显示单词的详细信息
            if (item != null) {
                textViewWord.setText(item.word);
                textViewWordMeaning.setText(item.meaning);
                textViewWordSample.setText(item.sample);
            } else {//如果找不到该单词则全部设为空
                textViewWord.setText("");
                textViewWordMeaning.setText("");
                textViewWordSample.setText("");
            }
        }
        return view;
    }
}