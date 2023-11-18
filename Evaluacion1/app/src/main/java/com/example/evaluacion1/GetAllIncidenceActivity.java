package com.example.evaluacion1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class GetAllIncidenceActivity extends AppCompatActivity {

    private ListView listView;
    private ArrayList<IncidenceModel> incidenceModelArrayList;
    private CustomAdapter customAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_all_incidence);

        listView = findViewById(R.id.lv);

        databaseHelper = new DatabaseHelper(this);

        incidenceModelArrayList = databaseHelper.getAllIncidences();

        customAdapter = new CustomAdapter(this, incidenceModelArrayList);
        listView.setAdapter(customAdapter);
        Button btnBackToMain = findViewById(R.id.btnBackToMain);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(GetAllIncidenceActivity.this, UpdateDeleteActivity.class);
                intent.putExtra("incidence", incidenceModelArrayList.get(position));
                startActivity(intent);
            }
        });

        btnBackToMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                // Vuelve al MainActivity
                Intent intent = new Intent(GetAllIncidenceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }



}