package com.lab5;

import android.graphics.pdf.PdfDocument;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ParseJSON {
    private String url;
    private String page;
    private static final Gson gson = new Gson();

    ParseJSON(String url) {
        this.url = url;
    }

    public Person getCat() throws IOException {
        page = getPage(url);

        Gson gson = new GsonBuilder().create();
        return gson.fromJson(page, Person[].class)[0];
    }

    public ArrayList<Cat> getCats() throws Exception {
        page = getPage(url);

        ArrayList<Cat> toReturn = new ArrayList<Cat>();
        Cat[] cats;
        Gson gson = new GsonBuilder().create();
        cats = gson.fromJson(page, Cat[].class);
        Collections.addAll(toReturn, cats);
        return toReturn;
    }

    private String getPage(String url) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String serverAnswer = null;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("x-api-key", "2d3afec3-2896-43cb-9f4c-81f03ce9f8bc")
                .build();
        try {

            Response response = client.newCall(request).execute();
            serverAnswer = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return serverAnswer;
    }

    public Person getCatById() {

        return null;
    }
}
