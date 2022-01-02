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
    String json_string = "http://165.227.134.175/getKnihy.php";


    JSONArray obj;
    ApiCallback apiCallback;


    public BackgroundTask(String table, String action, String scr, String php){
        url = "http://165.227.134.175/"+php+".php?table="+table+"&action="+action+"&scr="+scr;
        Log.d("url", url);

    }

    public void setApiCallback(ApiCallback apiCallback){
        this.apiCallback = apiCallback;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }


    @Override
    protected void onPostExecute(String aVoid) {
        /*AdapterProcess adapter = new AdapterProcess(mKnihy, activity);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);*/
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
    public interface ApiCallback{
        void populateLay(JSONArray obj);
    }
}
