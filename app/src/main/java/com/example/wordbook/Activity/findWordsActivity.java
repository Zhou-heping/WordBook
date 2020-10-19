package com.example.wordbook.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.wordbook.Methods.WordsDB;
import com.example.wordbook.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class findWordsActivity extends AppCompatActivity {

    private TextView word,meaning,sample;
    private EditText input;
    private Button sure,add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_words);
        initView();
    }

    private void initView() {
        word = (TextView) findViewById(R.id.get_word);
        meaning = (TextView) findViewById(R.id.get_meaning);
        sample = (TextView) findViewById(R.id.get_sample);
        input = (EditText) findViewById(R.id.input);
        sure = (Button) findViewById(R.id.sure);
        add = (Button) findViewById(R.id.add);//添加到生词本

        add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String getInput = input.getText().toString();
                String getWord = word.getText().toString();
                getWord = dealString(getWord);
                String getMeaning = meaning.getText().toString();
                getMeaning = dealString(getMeaning);
                String getSample = sample.getText().toString();
                getSample = dealString(getSample);
                if(!(getInput.equals("")||getInput.isEmpty()) && !(getWord.equals("")||getWord.isEmpty())) { //判断是否有输入，以及是否显示了输出单词或者汉语翻译才可以执行操作
                        if(getInput.matches("[A-Za-z ]*")){//如果输入为单词，执行插入操作（即输入为字母和空格）
                            //输入框为英文单词
                            WordsDB wordsDB = WordsDB.getWordsDB();
                            wordsDB.InsertUserSql(getInput,getMeaning,getSample);
                            Toast.makeText(getApplication(),"添加成功",Toast.LENGTH_SHORT).show();
                        }
                        else{//否则为汉字，或者其他内容,，可以简要理解为输入为汉字
                            WordsDB wordsDB = WordsDB.getWordsDB();
                            wordsDB.InsertUserSql(getWord,getMeaning,getSample);
                            Toast.makeText(getApplication(),"添加成功",Toast.LENGTH_SHORT).show();
                        }
                }
            }

        });
        //为Button添加点击事件
        sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                //1、获取输入框的内容
                String s = input.getText().toString();
                //判断输入是否为空
                if (s == null) {
                    Toast.makeText(getApplicationContext(), "输入框不能为空", Toast.LENGTH_LONG).show();
                }

                //3.拼接url
                final String getUrl = "http://fanyi.youdao.com/openapi.do?keyfrom=aaa123ddd&key=336378893&type=data&doctype=json&version=1.1&q=" + s;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            URL url = new URL(getUrl);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            //设置请求方式
                            conn.setConnectTimeout(30000);//设置连接时间
                            conn.setRequestMethod("POST");//默认为get
                            conn.setDoInput(true);//允许写入,,默认为true
                            conn.setDoOutput(true);//允许写出，其默认值为false
                            conn.connect();//连接
                            int responseCode = conn.getResponseCode();
                            if (responseCode == HttpURLConnection.HTTP_OK) {
                                InputStream inputStream = conn.getInputStream();
                                Log.e("resultinputStream:", inputStream + "");
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                byte[] buffer = new byte[2048];
                                int length = 0;
                                while ((length = inputStream.read(buffer)) != -1) {
                                    byteArrayOutputStream.write(buffer, 0, length);//写入输出流
                                }
                                inputStream.close();//读取完毕，关闭输入流
                                String result = new String(byteArrayOutputStream.toByteArray(), "UTF-8");
                                eJson(result);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        });
    }

    //解析数据并显示
    private void eJson(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            String getWord = jsonObject.getString("translation");
            getWord = dealString(getWord);
            word.setText(getWord);

            JSONObject object = jsonObject.getJSONObject("basic");
            String  getMeaning = object.getString("explains");
            getMeaning = dealString(getMeaning);
            meaning.setText(getMeaning);

            String getSample = "";
            JSONArray ja = jsonObject.getJSONArray("web");
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jsonObject3 = (JSONObject) ja.get(i);
                getSample = getSample + jsonObject3.getString("value") + ":";
                getSample = getSample + jsonObject3.getString("key") + "\n";
            }
            //在顶部显示
            getSample = dealString(getSample);
            sample.setText(getSample);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public String dealString(String string){
        string = string.replaceAll("\\[","");
        string = string.replaceAll("\\]","");
        string = string.replaceAll("\"","");
        return string;
    }
}
