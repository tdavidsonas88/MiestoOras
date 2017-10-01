package lt.agmis.tadas.miestooras;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends Activity {

    String[] cities = {"Kaunas", "Vilnius", "Klaipėda"};

    JSONObject data = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for(int i = 0; i < cities.length; i++){
            getJSON(cities[i], i+1);
        }

//        getJSON("Kaunas", 1);
//        getJSON("Vilnius", 2);
//        getJSON("Klaipėda", 3);

        Button addCityBtn = (Button) findViewById(R.id.addCityBtn);
        addCityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Add city button clicked", "You pressed add city btn");
                startActivity(new Intent(MainActivity.this, Pop.class));
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    public void getJSON(final String city, final int textViewNr ){

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            public double convertKelvinToCelcius(double tempInKelvin){
                return tempInKelvin - 273.15;
            }

            public double getCityTemperatureFromJsonData(){
                // data retrieved about city Kaunas as a sample

                // {"coord":{"lon":23.9,"lat":54.9},"weather":[{"id":800,"main":"Clear",
                // "description":"clear sky","icon":"01n"}],"base":"stations",
                // "main":{"temp":285.15,"pressure":1035,"humidity":76,"temp_min":285.15,
                // "temp_max":285.15},"visibility":10000,"wind":{"speed":2.1,"deg":70},
                // "clouds":{"all":0},"dt":1506446400,"sys":{"type":1,"id":5434,"message":0.0024,
                // "country":"LT","sunrise":1506399527,"sunset":1506442261},
                // "id":598316,"name":"Kaunas","cod":200}

                // parsing the JSON object main temperature

                try {
                    JSONObject mainObject = data.getJSONObject("main");
                    double tempKelvin = mainObject.getDouble("temp");
                    double tempCelcius = convertKelvinToCelcius(tempKelvin);

                    Log.d(city + " temp ", String.valueOf(tempCelcius));

                    TextView textView = null;

//                    LinearLayout myLayout = (LinearLayout) findViewById(R.id.mainLinearLayout);
//                    textView = new TextView(getApplicationContext());
//                    textView.setText(city);
//                    textView.setId(textViewNr);
//
//                    myLayout.addView(textView);

                    switch(textViewNr){
                        case 1:
                            textView = (TextView) findViewById(R.id.textView1);
                            break;
                        case 2:
                            textView = (TextView) findViewById(R.id.textView2);
                            break;
                        case 3:
                            textView = (TextView) findViewById(R.id.textView3);
                            break;
                        case 4:
                            textView = (TextView) findViewById(R.id.textView4);
                            break;
                    }

//                    TextView textView = (TextView) findViewById(R.id.textView1);
                    textView.setText(city + " " + String.valueOf(tempCelcius) + (char) 0x00B0 + "C");

                    return tempCelcius;
                }catch (Exception e){
                    System.out.println("Exception " + e.getMessage());
                }
                return -1000;

            }

            @Override
            protected Void doInBackground(Void... params) {

                try {
                    // API key with the city variable
                    URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q="+city+"&APPID=f4ad7b7bd41aad3a7b309e8aa0bf3550");

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuffer json = new StringBuffer(1024);
                    String tmp = "";

                    // reader.readLine reads  the lines to the stringBuffer reader
                    while((tmp = reader.readLine()) != null)
                        json.append(tmp).append("\n");

                    reader.close();

                    data = new JSONObject(json.toString());

                    getCityTemperatureFromJsonData();

                    if(data.getInt("cod") != 200){
                        System.out.println("Cancelled");
                        return null;
                    }

                } catch (Exception e){
                    System.out.println("Exception " + e.getMessage());
                    return null;
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(data != null){
                    // wifi/internet must be turned on on the mobile phone
                    Log.d("Miesto oras gautas", data.toString());
                }
                super.onPostExecute(aVoid);
            }
        }.execute();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
