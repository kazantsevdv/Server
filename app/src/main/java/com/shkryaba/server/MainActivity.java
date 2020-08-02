package com.shkryaba.server;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private TextView mInfoTextView;
    private ProgressBar progressBar;
    private EditText editText;

//  private OkHttpClient client;
//  private HttpUrl.Builder builder;

    private RestAPI restAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        mInfoTextView = findViewById(R.id.tvLoad);
        progressBar = findViewById(R.id.progressBar);

        Button btnLoad = findViewById(R.id.btnLoad);
        btnLoad.setOnClickListener((v) -> onClick());

//      client = new OkHttpClient();

        initOkHttp();
    }

    private void initOkHttp() {
    }

    public void onClick() {

        //TODO HttpUrlConnection
//        String bestUrl = "https://api.github.com/users";  // userName == mojombo
//        if (!editText.getText().toString().isEmpty())
//            bestUrl += "/" + editText.getText();
//
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
//        if (networkinfo != null && networkinfo.isConnected())
//            new DownloadPageTask().execute(bestUrl); // запускаем в новом потоке
//        else
//            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();

        //TODO okHttp
//        builder = HttpUrl.parse("https://api.github.com/users").newBuilder();
//        if (!editText.getText().toString().isEmpty())
//            builder.addQueryParameter("login", editText.getText().toString()); // "mojombo"
//
//        String url = builder.build().toString();
//        Request request = new Request.Builder()
//                .url(url)
//                .build();
//        ConnectivityManager connectivityManager =
//                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
//        if (networkinfo != null && networkinfo.isConnected())
//            try {
//                downloadOneUrl(request);
//            }
//            catch (IOException e) {
//                Log.e("server", "failed", e);
//            }
//        else
//            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();

        mInfoTextView.setText("");
        Retrofit retrofit;

        try {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.github.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            restAPI = retrofit.create(RestAPI.class);
        }
        catch (Exception e) {
            mInfoTextView.setText("Exception: " + e.getMessage());
            return;
        }

        Call<List<RetrofitModel>> call = restAPI.loadUsers();
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkinfo = connectivityManager.getActiveNetworkInfo();
        if (networkinfo != null && networkinfo.isConnected())
            try {
                progressBar.setVisibility(View.VISIBLE);
                downloadOneUrl(call);
            }
            catch (IOException e) {
                Log.e("server", "failed", e);
            }
        else
            Toast.makeText(this, "Подключите интернет", Toast.LENGTH_SHORT).show();
    }

    private void downloadOneUrl(Call<List<RetrofitModel>> call) throws IOException {

        //TODO HttpUrlConnection
//        InputStream inputStream = null;
//        String data = "";
//        try {
//            URL url = new URL(address);
//            HttpURLConnection connection = (HttpURLConnection) url
//                    .openConnection();
//            connection.setReadTimeout(100000);
//            connection.setConnectTimeout(100000);
//            //connection.setRequestMethod("GET");
//            connection.setInstanceFollowRedirects(true);
//            connection.setUseCaches(false);
//            connection.setDoInput(true);
//
//            int responseCode = connection.getResponseCode();
//            if (responseCode == HttpURLConnection.HTTP_OK) {  // 200 OK
//                Log.d("server", "Метод запроса: " +
//                        connection.getRequestMethod());
//                // Вывести код ответа
//                Log.d("server", "Ответное сообщение: " +
//                        connection.getResponseMessage());
//                // Получить список полей и множество ключей из заголовка
//                Map<String, List<String>> myMap = connection.getHeaderFields();
//                Set<String> myField = myMap.keySet();
//                Log.d("server", "Далее следует заголовок:");
//                // Вывести все ключи и значения из заголовка
//                for (String k: myField)
//                    Log.d("server", "Ключ: " + k + " Значение: "
//                            + myMap.get(k));
//                inputStream = connection.getInputStream();
//                ByteArrayOutputStream bos = new ByteArrayOutputStream();
//                int read = 0;
//                while ((read = inputStream.read()) != -1)
//                    bos.write(read);
//                byte[] result = bos.toByteArray();
//                bos.close();
//                data = new String(result);
//            }
//            else
//                data = connection.getResponseMessage() + " . Error Code : " + responseCode;
//            connection.disconnect();
//        }
//        catch (MalformedURLException e) {
//            Log.e("server", "failed", e);
//        }
//        catch (IOException e) {
//            Log.e("server", "failed", e);
//        }
//        finally {
//            if (inputStream != null) inputStream.close();
//        }
//        return data;

        //TODO okHttp
//        progressBar.setVisibility(View.VISIBLE);
//        client.newCall(request).enqueue(new Callback() {
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                Log.e("server", "failed", e);
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (!response.isSuccessful())
//                    throw new IOException("Unexpected code " + response);
//
//                Headers responseHeaders = response.headers();
//                for (int i = 0; i < responseHeaders.size(); i++)
//                    Log.d("server", responseHeaders.name(i) + ": " + responseHeaders.value(i));
//
//                final String responseData = response.body().string();
//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        mInfoTextView.setText(responseData);
//                        progressBar.setVisibility(View.GONE);
//                    }
//                });
//            }
//        });

        call.enqueue(new Callback<List<RetrofitModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<RetrofitModel>> call,
                                   @NonNull Response<List<RetrofitModel>> response) {
                if (response.isSuccessful()) {
                    RetrofitModel retrofitModel;
                    for (int i = 0; i < response.body().size(); i++) {
                        retrofitModel = response.body().get(i);
                        mInfoTextView.append("\nLogin" + retrofitModel.getLogin() +
                        "\nId" + retrofitModel.getId() +
                        "\nURL" + retrofitModel.getAvatarUrl() +
                        "\n-------------");
                    }
                }
                else
                    mInfoTextView.setText("onResponse: " + response.code());

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<List<RetrofitModel>> call, @NonNull Throwable t) {
                mInfoTextView.setText("onFailure: " + t.getMessage());
                progressBar.setVisibility(View.GONE);
            }
        });
    }
}
