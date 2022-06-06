package com.example.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messenger.network.NetworkMethodSend;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    public String serverURL = "https://7a4e-89-232-82-1.eu.ngrok.io"; // URL сервера
    public EditText msgtext, nametext;
    public Button sendBtn;
    public TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Objects.requireNonNull(getSupportActionBar()).hide();

        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.rgb(40, 40, 40));

        txt = (TextView) findViewById(R.id.textView);
        msgtext = (EditText) findViewById(R.id.messageinput);
        nametext = (EditText) findViewById(R.id.nameinput);
        ListView lv = (ListView) findViewById(R.id.listView);
        sendBtn = (Button) findViewById(R.id.button);

        sendBtn.setOnClickListener(view -> {
            try {
                NetworkMethodSend nms = new NetworkMethodSend();
                String name = nametext.getText().toString();
                String msg = msgtext.getText().toString();
                if (name.length() > 20) {
                    name = name.substring(0, 20);
                }
                if (msg.length() > 100) {
                    msg = msg.substring(0, 100);
                }

                nms.sendPOST(serverURL, "{\"name\":\"" + name + ":\", \"message\":\"" + msg + "\"}");
            }
            catch (Exception e) {
                Toast.makeText(getApplicationContext(),"NO CONNECTION",
                        Toast.LENGTH_LONG).show();
            }
            msgtext.setText("");
        });
        runThread(this, lv);
    }

    Message[] makeMessage() {
        Message[] arr = new Message[10];
        String[] nameArr;
        String[] msgArr;
        try {
            sendGET(serverURL);
            String jsonResponse = (String) txt.getText();
            Gson gson = new Gson();
            MessagesResponse msgs = gson.fromJson(jsonResponse, MessagesResponse.class);

            nameArr = msgs.names;
            msgArr = msgs.messages;

        }
        catch (Exception e) {
            nameArr = new String[]{"", "", "", "", "", "                  ЗАГРУЗКА...", "", "", "", ""};
            msgArr = new String[]{"", "", "", "", "", "если загрузка идёт слишком долго, проверьте подключение к сети, либо дождитесь починки сервера", "", "", "", ""};

            System.out.println(e.getMessage());
        }

        for (int i = 0; i < arr.length; i++) {
            Message message = new Message(msgArr[i], nameArr[i]);
            arr[i] = message;
        }

        return arr;
    }

    private void runThread(Context context, ListView lv) {
        new Thread() {
            public void run() {
                while (true) {
                    try {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                MsgAdapter adapter = new MsgAdapter(context, makeMessage());
                                lv.setAdapter(adapter);
                            }
                        });
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void sendGET(String url) throws IOException {
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        httpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try (ResponseBody responseBody = response.body()) {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    final String responseStr = response.body().string();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            txt.setText(responseStr);
                        }
                    });


                }

            }
        });
    }

}
