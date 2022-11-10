package com.example.helperbot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatBot extends AppCompatActivity {
    private RecyclerView chatRV;
    private EditText userMsgEdt;
    private FloatingActionButton sendMsgFab;
    private final String BOT_KEY = "bot";
    private final String USER_KEY = "user";
    private ArrayList<ChatModal> chatModalArrayList;
    private ChatRVAdapter chatRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_bot2);

        chatRV =  findViewById(R.id.idRvChats);
        userMsgEdt = findViewById(R.id.idEditMessage);
        sendMsgFab = findViewById(R.id.idFabSend);
        chatModalArrayList = new ArrayList<>();
        chatRVAdapter = new ChatRVAdapter(chatModalArrayList,this);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        chatRV.setLayoutManager(manager);
        chatRV.setAdapter(chatRVAdapter);

        sendMsgFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userMsgEdt.getText().toString().isEmpty()){
                    Toast.makeText(ChatBot.this, "Please enter your name", Toast.LENGTH_SHORT).show();
                }
                getResponse(userMsgEdt.getText().toString());
                userMsgEdt.setText("");

            }
        });
    }
    private void getResponse(String message) {
        chatModalArrayList.add(new ChatModal(message, USER_KEY));
        chatRVAdapter.notifyDataSetChanged();
        String url = "http://api.brainshop.ai/get?bid=169277&key=nhU3rAEM3XXReuUv&uid=uid&msg=hi"+message;
        String BASE_URL =  "http://api.brainshop.ai/";




        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitApi retrofitApi = retrofit.create(RetrofitApi.class);
        Call<MsgModal> call = retrofitApi.getMessage(url);
        call.enqueue(new Callback<MsgModal>() {
            @Override
            public void onResponse(Call<MsgModal> call, Response<MsgModal> response) {
                if(response.isSuccessful()){
                    MsgModal modal = response.body();
                    chatModalArrayList.add(new ChatModal(modal.getCnt(),BOT_KEY));
                    chatRVAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<MsgModal> call, Throwable t) {
                chatModalArrayList.add(new ChatModal("Please revert your question",BOT_KEY));
                chatRVAdapter.notifyDataSetChanged();
            }
        });

    }

}