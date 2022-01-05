package com.example.littlebooks;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class BackgroundTask extends AsyncTask<Void, Void, String> {
    String url;
    String json_string;



    JSONArray obj;
    ApiCallback apiCallback;

    //konstruktor - prva metoda,kt sa zavola - urobi url ktora vola php
    public BackgroundTask(String table, String action, String scr, String php, String podmienka, String search){
        Log.d("url" ,"search "+search);
        if (podmienka.isEmpty()&&search.isEmpty()) {
            url = "http://165.227.134.175/" + php + ".php?table=" + table + "&action=" + action + "&scr=" + scr;
            Log.d("url1", url);
        }
        else if (search.isEmpty()) {
            url = "http://165.227.134.175/" + php + ".php?table=" + table + "&action=" + action + "&scr=" + scr + "&podmienka=" + podmienka;
            Log.d("url2", url);
        }else{
            url = "http://165.227.134.175/"+php+".php?table="+table+"&action="+action+"&scr="+scr+"&searchS="+search;
            Log.d("url3", url);
        }
    }

    //riesi interface idk
    public void setApiCallback(ApiCallback apiCallback){
        this.apiCallback = apiCallback;
    }

    //nic
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    //nic
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    //metoda ktora sa zavola po dokonceni poziadavky - vola interface dole
    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        apiCallback.populateLay(obj);
        try {
            Log.d("JSON", obj.getJSONObject(1).getString("nazov"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){

        }
    }

    //riesi poziadavku, dostane odpoved z php a spracuje zo stringu na JSON
    @Override
    protected String doInBackground(Void... voids) {
        Log.d("BackG", "...");
        try{
            URL url = new URL(this.url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while((line = bufferedReader.readLine())!=null){
                stringBuilder.append(line+"\n");

            }
            httpURLConnection.disconnect();
            json_string = stringBuilder.toString().trim();
            Log.d("JSON", "JSON "+json_string);


        }catch (MalformedURLException e){
            e.printStackTrace();
            Log.e("JSON", e.getMessage());

        } catch (IOException e) {
            e.printStackTrace();
            Log.e("JSON", e.getMessage());

        }
        try {
            obj = new JSONArray(json_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json_string;
    }
    //interface - zavola metodu populateLay z metody kde bola trieda zavolana - home fragment / detail knihy
    public interface ApiCallback{
        void populateLay(JSONArray obj);
    }
}
