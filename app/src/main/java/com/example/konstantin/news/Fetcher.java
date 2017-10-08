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
import java.util.Calendar;
import java.util.Date;
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
            String sI = userJSONObject.getString("cover");
            int si = sI.indexOf(".");
            int sii = sI.indexOf("/", si);
            String resultI = sI.substring(si + 1, sii);
            item.setSource(resultI);

            Long currentDate = (new Date().getTime()) / 1000;
            Long PostDate = Long.decode(userJSONObject.getString("date"));
            Long Razn = currentDate - PostDate;
            String d = "";
            long day = Razn / 86400;
            Log.e("Day ", String.valueOf(day));
            long hours = Razn / 3600;
            Log.e("Hours ", String.valueOf(hours));
            Long minutes = Razn / 60;
            Log.e("minutes ", String.valueOf(minutes));
            if (day > 0) d = String.valueOf(day) + " days ago";
            else if (hours > 0) d = String.valueOf(hours) + " hours ago";
            else d = String.valueOf(minutes) + " min ago";
            Log.e("time at ", d);
            item.setTime(d);

            items.add(item);

            //--------------------check TOP news---------------------------
            if (true) {
                utils.setSliderImageUrl(userJSONObject.getString("cover"));
                String sU = userJSONObject.getString("cover");
                int su = sU.indexOf(".");
                int suu = sU.indexOf("/", su);
                String resultU = sU.substring(su + 1, suu);
                utils.setSource(resultU);
                utils.setTitle(userJSONObject.getString("name"));

                Long currentDateU = (new Date().getTime()) / 1000;
                Long PostDateU = Long.decode(userJSONObject.getString("date"));
                Long RaznU = currentDateU - PostDateU;
                String dU = "";
                long dayU = RaznU / 86400;
                Log.e("Day ", String.valueOf(dayU));
                long hoursU = RaznU / 3600;
                Log.e("Hours ", String.valueOf(hoursU));
                Long minutesU = RaznU / 60;
                Log.e("minutes ", String.valueOf(minutesU));
                if (dayU > 0) dU = String.valueOf(dayU) + " days ago";
                else if (hoursU > 0) dU = String.valueOf(hoursU) + " hours ago";
                else dU = String.valueOf(minutesU) + " min ago";
                Log.e("time at ", dU);
                utils.setTime(dU);

                slImg.add(utils);

            }
            //--------------------------------------------------------------
        }

    }

}