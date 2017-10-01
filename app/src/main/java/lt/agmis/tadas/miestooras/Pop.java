package lt.agmis.tadas.miestooras;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tadas on 2017-09-28.
 */

class Pop extends Activity implements AdapterView.OnItemClickListener {

    ListView lst;
    String[] cities = {"Kaunas", "Vilnius", "Klaipėda", "Alytus", "Panevėžys"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.popwindow);

        lst = (ListView) findViewById(R.id.listvw);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this, android.R.layout.simple_list_item_1, cities);
        lst.setAdapter(arrayAdapter);
        lst.setOnItemClickListener(this);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8), (int) (height*.8));




    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView) view;
        Toast.makeText(this, "you clicked on " + tv.getText() + " " + position, Toast.LENGTH_SHORT).show();
    }
}
