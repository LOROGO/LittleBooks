package com.example.littlebooks;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class test extends AppCompatActivity {


    TextView text, errorText;
    Button show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test2);

        text = findViewById(R.id.testText);
        errorText = findViewById(R.id.errorsText);
        show = findViewById(R.id.button2);

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Async().execute();
            }
        });
    }



    /*class Task extends AsyncTask<Void, Void, Void>{
        String records = "", error = "";

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Connection con = DriverManager.getConnection("db-mysql-fra1-76684-do-user-10334479-0.b.db.ondigitalocean.com:25060/defaultdb", "doadmin", "vhVfELGWlFZ7nzOa");
                Statement statement = con.createStatement();
                ResultSet resultSet = statement.executeQuery("Select * from kniha");
                while (resultSet.next()){
                    records += resultSet.getString(1)+" "+resultSet.getString(2)+" "+resultSet.getString(3)+"\n";
                }
            }catch (Exception e){
               error = e.toString();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            text.setText(records);
            if (error!=null){
                errorText.setText(error);
            }
            super.onPostExecute(aVoid);
        }
    }*/
    class Async extends AsyncTask<Void, Void, Void> {



        String records = "",error="";

        @Override

        protected Void doInBackground(Void... voids) {

            try

            {

                Class.forName("com.mysql.jdbc.Driver");
                Connection connection = DriverManager.getConnection("jdbc:mysql://db-mysql-fra1-76684-do-user-10334479-0.b.db.ondigitalocean.com:25060/defaultdb", "doadmin", "vhVfELGWlFZ7nzOa");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM kniha");
                while(resultSet.next()) {
                    records += resultSet.getString(1) + " " + resultSet.getString(2) + "\n";
                }
            }

            catch(Exception e)
            {
                error = e.toString();
            }

            return null;

        }



        @Override

        protected void onPostExecute(Void aVoid) {

            text.setText(records);
            if(error != "")
                errorText.setText(error);
            super.onPostExecute(aVoid);

        }





    }
}