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
    public String url= "";
    public String json_string= "";

    public String table= "";
    public String action= "";
    public String php= "";
    public String podmienka= "";
    public String search= "";
    public String scr= "";
    public String uid= "";
    public String id_kniha= "";
    public String popis="";
    public String hodnotenie="";
    public int vyber = 0;

    JSONArray obj;
    ApiCallback apiCallback;
    CallbackReview callbackReview;

    //konstruktor - prva metoda,kt sa zavola - urobi url ktora vola php
    public BackgroundTask(int vyber){
        this.vyber = vyber;
        Log.d("url", "som v backgt");
    }

    //riesi interface idk
    public void setApiCallback(ApiCallback apiCallback){
        this.apiCallback = apiCallback;
    }
    public void setApiCallback1(CallbackReview callbackReview){
        this.callbackReview = callbackReview;
    }


    //nic
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (vyber==1) {//main
            url = "http://159.223.112.133/" + php + ".php?table=" + table + "&action=" + action + "&scr=" + scr;
            Log.d("url1", url);
        }
        else if (vyber==2) {//detail
            url = "http://159.223.112.133/" + php + ".php?table=" + table + "&action=" + action + "&scr=" + scr + "&podmienka=" + podmienka;
            Log.d("url2", url);
        }else if(vyber==3){//search
            url = "http://159.223.112.133/"+php+".php?table="+table+"&action="+action+"&scr="+scr+"&searchS="+search;
            Log.d("url3", url);
        }
        else if (vyber==4){//recenzia
            url ="http://159.223.112.133/"+php+".php?id_kniha="+id_kniha+"&action="+action;
        }

    }
    //nic
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }



    //riesi poziadavku, dostane odpoved z php a spracuje zo stringu na JSON
    //zoberie vsetko z webu a ulozi do stringu JSON
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
    //metoda ktora sa zavola po dokonceni poziadavky - vola interface dole
    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        if (vyber==4){
            callbackReview.populateLayReview(obj);
        }else
        apiCallback.populateLay(obj);
        try {
            Log.d("JSON", obj.getJSONObject(1).getString("nazov"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        catch (NullPointerException e){

        }
    }


    //interface - zavola metodu populateLay z metody kde bola trieda zavolana - home fragment / detail knihy
    public interface ApiCallback{
        void populateLay(JSONArray obj);
    }
    public interface CallbackReview{
        void populateLayReview(JSONArray obj);
    }
}
