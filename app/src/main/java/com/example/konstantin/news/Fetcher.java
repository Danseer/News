package com.example.konstantin.news;

/**
 * Created by Konstantin on 06.10.2017.
 */

import android.net.Uri;
import android.util.Log;

import com.example.konstantin.news.GalleryItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Fetcher {
    private static final String TAG = "Fetcher";
    static List<SliderUtils> slImg = new ArrayList<>();

    public static List<SliderUtils> getSlImg() {
        return slImg;
    }


    public String getJSONString(String URLSpec) throws IOException {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(URLSpec)
                .build();
        Response response = client.newCall(request).execute();
        String result = response.body().string();

        return result;
    }

    public List<GalleryItem> fetchItems() {
        List<GalleryItem> itemsList = new ArrayList<>();
        try {
            String url = "http://owledge.ru/api/v1/feedNews?lang=en&count=10&sources=7,19,13,5,15,16,12,9,10012,10010,10013,10014,10019,10018,10011&feedLineId=5";


            String jsonString = getJSONString(url);
            Log.e("url", url);
            Log.e("jsonString", jsonString);


            parseItems(itemsList, jsonString);

        } catch (IOException e) {
            Log.e(TAG, "Ощибка загрузки данных", e);
        } catch (JSONException e) {
            Log.e(TAG, "Ошибка парсинга JSON", e);
        }
        return itemsList;
    }

    private void parseItems(List<GalleryItem> items, String fromServer) throws IOException, JSONException {
        JSONArray usersJSONArray = new JSONArray(fromServer);

        for (int i = 0; i < usersJSONArray.length(); i++) {
            JSONObject userJSONObject = usersJSONArray.getJSONObject(i);
            GalleryItem item = new GalleryItem();
            SliderUtils utils = new SliderUtils();
            item.setTitle(userJSONObject.getString("name"));
            item.setUrl(userJSONObject.getString("cover"));

            if (true) {
                utils.setSliderImageUrl(userJSONObject.getString("cover"));
                slImg.add(utils);
                Log.e("slImg ", utils.getSliderImageUrl());

            }

            item.setSource(userJSONObject.getString("sourceId"));
            item.setTime(userJSONObject.getString("date"));
            items.add(item);
        }

    }

}