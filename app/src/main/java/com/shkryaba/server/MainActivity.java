package com.shkryaba.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mInfoTextView;
    private ProgressBar progressBar;
    private EditText editText;
    private ImageView imageView;
    private RestAPI restAPI;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        mInfoTextView = findViewById(R.id.tvLoad);
        progressBar = findViewById(R.id.progressBar);
        imageView = findViewById(R.id.imageView);
        Button btnLoad = findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener((v) -> onClick());
        initOkHttp();
    }

    private void initOkHttp() {
    }

    public void onClick() {


        mInfoTextView.setText("");
        imageView.setImageURI(Uri.EMPTY);
        Retrofit retrofit;

        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restAPI = retrofit.create(RestAPI.class);
        } catch (Exception e) {
            mInfoTextView.setText(String.format("Exception: %s", e.getMessage()));
            return;
        }

        Call<RetrofitModel> call = restAPI.loadUser(editText.getText().toString());
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected())
            try {
                progressBar.setVisibility(View.VISIBLE);
                downloadOneUrl(call);
            } catch (IOException e) {
                Log.e("server", "failed", e);
            }
        else
            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
    }

    private void downloadOneUrl(Call<RetrofitModel> call) throws IOException {

        call.enqueue(new Callback<RetrofitModel>() {
            @Override
            public void onResponse(@NonNull Call<RetrofitModel> call,
                                   @NonNull Response<RetrofitModel> response) {
                if (response.isSuccessful()) {
                    RetrofitModel retrofitModel;
                    retrofitModel = response.body();
                    mInfoTextView.append("\nLogin" + Objects.requireNonNull(retrofitModel).getLogin() +
                            "\nId:" + retrofitModel.getId() +
                            "\nURL" + retrofitModel.getAvatarUrl() +
                            "\n-------------");
                    Picasso.get()
                            .load(retrofitModel.getAvatarUrl())
                            .into(imageView);

                    Call<List<UserReposModel>> callRepo = restAPI.loadUserRepo(editText.getText().toString());
                    callRepo.enqueue(new Callback<List<UserReposModel>>() {
                        @Override
                        public void onResponse(Call<List<UserReposModel>> call, Response<List<UserReposModel>> response) {
                            if (response.isSuccessful()) {
                                UserReposModel retrofitModel;
                                if (response.body() != null) {
                                    for (int i = 0; i < response.body().size(); i++) {

                                        retrofitModel = response.body().get(i);
                                        mInfoTextView.append(
                                                "\nNAME_Repo: " + retrofitModel.getName() +
                                                        "\n-------------");
                                    }
                                }
                            } else
                                mInfoTextView.setText(String.format("onResponse: %d", response.code()));
//                            progressBar.setVisibility(View.GONE);

                        }

                        @Override
                        public void onFailure(Call<List<UserReposModel>> call, Throwable t) {
                            mInfoTextView.setText(String.format("onFailure: %s", t.getMessage()));
                            progressBar.setVisibility(View.GONE);
                        }
                    });
                } else
                    mInfoTextView.setText(String.format("onResponse: %d",
                            response.code()));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<RetrofitModel> call, @NonNull Throwable t) {
                mInfoTextView.setText(String.format("onFailure: %s", t.getMessage()));
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
