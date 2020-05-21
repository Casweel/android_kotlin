package com.lab5;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.function.Consumer;

public class GetCat {
    private static final String BASE_URL = "https://api.thecatapi.com/v1/breeds?attach_breed=1&limit=10";
    private static final Gson gson = new Gson();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public ArrayList<Cat> getCats() throws IOException {
        String endpoint = "";
        JsonArray jsonArray = getCatJson(endpoint).getAsJsonArray();
        final ArrayList<Cat> cats = new ArrayList<>();
        jsonArray.forEach(new Consumer<JsonElement>() {
            @Override
            public void accept(JsonElement jsonElement) {
                cats.add(gson.fromJson(jsonElement, Cat.class));
            }
        });
        return cats;
    }

    private static JsonObject getCatJson(String endpoint) throws IOException {
        String url = BASE_URL + endpoint;
        return readJsonUrl(url).getAsJsonObject();
    }

    private static JsonElement readJsonUrl(String url) throws IOException {
        return new JsonParser().parse(getPage(url));
    }

    private static String getPage(String url) throws IOException {
        try{
            URL url1 = new URL(url);
            URLConnection connection = url1.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 5.1; rv:19.0) Gecko/20100101 Firefox/19.0");
            connection.connect();
            BufferedReader serverResponse = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String response = serverResponse.readLine();
            serverResponse.close();
            return response;
        }
        catch (IOException ex)
        {
            return null;
        }
    }
}
